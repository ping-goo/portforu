package org.pinggu.portforu.domain.subscribe.enums;

public enum SubscribeStatus {
    ACTIVE, // 정상 구독 상태
    CANCELLED, // 사용자가 취소했지만 만료 전까지는 유효
    EXPIRED // 기간 만료됨
}
