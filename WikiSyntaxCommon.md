# Introduction #

The common Wiki Syntax was developed as the "native" Wiki Model Syntax. It contains all syntactic elements covered by Wiki Model and contains the following specific elements not available in other syntaxes:
  * _embedded documents_ - using this feature you can insert one table into another or a header in a list.
  * _hierarchical properties_ are used to define specific "labeled" parts of wiki documents



# Details #

## Simple Formatting ##

```
*bold*
__italic__ 
^^superscript^^
~~subscript~~
```

## References ##
```
http://www.google.com - simple link
[http://www.google.com Google] - link with a custom label
```

## Images ##
```
img:http://www.example.com/images/MyImage.png - an image
img:MyImage.jpg - a local image
```


## Block elements ##
### Lists ###
Unordered list:
```
* item one
* item two
  * subitem 1
  * subitem 2
* item three
```
or:
```
- item one 
- item two
  - subitem
  - ... 
```

Ordered list:
```
+ item one
+ item two
  + subitem 1
  + subitem 2
+ item three
```

### Headers ###
```
= Heading1 =
== Heading2 ==
=== Heading3 ===
==== Heading4 ====
===== Heading5 =====
```
The "=" symbols at the end of lines are optional. So it is possible to write:
```
= Heading1
== Heading2
=== Heading3
==== Heading4
===== Heading5
```

Headers with parameters (in this example the header is on the blue background):
```
{{style='background-color: blue'}}
== Header 2 ==
```

### Tables ###
Recommended syntax:
```
!! Header 1 !! Header 2
:: Cell 1   :: Cell 2
```
This syntax is recommended because it can be entered using almost any keyboard layouts without changing the current localized keyboard settings.

Recognized syntax:
```
|| Header 1 || Header 2
| Cell 1    | Cell 2
```

Tables with parameters:
```
{{width="100%"}}
!! Header 1 !! Header 2
:: Cell 1   :: Cell 2
```

Table row parameters (in this example the first row is blue and the second is red):
```
{{style='background-color: blue;'}}!! Header 1 !! Header 2
{{style='background-color: red;' }}:: Cell 1   :: Cell 2
```

Table cell parameters (in this example the first cell is spanned to two columns):
```
!!{{colspan='2'} Header 1 
:: Cell 1  :: Cell 2
```

Combined example:
```
{{width="100%" border="1"}}
!!{{colspan='2'}} Table Header 
{{style='background-color:red'}}:: Cell One :: Cell Two
{{style='background-color:blue'}}:: Cell Three :: Cell Four
```

### Verbatim Block ###
Verbatim content is not interpreted by the parser and is returned to the listeners as is.

```
 {{{ This is a 
    verbatim 
  block.
  {{{ And this is an 
     internal verbatim
   block }}}
``` {{{ This is a 
    verbatim 
  block.
  {{{ And this is an 
     internal verbatim
   block }}}
}}}```