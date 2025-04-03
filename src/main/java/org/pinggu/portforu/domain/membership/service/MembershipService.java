package org.pinggu.portforu.domain.membership.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.pinggu.portforu.common.dto.AuthMember;
import org.pinggu.portforu.common.exception.CustomException;
import org.pinggu.portforu.domain.membership.dto.request.CreateMembershipRequestDto;
import org.pinggu.portforu.domain.membership.dto.request.UpdateMembershipRequestDto;
import org.pinggu.portforu.domain.membership.dto.response.MembershipResponseDto;
import org.pinggu.portforu.domain.membership.entity.Membership;
import org.pinggu.portforu.domain.membership.repository.MembershipRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MembershipService {

    private final MembershipRepository membershipRepository;

    public MembershipResponseDto saveMembership(
            AuthMember authMember,
            @Valid CreateMembershipRequestDto request
    ) {

        Membership membership = Membership.builder()
                .name(request.getName())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .year(request.getYear())
                .build();

        Membership savedMembership = membershipRepository.save(membership);
        return new MembershipResponseDto(
                savedMembership.getId(),
                savedMembership.getName(),
                savedMembership.getPrice(),
                savedMembership.getQuantity(),
                savedMembership.getYear(),
                savedMembership.getCreatedAt(),
                savedMembership.getUpdatedAt(),
                savedMembership.getDeletedAt()
        );
    }

    public Page<MembershipResponseDto> findByAllMemberships(
            AuthMember authMember,
            Pageable pageable
    ) {
        return membershipRepository.findAll(pageable).map(membership -> new MembershipResponseDto(
                membership.getId(),
                membership.getName(),
                membership.getPrice(),
                membership.getQuantity(),
                membership.getYear(),
                membership.getCreatedAt(),
                membership.getUpdatedAt(),
                membership.getDeletedAt()
        ));
    }

    public MembershipResponseDto findByIdMemberships(
            AuthMember authMember,
            Long membershipId
    ) {
        Membership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "아이디가 없습니다."));

        return new MembershipResponseDto(
                membership.getId(),
                membership.getName(),
                membership.getPrice(),
                membership.getQuantity(),
                membership.getYear(),
                membership.getCreatedAt(),
                membership.getUpdatedAt(),
                membership.getDeletedAt()
        );
    }

    public MembershipResponseDto updateMembership(
            AuthMember authMember,
            Long membershipId,
            UpdateMembershipRequestDto request
    ) {
        Membership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "아이디가 없습니다."));

        membership.setName(request.getName());
        membership.setPrice(request.getPrice());
        membership.setQuantity(request.getQuantity());
        membership.setYear(request.getYear());

        Membership updatedMembership = membershipRepository.save(membership);

        return new MembershipResponseDto(
                updatedMembership.getId(),
                updatedMembership.getName(),
                updatedMembership.getPrice(),
                updatedMembership.getQuantity(),
                updatedMembership.getYear(),
                updatedMembership.getCreatedAt(),
                updatedMembership.getUpdatedAt(),
                updatedMembership.getDeletedAt()
        );
    }

    public MembershipResponseDto deleteMembership(AuthMember authMember, Long membershipId) {
        Membership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "아이디가 없습니다."));

//        membership.setDeletedAt(LocalDateTime.now());
        Membership deletedMembership = membershipRepository.save(membership);

        return new MembershipResponseDto(
                deletedMembership.getId(),
                deletedMembership.getName(),
                deletedMembership.getPrice(),
                deletedMembership.getQuantity(),
                deletedMembership.getYear(),
                deletedMembership.getCreatedAt(),
                deletedMembership.getUpdatedAt(),
                deletedMembership.getDeletedAt()
        );
    }
}
