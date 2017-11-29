package com.pneuberger.xmageimageloader.crawler;

import com.pneuberger.xmageimageloader.data.MTGCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;

class CardDownloader {

    private static final String CARD_NAME_SUFFIX = ".full.jpg";

    private static final Logger LOGGER = LoggerFactory.getLogger(CardDownloader.class);

    void download(MTGCard mtgCard, String destination) {
        try {
            File destinationFolder = new File(destination);

            if (!destinationFolder.exists()) {
                boolean dirWasCreated = destinationFolder.mkdir();
                if (!dirWasCreated) {
                    throw new IOException("Could not create folder: "+destination);
                }
            }

            LOGGER.info("Downloading " + mtgCard.getName() + " from: " + mtgCard.getImageURL());
            downloadImage(mtgCard.getImageURL(), destinationFolder.getAbsolutePath() + File.separator + mapCardNameToXmage(mtgCard.getName()));
        } catch (IOException e) {
            LOGGER.error(mtgCard.getImageURL() + " downloading failed!", e);
        }

    }

    private String mapCardNameToXmage(String cardName) {
        String name = cardName;

        name = name.replace("//", "-");

        return name + CARD_NAME_SUFFIX;
    }

    private void downloadImage(String source, String destination) throws IOException {
        URL sourceURL = new URL(source);

        try (InputStream is = sourceURL.openStream();
             OutputStream os = new FileOutputStream(destination)) {
            byte[] b = new byte[2048];
            int length;

            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }
        }


    }
}
