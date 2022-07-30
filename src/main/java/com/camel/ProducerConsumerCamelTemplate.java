package com.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class ProducerConsumerCamelTemplate {

    public static void main(String[] args) throws Exception {

        CamelContext context = new DefaultCamelContext();
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                /* Simply want to send a message to the endpoint.
                So the same message is send to another endpoint. S
                Whatever message is sent to another endpoint from that endpoint, message will be received.
                 */
                from("direct:start")       // First endpoint
                        .to("seda:end");     // Second endpoint
            }
        });

        // Start camel route
        context.start();

        // Camel producer template to produce a message and send it to "from" endpoint
        ProducerTemplate producerTemplate = context.createProducerTemplate();
        producerTemplate.sendBody("direct:start", "Hello everyone!");

        // Camel consumer template to consume the message produced by the camel producer and consume it from "to" endpoint.
        ConsumerTemplate consumerTemplate = context.createConsumerTemplate();
        String consumedMessage = consumerTemplate.receiveBody("seda:end", String.class);

        System.out.println(consumedMessage);

        // Stop camel route
        context.stop();
    }
}
