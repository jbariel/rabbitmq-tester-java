package com.barielinc.cloud.rabbitmq;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class RMQProperties {

	private String username = StringUtils.EMPTY;
	private String password = StringUtils.EMPTY;
	private String vHost = StringUtils.EMPTY;
	private String hostname = "localhost";
	private int port = 5672;
	private boolean isProducer = false;
	private boolean isConsumer = false;
	private int numberOfConsumers = 1;
	private long producerMessageRate = 1000L;

	public RMQProperties() {
		super();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getvHost() {
		return vHost;
	}

	public void setvHost(String vHost) {
		this.vHost = vHost;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isProducer() {
		return isProducer;
	}

	public void setProducer(boolean isProducer) {
		this.isProducer = isProducer;
	}

	public boolean isConsumer() {
		return isConsumer;
	}

	public void setConsumer(boolean isConsumer) {
		this.isConsumer = isConsumer;
	}

	public int getNumberOfConsumers() {
		return numberOfConsumers;
	}

	public void setNumberOfConsumers(int numberOfConsumers) {
		this.numberOfConsumers = numberOfConsumers;
	}

	public long getProducerMessageRate() {
		return producerMessageRate;
	}

	public void setProducerMessageRate(long producerMessageRate) {
		this.producerMessageRate = producerMessageRate;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	public String toUriString() {
		return String.format("amqp:/%s:%s@%s:%d/%s", getUsername(), getPassword(), getHostname(), getPort(),
				getvHost());
	}

}
