# `robots.txt` parser

[![License](https://img.shields.io/badge/License-GPLv3%202.0-brightgreen.svg?style=for-the-badge)](https://www.gnu.org/licenses/gpl-3.0)

Project to parse `robots.txt` files. It uses [`ANTLR`](https://www.antlr.org/) to build the parser.
For simplicity, this tool is included in the current directory to make things easier for new developers. If you
don't want to use the included binary, see [the ANTLR quick start](https://www.antlr.org/) to download and setup it.

## Generate grammar

In order to generate the lexer and parser classes, execute the following command:

```
$ make generate
```
>All generated files are under the `com.fooock.robotstxt.parser.generated` package

## Recommended plugins

If you are working with Intellij, I recommend installing these plugins:

* [ANTLR v4 grammar](https://plugins.jetbrains.com/plugin/7358-antlr-v4-grammar-plugin)
