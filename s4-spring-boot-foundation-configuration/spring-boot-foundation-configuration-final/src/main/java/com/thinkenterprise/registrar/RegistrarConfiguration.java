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

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Aktiviert den {@link ExampleBeanRegistrar}.
 *
 * <p>Ein {@link org.springframework.beans.factory.BeanRegistrar} wird erst wirksam,
 * wenn er auf einer {@code @Configuration} ueber {@code @Import} eingebunden wird.
 * Genau das macht diese Klasse &ndash; sie wird vom Component-Scan
 * (Basis-Paket {@code com.thinkenterprise}) erfasst, daher laeuft der Registrar
 * beim Hochfahren des Context automatisch mit.
 *
 * <p>Die registrierten Beans sind reine Demo-Objekte (siehe {@link ExampleBeanRegistrar}),
 * mit {@code notAutowirable()} markiert und werden nirgends verwendet &ndash; der
 * uebrige Anwendungs-Context bleibt unveraendert lauffaehig.
 */
@Configuration
@Import(ExampleBeanRegistrar.class)
public class RegistrarConfiguration {
}
