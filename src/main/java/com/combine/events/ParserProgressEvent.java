package com.combine.events;

import com.combine.model.ParserProgressMessage;
import org.springframework.context.ApplicationEvent;

public class ParserProgressEvent extends ApplicationEvent {
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public ParserProgressEvent(ParserProgressMessage source) {
        super(source);
    }
}
