package com.vvsoft.saathi.drobox.client;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.oauth.DbxCredential;

public interface DropboxClientAuthorizer {
    DbxCredential requestAuthorization(DropboxAuthorizationRequest authorizationRequest);
}
