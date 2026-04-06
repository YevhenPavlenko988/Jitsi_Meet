package com.github.yevhen.jitsimeet.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.jitsi")
public class JitsiProperties {

    /** Browser-facing domain (no protocol), e.g. "meet.jit.si" or "localhost:8088" */
    private String serverUrl;
    /** Protocol used by the browser to reach the server: "https" or "http" */
    private String serverProtocol = "https";
    /** XMPP domain used as JWT "sub" claim on self-hosted servers, e.g. "meet.jitsi" */
    private String xmppDomain;
    /** When true the frontend will attach the JWT token to the Jitsi IFrame API call */
    private boolean useJwt = false;
    /**
     * Base URL used by the browser to load external_api.js.
     * May differ from serverUrl when the IFrame needs HTTPS but the script can be served over HTTP
     * (e.g. to avoid self-signed certificate issues with Chrome's script loading).
     * Defaults to "{serverProtocol}://{serverUrl}" when not set.
     */
    private String scriptOrigin;
    private String appId;
    private String appSecret;
    private String roomPrefix = "";
    private long tokenExpirySeconds = 7200;

    public String getServerUrl() { return serverUrl; }
    public void setServerUrl(String serverUrl) { this.serverUrl = serverUrl; }

    public String getServerProtocol() { return serverProtocol; }
    public void setServerProtocol(String serverProtocol) { this.serverProtocol = serverProtocol; }

    public String getXmppDomain() { return xmppDomain != null ? xmppDomain : serverUrl; }
    public void setXmppDomain(String xmppDomain) { this.xmppDomain = xmppDomain; }

    public boolean isUseJwt() { return useJwt; }
    public void setUseJwt(boolean useJwt) { this.useJwt = useJwt; }

    public String getScriptOrigin() {
        return scriptOrigin != null ? scriptOrigin : serverProtocol + "://" + serverUrl;
    }
    public void setScriptOrigin(String scriptOrigin) { this.scriptOrigin = scriptOrigin; }

    public String getAppId() { return appId; }
    public void setAppId(String appId) { this.appId = appId; }

    public String getAppSecret() { return appSecret; }
    public void setAppSecret(String appSecret) { this.appSecret = appSecret; }

    public String getRoomPrefix() { return roomPrefix; }
    public void setRoomPrefix(String roomPrefix) { this.roomPrefix = roomPrefix; }

    public long getTokenExpirySeconds() { return tokenExpirySeconds; }
    public void setTokenExpirySeconds(long tokenExpirySeconds) { this.tokenExpirySeconds = tokenExpirySeconds; }
}
