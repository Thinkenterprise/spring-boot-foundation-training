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
	private TestRestTemplate testRestTemplate;
	
	@Autowired
	private RouteController routeController;

	
	@Test
	public void testHelloWorld() {
		
		String result = testRestTemplate.getForObject("/helloWorld", String.class);
		Assert.assertTrue(result.contentEquals("Hello World"));

	}
	
	@Test
	public void testHelloWorldOnController() {
		String result = routeController.helloWorld();
		Assert.assertTrue(result.contentEquals("Hello World"));
		
	}
}
```


## Test ausführen     

	
