package org.pinggu.portforu.common.utils;

import lombok.RequiredArgsConstructor;
import org.pinggu.portforu.domain.jobposting.dto.request.JobPostingSaveRequestDto;
import org.pinggu.portforu.domain.jobposting.service.JobPostingService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

//@Component
//@RequiredArgsConstructor
//public class DummyDataInitializer implements CommandLineRunner {
//
//    private final JobPostingService jobPostingService;
//
//    @Override
//    public void run(String... args) {
//        System.out.println("=== 더미 데이터 1000개 생성을 시작합니다 ===");
//
//        for (int i = 1; i <= 100000; i++) {
//            JobPostingSaveRequestDto requestDto = JobPostingSaveRequestDto.builder()
//                    .title("공고 제목 " + i)
//                    .company("테스트 기업 " + (i % 10))
//                    .location("서울 강남구")
//                    .link("https://career.portforu.com/job/" + i)
//                    .salary("4000-6000")
//                    .duty("백엔드 개발")
//                    .employmentType("정규직")
//                    .educationLevel("대졸 이상")
//                    .experienceYears("신입-3년")
//                    .keyAbilities("협업능력, 성실함") // String 타입에 맞춤
//                    .minExperienceYears(0)
//                    .maxExperienceYears(3)
//                    .hiringStartAt(ZonedDateTime.now()) // ZonedDateTime 적용
//                    .hiringEndAt(ZonedDateTime.now().plusDays(30)) // ZonedDateTime 적용
//                    .skills("Java, Spring Boot, MySQL") // String 타입에 맞춤
//                    .build();
//
//            try {
//                jobPostingService.saveJobPosting(requestDto);
//            } catch (Exception e) {
//                // 중복 데이터 방지 등을 위해 에러 시 로그만 찍고 넘어감
//                continue;
//            }
//        }
//
//        System.out.println("=== 더미 데이터 1000개 생성 완료! ===");
//    }
//}