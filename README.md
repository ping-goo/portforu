# PortForU - Team Ping-gu

---


# 프로젝트 소개
____

## 🚒 "포트폴리오를 구해줄게!"

'PortForU'는 개발자들의 포트폴리오를 공유하고 이에 맞는 채용공고들을 모아 추천받을 수 있는 서비스 입니다.

포트폴리오를 공유하고 이에 대한 피드백을 받을 수 있으며, 회원의 선호 직무 또는 기술과 연관된 채용 공고를 추천받거나, 이메일 알림으로 받을 수 있습니다.



### 핵심기능

- 채용공고 스크랩 및 이메일 알림
  - 기술 및 직무를 키워드로 검색, 조회
  - 회원이 스크랩한 공고에 대해 채용 마감 일정 직전 알림을 위한 이메일 발송 

- 포트폴리오 저장 및 공유
  - 포트폴리오 게시를 통한 정보 공유 및 피드백
  - 멤버십 가입을 통한 포트폴리오 무한 조회 및 댓글 작성
  - 포트폴리오 설명과 키워드와 연관된 채용정보 연계 추천

- 멤버십 대상 맞춤공고 발송
  - 회원 별 관련 직무 및 기술과 관련한 채용공고 업데이트에 대한 알림 발송

<details>
  <summary>기본기능</summary>

- 사용자 인증 및 관리
    - SpringSecurity&JWT를 이용한 회원가입/로그인
    - 일반회원가입
    - 소셜로그인
        - 카카오
        - 네이버
        - 구글
    - 사용자 프로필 관리
- 포트폴리오
    - 사용자 포트폴리오 업로드 기능
    - 포트폴리오 조회 기능
    - 스크랩 기능
    - 댓글/피드백 기능(멤버십만)
- 멤버십
    - 멤버십 생성(관리자)
    - 구독기능(toss)
- 기업별 채용공고 및 스크랩
    - IT기업 중심의 채용공고(잡코리아, 사람인)
    - 인재상, 스킬 및 채용 우대사항 제공
    - 직무/지역/경력/기술스택 필터링 및 검색 및 추천 공고 플로팅
    - 공고 상세
        - 공고 내용,지원 자격, 우대 기술 , 지원 링크 제공
        - 관심 공고 마감 임박 알림(이메일)
- 기타 기능
    - 관리자 페이지
    - 결제(PG) 연동
    - 알림 기능
</details>

------



# 프로젝트 개발

- 개발 일정 : 2025.04.01 ~ 2025.05.04
- 개발 주체 : Team Ping-gu

---
### Wireframe
<details>
  <summary>클릭해서 열기</summary>
여기 이미지 삽입
</details>

### ERD
<details>
  <summary>클릭해서 열기</summary>
여기 이미지 삽입
</details>

### API
<details>
  <summary> Auth </summary>

| method  | 기능               | URL | JWT Required | status                                                                              |
|---------|------------------|-----|--------------|-------------------------------------------------------------------------------------|
| <code>POST</code>    | 회원가입             | /api/v1/auth/sign-up | ❌           | <code>200 OK</code><br/>, <code>400 Bad Request</code>                              |
| <code>POST</code>    | 로그인              | /api/v1/auth/sign-in | ❌      | <code>200 OK</code><br/>, <code>400 Bad Request</code>, <code>404 Not Found</code>  | 
| <code>POST</code>    | refreshToken 재발급 | /api/v1/auth/refresh | ✅      | <code>200 OK</code><br/>, <code>401 Unauthorized</code> | 


<details>
  <summary>dto</summary>

<회원가입 request body>

```json
{
  "email": "string",
  "password": "string",
  "name": "string",
  "phoneNumber": "string",
  "address": "string"
}
```
<회원가입 response body>
```json
{
    "data": {
    "accessToken": "JWT_TOKEN",
    "refreshToken": "JWT_TOKEN",
    "id": "long",
    "email": "string",
    "name": "string",
    "phoneNumber": "string",
    "address": "string",
    "userRole": "string",
    "createdAt": "instant",
    "updatedAt": "instant"
  }
}
```
<로그인 request body>
```json
{
  "email": "string",
  "password": "string"
}
```
<refresh Token 재발급>
```
- request header
   String bearerToken
```
</details>
</details>

