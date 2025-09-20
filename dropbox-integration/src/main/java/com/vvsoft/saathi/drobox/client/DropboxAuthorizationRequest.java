package com.vvsoft.saathi.drobox.client;

import com.dropbox.core.DbxAppInfo;

import java.util.List;

public record DropboxAuthorizationRequest(DbxAppInfo appInfo, List<String> scopes) {
}
