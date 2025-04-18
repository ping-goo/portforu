package org.pinggu.portforu.domain.subscribe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pinggu.portforu.common.dto.AuthMember;
import org.pinggu.portforu.common.exception.CustomException;
import org.pinggu.portforu.domain.member.entity.Member;
import org.pinggu.portforu.domain.member.repository.MemberRepository;
import org.pinggu.portforu.domain.member.service.MemberFinder;
import org.pinggu.portforu.domain.membership.entity.Membership;
import org.pinggu.portforu.domain.membership.repository.MembershipRepository;
import org.pinggu.portforu.domain.membership.service.MembershipFinder;
import org.pinggu.portforu.domain.payment.entity.Payment;
import org.pinggu.portforu.domain.payment.enums.PaymentStatus;
import org.pinggu.portforu.domain.payment.repository.PaymentRepository;
import org.pinggu.portforu.domain.payment.service.PaymentFinder;
import org.pinggu.portforu.domain.subscribe.dto.request.SubscribeRequestDto;
import org.pinggu.portforu.domain.subscribe.dto.response.SubscribeResponseDto;
import org.pinggu.portforu.domain.subscribe.entity.Subscribe;
import org.pinggu.portforu.domain.subscribe.enums.SubscribeStatus;
import org.pinggu.portforu.domain.subscribe.repository.SubscribeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.ZoneId;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscribeService {

    private final SubscribeRepository subscribeRepository;
    private final SubscribFinder subscribFinder;
    private final MemberFinder memberFinder;
    private final MembershipFinder membershipFinder;
    private final PaymentFinder paymentFinder;

    @Transactional
    public SubscribeResponseDto saveSubscribe(AuthMember authMember, Long membershipId, SubscribeRequestDto requestDto) {
        Member member = memberFinder.findMemberById(authMember.getId());
        Membership membership = membershipFinder.findByIdOrThrow(membershipId);

        paymentFinder.existsBySubscribe_Member_IdAndSubscribe_Membership_IdAndStatus(member, membershipId, PaymentStatus.PENDING);

        subscribFinder.hasValidSubscription(member, membershipId);

        long count = subscribeRepository.countActiveByMembership(membership, Instant.now());
        if (count >= membership.getQuantity()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "멤버십 정원이 초과되었습니다.");
        }

        int currentYear = Year.now().getValue();
        if (membership.getYear() != currentYear) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "해당 멤버십은 " + membership.getYear() + "년 전용입니다.");
        }

        Instant startDate = Instant.now();
        Instant endDate = LocalDateTime.of(currentYear, 12, 31, 23, 59, 59)
                .atZone(ZoneId.of("Asia/Seoul"))
                .toInstant();

        Subscribe subscribe = Subscribe.builder()
                .member(member)
                .membership(membership)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        Payment payment = Payment.builder()
                .paymentMethod(requestDto.getPaymentMethod())
                .status(PaymentStatus.PENDING)
                .subscribe(subscribe)
                .build();

        return SubscribeResponseDto.from(subscribe, payment);
    }

    @Transactional(readOnly = true)
    public Page<SubscribeResponseDto> findSubscribes(AuthMember authMember, Pageable pageable) {
        Member member = memberFinder.findMemberById(authMember.getId());

        return subscribeRepository.findAllByMemberAndDeletedAtIsNull(member, pageable)
                .map(subscribe -> {
                    Payment payment = paymentFinder.findBySubscribeId(subscribe.getId());
                    return SubscribeResponseDto.from(subscribe, payment);
                });
    }

    @Transactional
    public Long deleteSubscribe(Long memberId, Long subscribeId) {
        Subscribe subscribe = subscribFinder.findById(subscribeId);

        if (!subscribe.getMember().getId().equals(memberId)) {
            throw new CustomException(HttpStatus.FORBIDDEN, "내 구독만 취소할 수 있습니다.");
        }

        if (subscribe.isDeleted()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "이미 삭제된 구독입니다.");
        }

        Payment payment = paymentFinder.findBySubscribeId(subscribeId);

        if (payment.getStatus() == PaymentStatus.PENDING) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "결제가 진행되지 않은 구독은 취소할 수 없습니다.");
        }

        subscribe.cancel();
        subscribeRepository.save(subscribe);

        log.info("구독 취소됨: subscribeId={}, memberId={}, 상태={}", subscribeId, memberId, subscribe.getStatus());

        return subscribe.getId();
    }

    @Transactional
    public void updateSubscriptionStatus(Long subscribeId, PaymentStatus paymentStatus) {
        Subscribe subscribe = subscribFinder.findById(subscribeId);

        if (paymentStatus == PaymentStatus.COMPLETED) {
            subscribe.activate();
            subscribeRepository.save(subscribe);
            log.info("구독 활성화 완료: subscribeId={}, 상태={}", subscribeId, subscribe.getStatus());
        } else if (paymentStatus == PaymentStatus.FAILED) {
            subscribe.fail();
            subscribeRepository.save(subscribe);
            log.info("구독 실패 처리됨: subscribeId={}, 상태={}", subscribeId, subscribe.getStatus());
        } else if (paymentStatus == PaymentStatus.EXPIRED) {
            subscribe.expire();
            subscribeRepository.save(subscribe);
            log.info("구독 만료 처리됨: subscribeId={}, 상태={}", subscribeId, subscribe.getStatus());
        }
    }

}
