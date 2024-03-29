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

package de.materna.cms.merkur.generator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import de.materna.cms.merkur.generator.log.LogGenerator;

/**
 * Konfiguration des Spring {@link ApplicationContext}.
 * 
 * @author andreas
 */
@Configuration
@EnableAutoConfiguration
@EnableScheduling
public class ApplicationConfig implements SchedulingConfigurer {

	@Value("${count:1}")
	private int count;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.scheduling.annotation.SchedulingConfigurer#configureTasks
	 * (org.springframework.scheduling.config.ScheduledTaskRegistrar)
	 */
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		for (int i = 0; i < count; i++) {
			final LogGenerator logGenerator = logGenerator();
			logGenerator.setIndex(i);
			taskRegistrar.addFixedDelayTask(logGenerator,
					logGenerator.taskDelay());
		}
	}

	@Bean
	@Scope(BeanDefinition.SCOPE_PROTOTYPE)
	public LogGenerator logGenerator() {
		return new LogGenerator();
	}

}