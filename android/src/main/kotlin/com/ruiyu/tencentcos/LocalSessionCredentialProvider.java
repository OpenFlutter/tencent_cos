package com.ruiyu.tencentcos;

import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.qcloud.core.auth.BasicLifecycleCredentialProvider;
import com.tencent.qcloud.core.auth.QCloudLifecycleCredentials;
import com.tencent.qcloud.core.auth.SessionQCloudCredentials;

/**
 * 方法二：使用临时密钥进行签名（推荐使用这种方法），此处假设已获取了临时密钥 tempSecretKey, tempSecrekId,
 * sessionToken, expiredTime.
 */
public class LocalSessionCredentialProvider extends BasicLifecycleCredentialProvider {
    private String tempSecretId;
    private String tempSecretKey;
    private String sessionToken;
    private long expiredTime;

    public LocalSessionCredentialProvider(String tempSecretId, String tempSecretKey, String sessionToken, long expiredTime) {
        this.tempSecretId = tempSecretId;
        this.tempSecretKey = tempSecretKey;
        this.sessionToken = sessionToken;
        this.expiredTime = expiredTime;
    }

    /**
     * 返回 SessionQCloudCredential
     */
    @Override
    public QCloudLifecycleCredentials fetchNewCredentials() throws CosXmlClientException {
        return new SessionQCloudCredentials(tempSecretId, tempSecretKey, sessionToken, expiredTime);
    }
}