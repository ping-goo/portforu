package org.pinggu.portforu.domain.jobposting.service;

import lombok.RequiredArgsConstructor;
import org.pinggu.portforu.common.domain.Pagecond;
import org.pinggu.portforu.common.exception.CustomException;
import org.pinggu.portforu.domain.jobposting.dto.request.JobPostingSaveRequestDto;
import org.pinggu.portforu.domain.jobposting.dto.request.JobPostingUpdateRequestDto;
import org.pinggu.portforu.domain.jobposting.dto.response.JobPostingResponseDto;
import org.pinggu.portforu.domain.jobposting.entity.JobPosting;
import org.pinggu.portforu.domain.jobposting.repository.JobPostingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class JobPostingService {

    private final JobPostingFinder jobPostingFinder;
    private final JobPostingRepository jobPostingRepository;

    @Transactional
    public JobPostingResponseDto saveJobPosting(JobPostingSaveRequestDto requestDto) {
        JobPosting jobPosting = JobPosting.builder()
                .name(requestDto.getName())
                .industry(requestDto.getIndustry())
                .address(requestDto.getAddress())
                .salary(requestDto.getSalary())
                .qualifications(requestDto.getQualifications())
                .preferential(requestDto.getPreferential())
                .postingDate(requestDto.getPostingDate())
                .closingDate(requestDto.getClosingDate())
                .build();

        jobPostingRepository.save(jobPosting);

        return JobPostingResponseDto.from(jobPosting);
    }

    @Transactional(readOnly = true)
    public Page<JobPostingResponseDto> findJobPostings(Pagecond pagecond) {
        Pageable pageable = PageRequest.of(pagecond.getPageNum() - 1, pagecond.getPageSize(), Sort.by(Sort.Order.desc("createdAt")));
        Page<JobPosting> jobPostings = jobPostingRepository.findAllByDeletedAtIsNull(pageable);

        return jobPostings.map(JobPostingResponseDto::from);
    }

    @Transactional(readOnly = true)
    public JobPostingResponseDto findJobPosting(Long jobPostingId) {
        JobPosting jobPosting = jobPostingFinder.findJobPostingById(jobPostingId);

        return JobPostingResponseDto.from(jobPosting);
    }

    @Transactional
    public JobPostingResponseDto updateJobPosting(Long jobPostingId, JobPostingUpdateRequestDto requestDto) {
        jobPostingFinder.findJobPostingById(jobPostingId);

        Instant now = Instant.now();
        jobPostingRepository.updateJobPosting(
                jobPostingId, requestDto.getName(), requestDto.getIndustry(), requestDto.getAddress(), requestDto.getSalary(),
                requestDto.getQualifications(), requestDto.getPreferential(), requestDto.getClosingDate(), now
        );

        JobPosting updatedJobPosting = jobPostingFinder.findJobPostingById(jobPostingId);

        return JobPostingResponseDto.from(updatedJobPosting);
    }

    @Transactional
    public Long deleteJobPosting(Long jobPostingId) {
        JobPosting jobPosting = jobPostingFinder.findJobPostingById(jobPostingId);

        return jobPosting.delete();
    }

}
