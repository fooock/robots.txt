package com.fooock.robotstxt.downloader.service;

import com.fooock.robotstxt.database.RedisUrlRepository;
import com.fooock.robotstxt.database.RobotsRepository;
import com.fooock.robotstxt.database.entity.Entry;
import com.fooock.robotstxt.downloader.util.DomainUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.EmptyResultDataAccessException;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 *
 */
public class IncomingUrlServiceTest {

    private FileDownloaderService downloaderService;
    private RobotsRepository robotsRepository;
    private IncomingUrlService consumerService;
    private RedisUrlRepository redisUrlRepository;

    @Before
    public void setUp() {
        downloaderService = mock(FileDownloaderService.class);
        robotsRepository = mock(RobotsRepository.class);
        redisUrlRepository = mock(RedisUrlRepository.class);
        consumerService = new IncomingUrlService(robotsRepository, downloaderService, redisUrlRepository);
    }

    @After
    public void tearDown() {
        downloaderService = null;
        robotsRepository = null;
        consumerService = null;
    }

    @Test
    public void testNewUrlArrives() throws Exception {
        String url = "http://example.com/hello";
        when(robotsRepository.findByHost(anyString())).thenThrow(EmptyResultDataAccessException.class);
        when(redisUrlRepository.exists(anyString())).thenReturn(false);

        consumerService.receiveUrl(url);

        String baseHost = DomainUtils.base(new URI(url));
        String robotUrl = String.format("%s/robots.txt", baseHost);


        verify(downloaderService, times(1)).download(eq(baseHost), eq(robotUrl));
    }

    @Test
    public void testUrlExistsAndNeverDownloaded() {
        String url = "http://test.example.com/hello";
        Entry entry = createEntry(url, 10);
        when(robotsRepository.findByHost(anyString())).thenReturn(entry);
        when(redisUrlRepository.exists(anyString())).thenReturn(true);

        consumerService.receiveUrl(url);
        verify(downloaderService, never()).download(anyString(), anyString());
    }

    @Test
    public void testUrlExistsAndNeedsToBeUpdated() throws Exception {
        String url = "http://test.example.com/hello";
        Entry entry = createEntry(url, 2880); // two days
        when(robotsRepository.findByHost(anyString())).thenReturn(entry);
        when(redisUrlRepository.exists(anyString())).thenReturn(false);

        consumerService.receiveUrl(url);

        String baseHost = DomainUtils.base(new URI(url));
        String robotUrl = String.format("%s/robots.txt", baseHost);
        verify(downloaderService, times(1)).download(eq(baseHost), eq(robotUrl));
    }

    private Entry createEntry(String url, long minutes) {
        LocalDateTime updatedAt = LocalDateTime.now().minusMinutes(minutes);
        Date date = new Date(updatedAt.toInstant(ZoneOffset.UTC).toEpochMilli());

        Entry entry = new Entry();
        entry.setUpdatedAt(date);
        entry.setCreatedAt(date);
        entry.setHost(url);
        entry.setRules("{}");
        return entry;
    }
}
