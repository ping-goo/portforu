package org.pinggu.portforu.domain.membership.service;

import lombok.RequiredArgsConstructor;
import org.pinggu.portforu.domain.membership.dto.request.MembershipSaveRequestDto;
import org.pinggu.portforu.domain.membership.dto.request.MembershipUpdateRequestDto;
import org.pinggu.portforu.domain.membership.dto.response.MembershipResponseDto;
import org.pinggu.portforu.domain.membership.entity.Membership;
import org.pinggu.portforu.domain.membership.repository.MembershipRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MembershipService {

    private final MembershipRepository membershipRepository;
    private final MembershipFinder membershipFinder;

    @Transactional
    public MembershipResponseDto saveMembership(MembershipSaveRequestDto requestDto) {
        Membership membership = Membership.builder()
                .name(requestDto.getName())
                .price(requestDto.getPrice())
                .quantity(requestDto.getQuantity())
                .year(requestDto.getYear())
                .build();

        Membership savedMembership = membershipRepository.save(membership);

        return MembershipResponseDto.from(savedMembership);
    }

    @Transactional(readOnly = true)
    public List<MembershipResponseDto> findAllMemberships() {
        return membershipRepository.findAllActiveMemberships().stream()
                .map(MembershipResponseDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public MembershipResponseDto findMembershipById(Long membershipId) {
        return MembershipResponseDto.from(membershipFinder.findById(membershipId));
    }

    @Transactional
    public MembershipResponseDto updateMembership(
            Long membershipId,
            MembershipUpdateRequestDto request
    ) {

        Instant now = Instant.now();
        membershipRepository.updateMembership(
                membershipId, request.getName(), request.getPrice(), request.getQuantity(), request.getYear(), now
        );

        Membership updatedMembership = membershipFinder.findById(membershipId);

        return MembershipResponseDto.from(updatedMembership);
    }

    @Transactional
    public Long deleteMembership(Long membershipId) {
        Membership membership = membershipFinder.findById(membershipId);

        return membership.softDelete();
    }

}
