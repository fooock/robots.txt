grammar RobotsTxt;
import CommonLexerRules;

file   : ( group | sitemapRule | COMMENT )* ;
group  : ( agentRule agentRule* (allowRule | disallowRule | crawlDelayRule | COMMENT | malformedRule | unknownRule )* ) ;

agentRule      : USER_AGENT  COLON agentName    COMMENT* ;
disallowRule   : DISALLOW    COLON ID*          COMMENT* ;
allowRule      : ALLOW       COLON ID*          COMMENT* ;
sitemapRule    : SITEMAP     COLON ( URL | ID ) COMMENT* ;
crawlDelayRule : CRAWL_DELAY COLON NUMBER       COMMENT* ;
unknownRule    : ID          COLON ID           COMMENT* ;

// Used to detect malformed directives
malformedRule  : ( USER_AGENT | DISALLOW | ALLOW | SITEMAP | CRAWL_DELAY ) ID ;
agentName      : ( ID | NUMBER )+ ;
