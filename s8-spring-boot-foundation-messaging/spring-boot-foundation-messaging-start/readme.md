## Aufgabe 

Die Fluggesellschaft zeichnet das Tracking der Flüge auf. Die Tracking-Informationen werden von dem Tracking-Anbieter FlightAware als JMS-Nachricht im JSON-Format an eine Message Topic mit dem Namen FlightAwareTracking gesendet. Das Tracking besteht aus der Routenidentifikation, der Flugnummer, der Uhrzeit und einem Status. Die JMS-Nachricht wird nach dem Empfang auf der Konsole ausgegeben.
1. Senden Sie die Tracking-Objekte, die von einem Scheduler zyklisch erzeugt werden, als JMS- Nachricht im JSON-Format an die Topic FlightAwareTracking
2. Empfangen Sie die JMS-Nachricht von der Topic FlightAwareTracking und konvertieren Sie diese von JSON in ein Tracking-Objekts.
3. Geben Sie den Inhalt des Tracking Objekts auf der Konsole aus.
 
 
## JMS Starter hinzufügen 
 
```java

 <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-activemq</artifactId>
        </dependency>

```

## Sende Bean implementieren       


```java
@Component
public class JmsSender {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendMessage(Tracking tracking) {
        jmsTemplate.convertAndSend("FlightAwareTracking", tracking);
    }
}

```

## Sender Bean verwenden 


```java
SpringBootApplication
@EnableScheduling
public class Application {

    private static ConfigurableApplicationContext context;

     
    public static void main(String[] args) {
        context = SpringApplication.run(Application.class, args);
    }
    
    @Scheduled(initialDelay = 1000, fixedDelay = 5000)
    public void sendTracking() {
        ...

        JmsSender sender = context.getBean(JmsSender.class);
        sender.sendMessage(tracking);
    }
}

```


## Reciver Bean implementieren 

```java
@Component
public class JmsReceiver {
    
    @JmsListener(destination = "FlightAwareTracking")
    public void receiveMessage(Tracking tracking) {
       System.out.println(tracking);
    }
}

```

 
## JMS Konfiguration 

```java
@Configuration
public class JmsConfiguration {






    @Bean
    public ObjectMapper objectMapper() {
        
    	ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.findAndRegisterModules();
        return objectMapper;
    }

    @Bean
    public MessageConverter jacksonJmsMessageConverter(ObjectMapper objectMapper) {
        
    	MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        converter.setObjectMapper(objectMapper);
        return converter;
    }
    
    
    
    @Bean
    public Queue queue() {

      return new ActiveMQQueue("FlightAwareTracking");

    }
 
}
``
 
 
 