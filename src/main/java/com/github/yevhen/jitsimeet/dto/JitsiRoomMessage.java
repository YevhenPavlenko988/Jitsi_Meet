package com.github.yevhen.jitsimeet.dto;

import java.util.List;
import java.util.UUID;

public record JitsiRoomMessage(
        String roomName,
        String eventRef,
        UUID joinerUserId,
        List<String> participantEmails
) {}
