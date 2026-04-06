CREATE TABLE jitsi_room_participants (
    room_id           UUID         NOT NULL REFERENCES jitsi_rooms(id) ON DELETE CASCADE,
    participant_email VARCHAR(255) NOT NULL,
    PRIMARY KEY (room_id, participant_email)
);

CREATE INDEX idx_room_participants_room_id ON jitsi_room_participants(room_id);
