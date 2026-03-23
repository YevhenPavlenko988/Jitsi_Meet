package com.github.yevhen.jitsimeet.dto;

public record CreateRoomRequest(
        String eventRef,
        String displayName
) {}
