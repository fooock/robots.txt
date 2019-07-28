lexer grammar CommonLexerRules;

COMMENT : '#' ~( '\r' | '\n' )* -> skip ;
WS      : ( ' '| '\t'| '\n'| '\r' ) -> channel(HIDDEN) ;

fragment CHAR: ( [a-z] | [A-Z] | '/' | '~' | '-' | '_' | '&' | '?' | '.' | '*' | ',' | '$' | '%' | '..' | '@' | '+' | '(' | ' )' | '!' | '"' | '\'' | '|' | ';' | '#' | '=' )+;

fragment A : ( 'a' | 'A' ) ;
fragment B : ( 'b' | 'B' ) ;
fragment C : ( 'c' | 'C' ) ;
fragment D : ( 'd' | 'D' ) ;
fragment E : ( 'e' | 'E' ) ;
fragment F : ( 'f' | 'F' ) ;
fragment G : ( 'g' | 'G' ) ;
fragment H : ( 'h' | 'H' ) ;
fragment I : ( 'i' | 'I' ) ;
fragment J : ( 'j' | 'J' ) ;
fragment K : ( 'k' | 'K' ) ;
fragment L : ( 'l' | 'L' ) ;
fragment M : ( 'm' | 'M' ) ;
fragment N : ( 'n' | 'N' ) ;
fragment O : ( 'o' | 'O' ) ;
fragment P : ( 'p' | 'P' ) ;
fragment Q : ( 'q' | 'Q' ) ;
fragment R : ( 'r' | 'R' ) ;
fragment S : ( 's' | 'S' ) ;
fragment T : ( 't' | 'T' ) ;
fragment U : ( 'u' | 'U' ) ;
fragment V : ( 'v' | 'V' ) ;
fragment W : ( 'w' | 'W' ) ;
fragment X : ( 'x' | 'X' ) ;
fragment Y : ( 'y' | 'Y' ) ;
fragment Z : ( 'z' | 'Z' ) ;

fragment HTTP  : H T T P ;
fragment HTTPS : H T T P S ;

NUMBER : [0-9]+ | ( [0-9]* '.' [0-9]+ )+ ;

COLON       : ':' ;
USER_AGENT  : U S E R '-' A G E N T ;
SITEMAP     : S I T E M A P ;
DISALLOW    : D I S A L L O W ;
ALLOW       : A L L O W ;
CRAWL_DELAY : C R A W L '-' D E L A Y;

URL : ( HTTP | HTTPS ) '://' ( CHAR | NUMBER )* ;
ID  : ( CHAR | NUMBER )+ ;

// This rule allows ANTLR 4 to parse grammars using the UTF-8 encoding with a
// byte order mark. Since this Unicode character doesn't appear as a token
// anywhere else in the grammar, we can simply skip all instances of it without
// problem. This rule will not break usage of \uFEFF inside a LEXER_CHAR_SET or
// STRING_LITERAL.
// From ANTLR4 pull request #426
UNICODE_BOM : '\uFEFF' -> skip ;
