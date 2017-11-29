package com.pneuberger.xmageimageloader;

import com.pneuberger.xmageimageloader.crawler.ScryfallCrawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class XmageImageLoaderApplication implements CommandLineRunner {

    @Autowired
    private ScryfallCrawler scryfallCrawler;

    public static void main(String[] args) {
        SpringApplication.run(XmageImageLoaderApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        scryfallCrawler.loadAllSets();
    }
}
