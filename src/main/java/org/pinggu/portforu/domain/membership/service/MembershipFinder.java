package org.pinggu.portforu.domain.membership.service;

import lombok.RequiredArgsConstructor;
import org.pinggu.portforu.common.exception.CustomException;
import org.pinggu.portforu.domain.membership.entity.Membership;
import org.pinggu.portforu.domain.membership.repository.MembershipRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MembershipFinder {

    private final MembershipRepository membershipRepository;

    public Membership findById(Long id) {
        Membership membership = membershipRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "멤버십이 존재하지 않습니다."));

        if (membership.getIsDeleted()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "이미 삭제된 멤버십입니다.");
        }

        return membership;
    }

}
