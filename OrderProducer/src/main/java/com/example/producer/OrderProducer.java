package com.example.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class OrderProducer {
    private static final String QUEUE_NAME = "pedidos";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            Pedido pedido = new Pedido("123", "Juan Pérez", 249.99);
            ObjectMapper mapper = new ObjectMapper();
            String message = mapper.writeValueAsString(pedido);

            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("Pedido enviado: " + message);
        }
    }
}
