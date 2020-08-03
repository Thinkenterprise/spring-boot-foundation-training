## Aufgabe 

Die Fluggesellschaft stellt einen Route-Service bereit, über den die Routen der Fluggesellschaft bearbeitet werden können. Dazu wurden bereits Repositories implementiert. Nun sollen die elementaren CRUD-Operationen über ein REST-API über Spring MVC bereitgestellt werden.

1. Erstellen Sie den REST-Controller, der die CRUD-Operationen über ein REST- API bereitstellt.
2. Erstellen Sie einen Test, der beispielhaft eine Methode des REST-API testet.
3. Validieren Sie ein Attribut der Route und aktivieren Sie die Validierung in Ihrem REST-Controller.
4. Führen Sie eine Fehlerbehandlung ein, sobald eine Route über das REST-API nicht gefunden werden kann.
 

## Rest Controller implementieren 

Hier ist nur eine der CRUD Operationen beispielhaft gezeigt.      


```java
@RestController
@RequestMapping("routes")
public class RouteController {

    @Autowired
    private RouteService service;

  
	@RequestMapping(method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Iterable<Route>> getAll() {
		Iterable<Route> result = service.findAll();
		return new ResponseEntity<Iterable<Route>>(service.findAll(),HttpStatus.OK);
	}
	
	
}

```

## Test Rest Controller implementieren 

Hier ist nur zwei TEST für zwei CRUD-Operationen beispielhaft gezeigt.      


```java
	@Test
	public void get() {

		Map<String, String> keys = new HashMap<>();
		keys.put("id", "101");

		ResponseEntity<Route> routeEntity = this.restTemplate.getForEntity("/routes/{id}", Route.class, keys);
		Assert.assertEquals(HttpStatus.OK, routeEntity.getStatusCode());
		Assert.assertNotNull(routeEntity.getBody());
		Assert.assertEquals(101L, routeEntity.getBody().getId().longValue());

	}
	
	@Test
	public void getAll() {

		ResponseEntity<Iterable<Route>> routeResponse = restTemplate.exchange("/routes", HttpMethod.GET, null,
				new ParameterizedTypeReference<Iterable<Route>>() {
				});

		Iterable<Route> iterable = routeResponse.getBody();

		Assert.assertEquals(HttpStatus.OK, routeResponse.getStatusCode());
		Assert.assertNotNull(routeResponse.getBody());
		Assert.assertNotNull(iterable.iterator().hasNext());

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

Einen Exception Handler auf fachlciher eben im RouteController 

```java
 @ExceptionHandler(value = RouteNotFoundException.class)
	 @ResponseStatus(HttpStatus.BAD_REQUEST)
	 @ResponseBody
	 public RouteErrorStatus exception(RouteNotFoundException exception) {
	     return new RouteErrorStatus(6573, "Route Exception" + exception.getMessage());
	 }
```

und einen technischen Exception Handler über das Controler Advice, dass global für alle Controller gültig ist. 

```java
@ControllerAdvice
public class PersistenceControllerAdvice {
    
    @ExceptionHandler(value = PersistenceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RouteErrorStatus exception(PersistenceException exception) {
        return new RouteErrorStatus(1000L, "Persistence Exception" + exception.getMessage());

    }
}
```


