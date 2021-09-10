package com.ddinteractjava;

import com.ddinteractjava.cyp1a2.CYP1A2Cache;
import com.ddinteractjava.cyp3a4.CYP3A4Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.io.IOException;

@SpringBootApplication
public class DDInteractJavaApplication {
    @Autowired
    private CYP3A4Cache cyp3A4Cache;

    @Autowired
    private CYP1A2Cache cyp1A2Cache;

    public static void main(String[] args) {
        SpringApplication.run(DDInteractJavaApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() throws IOException {
        cyp3A4Cache.createCYP3A4Cache();
        cyp1A2Cache.createCYP1A1Cache();
    }
}

