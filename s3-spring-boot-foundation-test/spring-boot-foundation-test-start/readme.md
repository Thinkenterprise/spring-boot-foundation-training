## Aufgabe 

Erstellen Sie einen RestController der HelloWorld zurückgibt und Testen Sie diesen 


1. Controller schreiben 
2. Controller testen 




## Route REST Controller   


```java

@RestController
public class RouteController {
	
	@RequestMapping("/helloWorld")
	public String helloWorld() {
		return "Hello World";
		
	}

}

```

## Test POM    

```java

<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-test</artifactId>
</dependency>

```

## HelloWorld REST Controller Test  

```java
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RouteControllerTest {

    @Autowired
    private RouteController routeController;

    @LocalServerPort
    int port;
    
    private RestClient restClient;

    @BeforeEach
    void setUp() {
        restClient = RestClient.create("http://localhost:" + port);
    }

    @Test
    public void testHelloWorld() {
        String result = restClient.get().uri("/helloWorld").retrieve().body(String.class);
        assertThat(result).isEqualTo("Hello World");
    }

    @Test
    public void testHelloWorldOnController() {
        String result = routeController.helloWorld();
        assertThat(result).isEqualTo("Hello World");
    }
}
```


## Test ausführen     

	
