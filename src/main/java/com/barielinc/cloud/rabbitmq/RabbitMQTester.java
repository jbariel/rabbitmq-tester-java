package com.barielinc.cloud.rabbitmq;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.barielinc.cloud.rabbitmq.Out.LogLevel;

public class RabbitMQTester {

	public static ScheduledThreadPoolExecutor globalThreadPool = new ScheduledThreadPoolExecutor(10);

	private static boolean doProduce = false;
	private static boolean doConsume = false;

	public static void main(String[] args) {
		Out.i("Starting...");

		RMQProperties properties = loadProperties();

		Out.setLogLevel(properties.getLogLevel());
		doProduce = properties.isProducer();
		doConsume = properties.isConsumer();

		final RMQProducer rmqProducer = new RMQProducer(properties);
		final RMQConsumer rmqConsumer = new RMQConsumer(properties);
		Thread producer = null;
		Thread consumer = null;

		if (doProduce) {
			Out.d("Creating producer...");
			producer = new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						rmqProducer.start();
					} catch (IOException e) {
						Out.e("Error starting RMQProducer");
						e.printStackTrace();
					}
				}
			});
			Out.d("Starting producer...");
			producer.start();
		}

		if (doConsume) {
			// TODO jbariel => support more than 1 consumer
			Out.d("Creating consumer...");
			consumer = new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						rmqConsumer.start();
					} catch (IOException e) {
						Out.e("Error starting RQMConsumer");
						e.printStackTrace();
					}
				}
			});
			Out.d("Starting consumer...");
			consumer.start();
		}

		Out.i("Running...");

		Out.i("Press \"ENTER\" to continue...");
		try {
			System.in.read();
		} catch (

		IOException e) {
			e.printStackTrace();
		}

		Out.i("Closing down...");

		if (doProduce) {
			try {
				rmqProducer.stop();
			} catch (IOException e) {
				Out.w("IOException stopping producer!");
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
				Out.w("IOException stopping consumer!");
				e.printStackTrace();
			}

			if (null != consumer) {
				consumer.interrupt();
			}
		}

		Out.i("Exiting...");
		System.exit(0);
	}

	private static RMQProperties loadProperties() {
		Out.d("Loading properties...");
		RMQProperties props = new RMQProperties();

		Properties propfile = new Properties();
		InputStream input = null;

		try {
			input = new FileInputStream("props.properties");
			propfile.load(input);

			props.setUsername(StringUtils.trimToEmpty(propfile.getProperty("username")));
			props.setPassword(StringUtils.trimToEmpty(propfile.getProperty("password")));
			props.setPort(NumberUtils.toInt(propfile.getProperty("port", "5672")));
			props.setHostname(StringUtils.trimToEmpty(propfile.getProperty("hostname", "localhost")));
			props.setvHost(StringUtils.trimToEmpty(propfile.getProperty("vhost")));
			props.setLogLevel(LogLevel.valueOf(StringUtils.trimToEmpty(propfile.getProperty("loglevel", "INFO"))));
			props.setProducer(Boolean.parseBoolean(StringUtils.trimToEmpty(propfile.getProperty("producer", "false"))));
			props.setConsumer(Boolean.parseBoolean(StringUtils.trimToEmpty(propfile.getProperty("consumer", "false"))));
			props.setNumberOfConsumers(NumberUtils.toInt(propfile.getProperty("numberOfConsumers", "1")));
			props.setProducerMessageRate(NumberUtils.toLong(propfile.getProperty("producerMessageRate", "1000L")));

		} catch (IOException e) {
			Out.e("Exception loading properties file");
			e.printStackTrace();
		} finally {
			if (null != input) {
				try {
					input.close();
				} catch (IOException e) {
					Out.w("Error closing input...");
				}
			}
		}

		Out.i("Found properties: %s", props);
		return props;
	}

}
