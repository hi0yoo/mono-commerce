# 이커머스 실험실 사이드 프로젝트 (2025-05 ~ 2025-06)

성능 개선 포인트 도출을 위한 실험을 진행하는 사이드 프로젝트입니다.

[노션 바로가기](https://thoughtful-sodium-948.notion.site/1f0d2ba300e680189cc0c5746563ef47?pvs=4)

## 📌 프로젝트 목적
- 효율적인 프로젝트 관리를 위한 모듈 분리
- 세부 도메인별 요구사항 충족을 위한 기술 선택
- 부하를 측정하고 성능 개선 포인트 도출


## 🔧 기술 스택
- Language: Kotlin 1.9.25 (Java 21)
- Framework: Spring Boot 3.4.5
- ORM: JPA, QueryDSL
- Database: PostgreSQL 17.4, Redis 7.0.4
- Infra: Docker 28.0.1, Docker Compose 2.32.4
- Test: Artillery 1.7.9 (Node.js v24.1.0)


## 📁 프로젝트 구조
```bash
.
├── artillery                  # artillery test scripts
├── docker                     # docker compose settings for local
└── modules                    # gradle multi-module projects
    ├── app                    # spring boot application (monolithic architecture)  
    ├── common                 # 공통 모듈
    │   ├── auth         # 애플리케이션 인증 관련 공통 모듈
    │   └── snowflake    # unique key generator with snowflake 
    ├── order                  # 주문 관련 모듈
    │   └── order-api    # 주문 도메인 로직 구현 및 API 모듈
    └── product                # 상품 관련 모듈
        ├── product-api        # 상품 API
        └── product-core       # 상품 도메인 로직 구현 모듈
```

### app
스프링부트 애플리케이션 진입점입니다.  
`common`, `order`, `product` 등 하위 모듈을 `implementation`으로 연결하여 모노리스 애플리케이션을 구성합니다.  
실제 비즈니스 개발은 하위 도메인 모듈에서 수행되며, 배포는 `app` 모듈을 빌드하여 진행합니다.  


### common
공통 모듈로, 여러 도메인에서 재사용 가능한 유틸리티 또는 공통 기능을 제공합니다.

- auth: 인증 관련 공통 모듈입니다. 현재는 인증 시스템을 구현하지 않았으며, 인터페이스와 `FakeUserContext` 컴포넌트를 통해 테스트용 유저 정보를 제공합니다.
- snowflake: 유니크한 ID를 생성할 수 있는 Snowflake 기반 ID 생성기입니다.


### order
주문 도메인의 요구사항을 달성하기 위한 하위 모듈을 관리합니다.

- [order-api](modules/order/order-api/README.md): 주문 도메인의 API 및 비즈니스 로직을 포함하는 모듈입니다.


### product
상품 도메인의 요구사항을 달성하기 위한 하위 모듈을 관리합니다.

- [product-api](modules/product/product-api/README.md): 상품 도메인의 API를 담당합니다.
- [product-core](modules/product/product-core/README.md): 상품 도메인의 핵심 비즈니스 모델 및 로직을 관리합니다.  

> 📌 **모듈 네이밍 규칙**  
> 도메인 요구사항을 충족시키기 위해 신규 모듈이 필요하면 다음과 같이 추가할 수 있습니다.  
> - 주문 도메인에서 배치 관련 기능 추가 : order-batch
> - 주문 도메인에서 비동기 이벤트 처리 : order-consumer



## 🐳 commerce-app 도커 컨테이너 실행 방법
```shell
# run postgresql docker compose 
docker compose -f ./docker/postgresql/docker-compose.yml up -d
# run redis docker compose 
docker compose -f ./docker/redis/docker-compose.yml up -d
# run commerce-app docker compose  
docker compose -f ./docker/commerce-app/docker-compose.yml up -d 
```

