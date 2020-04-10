## Aufgabe



Der IT-Betrieb des Route-Service verlangt Laufzeitinformationen über den Route-Service, ohne die der Betrieb nicht freigegeben wird. Es sollen Informationen über den Gesundheitszustand und die Version des Route-Service bereitgestellt werden.

1. Stellen Sie Health-Informationen über Ihre Anwendung bereit. Simulieren Sie dabei einen Anwendungsstatus.
2. Stellen Sie Informationen über die Version der Anwendung bereit.
## Actuator Endpoints anzeigen    

Sie können ein paar Endpoints aufrufen und ausprobieren 

```java
POSTMAN: localhost:8080/thinkenterprise

```

## Health Indicator schreiben 

```java

@Component
public class RouteServiceHealthIndicator implements HealthIndicator {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouteService.class);

    @Autowired
    private RouteService service;

    @Override
    public Health health() {
        boolean serviceStatus = service.getServiceStatus();
        if (serviceStatus) {
            LOGGER.info("RouteService status: up");
            return Health.up().build();
        } else {
            LOGGER.warn("RouteService status: down");
            return Health.down().build();
        }
    }
}


```


## Health Indicator testen 

Nachdem Sie die Anwendung gebaut und gestartet haben, können Sie den Health Enpoint prüfen  

```java

POSTMAN: localhost:8080/thinkenterprise/health

```



## Build Information   

Fügen Sie dem Spring Boot Plugin das die folgende Execution hinzu. 

```java

  <execution>
       <id>build-info</id>
       <goals>
       <goal>build-info</goal>
       </goals>
  </execution>
```


## Build Endpoint testen  


Nachdem Sie die Anwendung gebaut und gestartet haben, können Sie den Health Enpoint prüfen  

```java

POSTMAN: localhost:8080/thinkenterprise/health

```
