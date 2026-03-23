package com.github.yevhen.jitsimeet.dto;

public record TokenResponse(
        String token,
        String roomName,
        String jitsiUrl,
        boolean moderator,
        /** Browser-facing domain for the Jitsi IFrame API (no protocol), e.g. "localhost:8088" */
        String serverDomain,
        /** Protocol for the browser to reach the server: "http" or "https" */
        String serverProtocol,
        /** True when the Jitsi server validates the JWT — frontend must pass the token */
        boolean useJwt,
        /** Base URL for loading external_api.js (may differ from serverDomain to avoid cert issues) */
        String scriptOrigin
) {}
