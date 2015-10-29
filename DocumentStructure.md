# Grammar #

**Note:** elements starting from the "#" symbol are used to re-group other elements but they don't have real representation in the document structure (ex: #internalBlock, #docElement...).

```
// Top-level document. 
doc :=  ( #docElement )*

// Building blocks of documents. 
#docElement := 
    (
        // Block elements which can have embedded documents
          table 
        | list 
    
        // Classical XHTML block elements
        | horline
        | header
        | quot
        | paragraph
        | emptyParagraph
        | verbatimBlock // pre-formatted text blocks
    
        // A special type of block elements. 
        // It can be considered as a paragraph with a "type".
        // Blocks of this type can contain embedded documents.
        | info 

        // Elements used to add additional (semantic) structure to documents
        | embeddedDocument
        | simpleProperty
        | documentProperty
    
        // For hackers
        | macroBlock
        | extensionBlock
    )


// Tables, table rows and table cells can have parameters.
table := (tableRow)+
tableRow := (tableCell)+
tableCell := ( #internalBlock )*

list := (listItem)+
listItem := ( #internalBlock )* ( list )*

#internalBlock := 
    ( 
          embeddedDocument 
        | verbatimBlock
        | macroBlock
        | extensionBlock
        | lines
    )

horline := #empty

header := ( line )?

quot := (quotLine)+
quotLine := line

paragraph := lines

emptyParagraph := ( newLine )+

verbatimBlock := #characters

info := ( #internalBlock )*

embeddedDocument := ( #docElement )*

simpleProperty := ( line )
documentProperty := #embeddedDocument

macroBlock := #characters

extensionBlock := #characters

/*----------------------------------------------------------------------------*/
lines := (multilineStyle)+
line := (simpleStyle)+

multilineStyle := ( line (newLine line)* )*
simpleStyle := line 

line := ( <CHAR> | <SPACE> )+
newLine := ( <EOL> )

```