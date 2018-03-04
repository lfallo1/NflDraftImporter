package com.combine.controller;

import com.combine.events.ParserProgressEvent;
import com.combine.events.ParserProgressEventPublisher;
import com.combine.model.ParserProgressMessage;
import com.combine.model.Player;
import com.combine.service.ParserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class StartImportMessageConsumer {

    static final int TYPE_ALL = 0;
    static final int TYPE_DRAFTEK = 1;
    static final int TYPE_DRAFTCOUNTDOWN = 2;
    static final int TYPE_WALTERFOOTBALL = 3;
    static final int TYPE_CBSSPORTS = 4;
    static final int TYPE_NFLCOM = 5;
    static final int TYPE_COMBINE = 6;

    private Logger logger = LoggerFactory.getLogger(StartImportMessageConsumer.class);

    @Autowired
    private ParserService parserService;

    @Autowired
    private ParserProgressEventPublisher parserProgressEventPublisher;

    @RabbitListener(queues = "parserInitializeQueue")
    public void receive(Integer type) {

        String uuid = UUID.randomUUID().toString();

        //publish initial event
        ParserProgressMessage progress = new ParserProgressMessage(uuid, "lfallo1", new Date(), 0, "Beginning import", new Date(), null);
        this.parserProgressEventPublisher.publish(new ParserProgressEvent(progress));

        try {

            List<Player> players = new ArrayList<>();
            switch (type) {
                case TYPE_ALL:
                    this.parserService.refreshAll(uuid, players, progress);
                    break;
                case TYPE_DRAFTEK:
                    this.parserService.loadDraftTek(uuid, players, progress.init(50));
                    break;
                case TYPE_DRAFTCOUNTDOWN:
                    this.parserService.loadNflDraftCountdown(2018, uuid, players, progress.init(50));
                    break;
                case TYPE_WALTERFOOTBALL:
                    this.parserService.loadWalterFootballDraft(uuid, players, progress.init(50));
                    break;
                case TYPE_CBSSPORTS:
//                    this.parserService.loadCbsSportsDraft(uuid, new ArrayList<>(), progress.init(50));
                    break;
                case TYPE_NFLCOM:
//                    this.parserService.loadCbsSportsDraft(uuid, new ArrayList<>(), progress.init(50));
                    break;
                case TYPE_COMBINE:
                    this.parserService.updateCombineResults(progress.init(50));
                    break;
            }

            this.parserService.insertPlayers(players, progress.init(100 - (int) progress.getProgress()));

            //send finished message
            this.parserProgressEventPublisher.publish(new ParserProgressEvent(progress.finish()));

        } catch (Exception e) {
            //if any error bubbles up, "gracefull" error out so that the queue doesn't continue delivering messages
            this.parserProgressEventPublisher.publish(new ParserProgressEvent(progress.with(new Date(), 0, "Import failed")));
            logger.warn("Error executing refresh");
        }
    }

}
