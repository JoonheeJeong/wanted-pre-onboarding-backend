# 원티드 프리온보딩 백엔드 인턴십(10월) 사전과제

## 📝 API 명세

| 완료  | Method   | End Point                        | 기능             |
|:---:|----------|----------------------------------|----------------|
|  ✅  | POST     | `/api/v1/jobs`                   | 채용공고 등록        |
|  ✅  | PUT      | `/api/v1/jobs/{job_id}`          | 채용공고 수정        |
|  ✅  | DELETE   | `/api/v1/jobs/{job_id}`          | 채용공고 삭제        |
|  ✅  | GET      | `/api/v1/jobs`                   | 채용공고 전체 조회     |
|     | GET      | `/api/v1/jobs?keyword={keyword}` | 채용공고 키워드 검색 조회 |
|  ✅  | GET      | `/api/v1/jobs/{job_id}`          | 채용공고 상세 조회     |
|     | POST     | `/api/v1/apply`                  | 채용 지원          |

## 📊 ERD
<img width="747" alt="wanted_preonboarding_erd" src="https://github.com/JoonheeJeong/wanted-pre-onboarding-backend/assets/22290112/a71d9c17-d713-4b14-a5ec-1d96ced0007d">

## ⚙️ 기능
기능별 요구사항 분석 및 구현과정 포함 필요

### 1. 채용공고 등록
#### 개요
회사가 채용공고를 등록함
#### 요구사항 분석
- 채용 포지션은 여러 범주가 섞인 복합 데이터이므로 직군, 직무, 경력 세 종류로 구분한다.
- 기술은 여러 개를 입력할 수 있도록 문자열 list로 입력 받는다.
#### Request
```http request
POST /api/v1/jobs HTTP/1.1
Host: localhost:8080
Content-Type: application/json;charset=UTF-8
Accept: application/json

{
   "companyId": 1,
   "group": "개발",
   "position": "백엔드 개발자",
   "career": "주니어",
   "reward": 1000000,
   "content": "원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..",
   "skills": [ "Java", "Spring Framework", "JPA", "SQL" ]
}
```
#### Response
성공: `201 Created`
```json
{
   "message": "채용공고가 등록되었습니다."
}
```
실패: `400 Bad Request`
  - 시간 남으면 세부 case로 분할
```json
{
   "message": "입력이 유효하지 않습니다." 
}
```
#### 구현과정
- 프론트 입장에서 필요한 요청과 응답을 기반으로 하여 필요 도메인을 도출했다.
- 이를 위해 컨트롤러 테스트를 바탕으로 레포지토리와 서비스의 테스트 작성 및 실제 비즈니스 로직 작성 순서로 작업을 진행했다.
- 성공과 실패 케이스가 공통된 응답을 갖도록 공통 응답 객체를 정의했다.
- 유효하지 않은 입력의 종류는 많겠으나 작업 기한을 고려하여 예외를 일반화하고 우선적으로 성공 케이스에 대해 완성시키는 것을 목적으로 삼았다.
- 채용공고라는 도메인은 회사, 기술에 종속적이므로 이들을 먼저 정의했고, 더미데이터를 작성했다.
- 직군, 직무, 경력은 상수이므로 `@JsonValue`를 이용하여 바인딩되게 했고, 바인딩 예외의 경우 `GlobalExceptionHandler`에서 처리하게 했다.
- 채용공고 도메인을 작성하고, ID, 생성시각, 수정시각을 자동생성하는 별도의 base 엔티티를 정의하여 활용했다.
- 채용공고는 기술을 리스트로 갖기 때문에 '채용공고 기술(`JobSkill`)'이라는 중간 테이블을 두었다.
- 당장 프로덕션 코드에서는 불필요하지만, 채용공고 기술 테이블의 저장 검증을 위해 개발 전용의 Profile을 이용하여 조회 메소드를 작성했다.

