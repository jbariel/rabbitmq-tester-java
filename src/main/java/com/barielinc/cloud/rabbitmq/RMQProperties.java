package com.barielinc.cloud.rabbitmq;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class RMQProperties {

	private String username;
	private String password;
	private String vHost;
	private String hostname;
	private int port;
	private boolean isProducer;
	private boolean isConsumer;
	private int numberOfConsumers;
	private long producerMessageRate;

	public RMQProperties() {
		super();
	}

	public RMQProperties(String username, String password, String vHost, String hostname, int port) {
		this();
		setUsername(username);
		setPassword(password);
		setvHost(vHost);
		setHostname(hostname);
		setPort(port);
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
