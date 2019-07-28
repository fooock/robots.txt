package com.fooock.robotstxt.crawlapi.controller;

import com.fooock.robotstxt.crawlapi.service.CrawlerApiService;
import com.fooock.robotstxt.database.entity.Entry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 *
 */
@RequiredArgsConstructor
@Slf4j
@RequestMapping("v1")
@RestController
public class CrawlApiController {
    private final CrawlerApiService crawlerApiService;

    /**
     * Sends a new crawl to the topic created to crawl new robots.txt files. Currently
     * this method only consumes url encoded content types.
     *
     * @param url Url to be crawled
     */
    @PostMapping(value = "send", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void send(String url) {
        log.debug("Called method to send URL {} to the streaming service", url);
        crawlerApiService.send(url);
    }

    /**
     * Try to update expired URLs from database
     */
    @PutMapping("update")
    public void updateExpired() {
        log.debug("Called method to update expired URLs from database");
        crawlerApiService.updateExpired();
    }

    /**
     * Retrieve from the database the number of saved urls
     *
     * @return Number of entries in database
     */
    @GetMapping("count")
    public long countUrls() {
        log.debug("Called method to count url's from database");
        return crawlerApiService.countUrls();
    }

    /**
     * Retrieve all elements from database using pagination. Pagination can be configured
     * using the page number and the number of elements for page.
     *
     * @param page Page to retrieve. Default value is 0
     * @param size Number of elements by page. Default value is 10
     * @return Paginated elements
     */
    @GetMapping("all")
    public Page<Entry> findAllPageable(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        log.debug("Called method to retrieve paginated results, from page {} with {} elements", page, size);
        return crawlerApiService.findAllPageable(page, size);
    }
}
