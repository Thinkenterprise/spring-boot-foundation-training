## Aufgabe 

Die Fluggesellschaft möchte einen Service bereitstellen, der für die Routenverwaltung verantwortlich ist. Dieser Service soll als Spring-Boot-Anwendung implementiert und als JAR für die Ausführung auf der Konsole bereitgestellt werden.


1. Erstellen Sie eine Spring-Boot-Anwendung, die als JAR deployed und ausgeführt werden kann.
2. Führen Sie die Anwendung aus.


## POM einrichten   
Entfernen Sie alle Ausdokumentierungen und ergänzen Sie fehlende Angaben wie z.B. das **repackage** etc. 

Fügen Sie den noch fehlenden Starter hinzu 

```
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
	</dependencies>
```


## Autoconfiguration    

	
```java	
	@SpringBootApplication
	public class Application implements ApplicationRunner  {
	
   	 public static void main(String[] args) {
   	 }
    	

}
```


## Start der Anwendung   

```java	
@SpringBootApplication
public class Application implements ApplicationRunner  {
	
	
    public static void main(String[] args) {
    	
    	SpringApplication springApplication = new SpringApplication(Application.class);
    	springApplication.run(args);    	

    }
}
```

## Bauen der Anwendung   

```

mvn clean package
```


## Starten der Anwendung


```

java -jar traget/s2-spring-boot-foundation-basics-start.jar 
```


