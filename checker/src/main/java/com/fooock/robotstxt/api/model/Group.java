package com.fooock.robotstxt.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 *
 */
@Data
@NoArgsConstructor
public class Group {
    private int delay;
    private List<String> userAgents;
    private List<Rule> rules;
}
