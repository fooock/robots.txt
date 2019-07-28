package com.fooock.robotstxt.api.service;

import com.fooock.robotstxt.api.engine.RuleMatcher;
import com.fooock.robotstxt.api.model.AllowData;
import com.fooock.robotstxt.api.model.AllowResponse;
import com.fooock.robotstxt.api.validator.UrlValidator;
import com.fooock.robotstxt.database.RobotsRepository;
import com.fooock.robotstxt.database.entity.Entry;
import com.fooock.robotstxt.database.entity.UrlRecord;
import com.fooock.robotstxt.database.service.ClusterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;

/**
 *
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class ApiService {
    private static final String KEY_INCOME_STREAM = "income";

    private final RobotsRepository robotsRepository;
    private final RuleMatcher ruleMatcher;
    private final ClusterService clusterService;

    /**
     * Check if the given data can be crawled. If the host of the given URL can't be found in the
     * database, then this method throws a 404 error.
     *
     * @param data Data to be checked
     * @return A valid response, can be allowed or not
     */
    @Cacheable(value = "allowed", key = "#data.toString()")
    public AllowResponse isAllowed(AllowData data) throws ResponseStatusException {

        // First check if the data URL is valid or not
        if (!UrlValidator.isValid(data.getUrl())) {
            log.error("URL {} is not valid!", data.getUrl());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Url is not valid");
        }

        URI uri = UrlValidator.toUri(data.getUrl());
        if (uri == null) {
            log.error("Can't create URI for the url {}", data.getUrl());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Can't create URI form given data");
        }

        String host = UrlValidator.base(uri);
        String path = uri.getPath();
        log.info("Prepared to check if '{}' can access path: {} / base is: {}", data.getAgent(), path, host);

        try {
            // If no results found, this method return an exception that we can capture
            // to return a more convenient 404 error
            long startTime = System.currentTimeMillis();
            Entry entry = robotsRepository.findByHost(host);
            log.debug("Found entry {} in {}ms with rules {}", entry.getHost(), (System.currentTimeMillis() - startTime), entry.getRules());

            AllowResponse response = new AllowResponse();
            response.setAgent(data.getAgent());

            startTime = System.currentTimeMillis();
            response.setAllowed(ruleMatcher.canCrawl(data.getAgent(), path, entry.getRules()));

            log.debug("The function canCrawl took {}ms to finish for url {}", (System.currentTimeMillis() - startTime), host);
            response.setUrl(data.getUrl());

            log.info("Can user agent {} crawl url {}? {}", data.getAgent(), host, response.isAllowed());
            return response;

        } catch (EmptyResultDataAccessException e) {

            // Send url to crawl and return a 404 error asap
            clusterService.add(KEY_INCOME_STREAM, new UrlRecord(host).toMap());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("%s not found in the database, sent to crawler", host));
        }
    }
}
