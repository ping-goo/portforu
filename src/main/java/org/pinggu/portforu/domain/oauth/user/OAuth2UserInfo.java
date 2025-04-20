package org.pinggu.portforu.domain.oauth.user;

import java.util.Map;

//TODO 이거 팩토리 패턴 해보는거 좋음 맨아래 주석
public interface OAuth2UserInfo {
    //TODO getProvider메소드 추가, 언젠간 쓰임 뭊조건
    String getEmail();

    String getName();

    String getPhone();

    Map<String, Object> getAttributes();

}

//@Component
//public class OAuth2UserInfoFactory {
//    public OAuth2UserInfo from(String provider, Map<String, Object> attributes) {
//        return switch (provider.toLowerCase()) {
//            case "google" -> new GoogleUserInfo(attributes);
//            case "kakao" -> new KakaoUserInfo(attributes);
//            case "naver" -> new NaverUserInfo(attributes);
//            default -> throw new CustomException(...);
//        };
//    }
//}