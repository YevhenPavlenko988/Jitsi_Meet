package com.github.yevhen.jitsimeet.dto;

import java.util.List;

public record InternalCreateRoomRequest(
        String createdByUserId,
        String eventRef,
        List<String> participantEmails
) {}
