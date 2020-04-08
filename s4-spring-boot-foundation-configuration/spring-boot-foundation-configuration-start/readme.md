## Aufgabe 

Die Fluggesellschaft stellt einen Route-Service bereit, über den alle Routen der Fluggesellschaft abgefragt werden können. Der Service gibt auf Anfrage eine Liste aller Routen zurück. Die Implementierung innerhalb des Service soll dabei in Form des Drei-Schichten-Modells (Interaktion, Service, Data) abgebildet werden. Die Testdaten werden über eine bereits bestehende Mock- Implementierung bereitgestellt.

1. Implementieren Sie die notwendigen Spring Business Beans für das Drei-Schichten-Modell, die über alle drei Schichten hinweg die Methode zur Bereitstellung der Routen implementieren.
2. Stellen Sie über eine Mock Spring Bean auf der Datenschicht eine Liste von n Routen bereit. Verwenden Sie dazu die Production-Mock Implementierung.
 
Die Anzahl der bereitgestellten Routen kann über ein @Value Property konfiguriert werden und getestet werden.

3. Stellen Sie über eine zweite Mock Spring Bean auf der Datenschicht eine Liste von n Routen bereit.
Verwenden Sie dazu die Test-Mock Implementierung.
4. Führen Sie ein Profile ein, sodass die Test-Mock Implementierung nur im Testfall greift.
5. Es wird ein Test bereitgestellt, der prüft, ob die rudimentäre Anzahl der Routen bereitgestellt wird. Routen > 0.



## Repository erstellen   


```java

@Repository
public class RouteRepository {

    @Autowired
    private RouteRepositoryDriver driver;

   
    public List<Route> findByDeparture(String departure) {
        return StreamSupport.stream(driver.getRouteList().spliterator(), false).filter(new Predicate<Route>() {
            @Override
            public boolean test(Route route) {
                return route.getDeparture().equals(departure);
            }
        }).collect(Collectors.toList());
    }

   
    public List<Route> findAll() {
        return driver.getRouteList();
    }
}

```

## Service erstellen  

```java

@Service
public class RouteService {

    @Autowired
    private RouteRepository routeRepository;

    public List<Route> findAll() {
        return routeRepository.findAll();
    }
}

```


## Rest Controller erstellen  

```java

@RestController
public class RouteController {
	
	@Autowired
	private RouteService routeService;
	
	@RequestMapping("/routes")
	public List<Route> routes() {
		return routeService.findAll();
	}


}
```

## Configuration für Production erstellen 

```java
@Configuration
public class ProductionConfiguration {

    @Bean
    public RouteRepositoryDriver developmentRouteRepositoryDriver() {
        return new ProductionRouteRepositoryDriver();
    }
}
```

## Property konfigurieren   



```java
route.count=5
```

## Property verwenden  



```java
public class ProductionRouteRepositoryDriver implements RouteRepositoryDriver {
	@Value("${route.count}")
    private short routeCount = 5;

}
```




## Configuration für Test-Mock erstellen 

```java
@Configuration

public class TestConfiguration {

    @Bean
    public RouteRepositoryDriver testRouteRepositoryDriver() {
        return new TestRouteRepositoryDriver();
    }
}
```


## Property für Test konfigurieren   

Sie benötigen nun eine neues Property File mit dem Namen **application-test.properties** .

```java
route.count=2
```


##  Test schreiben 


```java
@SpringBootTest
@ActiveProfiles("test")
public class RouteRepositoryTest {

    @Autowired
    private RouteRepository repository;

    @Test
    public void findByDeparture() throws Exception {
        Iterable<Route> actual = repository.findByDeparture("FRA");
        Assert.assertNotNull(actual.iterator().hasNext());
    }

    @Test
    public void findAll() throws Exception {
        Iterable<Route> actual = repository.findAll();
        Assert.assertNotNull(actual.iterator().hasNext());
    }
}
```

