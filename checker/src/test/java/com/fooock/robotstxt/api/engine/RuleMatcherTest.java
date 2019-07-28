package com.fooock.robotstxt.api.engine;

import com.fooock.robotstxt.api.TestUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 */
public class RuleMatcherTest {
    private RuleMatcher ruleMatcher;

    @Before
    public void setUp() {
        Gson gson = new GsonBuilder().create();
        ruleMatcher = new RuleMatcher(gson);
    }

    @After
    public void tearDown() {
        ruleMatcher = null;
    }

    @Test
    public void testNoGroupsFound() throws IOException {
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("empty-group.json");
        String content = TestUtils.readFile(stream);

        assertTrue(ruleMatcher.canCrawl("*", "/", content));
        assertTrue(ruleMatcher.canCrawl("abc", "/", content));
        assertTrue(ruleMatcher.canCrawl("abc", "/test", content));
    }

    @Test
    public void testPathIsRobotsFile() throws IOException {
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("group-without-rules.json");
        String content = TestUtils.readFile(stream);

        assertTrue(ruleMatcher.canCrawl("*", "/robots.txt", content));
        assertTrue(ruleMatcher.canCrawl("abc", "/", content));
        assertTrue(ruleMatcher.canCrawl("abc", "/test", content));
        assertTrue(ruleMatcher.canCrawl("*", "/", content));
    }

    @Test
    public void testGroupWithEmptyRules() throws IOException {
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("group-without-rules.json");
        String content = TestUtils.readFile(stream);

        assertTrue(ruleMatcher.canCrawl("*", "/", content));
        assertTrue(ruleMatcher.canCrawl("abc", "/", content));
        assertTrue(ruleMatcher.canCrawl("abc", "/test", content));
    }

    @Test
    public void testGroupWithOneAllowRule() throws IOException {
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("group-one-allow-rule.json");
        String content = TestUtils.readFile(stream);

        assertTrue(ruleMatcher.canCrawl("*", "/", content));
        assertTrue(ruleMatcher.canCrawl("abc", "/test", content));
        assertTrue(ruleMatcher.canCrawl("abc", "/*", content));
    }

    @Test
    public void testGroupWithOneDisallowRule() throws IOException {
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("group-one-disallow-rule.json");
        String content = TestUtils.readFile(stream);

        assertFalse(ruleMatcher.canCrawl("*", "/", content));
        assertFalse(ruleMatcher.canCrawl("ab", "/", content));
        assertFalse(ruleMatcher.canCrawl("ab", "/test", content));
    }

    @Test
    public void testGroupWithOneEmptyRule() throws IOException {
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("group-one-empty-rule.json");
        String content = TestUtils.readFile(stream);

        assertTrue(ruleMatcher.canCrawl("*", "/", content));
        assertTrue(ruleMatcher.canCrawl("ab", "/test", content));
        assertTrue(ruleMatcher.canCrawl("ab", "/", content));
    }

    @Test
    public void testGroupMultiRule() throws IOException {
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("group-multi-rule.json");
        String content = TestUtils.readFile(stream);

        assertFalse(ruleMatcher.canCrawl("*", "/users/hello", content));
        assertFalse(ruleMatcher.canCrawl("*", "/users/", content));
        assertFalse(ruleMatcher.canCrawl("*", "/links/source/test", content));
        assertFalse(ruleMatcher.canCrawl("*", "/links/source/", content));

        assertTrue(ruleMatcher.canCrawl("*", "/hello", content));
        assertTrue(ruleMatcher.canCrawl("*", "/links/hello", content));
        assertTrue(ruleMatcher.canCrawl("*", "/users/pazkidd/test", content));
        assertTrue(ruleMatcher.canCrawl("*", "/users/russelltodd/", content));
    }

    @Test
    public void testGroupMultiRuleWithUnknownUserAgent() throws IOException {
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("group-multi-rule.json");
        String content = TestUtils.readFile(stream);

        assertFalse(ruleMatcher.canCrawl("abc-bot", "/users/hello", content));
        assertFalse(ruleMatcher.canCrawl("abc-bot", "/users/", content));
        assertFalse(ruleMatcher.canCrawl("abc-bot", "/links/source/test", content));
        assertFalse(ruleMatcher.canCrawl("abc-bot", "/links/source/", content));

        assertTrue(ruleMatcher.canCrawl("abc-bot", "/hello", content));
        assertTrue(ruleMatcher.canCrawl("abc-bot", "/links/hello", content));
        assertTrue(ruleMatcher.canCrawl("abc-bot", "/users/pazkidd/test", content));
        assertTrue(ruleMatcher.canCrawl("abc-bot", "/users/russelltodd/", content));
    }

