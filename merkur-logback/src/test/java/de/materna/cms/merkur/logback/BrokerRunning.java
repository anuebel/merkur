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

package de.materna.cms.merkur.logback;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assume;
import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.util.StringUtils;

/**
 * Aus dem spring-rabbit-Modul übernommene {@link Rule}, die sicherstellt, dass
 * Tests geskipt werden, falls der Message-Broker nicht läuft.
 * 
 * @see BrokerRunning aus dem spring-rabbit-Modul
 */
public class BrokerRunning extends TestWatcher {

	private static final String DEFAULT_QUEUE_NAME = BrokerRunning.class
			.getName();

	private static Log logger = LogFactory.getLog(BrokerRunning.class);

	// Static so that we only test once on failure: speeds up test suite
	private static Map<Integer, Boolean> brokerOnline = new HashMap<Integer, Boolean>();

	// Static so that we only test once on failure
	private static Map<Integer, Boolean> brokerOffline = new HashMap<Integer, Boolean>();

	private final boolean assumeOnline;

	private final boolean purge;

	private Queue[] queues;

	private int DEFAULT_PORT = 5672;

	private int port;

	private String hostName = null;

	/**
	 * Ensure the broker is running and has an empty queue with the specified
	 * name in the default exchange.
	 * 
	 * @return a new rule that assumes an existing running broker
	 */
	public static BrokerRunning isRunningWithEmptyQueues(String... names) {
		Queue[] queues = new Queue[names.length];
		for (int i = 0; i < queues.length; i++) {
			queues[i] = new Queue(names[i]);
		}
		return new BrokerRunning(true, true, queues);
	}

	/**
	 * Ensure the broker is running and has an empty queue (which can be
	 * addressed via the default exchange).
	 * 
	 * @return a new rule that assumes an existing running broker
	 */
	public static BrokerRunning isRunningWithEmptyQueues(Queue... queues) {
		return new BrokerRunning(true, true, queues);
	}

	/**
	 * @return a new rule that assumes an existing running broker
	 */
	public static BrokerRunning isRunning() {
		return new BrokerRunning(true);
	}

	/**
	 * @return a new rule that assumes there is no existing broker
	 */
	public static BrokerRunning isNotRunning() {
		return new BrokerRunning(false);
	}

	private BrokerRunning(boolean assumeOnline, boolean purge, Queue... queues) {
		this.assumeOnline = assumeOnline;
		this.queues = queues;
		this.purge = purge;
		setPort(DEFAULT_PORT);
	}

	private BrokerRunning(boolean assumeOnline, Queue... queues) {
		this(assumeOnline, false, queues);
	}

	private BrokerRunning(boolean assumeOnline) {
		this(assumeOnline, new Queue(DEFAULT_QUEUE_NAME));
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(int port) {
		this.port = port;
		if (!brokerOffline.containsKey(port)) {
			brokerOffline.put(port, true);
		}
		if (!brokerOnline.containsKey(port)) {
			brokerOnline.put(port, true);
		}
	}

	/**
	 * @param hostName
	 *            the hostName to set
	 */
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	@Override
	public Statement apply(final Statement base, final Description description) {

		// Check at the beginning, so this can be used as a static field
		if (assumeOnline) {
			Assume.assumeTrue(brokerOnline.get(port));
		} else {
			Assume.assumeTrue(brokerOffline.get(port));
		}

		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();

		try {

			connectionFactory.setPort(port);
			if (StringUtils.hasText(hostName)) {
				connectionFactory.setHost(hostName);
			}
			RabbitAdmin admin = new RabbitAdmin(connectionFactory);

			for (Queue queue : queues) {
				String queueName = queue.getName();

				if (purge) {
					logger.debug("Deleting queue: " + queueName);
					// Delete completely - gets rid of consumers and bindings as
					// well
					admin.deleteQueue(queueName);
				}

				if (isDefaultQueue(queueName)) {
					// Just for test probe.
					admin.deleteQueue(queueName);
				} else {
					admin.declareQueue(queue);
				}
			}
			brokerOffline.put(port, false);
			if (!assumeOnline) {
				Assume.assumeTrue(brokerOffline.get(port));
			}

		} catch (Exception e) {
			logger.warn(
					"Not executing tests because basic connectivity test failed",
					e);
			brokerOnline.put(port, false);
			if (assumeOnline) {
				Assume.assumeNoException(e);
			}
		} finally {
			connectionFactory.destroy();
		}

		return super.apply(base, description);

	}

	private boolean isDefaultQueue(String queue) {
		return DEFAULT_QUEUE_NAME.equals(queue);
	}

}