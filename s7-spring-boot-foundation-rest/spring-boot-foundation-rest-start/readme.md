## Aufgabe 

Die Fluggesellschaft stellt einen Route-Service bereit, über den die Routen der Fluggesellschaft bearbeitet werden können. Dazu wurden bereits Repositories implementiert. Nun soll ein REST-API über Spring MVC bereitgestellt werden, über das eine Route angelegt und eine bestimmte Route gelesen werden kann. 
- Erstellen Sie den REST-Controller, über den das API bereitgestellt wird. 
- Erstellen Sie einen Test, der das korrekte Lesen einer bestimmten Route überprüft.   

Die Lösungen zum REST Client zur Validierung und Exception Handling können zusätzlich implementiert werden können, sofern Zeit vorhanden ist. 

## REST Controller implementieren 

Hier ist nur eine der CRUD Operationen beispielhaft gezeigt.      


```java
	@RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Route> get(@PathVariable(value = "id") Long id) {
		return new ResponseEntity<Route>(service.findById(id), HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Route> post(@Validated @RequestBody Route entity) {
		return new ResponseEntity<Route>(service.save(entity), HttpStatus.CREATED);
	}
```


## Test Rest Controller implementieren 

Hier ist nur ein TEST für eine der beiden CRUD-Operationen beispielhaft gezeigt.      


```java
	@Test
	public void get() {

            ResponseEntity<Route> routeEntity = restClient.get()
                    .uri("/routes/101")
                    .retrieve()
                    .toEntity(Route.class);
        
            Assertions.assertEquals(HttpStatus.OK, routeEntity.getStatusCode());
            Assertions.assertNotNull(routeEntity.getBody());
            Assertions.assertEquals(101L, routeEntity.getBody().getId().longValue());

	}
	

```


## REST Client implementieren 

```java
@SpringBootApplication
public class Application implements CommandLineRunner {
	
	public final static Logger logger = LoggerFactory.getLogger(Application.class);
	
	public static void main(String[] args) {
            SpringApplication.run(Application.class, args);
        }

	@Override
	public void run(String... args) throws Exception {

            RestClient restClient = RestClient.create("http://localhost:8080");
    
            ResponseEntity<Route> route = restClient.get()
                    .uri("/routes/101")
                    .retrieve()
                    .toEntity(Route.class);
    
            logger.info("### Response Status {}", route.getStatusCode());
    
    
            restClient.get()
                    .uri("/routes/110000")
                    .retrieve()
                    .onStatus(httpStatusCode -> httpStatusCode.isSameCodeAs(BAD_REQUEST), ((request, response) -> {
                        logger.error("### Response Status {}", response.getStatusCode());
                    }))
                    .toEntity(Route.class);
		
	}
        
}
```


## Validierung der Parameter 

Damit die Annotation gesetzt werden können, müssen sie einen weiteren Starter hinzufügen. 

```
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-validation</artifactId>
	</dependency>
```

Fügen Sie nun die Annotationen den Domain-Klassen hinzu, die für die Prüfung benötigt werden. 

```java
	@Entity
	public class Route extends AbstractEntity {

		@NotNull
		private String flightNumber;
	
		@Size(max=20)
		private String departure;
	

```

Nun schalten Sie noch die Validierung über @Validated ein. 

```java
	@RequestMapping(method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Route> post(@Validated @RequestBody Route entity) {	
	    return new ResponseEntity<Route>(service.save(entity),HttpStatus.CREATED);
	}	

```

Dafür ist nicht zwingend ein Test zu implementieren. Nur wenn Sie am Ende noch Zeit haben. In der Musterlösung können Sie später diesen 
Test finden.


## Exception Handler 

Sie können zwei Exception Handler definieren

Einen Exception Handler auf fachlicher eben im RouteController 

```java
        @ExceptionHandler(value = RouteNotFoundException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	ProblemDetail hndleException(RouteNotFoundException exception) {
		ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
		problemDetail.setType(URI.create("http://thinkenterprise.com/RouteNotFoundException"));
		problemDetail.setTitle("Route not found");
		problemDetail.setDetail(exception.getMessage());

		return problemDetail;
	}
```

und einen technischen Exception Handler über das Controler Advice, dass global für alle Controller gültig ist. 

```java
@ControllerAdvice
public class PersistenceControllerAdvice extends ResponseEntityExceptionHandler {
    

	@ExceptionHandler(value = PersistenceException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ProblemDetail exception(PersistenceException exception) {
		
            ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
            problemDetail.setType(URI.create("http://thinkenterprise.com/PersistenceException"));
            problemDetail.setTitle( "General Persistence Exception");
            problemDetail.setDetail(exception.getMessage());
            
            return problemDetail;
        }

}

```


