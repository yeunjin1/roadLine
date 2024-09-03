## 1. 개요
   - 여행 일정과 지출 내역을 관리하는 안드로이드 네이티브 앱
## 2. 목적
   - [안드로이드 공식 홈페이지](https://developer.android.com/topic/architecture?hl=ko)에서 권장하는 클린 아키텍처 적용
   - MVVM 디자인 패턴 적용
   - 유지보수하기 좋은 코드를 만드는 것이 목적
## 3. 화면 설명
   - 여행 목록 (메인 화면)  
     <img src="https://github.com/user-attachments/assets/94053625-c06e-451d-aa02-4cfa8fba0034" width="200"/>
     <img src="https://github.com/user-attachments/assets/21a983f8-40fb-49b2-9183-35b1dbf88390" width="200"/>
     <img src="https://github.com/user-attachments/assets/21d4c5ba-ae0c-4d24-84a1-7daaa31b2d82" width="200"/>
   - 여행 일정  
     <img src="https://github.com/user-attachments/assets/7fb18686-449d-464e-8efa-669d4608050b" width="200"/>
     <img src="https://github.com/user-attachments/assets/89f25bdd-0d92-4db6-b8f0-3ece9aa6d43a" width="200"/>
     <img src="https://github.com/user-attachments/assets/4a467b06-9f39-45eb-93a6-59347f4f10a1" width="200"/>
   - 가계부  
     <img src="https://github.com/user-attachments/assets/46e89b05-ce1b-4118-bdf6-0ddaffad6f0a" width="200"/>
     <img src="https://github.com/user-attachments/assets/3c00e5d0-d161-4c17-9ed0-c4f9a70aec3b" width="200"/>
     <img src="https://github.com/user-attachments/assets/b875d2fe-ab5f-4679-8401-efcaa15beb24" width="200"/>
## 4. 주요 라이브러리
   - Andorid/Kotlin 라이브러리
     - Room
     - Databinding
       - BindingAdapter, 단방향/양방향 데이터 바인딩 적용
       - 화면에 데이터를 셋팅하는 코드를 xml 바인딩으로 간결하게 작성할 수 있었음
  - Hilt
    - 어노테이션을 통해 의존성 주입
  - Flow
    - LiveData는 안드로이드 종속 라이브러리이기 때문에 Kotlin 모듈인 Domain Layer에서 사용할 수 없음 (Flow는 순수 kotlin 라이브러리)
    - Flow는 cold stream 방식으로 연속으로 들어오는 데이터를 처리하기 어려워 hot stream 방식인 SharedFlow, StateFlow 사용
  - Retrofit
    - 환율 데이터를 가져오기 위한 네트워크 통신 시 사용
    - [한국수출입은행 api](https://www.koreaexim.go.kr/ir/HPHKIR020M01?apino=2&viewtype=C&searchselect=&searchword=)
  - Google Maps
    - 일정 표시, 수정, 추가 시 사용
    - https://developers.google.com/maps/documentation/android-sdk/overview?hl=ko
    - https://developers.google.com/maps/documentation/places/android-sdk/overview?hl=ko
  - Chart
    - 가계부의 통계 화면에서 원형 차트에 사용
    - https://github.com/PhilJay/MPAndroidChart
  - calendarView
    - 여행을 추가할때 날짜 선택시 사용
    - https://github.com/kizitonwose/Calendar
## 5. 아키텍처 설명
  - 모듈 분리
    - app
      - Application Module
      - 앱 진입점, DI를 구성
    - data
      - Andorid Library
      - Room 데이터베이스에 접근하고 서버와 통신하는 역할
    - domain
      - Java or Kotlin Library
        - Android 종속성이 없으므로 Andorid 라이브러리를 사용할 수 없음
        - 다를 플랫폼에도 재사용 가능
      - 비즈니스 로직 구성
    - presentation
      - Andorid Library
      - 화면 UI 구성, 화면에 데이터 표시        
    <img src="https://github.com/user-attachments/assets/c8043bdc-8540-4f69-9432-803a077db586" width="300"/>
    <img src="https://github.com/user-attachments/assets/4690e186-dd3d-42e8-9c5c-6a5bdd0ffa3e" width="600"/>  
    <img src="https://github.com/user-attachments/assets/16811651-b3dc-4fe0-b513-6fe2e82ffe47" width="400"/>

