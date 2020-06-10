package com.barielinc.cloud.rabbitmq;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RabbitMQTester {

	private final static Logger log = LoggerFactory.getLogger(RabbitMQTester.class);

	public static ScheduledThreadPoolExecutor globalThreadPool = new ScheduledThreadPoolExecutor(10);

	private static boolean doProduce = false;
	private static boolean doConsume = false;

	public static void main(String[] args) {
		log.info("Starting...");

		RMQProperties properties = loadProperties();

		long initDelay = properties.getInitialDelay();
		try {
			log.info("Found initial delay of " + initDelay + " ms, sleeping...");
			Thread.sleep(initDelay);
			log.info("Continuing initialization...");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		doProduce = properties.isProducer();
		doConsume = properties.isConsumer();

		final RMQProducer rmqProducer = new RMQProducer(properties);
		final RMQConsumer rmqConsumer = new RMQConsumer(properties);
		Thread producer = null;
		Thread consumer = null;

		if (doProduce) {
			log.info("Creating producer...");
			producer = new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						rmqProducer.start();
					} catch (IOException e) {
						log.error("Error starting RMQProducer");
						e.printStackTrace();
					}
				}
			});
			log.info("Starting producer...");
			producer.start();
		}

		if (doConsume) {
			// TODO jbariel => support more than 1 consumer
			log.info("Creating consumer...");
			consumer = new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						rmqConsumer.start();
					} catch (IOException e) {
						log.error("Error starting RQMConsumer");
						e.printStackTrace();
					}
				}
			});
			log.info("Starting consumer...");
			consumer.start();
		}

		log.info("Running...");

		log.info("Press \"ENTER\" to continue...");
		try {
			System.in.read();
		} catch (

		IOException e) {
			e.printStackTrace();
		}

		log.info("Closing down...");

		if (doProduce) {
			try {
				rmqProducer.stop();
			} catch (IOException e) {
				log.warn("IOException stopping producer!");
				e.printStackTrace();
			}

			if (null != producer) {
				producer.interrupt();
			}
		}

		if (doConsume) {
			try {
				rmqConsumer.stop();
			} catch (IOException e) {
				log.warn("IOException stopping consumer!");
				e.printStackTrace();
			}

			if (null != consumer) {
				consumer.interrupt();
			}
		}

		log.info("Exiting...");
		System.exit(0);
	}

	private static RMQProperties loadProperties() {
		log.debug("Loading properties...");
		RMQProperties props = new RMQProperties();

		Properties propfile = new Properties();

		try (InputStream input = RabbitMQTester.class.getClassLoader().getResourceAsStream("props.properties")) {
			propfile.load(input);

			props.setUsername(StringUtils.trimToEmpty(propfile.getProperty("username")));
			props.setPassword(StringUtils.trimToEmpty(propfile.getProperty("password")));
			props.setPort(NumberUtils.toInt(propfile.getProperty("port"), props.getPort()));
			props.setHostname(StringUtils.trimToEmpty(propfile.getProperty("hostname", "localhost")));
			props.setvHost(StringUtils.trimToEmpty(propfile.getProperty("vhost")));
			props.setProducer(Boolean.parseBoolean(StringUtils.trimToEmpty(propfile.getProperty("producer"))));
			props.setConsumer(Boolean.parseBoolean(StringUtils.trimToEmpty(propfile.getProperty("consumer"))));
			props.setNumberOfConsumers(
					NumberUtils.toInt(propfile.getProperty("numberOfConsumers"), props.getNumberOfConsumers()));
			props.setProducerMessageRate(
					NumberUtils.toLong(propfile.getProperty("producerMessageRate"), props.getProducerMessageRate()));
			props.setInitialDelay(NumberUtils.toLong(propfile.getProperty("initialDelay"), props.getInitialDelay()));

		} catch (IOException e) {
			log.error("Exception loading properties file");
			e.printStackTrace();
		}

		Map<String, String> env = System.getenv();

		props.setUsername(env.getOrDefault("RMQ_USERNAME", props.getUsername()));
		props.setPassword(env.getOrDefault("RMQ_PASSWORD", props.getPassword()));
		props.setvHost(env.getOrDefault("RMQ_VHOST", props.getvHost()));
		props.setHostname(env.getOrDefault("RMQ_HOSTNAME", props.getHostname()));
		props.setPort(NumberUtils.toInt(env.getOrDefault("RMQ_PORT", String.valueOf(props.getPort()))));
		props.setProducer(Boolean.valueOf(env.getOrDefault("RMQ_PRODUCER", String.valueOf(props.isProducer()))));
		props.setConsumer(Boolean.valueOf(env.getOrDefault("RMQ_CONSUMER", String.valueOf(props.isConsumer()))));
		props.setNumberOfConsumers(
				NumberUtils.toInt(env.getOrDefault("RMQ_NUM_CONSUMERS", String.valueOf(props.getNumberOfConsumers()))));
		props.setProducerMessageRate(NumberUtils
				.toLong(env.getOrDefault("RMQ_MESSAGE_RATE", String.valueOf(props.getProducerMessageRate()))));
		props.setInitialDelay(
				NumberUtils.toLong(env.getOrDefault("RMQ_INITIAL_DELAY", String.valueOf(props.getInitialDelay()))));

		log.info(String.format("Found properties: %s", props));
		return props;
	}

}
