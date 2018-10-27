package com.combine.schedule;

import com.combine.service.ParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DraftScheduler {

    @Autowired
    private ParserService parserService;

    //    @Scheduled(fixedRate = 600000)
    public void refreshDraftPicks() {
        this.parserService.updateDraftPicks();
    }

}
