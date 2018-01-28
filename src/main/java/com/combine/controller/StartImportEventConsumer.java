package com.combine.controller;

import com.combine.model.ParserProgressEvent;
import com.combine.model.Player;
import com.combine.service.MessageService;
import com.combine.service.ParserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class StartImportEventConsumer {

    static final int TYPE_ALL = 0;
    static final int TYPE_DRAFTEK = 1;
    static final int TYPE_DRAFTCOUNTDOWN = 2;
    static final int TYPE_WALTERFOOTBALL = 3;
    static final int TYPE_CBSSPORTS = 4;

    private Logger logger = LoggerFactory.getLogger(StartImportEventConsumer.class);

    @Autowired
    private ParserService parserService;

    @Autowired
    private MessageService messageService;

    @RabbitListener(queues = "parserInitializeQueue")
    public void receive(Integer type) {
        //TODO refactor - setup progress messages inside each individual importer.
        try {
            String uuid = UUID.randomUUID().toString();
            List<Player> players = new ArrayList<>();
            switch (type) {
                case TYPE_ALL:
                    this.parserService.refreshAll(uuid, players);
                    break;
                case TYPE_DRAFTEK:
                    this.messageService.sendProgressMessage(new ParserProgressEvent(uuid, new Date(), 50, "Importing from DraftTek"));
                    this.parserService.loadDraftTek(uuid, players);
                    break;
                case TYPE_DRAFTCOUNTDOWN:
                    this.messageService.sendProgressMessage(new ParserProgressEvent(uuid, new Date(), 50, "Importing from NflDraftCountdown"));
                    this.parserService.loadNflDraftCountdown(2018, uuid, players);
                    break;
                case TYPE_WALTERFOOTBALL:
                    this.messageService.sendProgressMessage(new ParserProgressEvent(uuid, new Date(), 50, "Importing from Walter Football"));
                    this.parserService.loadWalterFootballDraft(uuid, players);
                    break;
                case TYPE_CBSSPORTS:
                    /* Currently disabled */
//                    this.parserService.loadCbsSportsDraft(uuid, new ArrayList<>());
                    break;
            }

            //insert players
            this.messageService.sendProgressMessage(new ParserProgressEvent(uuid, new Date(), 75, "Saving records to database"));
            this.parserService.insertPlayers(players);

            //send finished message
            this.messageService.sendProgressMessage(new ParserProgressEvent(uuid, new Date(), 100, "Import complete"));

        } catch (IOException e) {
            logger.warn("Error executing refresh");
        }
    }

}
