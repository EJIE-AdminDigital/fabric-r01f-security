package r01f.security.authn.provider.config;

import java.util.Properties;

import r01f.httpclient.HttpClientProxySettings;

public interface IsAuthenticationProviderSecretConfigSet {

	Properties getUrlVars();

	HttpClientProxySettings getProxySettings();

}