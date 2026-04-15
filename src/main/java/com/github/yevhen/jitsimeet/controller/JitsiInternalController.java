package com.github.yevhen.jitsimeet.controller;

import com.github.yevhen.jitsimeet.dto.InternalCreateRoomRequest;
import com.github.yevhen.jitsimeet.dto.RoomResponse;
import com.github.yevhen.jitsimeet.service.JitsiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Internal service-to-service endpoint — not exposed via service_bus, reachable only
 * on the Docker-internal network (e.g. google-service → http://jitsi-meet:8083).
 */
@RestController
@RequestMapping("/jitsi/internal")
public class JitsiInternalController {

    private final JitsiService jitsiService;

    public JitsiInternalController(JitsiService jitsiService) {
        this.jitsiService = jitsiService;
    }

    @PostMapping("/rooms")
    public ResponseEntity<RoomResponse> createRoom(@RequestBody InternalCreateRoomRequest request) {
        return ResponseEntity.ok(jitsiService.getOrCreateRoomInternal(request));
    }
}
