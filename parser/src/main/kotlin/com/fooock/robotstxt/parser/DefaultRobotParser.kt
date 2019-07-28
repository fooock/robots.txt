package com.fooock.robotstxt.parser

import com.fooock.robotstxt.parser.generated.RobotsTxtLexer
import com.fooock.robotstxt.parser.generated.RobotsTxtParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTreeWalker
import java.io.ByteArrayInputStream
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

/**
 * Default implementation of [BaseParser] interface.
 */
class DefaultRobotParser : BaseParser {
    override fun parse(host: String, content: ByteArray): Result {
        // By default, if the content is zero, we allow all
        if (content.isEmpty()) return Result.Builder().groups(setOf(AllowAllGroup().group)).build()

        val charset = charsetFromContent(content)
        val lexer = RobotsTxtLexer(toStream(content, charset))
        val tokens = CommonTokenStream(lexer)

        val parser = RobotsTxtParser(tokens)
        val walker = ParseTreeWalker()
        val groupListener = GroupListener()

        walker.walk(groupListener, parser.file())

        return Result.Builder()
            .sitemap(groupListener.sitemaps)
            .groups(groupListener.groups)
            .build()
    }

    private fun isUtf8Bom(c: ByteArray) = c[0] == 0xEF.toByte() && c[1] == 0xBB.toByte() && c[2] == 0xBF.toByte()
    private fun isUtf16LittleEndianBom(c: ByteArray) = c[0] == 0xFF.toByte() && c[1] == 0xFE.toByte()
    private fun isUtf16BigEndianBom(c: ByteArray) = c[0] == 0xFE.toByte() && c[1] == 0xFF.toByte()
    private fun toStream(c: ByteArray, charset: Charset) = CharStreams.fromStream(ByteArrayInputStream(c), charset)

    /**
     * Return the correct charset for the given byte array content
     */
    private fun charsetFromContent(content: ByteArray): Charset {
        return when {
            isUtf8Bom(content) -> StandardCharsets.UTF_8
            isUtf16BigEndianBom(content) -> StandardCharsets.UTF_16BE
            isUtf16LittleEndianBom(content) -> StandardCharsets.UTF_16LE
            else -> StandardCharsets.UTF_8
        }
    }
}
