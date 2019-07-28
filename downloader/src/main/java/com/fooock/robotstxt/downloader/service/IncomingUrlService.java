package com.fooock.robotstxt.downloader.service;

import com.fooock.robotstxt.database.RedisUrlRepository;
import com.fooock.robotstxt.database.RobotsRepository;
import com.fooock.robotstxt.database.entity.Entry;
import com.fooock.robotstxt.downloader.util.DomainUtils;
import com.fooock.robotstxt.downloader.util.UrlUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

/**
 *
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class IncomingUrlService {
    private final RobotsRepository robotsRepository;
    private final FileDownloaderService downloaderService;
    private final RedisUrlRepository redisUrlRepository;

    /**
     * Method called when a new URL is received from the given Kafka topic. The new url is normalized
     * to the form of protocol, host (including any subdomain) and port (if there is one that is not the default one).
     *
     * @param url Url to retrieve robots.txt rules
     */
    public void receiveUrl(String url) {
        // Check if url is a valid http or https resource
        if (!UrlUtils.isValid(url)) {
            log.error("Url {} is not valid", url);
            return;
        }
        log.info("Received url {}, prepared to fetch robots.txt file for it", url);
        try {
            // Extract base url to create robots.txt url
            URI uri = new URI(url);
            String baseHost = DomainUtils.base(uri);

            log.debug("Validated url to base host: {}", baseHost);
            checkHost(baseHost);

        } catch (URISyntaxException e) {
            log.error("Can't create URI using url {}", url, e);
        }
    }

    /**
     * Check if the current host exists in the database or needs to be downloaded. If the host
     * currently exists, we check if we need to re-download it to maintain it up to date.
     *
     * @param host Url to check
     */
    private void checkHost(String host) {
        // IF url is pending, then continue with the next one...
        if (redisUrlRepository.exists(host)) {
            log.info("URL {} exists in queue, continue with the next one", host);
            return;
        }
        try {
            // Currently a host exist in the database, so try to update it if needed
            // For this purpose we check the updated data field
            Entry entry = robotsRepository.findByHost(host);

            log.info("Found entry in database updated at {} with id {} and url {}",
                    entry.getUpdatedAt(), entry.getId(), entry.getHost());

            // Check caching strategy to know when a saved robots rules need to be
            // re-crawled
            if (entry.needsUpdate()) {
                log.info("Url {} needs to be re-crawled again", host);
                downloadRules(host);
                return;
            }
            log.debug("Ignoring url {} because it is already up to date", host);

        } catch (EmptyResultDataAccessException e) {
            log.info("Host {} doesn't exist in database, download rules", host);
            downloadRules(host);
        }
    }

    /**
     * Add the host to the list of pending URL to be processed and send it to download
     *
     * @param host Host to be downloaded
     */
    private void downloadRules(String host) {
        // TODO: Add a more realistic caching strategy, see #5
        boolean pending = redisUrlRepository.addPending(host, 23, TimeUnit.HOURS);

        log.debug("Added url {} to redis? {}", host, pending);
        downloaderService.download(host, String.format("%s/robots.txt", host));
    }
}
