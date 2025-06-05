# 이커머스 실험실 사이드 프로젝트 (2025-05-01 ~ )

효율적인 아키텍처와 성능 개선 포인트 도출을 위한 실험을 진행하는 사이드 프로젝트입니다.

[노션 바로가기](https://thoughtful-sodium-948.notion.site/1f0d2ba300e680189cc0c5746563ef47?pvs=4)

## 📌 프로젝트 목적
- 이커머스 도메인을 중심으로 다양한 실험을 진행하는 프로젝트
- 효율적인 프로젝트 관리를 위한 모듈 분리
- 세부 도메인별 요구사항 충족을 위한 기술 선택
- 부하를 측정하고 성능 개선 포인트 도출


## 🔧 기술 스택
- Language: Kotlin 1.9.25 (Java 21)
- Framework: Spring Boot 3.4.5
- ORM: JPA, QueryDSL
- Database: PostgreSQL 17.4, Redis 7.0.4
- Infra: Docker 28.0.1, Docker Compose 2.32.4
- Test: Artillery 2.0.22 (Node.js v23.11.0)



## 🐳 commerce-app 도커 컨테이너 실행 방법
```shell
# run postgresql docker compose 
docker compose -f ./docker/postgresql/docker-compose.yml up -d
# run redis docker compose 
docker compose -f ./docker/redis/docker-compose.yml up -d
# run nginx & commerce-app docker compose  
docker compose -f ./docker/commerce-app/docker-compose.yml up -d 
```

