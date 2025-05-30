package com.example.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class OrderConsumer {
    private static final String QUEUE_NAME = "pedidos";
    private static final String QUEUE_CONFIRMACIONES = "confirmaciones";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.queueDeclare(QUEUE_CONFIRMACIONES, false, false, false, null);
        System.out.println("Esperando pedidos...");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String json = new String(delivery.getBody(), "UTF-8");
            ObjectMapper mapper = new ObjectMapper();
            Pedido pedido = mapper.readValue(json, Pedido.class);

            System.out.println("Pedido recibido:");
            System.out.println("- ID: " + pedido.id);
            System.out.println("- Cliente: " + pedido.cliente);
            System.out.println("- Total: $" + pedido.total);

            String confirmationMessage = "Confirmación: Pedido " + pedido.id + " procesado para " + pedido.cliente;
            channel.basicPublish("", QUEUE_CONFIRMACIONES, null, confirmationMessage.getBytes());
            System.out.println("Mensaje de confirmación enviado.");
        };

        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
    }
}
