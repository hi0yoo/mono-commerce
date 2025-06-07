# product-core

## 개요
상품 도메인의 핵심 비즈니스 로직을 관리합니다.  


## 패키지 구조
```bash
.
├── config
├── application
├── domain
└── infrastructure
```

### config
`product-core` 모듈에 필요한 설정을 담당합니다.  

QueryDsl 설정을 담당하는 `ProductCoreQuerydslConfiguration`과 같은 Config Bean이 예시입니다.

### application
모듈 외부에서 도메인에 접근하기 위한 진입점을 제공하고, 도메인 객체간 순서를 제어하여 요구사항을 처리할 수 있도록 관리합니다.  
  
진입점에 해당하는 `UseCase`는 `application.port.in` 패키지에 인터페이스로 제공합니다.  

타 모듈이나 외부 시스템, 특정 기술 구현 세부에 의존해야 하는 부분은 `application.port.out` 패키지에 추상화하여 작성합니다.  
이에 대한 구현은 `infrastructure.adapter.out` 패키지에 작성합니다.  


### domain
핵심 비즈니스 규칙을 담당하는 모델을 관리합니다.  
JPA Entity로 작성된 비즈니스 모델, 도메인 예외, 모델로는 표현하기 어려운 도메인 서비스 등이 있습니다.


### infrastructure
타 모듈이나 외부 시스템, 특정 기술 구현 세부에 의존하는 부분을 관리합니다.  

- `infrastructure.adapter.out` : `application.port.out`에 작성한 포트의 구현체인 `Adapter` 구현
- `infrastructure.repository` : `spring-data-jpa.JpaRepository`를 이용한 `Repository` 구현
