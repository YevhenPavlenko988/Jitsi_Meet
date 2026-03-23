package com.github.yevhen.jitsimeet.service;

import com.github.yevhen.common.exception.ServiceException;
import com.github.yevhen.common.security.CallerInfo;
import com.github.yevhen.common.security.JwtHelper;
import com.github.yevhen.jitsimeet.config.JitsiProperties;
import com.github.yevhen.jitsimeet.dto.CreateRoomRequest;
import com.github.yevhen.jitsimeet.dto.RoomResponse;
import com.github.yevhen.jitsimeet.dto.TokenResponse;
import com.github.yevhen.jitsimeet.model.JitsiRoom;
import com.github.yevhen.jitsimeet.repository.JitsiRoomRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class JitsiService {

    private static final Set<String> MODERATOR_ROLES = Set.of("ADMIN", "MANAGER", "TEACHER");

    private final JitsiProperties props;
    private final JwtHelper jwtHelper;
    private final JitsiRoomRepository roomRepository;

    public JitsiService(JitsiProperties props, JwtHelper jwtHelper, JitsiRoomRepository roomRepository) {
        this.props = props;
        this.jwtHelper = jwtHelper;
        this.roomRepository = roomRepository;
    }

    // ─── Room management ─────────────────────────────────────────────────────

    @Transactional
    public RoomResponse getOrCreateRoom(CreateRoomRequest request, CallerInfo caller) {
        if (request.eventRef() != null && !request.eventRef().isBlank()) {
            return roomRepository.findByEventRef(request.eventRef())
                    .map(room -> RoomResponse.from(room, props.getServerProtocol(), props.getServerUrl()))
                    .orElseGet(() -> createNew(request.eventRef(), caller));
        }
        return createNew(null, caller);
    }

    public List<RoomResponse> getRooms(CallerInfo caller) {
        return roomRepository.findAllByCreatedByOrderByCreatedAtDesc(caller.id())
                .stream()
                .map(room -> RoomResponse.from(room, props.getServerProtocol(), props.getServerUrl()))
                .toList();
    }

    @Transactional
    public void deleteRoom(String roomName, CallerInfo caller) {
        JitsiRoom room = roomRepository.findByRoomName(roomName)
                .orElseThrow(() -> new ServiceException("Room not found", HttpStatus.NOT_FOUND));

        if (!room.getCreatedBy().equals(caller.id()) && !caller.role().equals("ADMIN")) {
            throw new ServiceException("Access denied", HttpStatus.FORBIDDEN);
        }

        roomRepository.delete(room);
    }

    // ─── Token ───────────────────────────────────────────────────────────────

    public TokenResponse getToken(String roomName, String displayName, CallerInfo caller) {
        roomRepository.findByRoomName(roomName)
                .orElseThrow(() -> new ServiceException("Room not found", HttpStatus.NOT_FOUND));

        boolean isModerator = MODERATOR_ROLES.contains(caller.role());
        String resolvedName = (displayName != null && !displayName.isBlank()) ? displayName : caller.email();
        String token = generateJitsiToken(caller.id(), caller.email(), resolvedName, isModerator, roomName);

        return new TokenResponse(
                token,
                roomName,
                props.getServerProtocol() + "://" + props.getServerUrl() + "/" + roomName,
                isModerator,
                props.getServerUrl(),
                props.getServerProtocol(),
                props.isUseJwt(),
                props.getScriptOrigin()
        );
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────

    private RoomResponse createNew(String eventRef, CallerInfo caller) {
        String roomName = props.getRoomPrefix()
                + UUID.randomUUID().toString().replace("-", "").substring(0, 12);

        JitsiRoom room = new JitsiRoom(roomName, caller.id(), eventRef);
        return RoomResponse.from(roomRepository.save(room), props.getServerProtocol(), props.getServerUrl());
    }

    private String generateJitsiToken(UUID userId, String email, String displayName,
                                      boolean isModerator, String roomName) {
        SecretKey key = Keys.hmacShaKeyFor(
                props.getAppSecret().getBytes(StandardCharsets.UTF_8));

        long nowMs = System.currentTimeMillis();
        long expMs = nowMs + props.getTokenExpirySeconds() * 1000;

        Map<String, Object> user = new HashMap<>();
        user.put("id", userId.toString());
        user.put("name", displayName);
        user.put("email", email);
        user.put("moderator", isModerator);

        Map<String, Object> context = new HashMap<>();
        context.put("user", user);

        // prosody mod_auth_token requires typ:"JWT" in the header — JJWT 0.12+ omits it by default
        return Jwts.builder()
                .header().add("typ", "JWT").and()
                .issuer(props.getAppId())
                .subject(props.getXmppDomain())
                .claim("aud", "jitsi")
                .claim("room", roomName)
                .claim("context", context)
                .issuedAt(new Date(nowMs))
                .notBefore(new Date(nowMs - 10_000))
                .expiration(new Date(expMs))
                .signWith(key)
                .compact();
    }
}
