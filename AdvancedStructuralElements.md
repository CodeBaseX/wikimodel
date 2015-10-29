# Introduction #
The WikiModel and the CommonWikiSyntax provides access to the following features:
  * "embedded documents"
  * semantic properties
  * macros


# Semantic Properties #

WikiModel introduces the notion of wiki document properties. There are two types of properties:
  * one-line properties -
  * block properties -


# Embedded documents #

The WikiModel introduces the notion of "embedded documents". The possible structure of such a document is exactly the same as the structure of the topmost one. It means that using embedded documents becomes possible to put block elements inside others blocks. This is a unique feature of the WikiModel. Embedded documents are delimited by the following syntactical elements: `(((`...`)))`. One embedded document can contain its own embedded  documents and there is no limits of imbrications.

Syntax for embedded documents:
```
The top-level document content
(((
... the embedded document ...
)))
... the top-level document again
```

The following example shows how a table can contain internal headers, lists and paragraphs:
```
= The top-level document

!! Table Header 1 !! Table Header 2 !! Table Header 3
:: Cell One :: Cell Two (((
= This is an embedded document

The first paragraph of the embedded document

- list item one
- list item two
  - sub-item 1
  - sub-item 2
))) :: Cell Three

The next paragraph in the top-level document
```

# Properties #

All documents (top-level and embedded ones) can have their own properties. Using this features it is possible to create very complex structured documents containing at the same time semantic markup and visual formatting.

The interpretation of such properties depends completly on developers using the WikiModel.
One possible application of this notion -- is the generation of XML structures inter-mixed with human-readable formatting. But the original goal of these structural elements is the intruduction of a unified syntax for **semantic markups** inside of wiki documents. Each property can be used to declare a semantic statement about the document in which it was defined.

The simplest property declaration:
```
%propertyName a property value 
```

A property with an embedded document as its value:
```
%description (((
= Header 
This is a structured content of the property "description"
)))
```

More complex example of properties:
```
%title A simple document

%summary A short description of this document. 
It can contain *in-line formatting* and it can be
spawn on multiple lines forming one big paragraph.

%author (((
  %firstName Mikhail
  %lastName Kotelnikov
  %worksIn (((
    %type [Company]
    %name Cognium Systems
    %address (((
         ....
    )))
    %description *Cognium Systems* is a 
    semantic web company...
  )))
)))

This is a simple content of the top-level document...
 
```

The WikiModel contains a notion of "in-line" properties:
```
I am living in %liveIn(Paris).
```