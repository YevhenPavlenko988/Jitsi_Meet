package com.github.yevhen.jitsimeet.dto;

import java.util.List;

public record CreateRoomRequest(
        String eventRef,
        String displayName,
        List<String> participantEmails
) {}
