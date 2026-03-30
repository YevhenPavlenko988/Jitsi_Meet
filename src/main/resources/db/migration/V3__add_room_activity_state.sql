ALTER TABLE jitsi_rooms
    ADD COLUMN is_active BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN active_host_id UUID,
    ADD COLUMN activated_at TIMESTAMP;

CREATE INDEX idx_jitsi_rooms_is_active ON jitsi_rooms(is_active);