<details>
  <summary> Member </summary>

| method              | 기능                    | URL                             | JWT Required | status                                                                                                              |
|---------------------|-----------------------|---------------------------------|--------------|---------------------------------------------------------------------------------------------------------------------|
| <code>GET</code>    | 내 정보 조회               | /api/v1/members/{id}            | ✅           | <code>200 OK</code><br/>, <code>401 Unauthorized</code>, <code>403 Forbidden</code>                                 |
| <code>GET</code>    | (ADMIN) 회원 단건 조회      | /api/v1/admin/members/{memberId} | ✅      | <code>200 OK</code><br/>, <code>401 Unauthorized</code>, <code>403 Forbidden</code>                                 | 
| <code>GET</code>    | (ADMIN) 회원 전체 조회(페이징) | /api/v1/admin/members           | ✅      | <code>200 OK</code><br/>, <code>401 Unauthorized</code>, <code>403 Forbidden</code>                                 | 
| <code>PUT</code>    | 회원정보 수정               | /api/v1/members/{id}             | ✅      | <code>200 OK</code><br/>, <code>400 Bad Request</code>, <code>401 Unauthorized</code>, <code>403 Forbidden</code>, <code>404 Not Found</code> | 
| <code>PUT</code>    | 비밀번호 수                | /api/v1/members/{id}/password | ✅      | <code>200 OK</code><br/>, <code>400 Bad Request</code>, <code>401 Unauthorized</code>, <code>403 Forbidden</code>, <code>404 Not Found</code> | 
| <code>DELETE</code> | 회원 탈퇴                 | /api/v1/members/{id}           | ✅      | <code>200 OK</code><br/>, <code>400 Bad Request</code>, <code>401 Unauthorized</code>, <code>403 Forbidden</code>, <code>404 Not Found</code> | 
| <code>DELETE</code> | (ADMIN) 회원 삭제         | /api/v1/admin/members/{memberId}  | ✅      | <code>200 OK</code><br/>, <code>400 Bad Request</code>, <code>401 Unauthorized</code>, <code>403 Forbidden</code>, <code>404 Not Found</code> | 

<details>
  <summary>dto</summary>
<details>
  <summary> 내 정보 조회 response body </summary>

```json
{
  "data": {
    "id": "long",
    "email": "string",
    "name": "string",
    "phoneNumber": "string",
    "address": "string",
    "userRole": "string",
    "createdAt": "instant",
    "updatedAt": "instant"
  }
}
```

</details>

<details>
  <summary>(ADMIN) 회원 단건 조회 response body</summary>

```json
{
  "data": {
    "id": "long",
    "email": "string",
    "name": "string",
    "phoneNumber": "string",
    "address": "string",
    "userRole": "string",
    "createdAt": "instant",
    "updatedAt": "instant"
  }
}
```
</details>

<details>
  <summary> (ADMIN) 회원 전체 조회 response body </summary>

```json
{
  "data": [
    {
      "id": "long",
      "email": "string",
      "name": "string",
      "phoneNumber": "string",
      "address": "string",
      "userRole": "string",
      "createdAt": "instant",
      "updatedAt": "instant"
    },
    {
      "id": "long",
      "email": "string",
      "name": "string",
      "phoneNumber": "string",
      "address": "string",
      "userRole": "string",
      "createdAt": "instant",
      "updatedAt": "instant"
    } 
  ],
  "page": {
    "pageNum": 1,
    "pageSize": 10,
    "totalElement": 2,
    "totalPage": 1
  }
}
```
</details>

<details>
  <summary> 회원정보 수정 dto</summary>

