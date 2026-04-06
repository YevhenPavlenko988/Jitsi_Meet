package com.github.yevhen.jitsimeet.service;

import com.github.yevhen.jitsimeet.repository.JitsiRoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Periodically deactivates Jitsi rooms whose host left without calling the
 * deactivate endpoint (e.g. browser closed / page refreshed).
 *
 * A room is considered stale if it has been in "active" state for more than
 * STALE_HOURS hours without any update.
 */
@Component
public class RoomCleanupScheduler {

    private static final Logger log = LoggerFactory.getLogger(RoomCleanupScheduler.class);
    private static final long STALE_HOURS = 4;

    private final JitsiRoomRepository roomRepository;

    public RoomCleanupScheduler(JitsiRoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    /** Runs every 15 minutes. */
    @Scheduled(fixedDelay = 15 * 60 * 1000)
    @Transactional
    public void deactivateStaleRooms() {
        Instant threshold = Instant.now().minus(STALE_HOURS, ChronoUnit.HOURS);
        int count = roomRepository.deactivateStaleRooms(threshold);
        if (count > 0) {
            log.info("Deactivated {} stale Jitsi room(s) that were active for > {} hours", count, STALE_HOURS);
        }
    }
}
