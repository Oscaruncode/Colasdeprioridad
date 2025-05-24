package com.example.consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class ConfirmationConsumer {
    private static final String QUEUE_CONFIRMACIONES = "confirmaciones";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_CONFIRMACIONES, false, false, false, null);

        System.out.println("Esperando confirmaciones...");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("Confirmación recibida: " + message);
        };

        channel.basicConsume(QUEUE_CONFIRMACIONES, true, deliverCallback, consumerTag -> { });
    }
}
