package com.fooock.robotstxt.api.controller;

import com.fooock.robotstxt.api.model.AllowData;
import com.fooock.robotstxt.api.model.AllowResponse;
import com.fooock.robotstxt.api.service.ApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Application main controller
 */
@RequiredArgsConstructor
@Slf4j
@RequestMapping("v1")
@RestController
public class ApiController {
    private final ApiService apiService;

    /**
     * Check if the given URL resource can be accessed with the provided agent.
     *
     * @param data Data to be checked
     * @return An object with the data sent and a flag to indicate if the resource can be accessed or not
     */
    @PostMapping("allowed")
    public AllowResponse isAllowed(@RequestBody @Valid AllowData data) {
        log.debug("[POST] Called method isAllowed to check resource {}", data.toString());
        return apiService.isAllowed(data);
    }

    /**
     * Check if the given URL resource can be accessed with the provided agent.
     *
     * @param url   Url to check
     * @param agent Agent name
     * @return An object with the data sent and a flag to indicate if the resource can be accessed or not
     */
    @GetMapping(value = "allowed", params = {"url", "agent"})
    public AllowResponse isAllowed(@RequestParam("url") String url, @RequestParam("agent") String agent) {
        log.debug("[GET] Called method isAllowed with url {} and user agent {}", url, agent);

        AllowData data = new AllowData();
        data.setAgent(agent);
        data.setUrl(url);

        return apiService.isAllowed(data);
    }
}
