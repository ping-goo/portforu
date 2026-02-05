package org.pinggu.portforu.domain.elastic;

import org.junit.jupiter.api.Test;
import org.pinggu.portforu.domain.jobposting.elastic.document.JobPostingDocument;
import org.pinggu.portforu.domain.jobposting.elastic.service.JobPostingSearchService;
import org.pinggu.portforu.domain.jobposting.entity.JobPosting;
import org.pinggu.portforu.domain.jobposting.repository.JobPostingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.util.List;

@SpringBootTest
class SearchPerformanceTest {

    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Autowired
    private JobPostingSearchService jobPostingSearchService;

    @Test
    void 성능비교() {
        String keyword = "공고";
        int page = 0;
        int size = 10;

        StopWatch stopWatch = new StopWatch("성능비교");

        // RDBMS 검색 측정
        stopWatch.start("RDBMS");
        List<JobPosting> dbResults = jobPostingRepository.findByTitleContaining(keyword);
        stopWatch.stop();

        // Elasticsearch 검색 측정
        stopWatch.start("Elasticsearch");
        List<JobPostingDocument> esResults = jobPostingSearchService.search(keyword, page, size);
        stopWatch.stop();

        // 시간 추출
        long dbTime = stopWatch.getTaskInfo()[0].getTimeMillis();
        long esTime = stopWatch.getTaskInfo()[1].getTimeMillis();

        // 배수 계산
        double improvement = (double) dbTime / esTime;

        // 향상률
        double enhancementPercentage = ((double) (dbTime - esTime) / dbTime) * 100;

        // 결과 출력
        System.out.println(stopWatch.prettyPrint());
        System.out.printf("ES가 DB보다 약 %.2f배 빠릅니다.%n", improvement);
        System.out.printf("기존 대비 검색 속도가 약 %.2f%% 향상되었습니다.%n", enhancementPercentage);
    }
}