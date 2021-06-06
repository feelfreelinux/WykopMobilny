package io.github.wykopmobilny.utils.api

import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.intellij.markdown.flavours.commonmark.CommonMarkMarkerProcessor
import org.intellij.markdown.flavours.gfm.GFMTokenTypes
import org.intellij.markdown.flavours.gfm.lexer._GFMLexer
import org.intellij.markdown.html.GeneratingProvider
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.lexer.MarkdownLexer
import org.intellij.markdown.parser.LinkMap
import org.intellij.markdown.parser.sequentialparsers.SequentialParserManager
import org.intellij.markdown.parser.sequentialparsers.impl.AutolinkParser
import org.intellij.markdown.parser.sequentialparsers.impl.BacktickParser
import org.intellij.markdown.parser.sequentialparsers.impl.EmphStrongParser
import org.intellij.markdown.parser.sequentialparsers.impl.InlineLinkParser
import org.intellij.markdown.parser.sequentialparsers.impl.ReferenceLinkParser
import java.net.URI

class WykopFlavorDescriptor : CommonMarkFlavourDescriptor() {

    override val markerProcessorFactory = CommonMarkMarkerProcessor.Factory

    override fun createInlinesLexer() = MarkdownLexer(_GFMLexer())

    override val sequentialParserManager = object : SequentialParserManager() {
        override fun getParserSequence() =
            listOf(
                AutolinkParser(listOf(MarkdownTokenTypes.AUTOLINK, GFMTokenTypes.GFM_AUTOLINK)),
                BacktickParser(),
                InlineLinkParser(),
                ReferenceLinkParser(),
                EmphStrongParser()
            )
    }

    override fun createHtmlGeneratingProviders(linkMap: LinkMap, baseURI: URI?) =
        super.createHtmlGeneratingProviders(linkMap, baseURI) + hashMapOf(
            GFMTokenTypes.GFM_AUTOLINK to object : GeneratingProvider {
                override fun processNode(visitor: HtmlGenerator.HtmlGeneratingVisitor, text: String, node: ASTNode) {
                    val linkDestination = node.getTextInNode(text)
                    visitor.consumeTagOpen(node, "a", "href=\"$linkDestination\"")
                    visitor.consumeHtml(linkDestination)
                    visitor.consumeTagClose("a")
                }
            }
        )
}
