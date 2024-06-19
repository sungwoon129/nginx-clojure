## nginx-clojure 웹서버를 활용한 HTML 수정 ##
### 구글 검색 페이지 로고 변경하기 ### 

Nginx-Clojure는 Nginx 웹 서버에서 Clojure, Java, 그리고 Groovy 언어로 작성된 웹 애플리케이션을 실행할 수 있도록 해주는 모듈입니다.  
이 모듈은 Nginx의 고성능과 비동기 이벤트 기반 아키텍처를 활용하여, Clojure와 같은 JVM 기반 언어로 작성된 코드를 실행할 수 있게 해줍니다.

Nginx-Clojure는 아래와 같은 목적으로 주로 활용합니다.

> + HTTP 요청 처리  
> + 필터링 및 리다이렉션  
> + 동적 컨텐츠 생성  
> + WebSocket 지원  
> + RESTful API 구현  
 
서버로 들어온 요청에 대해서 처리하는 웹서버의 역할을 수행할 수 있어 위와 같은 일들이 가능합니다.
하지만 규모가 크고 트래픽이 많은 서비스의 경우 SpringBoot나 NodeJS 같은 프레임워크를 활용해 별도의 서버를 구축하고 Nginx는 리버스 프록시 서버로서의 역할을 수행하는 경우가 더 일반적입니다.  
그래서 nginx-clojure 는 nginx 서버에서 JVM 기반의 언어로 이루어진 프로그램을 실행하는 목적으로 주로 활용됩니다.  
별도의 서버를 만들지 않고 클라이언트의 요청에 대해서 어떤 처리를 하려고 하는 경우에 적합합니다.  

이 프로젝트에서는 localhost 환경에서 구글 검색 페이지의 왼쪽 상단의 구글로고를 다른 이미지로 변경한 페이지를 클라이언트에 반환하는 프로그램을 구현했습니다.
HTML 문서내의 로고만 변경하는 간단한 기능을 가진 프로젝트이므로 Spring Framework 를 활용해도 가능하지만 Nginx-clojure를 활용해 외부 서버에 의존하지않고 nginx 서버 내부에서 동작하는 프로그램입니다. 



#### 개발환경 ####
> OS : Window 10  
> VM : Ubuntu  
> SERVER : Nginx-clojure  
> Language : Java 8