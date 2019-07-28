package com.fooock.robotstxt.downloader.service;

import com.fooock.robotstxt.database.RedisUrlRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 *
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class UpdateUrlService {
    private final FileDownloaderService downloaderService;
    private final RedisUrlRepository redisUrlRepository;

    /**
     * Update an existing URL in the database
     *
     * @param url URL to update
     */
    public void updateUrl(String url) {
        log.debug("Prepared to update url {}", url);

        // Update redis queue with the current url but first try to delete the existing one
        // if it exists
        redisUrlRepository.deletePending(url);
        redisUrlRepository.addPending(url, 23, TimeUnit.HOURS);

        // Download url
        String robotsUrl = String.format("%s/robots.txt", url);
        log.info("Update expired robots.txt file from url {} ({})", url, robotsUrl);
        downloaderService.download(url, robotsUrl);
    }
}
