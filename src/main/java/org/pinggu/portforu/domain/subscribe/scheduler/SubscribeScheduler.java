package org.pinggu.portforu.domain.subscribe.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pinggu.portforu.domain.subscribe.entity.Subscribe;
import org.pinggu.portforu.domain.subscribe.enums.SubscribeStatus;
import org.pinggu.portforu.domain.subscribe.repository.SubscribeRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubscribeScheduler {

    private final SubscribeRepository subscribeRepository;

    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void expireEndedSubscriptions() {
        Instant now = Instant.now();
        List<Subscribe> expiredSubs = subscribeRepository.findAllByEndDateBeforeAndDeletedAtIsNull(now);

        for (Subscribe sub : expiredSubs) {
            if (sub.getStatus() == SubscribeStatus.ACTIVE && sub.getEndDate().isBefore(now)) {
                sub.expire();
                sub.getMembership().increaseQuantity();
                subscribeRepository.save(sub);
                log.info("만료된 구독 처리 및 정원 복구: subscribeId={}, membershipId={}", sub.getId(), sub.getMembership().getId());
            }
        }
    }
}
