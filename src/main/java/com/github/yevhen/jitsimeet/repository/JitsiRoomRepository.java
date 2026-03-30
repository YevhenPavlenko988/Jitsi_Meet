package com.github.yevhen.jitsimeet.repository;

import com.github.yevhen.jitsimeet.model.JitsiRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JitsiRoomRepository extends JpaRepository<JitsiRoom, UUID> {
    Optional<JitsiRoom> findByRoomName(String roomName);
    Optional<JitsiRoom> findByEventRef(String eventRef);
    List<JitsiRoom> findAllByCreatedByOrderByCreatedAtDesc(UUID createdBy);
    List<JitsiRoom> findAllByIdInAndActiveTrue(Iterable<UUID> ids);

    /** Bulk-deactivate stale rooms whose host never called deactivate (e.g. browser closed). */
    @Modifying
    @Query("UPDATE JitsiRoom r SET r.active = false, r.activeHostId = null WHERE r.active = true AND r.activatedAt < :before")
    int deactivateStaleRooms(Instant before);
}
