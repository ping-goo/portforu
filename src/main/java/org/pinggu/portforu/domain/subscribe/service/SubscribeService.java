package org.pinggu.portforu.domain.subscribe.service;

import lombok.RequiredArgsConstructor;
import org.pinggu.portforu.common.exception.CustomException;
import org.pinggu.portforu.domain.payment.entity.Payment;
import org.pinggu.portforu.domain.payment.repository.PaymentRepository;
import org.pinggu.portforu.domain.subscribe.dto.request.SubscribeRequestDto;
import org.pinggu.portforu.domain.subscribe.dto.response.SubscribeResponseDto;
import org.pinggu.portforu.domain.subscribe.entity.Subscribe;
import org.pinggu.portforu.domain.subscribe.repository.SubscribeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SubscribeService {

    private final SubscribeRepository subscribeRepository;
    private final PaymentRepository paymentRepository;

    // 구독 생성
    @Transactional
    public SubscribeResponseDto saveSubscribe(Long memberId, Long membershipId, SubscribeRequestDto requestDto) {

        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusMonths(1);

        // Subscribe도 builder 사용
        Subscribe subscribe = Subscribe.builder()
                .memberId(memberId)
                .membershipId(membershipId)
                .startDate(startDate)
                .endDate(endDate)
                .build();
        Subscribe savedSub = subscribeRepository.save(subscribe);

        Payment payment = Payment.builder()
                .paymentMethod(requestDto.getPaymentMethod())
                .status(Payment.PaymentStatus.COMPLETED)
                .subscribe(savedSub)
                .build();
        paymentRepository.save(payment);

        return SubscribeResponseDto.fromEntity(savedSub);
    }

    // 구독 목록 조회
    @Transactional(readOnly = true)
    public Page<SubscribeResponseDto> findSubscribes(Pageable pageable, Long memberId) {
        Page<Subscribe> page = subscribeRepository.findAllByMemberId(memberId, pageable);
        return page.map(SubscribeResponseDto::fromEntity);
    }

    // 구독 취소
    @Transactional
    public void deleteSubscribe(Long id) {
        if (!subscribeRepository.existsById(id)) {
            throw new CustomException(HttpStatus.NOT_FOUND, "해당 구독이 존재하지 않습니다");
        }
        subscribeRepository.deleteById(id);
    }
}

