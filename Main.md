# Projects goals #

The main goal of this project is creation of a “standard” model for wiki documents. It contains two different models:

  * [Wiki Event Model](http://wikimodel.googlecode.com/svn/trunk/org.wikimodel.wem/) (WEM) – like Simple API for XML (SAX)
  * Wiki Object Model (WOM) – like Document Object Model for XML (DOM)
  * Parsers for various wiki syntaxes. Right now parsers for following syntaxes are supported:
    * [Creole](http://www.wikicreole.org/)
    * Google Wiki (this syntax)
    * [JspWiki](http://www.jspwiki.org)
    * [MediaWiki](http://www.mediawiki.org)
    * [XWiki](http://www.xwiki.org)
    * WikiSyntaxCommon - a "native" WikiModel syntax providing functionalities like embedded documents,  properties and so on.
    * ... and other on the way (Confluence, MoinMoin, TWiki...)

For all of these wiki syntaxes the same event model is used.

This project is a "replacement" of the WikiModel initiative started on the SourceForge (http://wikimodel.sourceforge.net/)
