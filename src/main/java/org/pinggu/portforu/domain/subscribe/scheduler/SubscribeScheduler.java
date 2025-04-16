package org.pinggu.portforu.domain.subscribe.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pinggu.portforu.domain.subscribe.entity.Subscribe;
import org.pinggu.portforu.domain.subscribe.repository.SubscribeRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubscribeScheduler {

    private final SubscribeRepository subscribeRepository;

    @Scheduled(cron = "0 0 * * * *")
    public void expireEndedSubscriptions() {
        Instant now = Instant.now();
        List<Subscribe> expiredSubs = subscribeRepository.findAllByEndDateBeforeAndDeletedAtIsNull(now);

        for (Subscribe sub : expiredSubs) {
            if (!sub.isDeleted()) {
                sub.delete(); // Soft delete 처리
                subscribeRepository.save(sub);
                log.info("구독 만료 처리 - subscribeId={}", sub.getId());
            }
        }
    }
}
