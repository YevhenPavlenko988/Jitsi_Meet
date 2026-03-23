package com.github.yevhen.jitsimeet.controller;

import com.github.yevhen.common.security.CallerInfo;
import com.github.yevhen.common.security.JwtHelper;
import com.github.yevhen.jitsimeet.dto.CreateRoomRequest;
import com.github.yevhen.jitsimeet.dto.RoomResponse;
import com.github.yevhen.jitsimeet.dto.TokenResponse;
import com.github.yevhen.jitsimeet.service.JitsiService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jitsi")
public class JitsiController {

    private final JitsiService jitsiService;
    private final JwtHelper jwtHelper;

    public JitsiController(JitsiService jitsiService, JwtHelper jwtHelper) {
        this.jitsiService = jitsiService;
        this.jwtHelper = jwtHelper;
    }

    /** Creates a room (or returns existing one if eventRef already has a room). */
    @PostMapping("/rooms")
    public ResponseEntity<RoomResponse> getOrCreateRoom(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader,
            @RequestBody CreateRoomRequest request
    ) {
        CallerInfo caller = jwtHelper.extractCallerInfo(authHeader);
        return ResponseEntity.ok(jitsiService.getOrCreateRoom(request, caller));
    }

    /** List rooms created by the current user. */
    @GetMapping("/rooms")
    public ResponseEntity<List<RoomResponse>> getRooms(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader
    ) {
        CallerInfo caller = jwtHelper.extractCallerInfo(authHeader);
        return ResponseEntity.ok(jitsiService.getRooms(caller));
    }

    /** Get a Jitsi JWT token for a specific room. Moderator flag is set by role. */
    @GetMapping("/rooms/{roomName}/token")
    public ResponseEntity<TokenResponse> getToken(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader,
            @PathVariable String roomName,
            @RequestParam(required = false) String displayName
    ) {
        CallerInfo caller = jwtHelper.extractCallerInfo(authHeader);
        return ResponseEntity.ok(jitsiService.getToken(roomName, displayName, caller));
    }

    /** Delete a room (only creator or ADMIN). */
    @DeleteMapping("/rooms/{roomName}")
    public ResponseEntity<Void> deleteRoom(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader,
            @PathVariable String roomName
    ) {
        CallerInfo caller = jwtHelper.extractCallerInfo(authHeader);
        jitsiService.deleteRoom(roomName, caller);
        return ResponseEntity.noContent().build();
    }
}
