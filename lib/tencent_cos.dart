import 'dart:async';

import 'package:flutter/services.dart';

class TencentCos {
  static const MethodChannel _channel = const MethodChannel('tencent_cos');

  static Future<dynamic> uploadByFile(
      String region,
      String appid,
      String bucket,
      String secretId,
      String secretKey,
      String sessionToken,
      expiredTime,
      String cosPath,
      String localPath) {
    return _channel.invokeMethod<dynamic>('TencentCos.uploadFile', {
      'region': region,
      'appid': appid,
      'bucket': bucket,
      'secretId': secretId,
      'secretKey': secretKey,
      'expiredTime': expiredTime,
      'sessionToken': sessionToken,
      'cosPath': cosPath,
      'localPath': localPath,
    });
  }

  static void setMethodCallHandler(Future<dynamic> handler(MethodCall call)) {
    _channel.setMethodCallHandler(handler);
  }
}
