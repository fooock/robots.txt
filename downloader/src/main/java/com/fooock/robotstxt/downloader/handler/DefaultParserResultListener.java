package com.fooock.robotstxt.downloader.handler;

import com.fooock.robotstxt.database.RobotsRepository;
import com.fooock.robotstxt.database.entity.Entry;
import com.fooock.robotstxt.database.mapper.ResultToEntryMapper;
import com.fooock.robotstxt.downloader.type.ResultType;
import com.fooock.robotstxt.parser.AllowAllGroup;
import com.fooock.robotstxt.parser.BaseParser;
import com.fooock.robotstxt.parser.DisallowAllGroup;
import com.fooock.robotstxt.parser.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * Default implementation of {@link ParserResultListener}
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultParserResultListener implements ParserResultListener {
    private final ResultToEntryMapper mapper = new ResultToEntryMapper();
    private final RobotsRepository repository;
    private final BaseParser baseParser;

    @Override
    public void onResult(String url, Headers headers, byte[] content, ResultType type) {
        log.debug("Try to save url {}", url);
        Result result = parseResultForType(type, content, url);
        try {
            // Check if host exists in database. Note that if entry object
            // is not null, then it exists, and we need to update some values
            // in the new object to save it correctly
            Entry entry = repository.findByHost(url);
            Entry newEntry = mapper.map(url, result);

            log.debug("Found existing entry saved at {} with id {} and host {}",
                    entry.getUpdatedAt(), entry.getId(), entry.getHost());

            newEntry.setId(entry.getId());
            newEntry.setCreatedAt(entry.getCreatedAt());
            newEntry.setUpdatedAt(entry.getUpdatedAt());

            // Update entity
            Entry save = repository.save(newEntry);
            log.info("Updated entry at {} with id {} and url {}", save.getUpdatedAt(), save.getId(), save.getHost());

            return;

        } catch (EmptyResultDataAccessException e) {
            log.info("Url {} doesn't exists in database, save it", url);
        }

        // Save if not found
        Entry entry = repository.save(mapper.map(url, result));
        log.info("Saved new entry at {} with id {} and url {}", entry.getCreatedAt(), entry.getId(), entry.getHost());
    }

    /**
     * Parse the result depending on the current {@link ResultType}. This method only tries to parse
     * the content if the result type is {@link ResultType#CUSTOM}.
     *
     * @param type    Type of the result
     * @param content Content in bytes
     * @param url     Url of the content
     * @return Result
     */
    private Result parseResultForType(ResultType type, byte[] content, String url) {
        if (type == ResultType.ALLOW_ALL || content.length == 0)
            return new Result.Builder().groups(Collections.singleton(new AllowAllGroup().getGroup())).build();
        if (type == ResultType.DISABLE_ALL)
            return new Result.Builder().groups(Collections.singleton(new DisallowAllGroup().getGroup())).build();
        return baseParser.parse(url, content);
    }
}
