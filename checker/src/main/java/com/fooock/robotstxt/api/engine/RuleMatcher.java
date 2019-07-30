package com.fooock.robotstxt.api.engine;

import com.fooock.robotstxt.api.model.Content;
import com.fooock.robotstxt.api.model.Group;
import com.fooock.robotstxt.api.model.Rule;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 *
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class RuleMatcher {
    private static final String ALLOW = "allow";
    private static final String DISALLOW = "disallow";
    private static final String ALL = "*";
    private static final String DOLLAR = "$";

    private final Gson gson;
    private final Type type = new TypeToken<Content>() {
    }.getType();

    /**
     * Check if the path associated to the given host can be crawled by the user agent. For this
     * process we use the given json of rules. There are some notes to take account. If the current
     * path is {@code /robots.txt}, then the result always is true.
     *
     * @param agent Current user agent to check
     * @param path  Path to know if can be crawled by the given user agent
     * @param rules Set of rules for the current host
     * @return True if path can be crawled, false otherwise
     */
    public boolean canCrawl(String agent, String path, String rules) {
        // Fixes #29
        if (path == null || path.isEmpty()) path = "/";

        // if path is /robots.txt then allow always
        if ("/robots.txt".equals(path)) return true;

        Content content = gson.fromJson(rules, type);
        List<Group> groups = content.getGroups();

        // if no groups found, then allow all
        if (groups.isEmpty()) return true;

        List<String> userAgents = groups.stream()
                .flatMap(group -> group.getUserAgents().stream().map(String::toLowerCase))
                .collect(Collectors.toList());

        if (userAgents.isEmpty()) return true;
        if (userAgents.parallelStream().noneMatch(ALL::equals) && ALL.equals(agent)) return true;

        agent = agent.toLowerCase();
        if (noneMatch(agent, userAgents) && noneMatch(ALL, userAgents) && !ALL.equals(agent)) return true;

        // Flag used to check if the given user agent match any saved agent.
        // Note that if no user agent match, it will use * as base user agent
        boolean foundUserAgent = false;

        for (Group group : groups) {
            // Directive priority is decided using length, that is, longest first
            Collections.sort(group.getRules());

            for (String ua : group.getUserAgents()) {
                // Check if user agent match. User agent case is ignored as
                // google documentation says
                if (agent.contains(ua.toLowerCase())) {
                    foundUserAgent = true;
                    // If rules check, then allow
                    if (checkGroupRules(group, path)) return true;
                }
            }

            // Check with default user agent (*)
            if (!foundUserAgent) {
                for (String ua : group.getUserAgents()) {
                    // Continue loop until * found
                    if (!ALL.equals(ua)) continue;
                    if (checkGroupRules(group, path)) return true;
                }
            }
        }
        return false;
    }

    /**
     * @param agent      User agent to check
     * @param userAgents List of user agents found in the robots file
     * @return True if none agent match the given one, false otherwise
     */
    private boolean noneMatch(String agent, List<String> userAgents) {
        return userAgents.parallelStream().noneMatch(agent::contains);
    }

    /**
     * Check if the given path is allowed to be crawled or not. Note that empty
     * rules are ignored.
     *
     * @param group Current group
     * @param path  Path to be checked
     * @return True is allowed, false otherwise
     */
    private boolean checkGroupRules(Group group, String path) {
        List<Rule> groupRules = group.getRules();
        for (Rule rule : groupRules) {
            if (rule.getValue().isEmpty()) continue;

            // Check for $ symbol. If match, remove the $ char to check the given path string.
            if (rule.getValue().endsWith(DOLLAR) && !rule.getValue().contains(ALL)) {
                String sub = rule.getValue().substring(0, rule.getValue().length() - 1);

                if (path.equals(sub) && rule.getType().equals(ALLOW)) return true;
                if (path.equals(sub) && rule.getType().equals(DISALLOW)) return false;
            }

            // Check for * wildcard
            if (rule.getValue().contains(ALL)) {
                if (matchesRule(path, DISALLOW, rule)) return false;
                if (matchesRule(path, ALLOW, rule)) return true;
            }

            // If the path to check starts with the current rule, then
            // return false to not allow crawling to this resource
            if (checkRulePath(path, ALLOW, rule)) return true;
            if (checkRulePath(path, DISALLOW, rule)) return false;
        }
        // If no group satisfies either condition, no rules apply
        return true;
    }

    /**
     * Check if the current path matches the given rule value.
     *
     * @param path      Patch to check if match
     * @param directive Directive to match rule value
     * @param rule      Rule to match
     * @return True if the path value matches the rule value, false otherwise
     */
    private boolean matchesRule(String path, String directive, Rule rule) {
        if (!directive.equals(rule.getType())) return false;
        String quote = rule.getValue().replaceAll("\\*", "(?:.*)");
        Matcher matcher = Pattern.compile(quote).matcher(path);
        return matcher.matches();
    }

    /**
     * Check if the given path matches rule for the given directive to check.
     *
     * @param path      Path to check
     * @param directive Directive to match rule value
     * @param rule      Current rule to check
     * @return True if the given rule matches the given path
     */
    private boolean checkRulePath(String path, String directive, Rule rule) {
        return path.startsWith(rule.getValue()) && rule.getType().equals(directive);
    }
}
