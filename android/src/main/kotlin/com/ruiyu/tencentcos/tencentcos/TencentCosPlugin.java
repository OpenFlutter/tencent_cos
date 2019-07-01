package com.ruiyu.tencentcos.tencentcos;

import android.app.Activity;
import android.util.Log;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.transfer.COSXMLUploadTask;
import com.tencent.cos.xml.transfer.TransferConfig;
import com.tencent.cos.xml.transfer.TransferManager;

import java.util.HashMap;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * TencentCosPlugin
 */
public class TencentCosPlugin implements MethodCallHandler {
    Registrar registrar;
    MethodChannel channel;

    public TencentCosPlugin(Registrar registrar, MethodChannel channel) {
        this.registrar = registrar;
        this.channel = channel;
    }

    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "tencent_cos");
        channel.setMethodCallHandler(new TencentCosPlugin(registrar, channel));
    }

    @Override
    public void onMethodCall(MethodCall call, final Result result) {
        if (call.method.equals("TencentCos.uploadFile")) {
            LocalSessionCredentialProvider localCredentialProvider = new LocalSessionCredentialProvider(call.<String>argument("secretId"),
                    call.<String>argument("secretKey"), call.<String>argument("sessionToken"),
                    Long.parseLong(call.argument("expiredTime").toString())
            );
            String region = call.argument("region");
            String appid = call.argument("appid");
            String bucket = call.argument("bucket");
            String cosPath = call.argument("cosPath");
            final String localPath = call.argument("localPath");
            TransferConfig transferConfig = new TransferConfig.Builder().build();
            CosXmlServiceConfig.Builder builder = new CosXmlServiceConfig.Builder().setAppidAndRegion(appid, region).setDebuggable(false).isHttps(true);
//创建 CosXmlServiceConfig 对象，根据需要修改默认的配置参数
            CosXmlServiceConfig serviceConfig = new CosXmlServiceConfig(builder);
            CosXmlService cosXmlService = new CosXmlService(registrar.context(), serviceConfig, localCredentialProvider);
            //初始化 TransferManager
            TransferManager transferManager = new TransferManager(cosXmlService, transferConfig);

//上传文件
            COSXMLUploadTask cosxmlUploadTask = transferManager.upload(bucket, cosPath, localPath, null);

            final HashMap<String, Object> data = new HashMap<>();
            data.put("localPath", localPath);
            data.put("cosPath", cosPath);
            cosxmlUploadTask.setCosXmlProgressListener(new CosXmlProgressListener() {

                @Override
                public void onProgress(long complete, long target) {
                    ((Activity) registrar.activeContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            channel.invokeMethod("onProgress", data);
                        }
                    });

                    Log.e("TencentCosPlugin", "onProgress =${progress * 100.0 / max}%");
                }
            });
            //设置返回结果回调
            cosxmlUploadTask.setCosXmlResultListener(new CosXmlResultListener() {
                @Override
                public void onSuccess(CosXmlRequest request, CosXmlResult httPesult) {
                    Log.d("TencentCosPlugin", "Success: " + httPesult.printResult());
                    ((Activity) registrar.activeContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            result.success(data);
                        }
                    });
                }

                @Override
                public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                    Log.d("TencentCosPlugin", "Failed: " + (exception.toString() + serviceException.toString()));
                    data.put("message", (exception.toString() + serviceException.toString()));
                    ((Activity) registrar.activeContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            result.error("失败了", "失败了", "失败了");
                        }
                    });

                }
            });
        } else {
            result.notImplemented();
        }
    }

}