- request body
```json
{
    "name": "string",
    "phoneNumber": "string",
    "address": "string"
}
```
- response body
```json
{
  "data": {
      "id": "long",
      "email": "string",
      "name": "string",
      "phoneNumber": "string",
      "address": "string",
      "userRole": "string",
      "createdAt": "instant",
      "updatedAt": "instant"
    }
}
```

</details>

<details>
  <summary> 비밀번호 수정 dto</summary>

- request body
```json
{
  "oldPassword": "string",
  "newPassword": "string"
}
```
- response body
```json
{
  "data": {
    "id": "long",
    "email": "string",
    "name": "string",
    "phoneNumber": "string",
    "address": "string",
    "userRole": "string",
    "createdAt": "instant",
    "updatedAt": "instant"
  }
}
```

</details>


<details>
  <summary> 회원탈퇴 dto</summary>

- request body
```json
{
  "Password": "string",
  "passwordConfirm": "string"
}
```
- response body
```json
{
  "data": {
    "id": "long"
  }
}
```

</details>

<details>
  <summary> (ADMIN) 회원 삭제 response body</summary>

- response body
```json
{
  "data": {
    "id": "long"
  }
}
```

</details>

</details>
</details>

<details>
  <summary> Portfolio </summary>

| method              | 기능          | URL                              | JWT Required | Parameters                                                                        | status                                                      |
|---------------------|-------------|----------------------------------|----------|-----------------------------------------------------------------------------------|-------------------------------------------------------------|
| <code>Post</code>   | 포트폴리오 생성    | /api/v1/portfolios               | ✅       | NONE                                                                              | <code>200 OK</code><br/>, <code>400 Bad Request</code> |
| <code>GET</code>    | 포트폴리오 목록 조회 | /api/v1/portfolios               | ✅      | Query: - <code>page</code> (int, default: 1) <code>size</code> (int, default: 10) | <code>200 OK</code><br/>, <code>400 Bad Request</code> | 
| <code>GET</code>    | 포트폴리오 단일 조회 | /api/v1/portfolios/{portfolioId} | ✅      | Path: - <code>portfolioId</code> (Long)                                           | <code>200 OK</code><br/>, <code>404 Not Found</code> | 
| <code>PUT</code>    | 포트폴리오 수정    | /api/v1/portfolios/{portfolioId} | ✅      | Path: - <code>portfolioId</code> (Long)                                          | <code>200 OK</code><br/>, <code>400 Bad Request</code> |
| <code>DELETE</code> | 포트폴리오 삭제    | /api/v1/portfolios/{portfolioId} | ✅      | Path: - <code>portfolioId</code> (Long)                                          | <code>200 OK</code><br/>, <code>400 Bad Request</code> |

<details>
  <summary>dto</summary>
<details>
  <summary> 포트폴리오 생성 dto </summary>
- request body

```json
{
    "title" : "string",
    "description" : "string",
    "fileUrl" : "string"
}
```
- response body
```json
{
  "data": {
    "id": "long",
    "userId": "long",
    "email": "string",
    "name": "string",
    "phoneNumber": "string",
    "address": "string",
    "userRole": "string",
    "createdAt": "instant",
    "updatedAt": "instant"
  }
}
```


</details>

<details>
  <summary>포트폴리오 목록 조회 response body</summary>

```json
{
  "data": {
    "portfolio": {
      "id": "long",
      "userId": "long",
      "title": "string",
      "description": "string",
      "fileUrl": "string",
      "views": "int",
      "createdAt": "LocalDateTime",
      "updatedAt": "LocalDateTime",
      "deletedAt": "LocalDateTime"
    },
    
    "pageable": {
      "page": "Integer",
      "size": "Integer",
      "totalElements": "Long",
      "totalPages": "Integer"  }
  }
}
```
</details>

<details>
  <summary> 포트폴리오 단일 조회 response body </summary>

```json
{ 
  "data": {
    "id": "long",
    "userId": "long",
    "title": "string",
    "description": "string",
    "fileUrl": "string",
    "views": "int",
    "createdAt": "LocalDateTime",
    "updatedAt": "LocalDateTime",
    "deletedAt": "LocalDateTime"
  },
  "message": "포트폴리오 단일 조회 성공"}
```
</details>

