package com.github.yevhen.jitsimeet.repository;

import com.github.yevhen.jitsimeet.model.JitsiRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JitsiRoomRepository extends JpaRepository<JitsiRoom, UUID> {
    Optional<JitsiRoom> findByRoomName(String roomName);
    Optional<JitsiRoom> findByEventRef(String eventRef);
    List<JitsiRoom> findAllByCreatedByOrderByCreatedAtDesc(UUID createdBy);
}
