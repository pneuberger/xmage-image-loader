package com.pneuberger.xmageimageloader.crawler;

import com.pneuberger.xmageimageloader.data.MTGCard;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipOutputStream;

public class SetCrawler implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(SetCrawler.class);

    private static final String BASE_URL = "https://scryfall.com";
    private static final String SET_URL = BASE_URL + "/sets/";

    private static final String IMAGE_NAME_REGEX = "^[^\\(]*";
    private static final String IMAGE_URL_REGEX = "(^.*)(?=\\?)";

    private static final Pattern IMAGE_NAME_PATTERN = Pattern.compile(IMAGE_NAME_REGEX);
    private static final Pattern IMAGE_URL_PATTERN = Pattern.compile(IMAGE_URL_REGEX);

    private CardDownloader cardDownloader;
    private ZipDirectory zipDirectory;

    private String set;
    private String downloadFolder;


    public SetCrawler(String set, String downloadFolder) {
        this.set = set;
        this.downloadFolder = downloadFolder;
        this.zipDirectory = new ZipDirectory();
        this.cardDownloader = new CardDownloader();
    }

    @Override
    public void run() {
        try {
            crawlCardImages();
            packToZip();
        } catch (IOException e) {
            LOGGER.error("Connection error", e);
        }
    }

    private void crawlCardImages() throws IOException {
        if (!StringUtils.hasText(set)) {
            return;
        }

        Elements cardLinks = Jsoup.connect(SET_URL + set.toLowerCase()).get().select("a.card-grid-item");

        cardLinks.iterator().forEachRemaining(cardElement -> {
            String cardName = cardElement.select("img").attr("title");
            String destinationURL = cardElement.attr("href");
            String imageURL = "";
            try {
                imageURL = Jsoup.connect(BASE_URL + destinationURL).get().select("img").attr("src");
            } catch (IOException ioe) {
                LOGGER.error("", ioe);
            }

            Matcher nameMatcher = IMAGE_NAME_PATTERN.matcher(cardName);
            Matcher urlMatcher = IMAGE_URL_PATTERN.matcher(imageURL);

            if (nameMatcher.find() && urlMatcher.find()) {
                MTGCard mtgCard = new MTGCard(nameMatcher.group(0).trim(), urlMatcher.group(1));
                cardDownloader.download(mtgCard, getSetDownloadFolder());
            }
        });
    }

    private void packToZip() throws IOException {
        FileOutputStream fos = new FileOutputStream(downloadFolder + File.separator + set + ".zip");
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        File fileToZip = new File(downloadFolder + File.separator+ set);

        zipDirectory.zipFile(fileToZip, fileToZip.getName(), zipOut);
        zipOut.close();
        fos.close();
    }

    private String getSetDownloadFolder() {
        return downloadFolder + File.separator + set;
    }


}
