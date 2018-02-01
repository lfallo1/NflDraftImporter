package com.combine.service;

import com.combine.ParserMessagePostProcessor;
import com.combine.dao.CombineDao;
import com.combine.model.ParserProgressMessage;
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

    @Autowired
    private CombineDao combineDao;

    final MessagePostProcessor messagePostProcessor = new ParserMessagePostProcessor(10000);

    public void sendProgressMessage(ParserProgressMessage parserProgressMessage) {
        try {
            parserProgressMessage.setUsername("lfallo1");
            String payload = new ObjectMapper().writeValueAsString(parserProgressMessage);

            //add update to message queue
            this.rabbitTemplate.convertAndSend(exchange.getName(), config.getImportProgressRoutingKey(), payload, messagePostProcessor);

            //add update to database
            combineDao.addParserProgress(parserProgressMessage);
        } catch (JsonProcessingException e) {
            logger.warn("unable to send message: " + e.toString());
        }
    }

}
