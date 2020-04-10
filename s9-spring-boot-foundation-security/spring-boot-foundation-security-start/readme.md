## Aufgabe 

Die Fluggesellschaft stellt einen Route-Service bereit, über den die Routen der Fluggesellschaft bearbeitet werden können. Für den Route-Service soll nun eine Authentifizierung und ein Autorisierung eingeführt werden.

1. Führen Sie über Basic Authentication eine Authentifizierung für den User route mit dem Password route und der Rolle ROUTE ein. 
2. Schützen Sie über Method Based Authorization die Abfrage aller Routen so, dass nur Bentuzer in der Rolle ROUTE diese Abrufen dürfen.  

 
 
## Security Starter hinzufügen 
 
```
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

## Security prüfen  

Der Aufruf dürfte nicht mehr funktionieren 

```java
	curl localhost:8080/routes
```

Über das automatisch generierte Passwort allerdings schon. 

```java
	curl -u user:#hash localhost:8080/routes
```

## Security Configuration User & Password Customizing


```java
@Component
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        
   	 auth.inMemoryAuthentication()
        .withUser("user")
        .password(passwordEncoder().encode("password"))
        .roles("ADMIN");
    }
}

```

## Security Configuration für Autorisierung 


```java
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

}
```
 
## Method Based Security hinzufügen  

```java
    @RequestMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Iterable<Route>> findAll() {
        return ResponseEntity.ok(routeRepository.findAll());
    }
```
 
 
 