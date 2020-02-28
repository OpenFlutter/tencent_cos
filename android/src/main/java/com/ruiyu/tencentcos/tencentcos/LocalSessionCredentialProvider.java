package com.ruiyu.tencentcos.tencentcos;

import com.tencent.qcloud.core.auth.BasicLifecycleCredentialProvider;
import com.tencent.qcloud.core.auth.QCloudLifecycleCredentials;
import com.tencent.qcloud.core.auth.SessionQCloudCredentials;
import com.tencent.qcloud.core.common.QCloudClientException;

/**
 * 方法二：使用临时密钥进行签名（推荐使用这种方法），此处假设已获取了临时密钥 tempSecretKey, tempSecrekId,
 * sessionToken, expiredTime.
 */
class LocalSessionCredentialProvider extends BasicLifecycleCredentialProvider {
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
    protected QCloudLifecycleCredentials fetchNewCredentials() throws QCloudClientException {
        return new SessionQCloudCredentials(tempSecretId, tempSecretKey, sessionToken, expiredTime);
    }
}