<details>
  <summary> 포트폴리오 수정 dto</summary>

- request body
```json
{ 
    "title": "string",
    "description": "string",
    "fileUrl": "string"
}
```
- response body
```json
{
  "data": {
    "id": "long",
    "userId": "long",
    "title": "string",
    "description": "string",
    "fileUrl": "string",
    "views": "int",
    "createdAt": "LocalDateTime",
    "updatedAt": "LocalDateTime",
    "deletedAt": "LocalDateTime"
  },
  "message": "포트폴리오 수정 성공"}
```

</details>

<details>
  <summary> 포트폴리오 삭제 response body</summary>

- response body
```json
{ 
    "data": null,
    "message": "포트폴리오 삭제 성공"
}

```

</details>

</details>
</details>

<details>
  <summary> Comment </summary>

| method              | 기능                | URL                              | JWT Required | Parameters                                       | status                                                      |
|---------------------|-------------------|----------------------------------|----------|-------------------------------------------------------|-------------------------------------------------------------|
| <code>Post</code>   | 댓글 생성             | /api/v1/portfolios/{portfolioId}/comments  | ✅       | Path: - <code>portfolioId</code> (Long)   | <code>200 OK</code><br/>, <code>400 Bad Request</code>, <code>404 Not Found</code> |
| <code>GET</code>    | 단일 포트폴리오 댓글 목록 조회 | /api/v1/portfolios/{portfolioId}/comments | ✅      | Path: - <code>portfolioId</code> (Long) | <code>200 OK</code><br/>, <code>400 Bad Request</code>, <code>404 Not Found</code> |
| <code>PUT</code>    | 댓글 수정             | /api/v1/comments/{commentId} | ✅      | Path: - <code>portfolioId</code> (Long)                  | <code>200 OK</code><br/>, <code>400 Bad Request</code>, <code>404 Not Found</code> |
| <code>DELETE</code> | 댓글 삭제             | /api/v1/comments/{commentId} | ✅      | Path: - <code>portfolioId</code> (Long)                  | <code>200 OK</code><br/>, <code>400 Bad Request</code>, <code>403 Forbidden</code>, <code>404 Not Found</code> |

<details>
  <summary>dto</summary>
<details>
  <summary> 댓글 생성 dto </summary>
- request body

```json
{
    "content" : "string"
}
```
- response body
```json
{
  "message": "댓글이 생성되었습니다."
}
```


</details>

<details>
  <summary> 단일 포트폴리오 댓글 목록 조회 response body</summary>

```json
{
      "id": "long",
      "userId": "long",
      "portfolioId": "long",
      "content": "String"
}
```
</details>

<details>
  <summary> 댓글 수정 dto </summary>

- request body
```json
{
  "content" : "string"
}
```
- response body
```json
{
  "message": "댓글이 수정되었습니다."
}
```

</details>

<details>
  <summary> 댓글 삭제 response body</summary>

- response body
```json
{
  "message": "댓글이 삭제되었습니다."
}
```

</details>

</details>
</details>

<details>
  <summary> JobPosting </summary>

