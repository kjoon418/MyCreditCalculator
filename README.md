# 학점 계산기
리프레시 토큰 추가 완료
## API 명세서
인증이 필요할 경우, Authorization 헤더에 Access token을 같이 담아 보내야 한다.
|경로|METHOD|인증 필요 여부|전달해야 할 값(name: value)|반환되는 값|설명|
|---|---|---|---|---|---|
|/authorization|POST|false|email: 이메일<br>password: 비밀번호<br>name: 유저명|accessToken: 엑세스 토큰<br>refreshToken: 리프레시 토큰|회원 가입|
|/authorization|GET|false|email: 이메일<br>password: 비밀번호|accessToken: 엑세스 토큰<br>refreshToken: 리프레시 토큰|로그인|
|/authorization/reissue|GET|true<br>(Access Token 대신 Refresh Token을 담아 보내야 함)|없음|accessToken: 엑세스 토큰|엑세스 토큰 재발급|
|/authorization/email|GET|false|email: 이메일|email: 이메일|이메일 사용 가능 여부(유효성) 확인|
|/expire|GET|true|없음|accessToken: 기간이 만료된 엑세스 토큰|로그아웃|
|/withdraw|DELETE|true|없음|accessToken: 기간이 만료된 엑세스 토큰|회원 삭제|
|/member/email|PATCH|true|email: 이메일|name: 이름|이메일 변경|
|/member/password|PATCH|true|password: 비밀번호|name: 이름|비밀번호 변경|
|/member/name|PATCH|true|name: 이름|name: 이름|이름 변경|
|/lecture|GET|true|없음|아래의 정보를 담은 JOSN 배열 반환<br>{<br>id: PK,<br>name: 강의명,<br>credit: 성적,<br>major: 전공명,<br>semester: 학기,<br>type: 강의 타입(하단 참고)<br>}|해당 회원의 모든 강의 조회|
|/lecture|POST|true|name: 강의명<br>credit: 성적<br>major: 전공명<br>semester: 학기<br>type: 강의 타입|id: PK<br>name: 강의명<br>credit: 성적<br>major: 전공명<br>semester: 학기<br>type: 강의 타입|강의 정보 등록|
|/lecture/{id}|DELETE|true|없음|id: PK<br>name: 강의명<br>credit: 성적<br>major: 전공명<br>semester: 학기<br>type: 강의 타입|강의 삭제|
|/lecture/search|GET|true|semester: 학기<br> majorOnly: 전공만 검색할지 여부|아래의 정보를 담은 JOSN 배열 반환<br>{<br>id: PK,<br>name: 강의명,<br>credit: 성적,<br>major: 전공명,<br>semester: 학기,<br>type: 강의 타입<br>}|조건에 부합하는 강의 조회|
|/credit|GET|true|없음|실수형 값|전체 강의의 평균 학점 조회|
|/credit/search|GET|true|semester: 학기<br> majorOnly: 전공만 검색할지 여부|실수형 값|조건에 부합하는 강의의 평균 학점 조회|

### 참고사항
- 다른 사용자가 사용하고 있는 이메일은 사용할 수 없다
- 이메일, 비밀번호, 이름에는 알파벳, 숫자, 일부 특수문자만이 허용된다(공백도 허용하지 않는다)
- 성적(credit)은 실수 값을 사용한다
- 학기(semester)는 정수 값을 사용한다
- 강의 타입(type)은 "MajorCoureses" 혹은 "GeneralEducationCoureses" 값만 지닐 수 있다
- 강의 정보 중, 강의명, 학기, 강의 타입에 대한 정보는 null 값을 가질 수 없다
- 성적 정보가 null인 강의는 학점 계산에서 제외된다
- 강의/학점 검색에서 특정 조건을 지정하지 않을 경우(값을 전달하지 않거나, null로 지정한 경우), 해당 조건 없이 검색한다

## ER-다이어그램
[ERDCloud](https://www.erdcloud.com/d/E75KjvnzSMLaRptQE)<br>
위 링크 참고
