/*
 * Copyright (C) 2016 Thinkenterprise
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 * @author Spring Boot Foundation Training
 */

package com.thinkenterprise.registrar;

import org.springframework.beans.factory.BeanRegistrar;
import org.springframework.beans.factory.BeanRegistry;
import org.springframework.core.env.Environment;

/**
 * Beispiel: Programmatische Bean-Registrierung mit {@link BeanRegistrar}
 * (neu in Spring Framework 7 / Spring Boot 4).
 *
 * <p>Es gibt damit drei Wege, eine Bean zu registrieren:
 * <ol>
 *   <li><b>implizit</b>     &ndash; {@code @Component} + Component-Scan</li>
 *   <li><b>explizit</b>     &ndash; {@code @Bean}-Methode in einer {@code @Configuration}</li>
 *   <li><b>programmatisch</b> &ndash; dieser {@code BeanRegistrar}: typsicher,
 *       gut fuer dynamische/bedingte Registrierung und AOT-/Native-freundlich
 *       (loest das alte {@code ImportBeanDefinitionRegistrar} /
 *       {@code BeanDefinitionRegistryPostProcessor} ab).</li>
 * </ol>
 *
 * <p>Aktiviert wird der Registrar ueber {@link RegistrarConfiguration} (dort per
 * {@code @Import} eingebunden). Damit die Demo den laufenden Anwendungs-Context
 * nicht beeinflusst, sind die Beans bewusst nebenwirkungsfrei gehalten:
 * <ul>
 *   <li>es sind eigenstaendige Demo-Typen ohne Bezug zum uebrigen Code,</li>
 *   <li>sie werden mit {@code notAutowirable()} markiert, koennen also nie
 *       versehentlich in andere Beans injiziert werden,</li>
 *   <li>Abhaengigkeiten werden eindeutig per Namen aufgeloest.</li>
 * </ul>
 */
public class ExampleBeanRegistrar implements BeanRegistrar {

    /**
     * Die einzige Methode des Interface. Wir bekommen zwei Werkzeuge:
     * die {@link BeanRegistry} (zum Registrieren) und das {@link Environment}
     * (fuer Bedingungen wie aktive Profile oder Properties).
     */
    @Override
    public void register(BeanRegistry registry, Environment env) {

        // 1) Einfachste Form: Bean per Typ. Spring instanziiert sie ueber den
        //    Default-Konstruktor selbst.
        registry.registerBean(RouteGreeter.class);

        // 2) Mit explizitem Namen und Customizer (BeanSpec): eigene Beschreibung,
        //    eigener Supplier und notAutowirable() - die Bean ist damit kein
        //    Kandidat fuer Autowiring und kann nichts im Context stoeren.
        registry.registerBean("germanGreeter", RouteGreeter.class, spec -> spec
                .description("Deutscher Gruss, programmatisch registriert")
                .notAutowirable()
                .supplier(context -> new RouteGreeter("Hallo Welt")));

        // 3) Voller Customizer: Prototype-Scope, Lazy-Init und ein Supplier, der
        //    ueber den SupplierContext eine andere Bean EINDEUTIG per Namen holt.
        registry.registerBean("welcomeBanner", RouteBanner.class, spec -> spec
                .prototype()
                .lazyInit()
                .notAutowirable()
                .description("Banner, programmatisch registriert")
                .supplier(context -> new RouteBanner(context.bean("germanGreeter", RouteGreeter.class))));

        // 4) Bedingte Registrierung ueber das Environment - das programmatische
        //    Pendant zu @Profile (vgl. ProductionConfiguration / TestConfiguration).
        if (env.matchesProfiles("production")) {
            registry.registerBean("activeGreeter", RouteGreeter.class, spec -> spec
                    .notAutowirable()
                    .supplier(context -> new RouteGreeter("Welcome aboard")));
        }
        else {
            registry.registerBean("activeGreeter", RouteGreeter.class, spec -> spec
                    .notAutowirable()
                    .supplier(context -> new RouteGreeter("Hallo (Test/Default)")));
        }
    }

    // ------------------------------------------------------------------
    // Kleine, in sich geschlossene Demo-Typen (kein Bezug zum uebrigen Code).
    // ------------------------------------------------------------------

    static class RouteGreeter {

        private final String message;

        RouteGreeter() {
            this("Hello World");
        }

        RouteGreeter(String message) {
            this.message = message;
        }

        String greet() {
            return message;
        }
    }

    static class RouteBanner {

        private final RouteGreeter greeter;

        RouteBanner(RouteGreeter greeter) {
            this.greeter = greeter;
        }

        String render() {
            return "*** " + greeter.greet() + " ***";
        }
    }
}