| method              | 기능             | URL                                 | JWT Required | Parameters                                                                        | status                                                      |
|---------------------|----------------|-------------------------------------|----------|-----------------------------------------------------------------------------------|-------------------------------------------------------------|
| <code>Post</code>   | (Admin)채용공고 생성 | /api/v1/admin/job-postings                | ✅       | NONE                                                                              | <code>200 OK</code><br/>, <code>400 Bad Request</code>, <code>403 Forbidden</code> |
| <code>GET</code>    | 채용공고 목록 조회     | api/v1/job-postings                 | ✅      | Query: - <code>page</code> (int, default: 1) <code>size</code> (int, default: 10) | <code>200 OK</code><br/> |
| <code>GET</code>    | 채용공고 상세조회      | /api/v1/job-postings/{jobPostingId} | ✅      | Path: - <code>jobPostingId</code> (Long)                                          | <code>200 OK</code><br/>, <code>404 Not Found</code> |
| <code>PUT</code>    | (Admin)채용공고 수정    | /api/v1/admin/job-postings/{jobPostingId} | ✅      | Path: - <code>jobPostingId</code> (Long)                                           | <code>200 OK</code><br/>, <code>400 Bad Request</code>, <code>403 Forbidden</code>, <code>404 Not Found</code> |
| <code>DELETE</code> | (Admin)채용공고 삭제        | /api/v1/admin/job-postings/{jobPostingId} | ✅      | Path: - <code>jobPostingId</code> (Long)                                           | <code>200 OK</code><br/>, <code>403 Forbidden</code>, <code>404 Not Found</code> |

<details>
  <summary>dto</summary>
<details>
  <summary> 채용공고 생성 dto </summary>
- request body

```json
{
  "title": "string",
  "company": "string",
  "location": "string",
  "link": "string",
  "salary": "string",
  "duty": "string",
  "employmentType": "string",
  "educationLevel": "string",
  "experienceYears": "string",
  "keyAbilities": "string",
  "minExperienceYears": "integer",
  "maxExperienceYears": "integer",
  "hiringStartAt": "datetime",
  "hiringEndAt": "datetime",
  "skills": "string"
}

```
- response body
```json
{
  "id": "long",
  "title": "string",
  "company": "string",
  "location": "string",
  "link": "string",
  "salary": "string",
  "duty": "string",
  "employmentType": "string",
  "educationLevel": "string",
  "experienceYears": "string",
  "keyAbilities": "string",
  "minExperienceYears": "integer",
  "maxExperienceYears": "integer",
  "hiringStartAt": "datetime",
  "hiringEndAt": "datetime",
  "skills": "string",
  "createdAt": "datetime",
  "updatedAt": "datetime",
  "isDeleted": "boolean"
}
```


</details>

<details>
  <summary> 채용 공고 목록 조회 response body</summary>

```json
{
  "data": [
    {
      "id": "Long",
      "title": "String",
      "company": "String",
      "location": "String",
      "link": "String",
      "salary": "String",
      "duty": "String",
      "employmentType": "String",
      "educationLevel": "String",
      "experienceYears": "String",
      "keyAbilities": "String",
      "minExperienceYears": "Integer",
      "maxExperienceYears": "Integer",
      "hiringStartAt": "ZonedDateTime",
      "hiringEndAt": "ZonedDateTime",
      "skills": "String",
      "createdAt": "Instant",
      "updatedAt": "Instant",
      "isDeleted": "Boolean"
    }
  ],
  "page": {
    "pageNum": "int",
    "pageSize": "int",
    "totalElement": "int",
    "totalPage": "int"
  }
}
```
</details>

<details>
  <summary> 채용 공고 상세 조회 response body</summary>

```json
{
  "id": "long",
  "title": "string",
  "company": "string",
  "location": "string",
  "link": "string",
  "salary": "string",
  "duty": "string",
  "employmentType": "string",
  "educationLevel": "string",
  "experienceYears": "string",
  "keyAbilities": "string",
  "minExperienceYears": "integer",
  "maxExperienceYears": "integer",
  "hiringStartAt": "datetime",
  "hiringEndAt": "datetime",
  "skills": "string",
  "createdAt": "datetime",
  "updatedAt": "datetime",
  "isDeleted": "boolean"
}
```
</details>

<details>
  <summary> 채용공고 수정 dto </summary>

- request body

