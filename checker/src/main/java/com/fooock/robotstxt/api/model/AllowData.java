package com.fooock.robotstxt.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * Represent a resource to check if it is allowed to be crawled by the given user agent
 */
@EqualsAndHashCode(doNotUseGetters = true)
@Data
@NoArgsConstructor
public class AllowData implements Serializable {

    @NotEmpty(message = "URL cannot be empty or null")
    private String url;
    private String agent;

    /**
     * @return True if agent is not null or empty, false otherwise.
     */
    public boolean isAgentValid() {
        return agent != null && !agent.isEmpty();
    }
}
