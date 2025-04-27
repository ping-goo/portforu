package org.pinggu.portforu.emailing.scheduler;

import lombok.RequiredArgsConstructor;
import org.pinggu.portforu.emailing.service.JobCloseNotificationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JobCloseNotificationScheduler {

    private final JobCloseNotificationService notificationService;

    @Scheduled(cron = "0 50 7 * * *") // 매일 아침 10시 30분
    public void sendClosingSoonEmails() {
        Long exampleJobPostingId = 1L;
        String exampleJobTitle = "백엔드 개발자 모집";

        notificationService.notifyClosingSoon(exampleJobPostingId, exampleJobTitle);
    }
}
