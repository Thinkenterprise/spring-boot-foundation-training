## Aufgabe 

Die Fluggesellschaft stellt einen Route-Service bereit, über den alle Routen der Fluggesellschaft bearbeitet werden können. Die Routen sollen persistiert werden. Dazu sollen CRUD- Persistenzfunktionen bereitgestellt werden und zusätzlich eine Query, über die alle Routen mit einer bestimmten Destination abgefragt werden können.

1. Implementieren Sie die Persistenzschicht für die genannten CRUD- Persistenzfunktionen. 
2. Erweitern Sie die Persistenzschicht um die Destination-Query.
3. Erstellen Sie einen Test-Slice, um die CRUD-Operationen und die Destination-Query auf Ihrem Domain Model zu testen.


## Spring Data JPA Starter hinzufügen     


```java
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

```



## Embedded Database hinzufügen 

```
<dependency>
	<groupId>com.h2database</groupId>
	<artifactId>h2</artifactId>
</dependency>
		
		
```

## Repository implementieren 

```java
public interface RouteRepository extends CrudRepository<Route, Long> {
    
}
```


## Repository um Query erweitern 

```java

	Iterable<Route> findByDeparture(String departure);

    @Query(value = "select r from Route r where r.departure = :departure")
    Iterable<Route> queryFindByDeparture(@Param("departure") String departure);


```



## Test schreiben   


```java

@DataJpaTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class RouteRepositoryDataJpaTest {

  
    @Autowired
    private RouteRepository repository;

    @Test
    public void findAll() throws Exception {
        Iterable<Route> actual = repository.findAll();
        Assert.assertNotNull(actual.iterator().hasNext());
    }

}

```
