/*
 * Copyright 2012 andreas.
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
 */

package de.materna.cms.merkur.generator;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import de.materna.cms.merkur.generator.config.ApplicationConfig;

/**
 * Einstiegspunkt der Applikation. Startet den Spring {@link ApplicationContext}
 * über Spring-Boot.
 * 
 * @author andreas
 * @see SpringApplication#run(Object, String...)
 */
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(ApplicationConfig.class, args);
	}
}