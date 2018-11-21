# tencent_cos

腾讯云cos插件,用于上传图片

## Getting Started

For help getting started with Flutter, view our online
[documentation](https://flutter.io/).

For help on editing plugin code, view the [documentation](https://flutter.io/developing-packages/#edit-plugin-package).
###直接使用
```
void upload() async {
    File image = await ImagePicker.pickImage(source: ImageSource.gallery);
    print(image.path);
    TencentCos.uploadByFile(
        "ap-beijing",
        "1253631018",
        "barber-1253631018",
        "AKIDalNaS49sWdG5nYGojvx4xvvY3C6MCcUk",
        "jh7Cx3QGwdcZJNmd70cxlEIJ0WmMAWDU",
        "ae7048f4b802e7efc12aae8242a068ddf5c7817430001",
        "1538105499",
        "pic/dynamic/video_dynamic/1/5.png",
        image.path);
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
'''

###android在build.gradle里添加
 maven {
            url "https://dl.bintray.com/tencentqcloudterminal/maven"
 }
  
具体参数参看腾讯cos ,下面4个参数是后台传递过来的,用于鉴权和加密
 * String secretId,
 * String secretKey,
 * String sessionToken,
 * String expiredTime,
 
后台鉴权代码在example里的android StorageSts.java里面,代码分为上半段,和下半段,上半段是设置自己的cos的密钥等信息,下半段是生成上面参数的json串

