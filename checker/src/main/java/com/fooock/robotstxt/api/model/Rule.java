package com.fooock.robotstxt.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 */
@Data
@NoArgsConstructor
public class Rule implements Comparable<Rule> {
    private String type;
    private String value;

    @Override
    public int compareTo(Rule o) {
        if (value.length() > o.value.length()) return -1;
        if (value.length() < o.value.length()) return 1;

        // If rules are equals, then the precedence is for the
        // allow rule always
        return "allow".equals(type) ? -1 : 1;
    }
}
