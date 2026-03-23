package com.github.yevhen.jitsimeet.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "jitsi_rooms")
public class JitsiRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "room_name", nullable = false, unique = true)
    private String roomName;

    @Column(name = "created_by", nullable = false)
    private UUID createdBy;

    @Column(name = "event_ref")
    private String eventRef;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected JitsiRoom() {}

    public JitsiRoom(String roomName, UUID createdBy, String eventRef) {
        this.roomName = roomName;
        this.createdBy = createdBy;
        this.eventRef = eventRef;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }

    public UUID getId() { return id; }
    public String getRoomName() { return roomName; }
    public UUID getCreatedBy() { return createdBy; }
    public String getEventRef() { return eventRef; }
    public Instant getCreatedAt() { return createdAt; }
}
