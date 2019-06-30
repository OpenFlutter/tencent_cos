package com.ruiyu.tencentcos.tencentcos

import com.tencent.cos.xml.exception.CosXmlClientException
import com.tencent.qcloud.core.auth.BasicLifecycleCredentialProvider
import com.tencent.qcloud.core.auth.QCloudLifecycleCredentials
import com.tencent.qcloud.core.auth.SessionQCloudCredentials

/**
 * 方法二：使用临时密钥进行签名（推荐使用这种方法），此处假设已获取了临时密钥 tempSecretKey, tempSecrekId,
 * sessionToken, expiredTime.
 */
class LocalSessionCredentialProvider(private val tempSecretId: String, private val tempSecretKey: String, private val sessionToken: String, private val expiredTime: Long) : BasicLifecycleCredentialProvider() {

    /**
     * 返回 SessionQCloudCredential
     */
    @Throws(CosXmlClientException::class)
    public override fun fetchNewCredentials(): QCloudLifecycleCredentials {
        return SessionQCloudCredentials(tempSecretId, tempSecretKey, sessionToken, expiredTime)
    }
}