#!/bin/sh

dir=`pwd`
migrate=$dir/migrate
rm -rf $migrate >> /dev/null
mkdir $migrate

baseDir=org.wikimodel.wem
src="src/main/java/org/wikimodel/wem"
test="src/test/java/org/wikimodel/wem/test"

#
# Aggregator POM
#
cp $dir/pom-aggregator.xml $migrate/pom.xml

#
# Copy all the sources for the core bundle
#
(
cd $migrate
coreDir="${baseDir}.core"
mkdir -p ${coreDir}/${src}
cd ${coreDir}/${src}
cp -r ${dir}/${src}/*.java .
mkdir util
cp -r ${dir}/${src}/util/*.java util
mkdir common
cp -r ${dir}/${src}/common common/..
mkdir impl
cp -r ${dir}/${src}/impl/*.java impl
mkdir images
cp -r ${dir}/${src}/images/*.java images
# XHTML is coupled to the core for the listener in the tests
mkdir xhtml
cp -r ${dir}/${src}/xhtml xhtml/..

)


#
# Copy all the sources for the syntaxes
#
modules="confluence creole gwiki jspwiki mediawiki tex xhtml xml xwiki"

( 
cd $migrate
for module in $modules
do
  moduleDir="${baseDir}.${module}"
  mkdir $moduleDir
  (
    cd $moduleDir
    mkdir -p $src/$module
    cp -R $dir/$src/$module $src
  )
  moduleName=`ruby -e 'puts ARGV[0].capitalize' ${module}`
  sed "s/@MODULE@/${module}/g;s/@MODULE_NAME@/${moduleName}/" $dir/pom-module.xml > ${migrate}/${moduleDir}/pom.xml
done 
)

# POMs

cp $dir/pom-core.xml ${migrate}/org.wikimodel.wem.core/pom.xml
#cp $dir/pom-xhtml.xml ${migrate}/org.wikimodel.wem.xhtml/pom.xml

# Test in the core project

mkdir -p $migrate/org.wikimodel.wem.mediawiki/src/test/java/org/wikimodel/wem/mediawiki
cp $dir/src/test/java/org/wikimodel/wem/mediawiki/MediaWikiReferenceParserTest.java \
   $migrate/org.wikimodel.wem.mediawiki/src/test/java/org/wikimodel/wem/mediawiki

mkdir -p $migrate/org.wikimodel.wem.core/src/test/java/org/wikimodel/wem/test
files="AbstractWikiParserTest.java CommonWikiParserTest.java PrintCharactersTest.java WikiFormatTest.java WikiScannerUtilTest.java  EventDumpListenerTest.java"
for file in $files
do
cp $dir/src/test/java/org/wikimodel/wem/test/$file \
   $migrate/org.wikimodel.wem.core/src/test/java/org/wikimodel/wem/test
done   

mkdir -p $migrate/org.wikimodel.wem.core/src/test/java/org/wikimodel/wem/util
cp -r $dir/src/test/java/org/wikimodel/wem/util \
   $migrate/org.wikimodel.wem.core/src/test/java/org/wikimodel/wem

mkdir -p $migrate/org.wikimodel.wem.xml/src/test/java/org/wikimodel/wem/xml
cp -r $dir/src/test/java/org/wikimodel/wem/xml \
   $migrate/org.wikimodel.wem.xml/src/test/java/org/wikimodel/wem

cp $dir/src/test/java/org/wikimodel/wem/ReferenceHandlerTest.java \
   $migrate/org.wikimodel.wem.core/src/test/java/org/wikimodel/wem


# The XHTML stuff needs to be in the core
mkdir -p $migrate/org.wikimodel.wem.core/src/test/java/org/wikimodel/wem/test/xhtml
files="XHTMLWhitespaceXMLFilterTest.java XHtmlParserTest.java XMLWriter.java"
for file in $files
do
cp $dir/src/test/java/org/wikimodel/wem/test/xhtml/$file \
   $migrate/org.wikimodel.wem.core/src/test/java/org/wikimodel/wem/test/xhtml
done

#ConfluenceWikiParserTest.java
#CreoleWikiParserTest.java
#GWikiParserTest.java
#JspWikiParserTest.java
#MediawikiParserTest.java
#XWiki20ParserTest.java
#XWiki21ParserTest.java
#XWikiReferenceParserTest.java

mkdir -p $migrate/org.wikimodel.wem.confluence/src/test/java/org/wikimodel/wem/test
cp $dir/src/test/java/org/wikimodel/wem/test/ConfluenceWikiParserTest.java \
   $migrate/org.wikimodel.wem.confluence/src/test/java/org/wikimodel/wem/test

mkdir -p $migrate/org.wikimodel.wem.creole/src/test/java/org/wikimodel/wem/test
cp $dir/src/test/java/org/wikimodel/wem/test/CreoleWikiParserTest.java \
   $migrate/org.wikimodel.wem.creole/src/test/java/org/wikimodel/wem/test

mkdir -p $migrate/org.wikimodel.wem.gwiki/src/test/java/org/wikimodel/wem/test
cp $dir/src/test/java/org/wikimodel/wem/test/GWikiParserTest.java \
   $migrate/org.wikimodel.wem.gwiki/src/test/java/org/wikimodel/wem/test

mkdir -p $migrate/org.wikimodel.wem.jspwiki/src/test/java/org/wikimodel/wem/test
cp $dir/src/test/java/org/wikimodel/wem/test/JspWikiParserTest.java \
   $migrate/org.wikimodel.wem.jspwiki/src/test/java/org/wikimodel/wem/test

mkdir -p $migrate/org.wikimodel.wem.mediawiki/src/test/java/org/wikimodel/wem/test
cp $dir/src/test/java/org/wikimodel/wem/test/MediawikiParserTest.java \
   $migrate/org.wikimodel.wem.mediawiki/src/test/java/org/wikimodel/wem/test

mkdir -p $migrate/org.wikimodel.wem.xwiki/src/test/java/org/wikimodel/wem/test
files="XWiki20ParserTest.java XWiki21ParserTest.java XWikiReferenceParserTest.java"
for file in $files
do
cp $dir/src/test/java/org/wikimodel/wem/test/$file\
   $migrate/org.wikimodel.wem.xwiki/src/test/java/org/wikimodel/wem/test
done

# I'm not sure these are even used

# Tests

# AbstractWikiParserTest.java
# CommonWikiParserTest.java
# CreoleWikiParserTest.java
# GWikiParserTest.java
# JspWikiParserTest.java
# MediawikiParserTest.java
# PrintCharactersTest.java
# WikiScannerUtilTest.java
# XWikiParserTest.java

#mkdir -p $migrate/org.wikimodel.wem.core/src/test/java/org/wikimodel/wem/test
#
#cp $dir/../org.wikimodel.wem.test/src/org/wikimodel/wem/test/AbstractWikiParserTest.java \
#   $migrate/org.wikimodel.wem.core/src/test/java/org/wikimodel/wem/test
#
#cp $dir/../org.wikimodel.wem.test/src/org/wikimodel/wem/test/CommonWikiParserTest.java \
#   $migrate/org.wikimodel.wem.core/src/test/java/org/wikimodel/wem/test
#
#cp $dir/../org.wikimodel.wem.test/src/org/wikimodel/wem/test/PrintCharactersTest.java \
#   $migrate/org.wikimodel.wem.core/src/test/java/org/wikimodel/wem/test
#
#cp $dir/../org.wikimodel.wem.test/src/org/wikimodel/wem/test/WikiScannerUtilTest.java \
#   $migrate/org.wikimodel.wem.core/src/test/java/org/wikimodel/wem/test
#
## Other markups
#
#mkdir -p $migrate/org.wikimodel.wem.creole/src/test/java/org/wikimodel/wem/test
#cp $dir/../org.wikimodel.wem.test/src/org/wikimodel/wem/test/CreoleWikiParserTest.java \
#   $migrate/org.wikimodel.wem.creole/src/test/java/org/wikimodel/wem/test
#
#mkdir -p $migrate/org.wikimodel.wem.gwiki/src/test/java/org/wikimodel/wem/test
#cp $dir/../org.wikimodel.wem.test/src/org/wikimodel/wem/test/GWikiParserTest.java \
#   $migrate/org.wikimodel.wem.gwiki/src/test/java/org/wikimodel/wem/test
#
#mkdir -p $migrate/org.wikimodel.wem.jspwiki/src/test/java/org/wikimodel/wem/test
#cp $dir/../org.wikimodel.wem.test/src/org/wikimodel/wem/test/JspWikiParserTest.java \
#   $migrate/org.wikimodel.wem.jspwiki/src/test/java/org/wikimodel/wem/test
#
#mkdir -p $migrate/org.wikimodel.wem.mediawiki/src/test/java/org/wikimodel/wem/test
#cp $dir/../org.wikimodel.wem.test/src/org/wikimodel/wem/test/MediawikiParserTest.java \
#   $migrate/org.wikimodel.wem.mediawiki/src/test/java/org/wikimodel/wem/test
#
#mkdir -p $migrate/org.wikimodel.wem.xwiki/src/test/java/org/wikimodel/wem/test
#cp $dir/../org.wikimodel.wem.test/src/org/wikimodel/wem/test/XWikiParserTest.java \
#   $migrate/org.wikimodel.wem.xwiki/src/test/java/org/wikimodel/wem/test

# Resources

mkdir $migrate/org.wikimodel.wem.core/src/main/resources
cp -r $dir/src/main/resources/*  $migrate/org.wikimodel.wem.core/src/main/resources
cp $dir/resources/common-tokens.tmpl $migrate/org.wikimodel.wem.core/src/main/resources