    @Test
    public void testGroupMultiAgentAndRule() throws IOException {
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("group-multi-agent-and-rule.json");
        String content = TestUtils.readFile(stream);

        assertFalse(ruleMatcher.canCrawl("googlebot", "/users/hello", content));
        assertFalse(ruleMatcher.canCrawl("googlebot", "/users/", content));
        assertFalse(ruleMatcher.canCrawl("googlebot", "/links/source/test", content));
        assertFalse(ruleMatcher.canCrawl("googlebot", "/links/source/", content));

        assertTrue(ruleMatcher.canCrawl("googlebot", "/test", content));
        assertTrue(ruleMatcher.canCrawl("googlebot", "/users", content));

        assertTrue(ruleMatcher.canCrawl("*", "/links", content));
        assertTrue(ruleMatcher.canCrawl("*", "/links/sourc", content));
        assertTrue(ruleMatcher.canCrawl("*", "/hello/links/source", content));
        assertTrue(ruleMatcher.canCrawl("hello007", "/hello/links/source", content));
        assertTrue(ruleMatcher.canCrawl("hello007", "/users/javi", content));
    }

    @Test
    public void testCheckUserAgentPriority() throws IOException {
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("similar-agents-robots.json");
        String content = TestUtils.readFile(stream);

        assertTrue(ruleMatcher.canCrawl("GoogleBot-News", "/users", content));
        assertTrue(ruleMatcher.canCrawl("GoogleBot-News", "/hello", content));
        assertTrue(ruleMatcher.canCrawl("yanDex", "/test/hello", content));
        assertTrue(ruleMatcher.canCrawl("yanDex", "/testing", content));

        assertFalse(ruleMatcher.canCrawl("googlebot", "/users", content));
        assertFalse(ruleMatcher.canCrawl("007", "/Test/hello", content));
        assertFalse(ruleMatcher.canCrawl("007", "/hello", content));
    }

    @Test
    public void testEmptyUserAgentToBeMatched() throws IOException {
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("empty-default-agent.json");
        String content = TestUtils.readFile(stream);

        assertTrue(ruleMatcher.canCrawl("*", "/users", content));
        assertTrue(ruleMatcher.canCrawl("abc", "/users", content));
        assertTrue(ruleMatcher.canCrawl("abc", "/", content));

        assertFalse(ruleMatcher.canCrawl("googlebot", "/hello", content));
        assertFalse(ruleMatcher.canCrawl("googlebot", "/", content));
    }

    @Test
    public void testOnlyDefaultAgent() throws IOException {
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("only-default-agent.json");
        String content = TestUtils.readFile(stream);

        assertTrue(ruleMatcher.canCrawl("*", "/test", content));
        assertTrue(ruleMatcher.canCrawl("*", "/test/", content));
        assertTrue(ruleMatcher.canCrawl("abc", "/test/1", content));
        assertTrue(ruleMatcher.canCrawl("googlebot", "/hello", content));
        assertTrue(ruleMatcher.canCrawl("*", "/hello", content));
    }

    @Test
    public void testSameAllowAndDisallowDefaultRule() throws IOException {
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("same-default-rules.json");
        String content = TestUtils.readFile(stream);

        assertTrue(ruleMatcher.canCrawl("googlebot", "/test", content));
        assertTrue(ruleMatcher.canCrawl("googlebot", "/", content));
    }

    @Test
    public void testSameAllowAndDisallowRulePattern() throws IOException {
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("same-pattern-rules.json");
        String content = TestUtils.readFile(stream);

        assertTrue(ruleMatcher.canCrawl("googlebot", "/test/file.html", content));
        assertTrue(ruleMatcher.canCrawl("googlebot", "/test", content));
        assertTrue(ruleMatcher.canCrawl("googlebot", "/not-exists", content));
    }

    @Test
    public void testDollarPattern() throws Exception {
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("dollar-pattern.json");
        String content = TestUtils.readFile(stream);

        assertTrue(ruleMatcher.canCrawl("*", "/hello/test", content));
        assertTrue(ruleMatcher.canCrawl("*", "/hello/allow/this/rule/test", content));
        assertTrue(ruleMatcher.canCrawl("*", "/hello/allow/this/rule/test.php", content));
        assertTrue(ruleMatcher.canCrawl("*", "/name/test.php", content));

        assertFalse(ruleMatcher.canCrawl("*", "/hello/test%24", content));
        assertFalse(ruleMatcher.canCrawl("*", "/hello/test/", content));
        assertFalse(ruleMatcher.canCrawl("googleBot", "/hello/Test", content));
        assertFalse(ruleMatcher.canCrawl("007", "/test/", content));
        assertFalse(ruleMatcher.canCrawl("*", "/hello/allow/this/rule/test.PHP", content));
        assertFalse(ruleMatcher.canCrawl("*", "/hello/allow/this/rule/test.php5", content));
        assertFalse(ruleMatcher.canCrawl("*", "/hello/allow/this/rule/test.php/", content));
    }

    @Test
    public void testAllWildcardPattern() throws Exception {
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("wildcard-disallow-all.json");
        String content = TestUtils.readFile(stream);

        assertFalse(ruleMatcher.canCrawl("*", "/hello?user=javi", content));
        assertFalse(ruleMatcher.canCrawl("*", "/disallow/this/rule?user=javi", content));

        assertTrue(ruleMatcher.canCrawl("007", "/test?user=javi", content));
        assertTrue(ruleMatcher.canCrawl("007", "/test/allow/this/hello", content));
    }
}