### 2. 채용공고 수정
#### 개요
- 회사가 채용공고를 수정함
- 회사의 ID 외에 채용공고의 모든 정보 수정 가능 
#### 요구사항 분석
- 변경할 데이터만 받는 것도 방법일 수 있는데, 그렇게 하면 `@NotNull`를 이용하지 못하고 프로퍼티별로 Null 체크를 해야 한다. 
  편의를 위하여 FE에 양해를 구하고 변경하지 않을 데이터 포함 모든 데이터를 다 받는다고 생각하자.
- 작업 기한에 따른 구현의 편의를 고려하여 수정된 내용이 없더라도 일관되게 200으로 응답한다.
- 채용공고 기술을 수정할 때는, 추가와 삭제가 동시에 일어날 수 있다.
#### Request
```http request
PUT /api/v1/jobs/1 HTTP/1.1
Host: localhost:8080
Content-Type: application/json;charset=UTF-8
Accept: application/json

{
   "group": "개발",
   "position": "백엔드 개발자",
   "career": "주니어",
   "reward": 2000000,
   "content": "원티드랩에서 백엔드 주니어 개발자를 '적극' 채용합니다. 자격요건은..",
   "skills": [ "Java", "Spring Framework", "JPA", "SQL" ]
}
```
#### Response
성공: `200 OK`
```json
{
   "message": "채용공고가 수정되었습니다."
}
```
실패: `400 Bad Request`
  - 시간 남으면 세부 case로 분할
```json
{
   "message": "입력이 유효하지 않습니다." 
}
```
#### 구현과정
- 전반적인 흐름은 채용공고 등록과 유사하게 수행했습니다.
- 채용공고(`Job`)와 채용공고기술(`JobSkill`)이 양방향이라 채용공고기술 수정 시 List 관리가 어려웠다. 
  - 먼저 작성한 코드에서는 직접 `JobSkillRepository`에 save하였으나 List 관리가 되지 않아서, 
    채용공고에 별도로 채용공고기술을 추가하는 메소드를 작성하고, cascade를 이용하여 저장되도록 했다.
- 채용공고 기술 수정 발생 시 mocking 데이터 만들기가 어려워서 통합 테스트로 대체하고, 레포지토리 테스트도 따로 수행하지 않았다. 

### 3. 채용공고 삭제
#### 개요
회사가 채용공고를 삭제함 <br/>
#### 요구사항 분석
- DB에서 레코드를 삭제하지 않고 상태 컬럼을 두고 만료 상태로 둘 수도 있지만, 여기서는 아예 레코드를 날려버리기로 한다.
- 이에 따라 요청 HTTP 메소드는 `DELETE`를 이용한다.
- 응답은 성공 시 `204 No Content`로, 실패 시 전과 같이 400으로 내려주면 될 것 같다. 
#### Request
```http request
DELETE /api/v1/jobs/1 HTTP/1.1
Host: localhost:8080
```
#### Response
성공: `204 No Content`

실패: `400 Bad Request`
```json
{
   "message": "해당 ID의 채용공고가 존재하지 않습니다." 
}
```
#### 구현 과정
- DB에서 삭제하면 되기 때문에 spring data jpa의 자동생성 메소드를 통해 쉽게 구현이 가능했다.
- 다만 데이터 존재 여부 검증을 위해 삭제 전 조회를 먼저 수행했다.
### 4-1. 채용공고 목록 전체 조회
#### 개요
사용자가 채용공고의 목록을 조회함 <br/>
#### 요구사항 분석
- 채용내용을 제외한 채용공고의 정보와 회사의 정보를 함께 하나의 응답 객체로 만들고, 이것의 List를 최종 응답으로 넘겨주어야 한다.
- GET 요청이므로 200 OK로 내려주면 될 것 같다.
- 별도의 실패 응답은 필요 없어 보인다.
#### Request
```http request
GET /api/v1/jobs HTTP/1.1
Host: localhost:8080
Accept: application/json
```
#### Response
성공: `200 OK`
```json
[
  {
    "jobId": 1,
    "companyName": "원티드",
    "country": "한국",
    "city": "서울",
    "group": "개발",
    "position": "백엔드 개발자",
    "career": "주니어",
    "reward": 2000000,
    "content": "원티드랩에서 백엔드 주니어 개발자를 '적극' 채용합니다. 자격요건은..",
    "skills": [ "Java", "Spring Framework", "JPA", "SQL" ]
  },
  {
    "jobId": 2,
    "companyName": "Vercel",
    "country": "미국",
    "city": "샌프란시스코",
    "group": "개발",
    "position": "프론트엔드 개발자",
    "career": "시니어",
    "reward": 4000000,
    "content": "Vercel 프론트 채용 !!! ...",
    "skills": [ "JavaScript", "GIT" ]
  },
    ...
]
```
#### 구현 과정
- 응답 DTO를 만들고, 이것을 List로 감싸서 Body로 응답한다.
- 채용공고 목록을 stream으로 응답 DTO로 변환했다.  
- 트랜잭션은 readOnly로 등록했다.
- 컨트롤러 테스트에서 print를 찍어서 확인했다.
- 서비스 통합 테스트로 레포지토리 테스트를 대체했다.

