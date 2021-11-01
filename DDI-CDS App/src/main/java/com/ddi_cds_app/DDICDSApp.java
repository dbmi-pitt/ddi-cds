package com.ddi_cds_app;

import com.ddi_cds_app.cyp1a2.CYP1A2Cache;
import com.ddi_cds_app.cyp3a4.CYP3A4Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.io.IOException;

@SpringBootApplication
public class DDICDSApp {
    @Autowired
    private CYP3A4Cache cyp3A4Cache;

    @Autowired
    private CYP1A2Cache cyp1A2Cache;

    public static void main(String[] args) {
        SpringApplication.run(DDICDSApp.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() throws IOException {
        cyp3A4Cache.createCYP3A4Cache();
        cyp1A2Cache.createCYP1A1Cache();
    }
}

