#Flog-Server


## 소개
포스팅 서비스를 만들고 있습니다.

## 버전
1. Kotlin 1.3.72
2. Spring Boot 2.3.3
3. Mysql 5.7 

## 실행방법

### localhost
```
./gradlew  -Dspring.profiles.active=local -Djasypt.encryptor.password=Private_Key bootrun
```
 
### Intellij
1. Environment Variable에 -Djasypt.encryptor.password=Private_Key 설정 
2. Active profile = local 작성후 실행 (default=production)

### Docker run 구동 방법
```
# gcloud로 환경세팅 후 
$ gcloud container clusters get-credentials "$credential" --region "$region" --project "$project"
$ docker pull gcr.io/flogmasters/flog-server
$ docker run -e SPRING.PROFILES.ACTIVE=dev -it -p 8080:8080 gcr.io/flogmasters/flog-server /bin/sh
spring.profiles.active 와 port 는 상황에 맞게 변경
```

### 테스트
```
./gradlew clean test
```

###Hosts
- 미정

