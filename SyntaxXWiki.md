# Introduction #

XWiki parser from the WikiModel  is used as the main parser for the XWiki engine. This document defines the syntax recognized by the parser.

## Tables ##

The table syntax is:
```
!! Header 1.1 !! Header 1.2 !! Header 1.3
!! Header 2.1 :: Cell 2.2 :: Cell 2.3
```
OR
```
|| Header 1.1 || Header 1.2 || Header 1.3
|| Header 2.1 | Cell 2.2 | Cell 2.3
```

The first syntax is higly recomended - it is much simpler to use with non-english keyboard layouts (with cyrilic keyboard for example).

Parameters for tables, table rows and table cells can be added using the "(% param1=value1 param2=value2 %)" syntax.

Parameters for tables:
```
(%param1=value1 param2=value2 ... %)
!! Header 1 :: Cell 1
!! Header 2 :: Cell 2
!! Header 3 :: Cell 3
```

Parameters for  table rows:
```
!! Header 1 :: Cell 1
(%rowParam1=value1 rowParam2=value2...%)!! Header 2 :: Cell 2
!! Header 3 :: Cell 3
```

Parameters for cells:
```
!! Header 1 :: Cell 1
!! Header 2 ::(%cellParam1=value1 cellParam2=value2 ...%) Cell 2 
!! Header 3 :: Cell 3
```

The same parameters markup is used for "||" and "|" cell delimiters