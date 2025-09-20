package com.vvsoft.saathi.drobox.client;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.oauth.DbxCredential;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DropboxOfflineAccessAuthorizerTest {


    @Test
    void obtain_credential_for_app_when_app_credential_is_passed_to_authorizer() {
        DropboxOfflineAccessAuthorizer authorizer = new DropboxOfflineAccessAuthorizer();
        DbxAppInfo vivSoftApp = new DbxAppInfo(System.getenv("api_key"), System.getenv("api_secret"));
        DbxCredential dbxCredential = authorizer.requestAuthorization(new DropboxAuthorizationRequest(vivSoftApp, List.of("account_info.read")));
        assertNotNull(dbxCredential.getAccessToken());
        assertNotNull(dbxCredential.getRefreshToken());
        assertNotNull(dbxCredential.getExpiresAt());
        assertNotNull(dbxCredential.getExpiresAt());
    }
}