```json
{
  "title": "string",
  "company": "string",
  "location": "string",
  "link": "string",
  "salary": "string",
  "duty": "string",
  "employmentType": "string",
  "educationLevel": "string",
  "experienceYears": "string",
  "keyAbilities": "string",
  "minExperienceYears": "integer",
  "maxExperienceYears": "integer",
  "hiringStartAt": "datetime",
  "hiringEndAt": "datetime",
  "skills": "string"
}

```
- response body
```json
{
  "id": "long",
  "title": "string",
  "company": "string",
  "location": "string",
  "link": "string",
  "salary": "string",
  "duty": "string",
  "employmentType": "string",
  "educationLevel": "string",
  "experienceYears": "string",
  "keyAbilities": "string",
  "minExperienceYears": "integer",
  "maxExperienceYears": "integer",
  "hiringStartAt": "datetime",
  "hiringEndAt": "datetime",
  "skills": "string",
  "createdAt": "datetime",
  "updatedAt": "datetime",
  "isDeleted": "boolean"
}
```

</details>

<details>
  <summary> 채용공고 삭제 response body</summary>

- response body
```json
{
  "message": "채용공고가 삭제되었습니다."
}
```

</details>

</details>
</details>

<details>
  <summary> Scrap </summary>

| method              | 기능           | URL                              | JWT Required | Parameters                                                                                                                    | status                                                                                                         |
|---------------------|--------------|----------------------------------|----------|-------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------|
| <code>Post</code>   | 채용 공고 스크랩 기능 | /api/v1/scraps/job-postings/{jobPostingId}  | ✅       | Path: - <code>jobPostingId</code> (Long)                                                                                      | <code>200 OK</code><br/>, <code>403 Forbidden</code>, <code>404 Not Found</code>                               |
| <code>GET</code>    | 스크랩 목록 조회    | /api/v1/scraps/{memberId} | ✅      | Path: - <code>memberId</code> (Long) Query: - <code>pageNum</code> (int, default: 1) <code>pageSize</code> (int, default: 10) | <code>200 OK</code><br/>,  <code>403 Forbidden</code>                            |

<details>
  <summary>dto</summary>
<details>
  <summary> 채용공고 스크랩 기능 response body </summary>

```json
{
  "data": {
    "id" : "long",
    "memberId" : "long",
    "jobPostingId" : "long",
    "createdAt" : "LocalDateTime",
    "deletedAt" : "LocalDateTime"
  }
}
```

</details>

<details>
  <summary> 스크랩 목록 조회 response body</summary>

```json
{
  "data": [
    {
      "id": "Long",
      "title": "String",
      "company": "String",
      "location": "String",
      "link": "String",
      "salary": "String",
      "duty": "String",
      "employmentType": "String",
      "educationLevel": "String",
      "experienceYears": "String",
      "keyAbilities": "String",
      "minExperienceYears": "Integer",
      "maxExperienceYears": "Integer",
      "hiringStartAt": "ZonedDateTime",
      "hiringEndAt": "ZonedDateTime",
      "skills": "String",
      "createdAt": "Instant",
      "updatedAt": "Instant",
      "isDeleted": "Boolean"
    }
  ],
  "page": {
    "pageNum": "int",
    "pageSize": "int",
    "totalElement": "int",
    "totalPage": "int"
  }
}
```
</details>

</details>
</details>

<details>
  <summary> Membership </summary>

| method              | 기능        | URL                              | JWT Required | Parameters                                                                        | status                                                      |
|---------------------|-----------|----------------------------------|----------|-----------------------------------------------------------------------------------|-------------------------------------------------------------|
| <code>Post</code>   | 멤버십 생성    | /api/v1/memberships              | ✅       | NONE                                                                              | <code>200 OK</code><br/>, <code>400 Bad Request</code>, <code>404 Not Found</code> |
| <code>GET</code>    | 멤버십 목록 조회 | //api/v1/memberships             | ✅      | Query: - <code>page</code> (int, default: 1) <code>size</code> (int, default: 10) | <code>200 OK</code><br/>, <code>400 Bad Request</code>, <code>404 Not Found</code> | 
| <code>GET</code>    | 멤버십 상세 조회 | /api/v1/memberships/{membershipId} | ✅      | Path: - <code>membershipId</code> (Long)                                          | <code>200 OK</code><br/>, <code>400 Bad Request</code>, <code>404 Not Found</code> | 
| <code>PUT</code>    | 멤버십 수정    | /api/v1/memberships/{membershipId} | ✅      | Path: - <code>membershipId</code> (Long)                                           | <code>200 OK</code><br/>, <code>400 Bad Request</code>, <code>404 Not Found</code> |
| <code>DELETE</code> | 멤버십 취소    | /api/v1/memberships/{membershipId} | ✅      | Path: - <code>membershipId</code> (Long)                                           | <code>200 OK</code><br/>, <code>400 Bad Request</code>, <code>404 Not Found</code> |

