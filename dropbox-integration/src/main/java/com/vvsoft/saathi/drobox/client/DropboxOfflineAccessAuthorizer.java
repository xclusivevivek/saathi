package com.vvsoft.saathi.drobox.client;

import com.dropbox.core.*;
import com.dropbox.core.oauth.DbxCredential;

import java.util.List;

public class DropboxOfflineAccessAuthorizer implements DropboxClientAuthorizer{
    @Override
    public DbxCredential requestAuthorization(DropboxAuthorizationRequest authorizationRequest) {
        DbxRequestConfig requestConfig = DbxRequestConfig.newBuilder("vivSoftAuthorizationTest").build();
        DbxWebAuth dbxWebAuth = new DbxWebAuth(requestConfig, authorizationRequest.appInfo());
        DbxWebAuth.Request request = DbxWebAuth.newRequestBuilder()
                .withTokenAccessType(TokenAccessType.OFFLINE)
                .withScope(authorizationRequest.scopes())
                .withNoRedirect()
                .build();
        String authorizeUrl = dbxWebAuth.authorize(request);
        char[] authorizationCodes = System.getenv("authorization_code").toCharArray();
        try {
            DbxAuthFinish dbxAuthFinish = dbxWebAuth.finishFromCode(new String(authorizationCodes));
            return new DbxCredential(dbxAuthFinish.getAccessToken(),dbxAuthFinish.getExpiresAt(),dbxAuthFinish.getRefreshToken(),authorizationRequest.appInfo().getKey());
        } catch (DbxException e) {
            throw new RuntimeException(e);
        }
    }
}
