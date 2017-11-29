package com.pneuberger.xmageimageloader.crawler;

import com.pneuberger.xmageimageloader.data.MTGSetsConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ScryfallCrawler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScryfallCrawler.class);

    @Autowired
    private MTGSetsConfiguration setsConfiguration;

    @Value("${download.folder}")
    private String downloadFolder;

    public void loadAllSets() {

        ExecutorService executor = Executors.newSingleThreadExecutor();
//        ExecutorService executor = Executors.newFixedThreadPool(5);

        List<String> sets = setsConfiguration.getSets();

        sets.forEach(
                set -> {
                    try {
                        executor.execute(new SetCrawler(set, downloadFolder));
                    } catch (RejectedExecutionException e) {
                        LOGGER.error("Error during execution", e);
                    }
                }
        );

    }
}
