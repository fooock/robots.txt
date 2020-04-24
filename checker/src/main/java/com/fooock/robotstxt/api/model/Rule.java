package com.fooock.robotstxt.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;

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
        // First we sort by type of rules: allow > disallow
        // Second is to sort rules by length, longest first
        // Third, if there is two equal rules but one ends with ? and the other ends with $, the precedence makes
        // the rule that ends with ? first.
        return Comparator.comparing(Rule::getType)
                .thenComparingInt(rule -> Integer.compare(o.getValue().length(), rule.getValue().length()))
                .thenComparing((rule, t1) -> {
                    if (rule.getValue().equals(t1.getValue())) return 0;
                    return rule.getValue().endsWith("?") ? -1 : 1;
                })
                .compare(this, o);
    }
}
