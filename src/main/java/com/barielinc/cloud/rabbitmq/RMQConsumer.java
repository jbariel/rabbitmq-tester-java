package com.barielinc.cloud.rabbitmq;

import java.io.IOException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

public class RMQConsumer extends RMQConnection {

	String consumerTag;

	public RMQConsumer() {
		super();
	}

	public RMQConsumer(RMQProperties info) {
		super(info);
	}

	@Override
	public void start() throws IOException {

		connect();

		consumerTag = currentChannel.basicConsume(queueName, false, new Consumer() {

			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {

				log.info(new String(body));
				currentChannel.basicAck(envelope.getDeliveryTag(), false);
			}

			@Override
			public void handleConsumeOk(String consumerTag) {
				log.debug("HandleConsumeOk");
			}

			@Override
			public void handleCancelOk(String consumerTag) {
				log.debug("HandleCancelOk");
			}

			@Override
			public void handleCancel(String consumerTag) throws IOException {
				log.debug("HandleCancel");
			}

			@Override
			public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
				log.debug("HandleShutdownSignal");
			}

			@Override
			public void handleRecoverOk(String consumerTag) {
				log.debug("HandleRecoverOk");
			}
		});
	}

	@Override
	public void stop() throws IOException {
		currentChannel.basicCancel(consumerTag);
		genericStop();
	}

}
