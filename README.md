# DDD-demo

---

## 스펙

java8

SpringBoot 2.6.4

Lombok

Maven

---


## 패키지 구조


---

## 패키지 내 파일

- common
  - CommandLineAppStartupRunner.java : App 메인함수 실행 시 호출되는 클래스
  - CsvManager.java : csv 파일을 읽어들이는 기능 담당 클래스
  - PrintUtil.java : 콘솔화면의 출력을 담당 클래스


- constant
  - CommandKey.java : 콘솔화면에 입력될 커맨드를 관리하는 enum
  

- controller
  - AppController.java : 메인컨트롤러, 콘솔 입력값으로 기능제어

  
- domain
  - ShoppingMall.java : 쇼핑몰 도메인, 재고, 주문, 검증 기능 담당
  - Pay.java : 
  

- entity
  - Item.java : 상품 엔티티
  - RequestOrder : 주문서 엔티티


- entity
  - ItemService.java : 상품 기능 인터페이스
  - ItemServiceImpl.java : 상품인터페이스의 구현체

- test
  - MultiThread.java : 멀티스레드 주문테스트


---

## 개발방향

1. App 실행 시 CommandLineAppStartupRunner 메인컨트롤러 호출(AppController)
2. 출력만 담당하는 클래스 분리 생성
3. csv 담당 클래스 분리 생성
4. AppController 는 input 명령어를 통해 메인페이지, 주문, 앱종료 등의 기능을 제어(Spring 컨트롤러와 비슷한 컨셉)
5. 콘솔의 o, order, q, quit 명령어는 enum을 통해 관리
6. 쇼핑몰 도메인은 상품의 재고, 주문, 검증 기능을 담당하도록 설계
7. 결제에 필요한 상품, 주문, 결제 엔티티 분리 및 빌더 패턴으로 관리
8. 상품 서비스는 csv파일에서 상품리스트를 생성하는 기능 담당
9. 쇼핑몰 도메인의 결제 기능에서 멀티스레드 SoldOutException 기능 구현을 위해 
   1. 상품리스트는 ConcurrentHashMap으로 관리
   2. 주문 기능에서 synchronized 키워드 사용
