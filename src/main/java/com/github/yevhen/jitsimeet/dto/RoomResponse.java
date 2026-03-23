package com.github.yevhen.jitsimeet.dto;

import com.github.yevhen.jitsimeet.model.JitsiRoom;

import java.time.Instant;
import java.util.UUID;

public record RoomResponse(
        UUID id,
        String roomName,
        String eventRef,
        String jitsiUrl,
        Instant createdAt
) {
    public static RoomResponse from(JitsiRoom room, String protocol, String serverUrl) {
        return new RoomResponse(
                room.getId(),
                room.getRoomName(),
                room.getEventRef(),
                protocol + "://" + serverUrl + "/" + room.getRoomName(),
                room.getCreatedAt()
        );
    }
}
