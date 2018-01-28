package com.combine;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;

public class ParserMessagePostProcessor implements MessagePostProcessor {

    private final Integer ttl;

    public ParserMessagePostProcessor(final Integer ttl) {
        this.ttl = ttl;
    }

    @Override
    public Message postProcessMessage(final Message message) throws AmqpException {
        message.getMessageProperties().getHeaders().put("expiration", ttl.toString());
        return message;
    }

}
