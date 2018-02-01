package com.combine.events;

import com.combine.model.ParserProgressMessage;
import com.combine.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ParserProgressEventListener implements ApplicationListener<ParserProgressEvent> {

    @Autowired
    private MessageService messageService;

    @Override
    public void onApplicationEvent(ParserProgressEvent event) {
        if(event.getSource() instanceof ParserProgressMessage){
            this.messageService.sendProgressMessage((ParserProgressMessage) event.getSource());
        }
    }
}
