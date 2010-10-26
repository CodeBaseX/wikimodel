<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
    xmlns:w="http://www.wikimodel.org/ns/wem#"
    xmlns:u="http://www.wikimodel.org/ns/user-defined-params#"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:d="http://docbook.org/ns/docbook"
    xmlns:xlink="http://www.w3.org/TR/xlink/"
    exclude-result-prefixes="xsl w u">

    <!--
        This XSL transformation was "inspired" by this project:
        * http://wiki.docbook.org/topic/Html2DocBook
     -->
     
    <!--  
        Not interpreted tags
        TODO: FIX IT!
    -->
    <xsl:template match="w:br" />
    <xsl:template match="w:hr"/>
    <xsl:template match="w:esc"><xsl:value-of select="."/></xsl:template>

    <!-- The root -->
    <xsl:template match="w:document">
        <d:book xmlns:d="http://docbook.org/ns/docbook">
            <xsl:apply-templates select="*" />
        </d:book>
    </xsl:template>

    <!-- === Inline elements === -->

    <xsl:template match="w:format">
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="w:a">
        <d:link>
            <xsl:variable name="href" select="@w:href" />
            <xsl:variable name="label" select="@w:label" />
            <xsl:attribute name="xlink:href"><xsl:value-of select="$href"/></xsl:attribute>
            <xsl:choose>
                <xsl:when test="string-length($label) > 0"><xsl:value-of select="$label" /></xsl:when>
                <xsl:otherwise><xsl:value-of select="$href" /></xsl:otherwise>
            </xsl:choose>
        </d:link>
    </xsl:template>
    
    <xsl:template match="w:macroInline">
        <xsl:if test="normalize-space(.) != ''">
            <d:code>
                <xsl:apply-templates />
            </d:code>
        </xsl:if>
    </xsl:template>

    <xsl:template match="w:image">
        <d:mediaobject>
            <d:imageobject>
                <d:imagedata>
                    <xsl:attribute name="d:fileref">
                        <xsl:value-of select="@w:href" />
                        <!-- 
                        <xsl:call-template name="make_absolute">
                            <xsl:with-param name="filename" select="@src" />
                        </xsl:call-template>
                         -->
                    </xsl:attribute>
                    <xsl:if test="@u:height != ''">
                        <xsl:attribute name="d:depth">
                            <xsl:value-of select="@u:height" />
                        </xsl:attribute>
                    </xsl:if>
                    <xsl:if test="@u:width != ''">
                        <xsl:attribute name="d:width">
                            <xsl:value-of select="@u:width" />
                        </xsl:attribute>
                    </xsl:if>
                </d:imagedata>
            </d:imageobject>
        </d:mediaobject>
    </xsl:template>

    <!-- Catch-all template -->
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates />
        </xsl:copy>
    </xsl:template>


    <!-- ==== Block elements ==== -->

    <!-- Block Wrappers ==== -->
    <xsl:template match="w:section">
        <xsl:choose>
            <xsl:when test="@w:absLevel = 1">
                <d:chapter>
                    <xsl:apply-templates select="w:content/*" />
                </d:chapter>
            </xsl:when>
            <xsl:when test="@w:absLevel &lt; 6">
                <xsl:variable name="sectElement">d:sect<xsl:value-of select="@w:absLevel - 1" /></xsl:variable>
                <xsl:element name="{$sectElement}">
                    <xsl:call-template name="doc-SectionContent" />
                </xsl:element>
            </xsl:when>
            <xsl:otherwise>
                <d:sect>
                    <xsl:call-template name="doc-SectionContent" />
                </d:sect>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xsl:template name="doc-SectionContent">
        <xsl:variable name="title">sect<xsl:value-of select="w:header" /></xsl:variable>
        <xsl:if test="string-length($title) > 0">
            <d:title>
                <xsl:value-of select="w:header/*"/>
            </d:title>
        </xsl:if>
        <xsl:apply-templates select="w:content/*" />
    </xsl:template>

    <xsl:template match="w:info|w:blockquote">
        <xsl:element name="d:{local-name(.)}">
            <xsl:apply-templates />
        </xsl:element>    
    </xsl:template>

    <xsl:template match="w:macroBlock">
        <xsl:if test="normalize-space(.) != ''">
            <d:programlisting>
                <xsl:apply-templates />
            </d:programlisting>
        </xsl:if>
    </xsl:template>
    
    <!--  Paragraphs -->
    <xsl:template match="w:p">
        <d:para>
            <xsl:apply-templates select="@*|node()" />
        </d:para>
    </xsl:template>

    <xsl:template match="w:pre|w:preInline">
        <d:programlisting>
            <xsl:apply-templates />
        </d:programlisting>
    </xsl:template>

    <!--  Lists -->
    <xsl:template match="w:ul">
        <d:itemizedlist>
            <xsl:apply-templates select="w:li"/>
        </d:itemizedlist>
    </xsl:template>

    <xsl:template match="w:ol">
        <d:orderedlist>
            <xsl:apply-templates select="w:li" />
        </d:orderedlist>
    </xsl:template>

    <xsl:template match="w:li">
        <d:listitem>
            <xsl:apply-templates />
        </d:listitem>
    </xsl:template>

    <!-- Definition List => DocBook variablelist -->
    <xsl:template match="w:dl">
        <d:variablelist>
            <xsl:for-each select="w:dt">
                <d:varlistentry>
                    <d:term>
                        <xsl:apply-templates />
                    </d:term>
                    <d:listitem>
                        <xsl:apply-templates
                            select="following-sibling::w:dd[1]" />
                    </d:listitem>
                </d:varlistentry>
            </xsl:for-each>
        </d:variablelist>
    </xsl:template>

    <xsl:template match="w:dd">
        <xsl:choose>
            <xsl:when test="boolean(w:p)">
                <xsl:apply-templates />
            </xsl:when>
            <xsl:otherwise>
                <d:para>
                    <xsl:apply-templates />
                </d:para>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!-- Tables -->
    <xsl:template match="w:table">
        <d:informaltable>
            <d:tbody>
                <xsl:apply-templates select="w:tr" />
            </d:tbody>
        </d:informaltable>
    </xsl:template>
    <xsl:template match="w:tr">
        <d:tr>
            <xsl:apply-templates select="w:td|w:th"/>
        </d:tr>
    </xsl:template>
    <xsl:template match="w:th|w:td">
        <xsl:element name="d:{local-name(.)}">
            <xsl:apply-templates />
        </xsl:element>
    </xsl:template>
  
</xsl:stylesheet>

