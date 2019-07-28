package com.fooock.robotstxt.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Wrapper class around set of {@link Group}s
 */
@Data
@NoArgsConstructor
public class Content {
    private List<Group> groups;
}
