## Aufgabe 

Die Fluggesellschaft möchte einen Service bereitstellen, der für die Routenverwaltung verantwortlich ist. Dieser Service soll als Spring-Boot-Anwendung implementiert und als JAR für die Ausführung auf der Konsole bereitgestellt werden.


1. Erstellen Sie eine Spring-Boot-Anwendung, die als JAR deployed und ausgeführt werden kann.
2. Führen Sie die Anwendung aus.


## POM einrichten   
Fügen Sie alle fehlenden Einträge in die POM ein. Hinweise auf fehlende Deklarationen sind im POM File enthalten. 



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

## Bauen der Anwendung als JAR

```

mvn clean package
```


## Starten der Anwendung


```

java -jar traget/s2-spring-boot-foundation-basics-start.jar 
```

## Anwendung aufrufen 


```
Browser localhost:8080
```


## Bauen der Anwendung OCI Image 

```

mvn spring-boot:build-image
```


## Starten der Anwendung

```
docker run -it -p 8080:8080 s2-spring-boot-foundation-basics-final:0.0.1-SNAPSHOT
```


## Anwendung aufrufen 


```
Browser localhost:8080
```


## AOT Native Build with Buildpacks as OCI Image

Für den AOT Native Build verpackt in einem OCI Image ist der folgende Befehl auszuführen. 


```
mvn -Pnative spring-boot:build-image
```

Das erstellte Docker Image kan über **docker images** aufgelistet und über 

```
docker run -it -p 8080:8080 s2-spring-boot-foundation-basics-final:0.0.1-SNAPSHOT
```

getstartet werden. 

## AOT Native Build as Executable 

Für den AOT Native Build als Executable ist der folgende Befehl auszuführen. 


```
mvn -Pnative native:compile
```

Das erzeugte Executable ist unter **target** zu finden. Die erzeugten Konfigurationsdateien und **spring-aot** resources und der erzeugte Source Code unter **source**. 

Für den Strat des Executables ist der folgende Befehl 

```
s2-spring-boot-foundation-basics-final:0.0.1-SNAPSHOT
```

unter **target** auszuführen. 















Für eine native Compilierung ist zuvor die Installation der GraalVM notwendig. 

Dazu sind die folgenden Schritte notwendig:

1. Download GraalVM https://www.graalvm.org/ z.B. graalvm-ce-java17-darwin-amd64-22.3.1.tar.gz
2. Auspacken tar -xzf graalvm-ce-java17-darwin-amd64-22.3.1.tar.gz
3. Set Attributes sudo xattr -r -d com.apple.quarantine graalvm-ce-java17-22.3.1
4. Copy to Java Folder sudo mv graalvm-ce-java17-22.3.1 /Library/Java/JavaVirtualMachines
5. Check Installation /usr/libexec/java_home -V.
6. Open Environment .profile File  
7. Set Environment  

export PATH=$PATH:/Library/Java/JavaVirtualMachines/graalvm-ce-java17-22.3.1/Contents/Home/bin:/opt/apache-maven/bin
export JAVA_HOME=/Library/Java/JavaVirtualMachines/graalvm-ce-java17-22.3.1/Contents/Home


```
mvn -Pnative spring-boot:build-image
```



