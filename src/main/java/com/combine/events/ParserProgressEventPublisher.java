package com.combine.events;

import com.combine.service.CustomApplicationEventPublisher;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

/**
 * publisher that handles publishing of film-added events
 *
 * @author lfallon
 *
 */
@Component
public class ParserProgressEventPublisher extends CustomApplicationEventPublisher {

    private static final Logger logger = Logger.getLogger(ParserProgressEventPublisher.class);

    @Override
    public void publish(ApplicationEvent event) {
        logger.debug("##ParserProgressEventPublisher::publish => publishing event ");
        super.publish(event);
    }
}
