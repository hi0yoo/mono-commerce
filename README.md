# 이커머스 실험실 사이드 프로젝트 (2025-05-01 ~ )

효율적인 아키텍처와 성능 개선 포인트 도출을 위한 실험을 진행하는 사이드 프로젝트입니다.

[노션 바로가기](https://thoughtful-sodium-948.notion.site/1f0d2ba300e680189cc0c5746563ef47?pvs=4)

## 📌 프로젝트 목적
- 이커머스 도메인을 중심으로 다양한 실험을 진행하는 프로젝트
- 효율적인 아키텍처 탐색, 요구사항 충족을 위한 기술 적용
- 부하를 측정하고 성능 개선 포인트 도출


## 🔧 기술 스택
- Language: Kotlin 1.9.25 (Java 21)
- Framework: Spring Boot 3.4.5
- ORM: JPA, QueryDSL
- Database: PostgreSQL 17.4, Redis 7.0.4
- Infra: Docker 28.0.1, Docker Compose 2.32.4
- Test: Artillery 2.0.22 (Node.js v23.11.0)

---
초기에는 계층 단위 모듈 분리였음
api
core
infra

현재는 도메인 단위 모듈 분리
app
product
order

이유는, 도메인별로 요구사항이 다르기 때문에 요구사항을 효율적으로 달성하려면 도메인별로 분리하는 것이 적절하다고 판단함
예를들어 order 기준으로 단일 모듈로 개발을 진행하다가 api, batch 등 도입으로 도메인이 커지면 필요에 따라 분리할 수 있음.
여기서도 api, batch 등 각 모듈에서 동일한 도메인 로직이 한 번에 관리되어야 한다면 order-core 모듈로 분리 가능. 


---
도메인
기술 분리 ?
프레임워크의 변경은 잦지 않다.
기능 구현을 위한 기술 스택은 변경이 잦을 수 있다.
(DB, REDIS, Kafka, RebbitMQ 등)

응집도 및 결합도 고려하여 모듈화
infra 모듈은? 필요없지 않을까?

기술 계층은 모듈로 분리하지 않는다.
모듈 내부에서 계층을 구분한다.





## 📁 프로젝트 구조
- app
- product
- order





```bash
.
├── artillery             # test scripts
├── docker                # docker compose settings for local
│   ├── commerce-app
│   ├── mysql
│   ├── postgresql
│   └── redis
└── modules               # gradle multi-module projects
    ├── api               # presentation layer
    │   └── commerce
    ├── core              # application, domain layer
    │   ├── core-common
    │   └── core-order
    └── infra             # infrastructure layer
        ├── infra-common
        └── infra-order
````
- artillery 디렉토리에는 Artillery를 이용한 테스트 스크립트를 관리합니다.
- docker 디렉토리는 로컬에서 시스템 구동 및 테스를 위한 docker-compose 파일들을 관리합니다.
  - commerce-app의 yaml 파일로 docker compose 실행시, 프로젝트 루트의 Dockerfile을 이용해 도커 이미지를 빌드한 후 컨테이너를 생성/실행합니다.

## 📦 module 설명
### api module
presentation layer 입니다.
SpringBootApplication을 포함하여 애플리케이션 진입점이 됩니다.

각 모듈을 조합하여 시스템 전체를 관리합니다.

### core module
핵심 도메인 관련 모듈로 application, domain layer을 구현합니다.

- core-common : 전체 도메인에서 공통적으로 사용해야 하는 부분을 관리합니다.
  - ex) 예외, ID 클래스, 공통 유틸리티 등
- core-order : 주문 도메인 모듈. 주문 관련 핵심 도메인 로직을 관리합니다.


### infra module
특정 기술에 의존하는 구현체를 관리합니다.
ex) JPA, QueryDsl, Redis, Session 등

- infra-common : core-common 의존 구현
- infra-order : core-order 의존 구현


## 📂 Package 구조
```bash
.
└── modules
    ├── api                                  # 컨트롤러·DTO·보안 설정 등 “웹 어댑터” 전담
    │   └── commerce
    │       └── commerce
    │           ├── CommerceApplication.kt
    │           ├── api
    │           └── config
    ├── architecture-test                    # 아키텍처 Rule test
    ├── core                                 # 도메인 규칙을 담당하는 모듈
    │   ├── core-common                      # core 모듈에서 공통으로 사용
    │   │   └── commerce
    │   │       └── common
    │   │           ├── application          # application layer
    │   │           │   └── auth
    │   │           └── domain               # domain layer
    │   │               ├── enums
    │   │               ├── exception
    │   │               └── id
    │   └── core-order                       # 주문 모듈 핵심 도메인 규칙 담당
    │       └── commerce
    │           └── order
    │               ├── application          # application layer
    │               │   └── port             # 시스템 외부 입출력 포트 인터페이스 패키지
    │               └── domain               # domain layer
    └── infra                                # 외부 자원/기술구현 담당 모듈
        ├── infra-common                     # infra 모듈에서 공통으로 사용
        │   └── commerce
        │       └── common
        │           └── infrastructure       # infrastructure layer
        │               └── auth
        └── infra-order                      # 주문 모듈 외부 자원 접근/기술 구현 담당
            └── commerce
                └── order
                    └── infrastructure       # infrastructure layer
                        ├── adapter          # port에 대한 adapter 구현
                        ├── config
                        ├── product
                        ├── repository
                        └── sequence
```

### 의존성 요약
```
api       →        core-order.application   / .port
 ↑                           ↓
 └── response dto    core-order.domain          ↓
                             ↓
                  infra-order.repository    / .adapter
```
- core-* 모듈은 순수 비즈니스 로직만 보유. 외부 기술 의존 제거
- infra-* 모듈은 Database, Redis 등 구현 세부 담당
- ArchUnit 규칙을 architecture-test 모듈에서 실행하여 core → infra 단방향 의존만 허용하도록 빌드시 자동 검증



## 🐳 commerce-app 도커 컨테이너 실행 방법
```shell
docker network create commerce-net
# run postgresql docker compose 
docker compose -f ./docker/postgresql/docker-compose.yml up -d
# run redis docker compose 
docker compose -f ./docker/redis/docker-compose.yml up -d
# run nginx & commerce-app docker compose  
docker compose -f ./docker/commerce-app/docker-compose.yml up -d 
```

