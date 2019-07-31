package com.fooock.robotstxt.crawlapi.service;

import com.fooock.robotstxt.database.RobotsRepository;
import com.fooock.robotstxt.database.entity.Entry;
import com.fooock.robotstxt.database.entity.UrlRecord;
import com.fooock.robotstxt.database.service.ClusterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 *
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class CrawlerApiService {
    private static final String KEY_INCOME_STREAM = "income";
    private static final String KEY_UPDATE_STREAM = "update";

    private final ClusterService clusterService;
    private final RobotsRepository robotsRepository;

    /**
     * Method to send the URL to the streaming service. If the URL is not well formed, this method
     * throws a {@link ResponseStatusException} with a 500 error code.
     *
     * @param url Url to send
     */
    public void send(String url) {
        try {
            String decodedUrl = new URL(url).toString();
            RecordId id = clusterService.add(KEY_INCOME_STREAM, new UrlRecord(decodedUrl).toMap());

            if (id == null) return;
            log.info("Added new record with id: {} for url {}", id.toString(), decodedUrl);

        } catch (MalformedURLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Malformed URL");
        }
    }

    /**
     * Update the expired urls found in the database if any
     */
    public void updateExpired() {
        List<String> updateUrls = robotsRepository.findNeedToUpdateUrls();
        if (updateUrls.isEmpty()) {
            log.info("No urls found to update in database");
            return;
        }
        log.info("Found {} urls to update", updateUrls.size());
        updateUrls.forEach(url -> clusterService.add(KEY_UPDATE_STREAM, new UrlRecord(url).toMap()));
    }

    /**
     * @return Number of url's found in database
     */
    public long countUrls() {
        long count = robotsRepository.count();
        log.info("Found {} url's from database", count);
        return count;
    }

    /**
     * Find all elements from database using paginated results. If the given page is < than 0, then
     * page by default always is 0. Also, size param can't be < than 5 or > than 100.
     *
     * @param page Page number
     * @param size Number of results per page
     * @return Paginated results
     */
    public Page<Entry> findAllPageable(int page, int size) {
        if (page < 0) page = 0;
        if (size < 5) size = 5;
        if (size > 100) size = 100;
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Entry> entries = robotsRepository.findAll(pageRequest);
        log.info("Found {} entries for page {} and page size {}", entries.getTotalElements(), page, size);
        return entries;
    }

    /**
     * Try to find the given url in the database and return all related info.
     *
     * @param url Url to search for
     * @return Related url entry info
     */
    public Entry findByHost(String url) {
        try {
            return robotsRepository.findByHost(url);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Url {} doesn't exist in database", url);
            // if the url not exists in database then return a 404
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Url not found!");
        }
    }
}