<details>
  <summary>dto</summary>
<details>
  <summary> 멤버십 생성 dto </summary>
- request body

```json
{
    "title" : "string",
    "price" : "int",
    "year" : "int"
}
```
- response body
```json
{
  "message": "멤버십이 생성되었습니다."
}
```


</details>

<details>
  <summary>멤버십 목록 조회 response body</summary>

```json
{
  "data": {
    "membership": {
      "id": "long",
      "name": "string",
      "price": "int",
      "year": "int"
    },
    
    "pageable": {
      "page": "Integer",
      "size": "Integer",
      "totalElements": "Long",
      "totalPages": "Integer"  }
  }
}
```
</details>

<details>
  <summary> 멤버십 상세 조회 response body </summary>

```json
{
      "id": "long",
      "name": "string",
      "price": "int",
      "year": "int"
}

```
</details>

<details>
  <summary> 멤버십 수정 dto</summary>

- request body
```json
{
  "title" : "string",
  "price" : "int",
  "year" : "int"
}
```
- response body
```json
{
  "message": "멤버십이 수정되었습니다."
}
```

</details>

<details>
  <summary> 멤버십 삭제 response body</summary>

- response body
```json
{
    "message": "멤버십이 취소되었습니다."
}

```

</details>

</details>
</details>


<details>
  <summary> Subscribe </summary>

| method              | 기능       | URL                                | JWT Required | Parameters                                                                        | status                                                                                                         |
|---------------------|----------|------------------------------------|----------|-----------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------|
| <code>Post</code>   | 구독 생성    | /api/v1/subscribe/{membershipId}   | ✅       | Path: - <code>membershipId</code> (Long)                               | <code>200 OK</code><br/>, <code>400 Bad Request</code>, <code>404 Not Found</code>                             |
| <code>GET</code>    | 구독 목록 조회 | /api/v1/subscribes                    | ✅      | Query: - <code>page</code> (int, default: 1) <code>size</code> (int, default: 10) | <code>200 OK</code><br/>, <code>400 Bad Request</code>, <code>404 Not Found</code>                             |
| <code>DELETE</code> | 구독 취소    | /api/v1/memberships/{membershipId} | ✅      | Path: - <code>membershipId</code> (Long)                                           | <code>200 OK</code><br/>, <code>400 Bad Request</code>, <code>403 Forbidden</code>, <code>404 Not Found</code> |

<details>
  <summary>dto</summary>
<details>
  <summary> 구독 생성 dto </summary>
- request body

```json
{
    "paymentMethod" : "string"
}
```
- response body
```json
{
    "id" : "long",
    "membershipId" : "long",
    "startDate" : "string",
    "endDate" : "string",
    "paymentMethod" : "string",
    "active" : "boolean"
}
```

</details>

<details>
  <summary>구독 목록 조회 response body</summary>

```json
{
  "data": [
    {
      "id" : "long",
      "membershipId" : "long",
      "startDate" : "string",
      "endDate" : "string",
      "paymentMethod" : "string",
      "active" : "boolean"
    }
  ],
  "page": {
    "pageNum": "int",
    "pageSize": "int",
    "totalElement": "int",
    "totalPage": "int"
  }
}
```
</details>

<details>
  <summary> 구독 취소 response body </summary>

```json
{
   "message" : "구독이 취소되었습니다."
}

```
</details>

</details>
</details>


---------------------------

## System Architecture

------
### CI/CD


### Cloud Architecture







