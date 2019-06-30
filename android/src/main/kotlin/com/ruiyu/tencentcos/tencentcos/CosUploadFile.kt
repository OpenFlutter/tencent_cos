package com.ruiyu.tencentcos.tencentcos

import android.content.Context
import android.util.Log
import com.tencent.cos.xml.CosXmlService
import com.tencent.cos.xml.CosXmlServiceConfig
import com.tencent.cos.xml.exception.CosXmlClientException
import com.tencent.cos.xml.exception.CosXmlServiceException
import com.tencent.cos.xml.transfer.UploadService
import com.tencent.qcloud.core.auth.QCloudCredentialProvider
import io.flutter.plugin.common.MethodChannel


class CosUploadFile {
    companion object {
        fun upLoadFile(context: Context, appid: String, region: String, bucketName: String, cosPath: String, localPath: String, qCloudCredentialProvider: QCloudCredentialProvider, channel: MethodChannel) {

//创建 CosXmlServiceConfig 对象，根据需要修改默认的配置参数
            val serviceConfig = CosXmlServiceConfig.Builder()
                    .setAppidAndRegion(appid, region)
                    .setDebuggable(true).isHttps(false)
                    .builder()
            val cosXmlService = CosXmlService(context, serviceConfig, qCloudCredentialProvider)

            val uploadData = UploadService.ResumeData()
            uploadData.bucket = bucketName //"存储桶名称"
            uploadData.cosPath = cosPath //"[对象键](https://cloud.tencent.com/document/product/436/13324)，即存储到 COS 上的绝对路径" //格式如 cosPath = "test.txt";
            uploadData.srcPath = localPath//"本地文件的绝对路径" // 如 srcPath =Environment.getExternalStorageDirectory().getPath() + "/test.txt";
            uploadData.sliceSize = (1024 * 1024).toLong() //每个分片的大小
            uploadData.uploadId = null // 若是续传，则uploadId不为空

            val uploadService = UploadService(cosXmlService, uploadData)
            uploadService.setProgressListener { progress, max ->
                val data = HashMap<String, Any>()
                data["localPath"] = localPath
                data["progress"] = progress * 100.0 / max
                channel.invokeMethod("onProgress", data)
                Log.e("TencentCosPlugin", "onProgress =${progress * 100.0 / max}%")
            }


            /**
             * 开始上传
             */

            val data = HashMap<String, Any>()
            data["localPath"] = localPath
            data["cosPath"] = cosPath

            try {
                val cosXmlResult = uploadService.upload()
                Log.e("TencentCosPlugin", cosXmlResult.printResult())
                channel.invokeMethod("onSuccess", data)
            } catch (e: CosXmlClientException) {
                channel.invokeMethod("onFailed", data)
                data["message"] = e.message!!
                Log.e("TencentCosPlugin", "onFailed " + e.message)
            } catch (e: CosXmlServiceException) {
                channel.invokeMethod("onFailed", data)
                data["message"] = e.message!!
                Log.e("TencentCosPlugin", "onFailed " + e.message)
            }

        }

    }
}