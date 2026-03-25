package com.github.yevhen.jitsimeet.model;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "jitsi_room_participants")
public class JitsiRoomParticipant {

    @EmbeddedId
    private JitsiRoomParticipantId id;

    protected JitsiRoomParticipant() {}

    public JitsiRoomParticipant(UUID roomId, String participantEmail) {
        this.id = new JitsiRoomParticipantId(roomId, participantEmail);
    }

    public UUID getRoomId() { return id.getRoomId(); }
    public String getParticipantEmail() { return id.getParticipantEmail(); }

    @Embeddable
    public static class JitsiRoomParticipantId implements java.io.Serializable {
        @Column(name = "room_id")
        private UUID roomId;

        @Column(name = "participant_email")
        private String participantEmail;

        protected JitsiRoomParticipantId() {}

        public JitsiRoomParticipantId(UUID roomId, String participantEmail) {
            this.roomId = roomId;
            this.participantEmail = participantEmail;
        }

        public UUID getRoomId() { return roomId; }
        public String getParticipantEmail() { return participantEmail; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof JitsiRoomParticipantId other)) return false;
            return Objects.equals(roomId, other.roomId) && Objects.equals(participantEmail, other.participantEmail);
        }

        @Override
        public int hashCode() { return Objects.hash(roomId, participantEmail); }
    }
}
