package com.github.yevhen.jitsimeet.repository;

import com.github.yevhen.jitsimeet.model.JitsiRoomParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JitsiRoomParticipantRepository
        extends JpaRepository<JitsiRoomParticipant, JitsiRoomParticipant.JitsiRoomParticipantId> {

    List<JitsiRoomParticipant> findByIdRoomId(UUID roomId);
}
