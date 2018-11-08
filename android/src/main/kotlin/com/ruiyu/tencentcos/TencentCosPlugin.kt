package com.ruiyu.tencentcos

import android.util.Log
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.PluginRegistry.Registrar

class TencentCosPlugin(private val registrar: Registrar, private val channel: MethodChannel) : MethodCallHandler {
    companion object {
        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val channel = MethodChannel(registrar.messenger(), "tencent_cos")
            channel.setMethodCallHandler(TencentCosPlugin(registrar, channel))
        }
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        Log.e("TencentCosPlugin", call.method)
        when {
            call.method == "TencentCos.uploadFile" -> {

                val localCredentialProvider = LocalSessionCredentialProvider(call.argument<Any>("secretId").toString(),
                        call.argument<Any>("secretKey").toString(), call.argument<Any>("sessionToken").toString(), java.lang.Long.parseLong(call.argument<Any>("expiredTime").toString())
                )
                val region = call.argument<String>("region")
                val appid = call.argument<String>("appid")
                val bucket = call.argument<String>("bucket")
                val cosPath = call.argument<String>("cosPath")
                val localPath = call.argument<String>("localPath")
                CosUploadFile.upLoadFile(registrar.context(), appid!!, region!!, bucket!!, cosPath!!, localPath!!, localCredentialProvider,channel)

            }
            else -> result.notImplemented()
        }
    }
}
