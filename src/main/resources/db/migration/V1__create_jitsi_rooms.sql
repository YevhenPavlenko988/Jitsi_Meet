CREATE TABLE jitsi_rooms (
    id          UUID        NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),
    room_name   VARCHAR(255) NOT NULL UNIQUE,
    created_by  UUID        NOT NULL,
    event_ref   VARCHAR(255),
    created_at  TIMESTAMP   NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_jitsi_rooms_event_ref ON jitsi_rooms (event_ref);
CREATE INDEX idx_jitsi_rooms_created_by ON jitsi_rooms (created_by);
