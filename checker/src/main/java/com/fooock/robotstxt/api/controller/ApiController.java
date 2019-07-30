package com.fooock.robotstxt.api.controller;

import com.fooock.robotstxt.api.model.AllowData;
import com.fooock.robotstxt.api.model.AllowResponse;
import com.fooock.robotstxt.api.service.ApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
     * Check if the given URL resource can be accessed with the provided agent. Note that user agent is
     * mandatory for this method to retrieve a valid result. User agent can be provided in two different
     * modes: By request user-agent, or user defined agent. If the user agent param from the {@link AllowData}
     * class is not present, then the {@code user-agent} request header will be used, but if these header is
     * not set, the request will thrown an exception.
     *
     * @param agent Request default user agent. This field is not required to be present when the agent from
     *              the {@code data} params is present.
     * @param data  Data to be checked
     * @return An object with the data sent and a flag to indicate if the resource can be accessed or not
     */
    @PostMapping("allowed")
    public AllowResponse isAllowed(@RequestHeader(value = "user-agent", required = false) String agent,
                                   @RequestBody @Valid AllowData data) {
        if ((agent == null || agent.isEmpty()) && !data.isAgentValid())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "user-agent is not provided");
        if (!data.isAgentValid()) data.setAgent(agent);

        log.debug("[POST] Called method isAllowed to check resource {}", data.toString());
        return apiService.isAllowed(data);
    }

    /**
     * Check if the given URL resource can be accessed with the provided agent. Note that user agent is
     * mandatory for this method to retrieve a valid result. User agent can be provided in two different
     * modes: By request user-agent, or user defined agent. If the user agent param from the {@link AllowData}
     * class is not present, then the {@code user-agent} request header will be used, but if these header is not
     * set, the request will thrown an exception.
     *
     * @param requestAgent Request default user agent. Can be empty
     * @param url          Url to check. Can't be empty.
     * @param agent        Agent name. Can be empty.
     * @return An object with the data sent and a flag to indicate if the resource can be accessed or not
     */
    @GetMapping("allowed")
    public AllowResponse isAllowed(@RequestHeader(value = "user-agent", required = false) String requestAgent,
                                   @RequestParam("url") String url,
                                   @RequestParam(value = "agent", required = false) String agent) {
        // Create object with default data
        AllowData data = new AllowData();
        data.setAgent(agent);
        data.setUrl(url);

        // Validate provided user agent
        if ((requestAgent == null || requestAgent.isEmpty()) && !data.isAgentValid())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "user-agent is not provided");
        if (!data.isAgentValid()) data.setAgent(requestAgent);

        log.debug("[GET] Called method isAllowed with url {} and user agent {}", url, agent);
        return apiService.isAllowed(data);
    }
}
