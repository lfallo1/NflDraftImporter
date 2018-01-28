package com.combine.service;

import com.combine.ParserMessagePostProcessor;
import com.combine.model.ParserProgressEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private static final Logger logger = Logger.getLogger(MessageService.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Config config;

    @Autowired
    @Qualifier("parserEventExchange")
    private Exchange exchange;

    final MessagePostProcessor messagePostProcessor = new ParserMessagePostProcessor(10000);

    public void sendProgressMessage(ParserProgressEvent parserProgressEvent) {
        try {
            parserProgressEvent.setUsername("lfallo1");
            String payload = new ObjectMapper().writeValueAsString(parserProgressEvent);
            this.rabbitTemplate.convertAndSend(exchange.getName(), config.getImportProgressRoutingKey(), payload, messagePostProcessor);
        } catch (JsonProcessingException e) {
            logger.warn("unable to send message: " + e.toString());
        }
    }

}