### 4-2. 채용공고 목록 키워드 검색 조회
#### 개요
- 사용자가 키워드를 활용하여 채용공고의 목록을 조회함

### 5. 채용공고 상세 조회
#### 개요
- 사용자가 특정 채용공고의 상세 정보를 조회함
- 목록 조회와 다르게 "채용내용"이 포함됨
- (선택) 해당 회사가 올린 다른 채용공고가 추가적으로 포함됨
#### 요구사항 분석
- 기본 응답은 목록 조회의 응답 DTO와 동일하다.
- (선택) 회사로 채용공고를 조회하여 다른 채용공고들의 ID 목록을 필드로 덧붙이면 된다.
#### Response
성공: `200 OK`
```json
{
  "jobId": 1,
  "companyName": "원티드",
  "country": "한국",
  "city": "서울",
  "group": "개발",
  "position": "백엔드 개발자",
  "career": "주니어",
  "reward": 2000000,
  "content": "원티드랩에서 백엔드 주니어 개발자를 '적극' 채용합니다. 자격요건은..",
  "skills": [ "Java", "Spring Framework", "JPA", "SQL" ],
  "otherJobIds" : [ 2, 3 ]
}
```
실패: `400 Bad Request`
```json
{
   "message": "해당 ID의 채용공고가 존재하지 않습니다." 
}
```
#### 구현 과정
- (선택) 회사의 다른 채용공고를 조회하기 위하여 `@Query` 및 JPQL을 활용하였다.
- 역시 서비스 통합 테스트를 진행하였다.
- 컨트롤러 테스트 시 더미데이터를 추가하여 다른 채용공고의 ID 목록이 조회되는지 다시 한 번 확인하였다. 
- 중복 코드들(상수 및 로직)을 추출하였다.

### 6. 채용 지원
#### 개요
- 사용자가 특정 채용공고에 지원함
- 한 사용자는 특정 채용공고에 대하여 1회만 지원 가능

## 🚎 Git 커밋 메시지 컨벤션
| tag      | 설명                              | 
|----------|---------------------------------|
| setup    | 프로젝트 설정 관련 수정                   |
| docs     | 문서 수정                           |
| feat     | 새로운 기능 추가                       |
| refactor | 기능 변경 없는 코드 구조 변경               |
| fix      | 기능 수정 및 오류 해결                   |
| test     | 테스트 추가 및 수정                     |
| style    | 컨벤션 일치화, 빈 줄 추가 및 삭제, 포매팅 등의 수정 |

## 🪧 코드 컨벤션
1. 제어문의 소괄호, 중괄호, 연산자 열기 전, 닫은 후는 항상 빈 칸 추가
2. 제어문의 내용이 한 줄이라도 항상 중괄호 사용 및 줄바꿈
3. 메소드명은 항상 현재형 동사로 시작
    ```java
    public void validateIfSomethingIsValid() {
        if (numCompanies + numJobs == 0) {
            throw new CirtainException();
        }
        // something to do...
    }
    ```
4. 모든 파일의 마지막에 빈 줄 필수
