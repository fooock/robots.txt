package com.fooock.robotstxt.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 *
 */
@NoArgsConstructor
@Data
public class AllowResponse implements Serializable {
    private boolean isAllowed;

    private String url;
    private String agent;
}
