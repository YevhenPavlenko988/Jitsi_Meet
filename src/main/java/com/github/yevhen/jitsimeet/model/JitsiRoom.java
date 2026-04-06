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

    @Column(name = "is_active", nullable = false)
    private boolean active = false;

    @Column(name = "active_host_id")
    private UUID activeHostId;

    @Column(name = "activated_at")
    private Instant activatedAt;

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
    public boolean isActive() { return active; }
    public UUID getActiveHostId() { return activeHostId; }
    public Instant getActivatedAt() { return activatedAt; }

    public void activate(UUID hostId) {
        this.active = true;
        this.activeHostId = hostId;
        this.activatedAt = Instant.now();
    }

    public void deactivate() {
        this.active = false;
        this.activeHostId = null;
    }
}
