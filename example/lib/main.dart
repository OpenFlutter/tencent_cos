import 'dart:io';

import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:tencent_cos/tencent_cos.dart';
//import 'package:image_picker/image_picker.dart';

void main() => runApp(new MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => new _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
//    File image = await ImagePicker.pickImage(source: ImageSource.gallery);
//    print("abc" + image.path);

    TencentCos.uploadByFile(
        "ap-beijing",
        "1253631018",
        "barber-1253631018",
        "AKIDalNaS49sWdG5nYGojvx4xvvY3C6MCcUk",
        "jh7Cx3QGwdcZJNmd70cxlEIJ0WmMAWDU",
        "ae7048f4b802e7efc12aae8242a068ddf5c7817430001",
        "1538105499",
        "pic/dynamic/video_dynamic/1/5.png",
        "本地图片的路径");
    TencentCos.setMethodCallHandler(_handleMessages);
  }

  Future<Null> _handleMessages(MethodCall call) async {
    print(call.method);
    print(call.arguments);
    if(call.method == "onProgress"){

    }else if(call.method == "onFailed"){

    }else if(call.method == "onSuccess"){

    }
  }

  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      home: new Scaffold(
        appBar: new AppBar(
          title: const Text('Plugin example app'),
        ),
        body: new Center(
          child: new Text('Running on: $_platformVersion\n'),
        ),
      ),
    );
  }
}
