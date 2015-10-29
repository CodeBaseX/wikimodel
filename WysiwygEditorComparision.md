# Requirements #

  * Possibility to edit (X)HTML
  * Much more important to have access to **structural** blocks and not just to formatting (headers and styles instead of specific fonts and colors)
  * Possibility to have access to individual DOM elements in the edited document
  * Possibility to intercept editing of some parts (styled elements) in the document. It is required for creation specific editors for elements marked by some specific style classes. For example entering of the cursor in a <div>...</div> block can activate a pop-up window giving access to interactive maps. Or it can be useful for creation of "macro" editors in wiki pages.
  * Possibility to define CSS for edited documents


---


# Best candidates #

## GWT Native WYSIWYG ##

Pro:
  * Native solution
  * Very fast

Con:
  * Very limited
  * Don't have the notion of headers and styles (logical structure), but gives access to explicit font and color mantipulation (pure decoration)
  * Don't give access to individual elements
  * Very difficult to extend. To really have access to individual DOM elements there are two possibilities: 1) Create our own "native" javascript code working in parallel with Google's GWT implementaiton and accessing the same DOM nodes already manipulated by GWT-controlled code 2) Change the Google's code working with document in the editing mode to add required behaviour. The first approach is bad because it is complicated and collisions with GWT are possible (for example: event listening/canceling). The second approach requires modifications of the C/JNI platform-specific implementations for embedded browsers (only for Debug mode) and modifications of the Javascript generator for production mode.
  * Impossible to define CSS for edited documents

Conclusion: It seems that it is difficult to extend it properly!
Personally I (MikhailKotelnikov) did not find a way to get access to (and manipulate with)  DOM elements in the edited document, to the current cursor position, selections and CSS styles

## Axed ##

URL: http://code.google.com/p/axed/

This editor **does not** change the browser mode. It manipulates with DOM elements in memory and explicitly emulates editor-specific behaviour like cursor, selections and so on. Completly written in GWT. It seems that it does not contain "native" javascript; one source tree for all browser platform; contains no browser-specific code.

Pro:
  * Native GWT, no external stuff
  * The biggest plus: everything is explicitly controlled in the code which is the same for all browsers.
  * It seems that it is very easy to extend it to create specialized editors for specific styled blocks (macro editors, map editors, table editors and so on)
  * Does not change the mode of the browser, so this editor can have **interactive** widgets "embedded" directly in the edited document! These widgets will have the same behaviour as in the final Web page. It can be interactive GoogleMaps, Calendars, photo galeries (at least - in theory). This is a unique feature which is impossible to have in other editors changing the browser mode to "editMode".
  * Apache license

Con:
  * v1.0 is very slow for big text blocks (~10-30kb). It can change in the future.
  * It is not real WYSIWYG, but it is perfect for **structural** editing.
  * Bags and not implemented features
    * Does not work in Opera (linux); May not work in other browsers (like Safary, WebKit)
    * May have 'strange' behaviour for simple operations like copy-paste from external sources and so on.

Conclusion: It is a **very** interesting candidate as an editor for small documents with rich interactive content. It can be a basis not for a WYSIWYG Editor (What You See Is What You **Get**)  but for WYSIWYM Editor (What You See Is What You **Mean**) oriented on editing of  document structure.

## Yahoo! Editor ##

http://developer.yahoo.com/yui/editor/

A very good editor. Apparently it gives access to all individual elements in the edited text. It seems that it works well with common browsers (IE, Gecko-based, Opera, Safary...)

Pro:
  * Clean object desing and implementation
  * Big company behind
  * BSD license
  * Fast. Quite short loading time.
  * Gives access for all individual elements in the document
  * CSS can be re-defined
  * Extendable editors are possible (editor area grows with the content length)

Con:
  * Requires loading of external javascript files

Conclusion: The best candidate for real WYSIWYG editor in GWT

## FCKeditor ##

http://www.fckeditor.net/

Pro:
  * One of the most active project in SourceForge
  * Works well with almost all browsers
  * Stable
  * Extensible
  * It seems that it possible to have access to the internal structure of edited documents
  * A lot of plugins
  * Browser-specific code is explicitly externalized in separate files (don't load the code if it is not used)
  * I18N

Con:
  * LGPL
  * Requires loading of external javascript files
  * Heavyweight

Conclusion: Could be a good candidate if other variants does not work


---


# Rejected candites #

## TinyMCE ##

URL: http://www.tinymce.org

Pro:
  * Easy to embed in GWT
  * Widly used

Con:
  * Very low code quality (impossible to extend/modify/debug, platform-specific code is in the same file with "if"s)
  * Slow
  * LGPL

## DOJO WYSIWYG ##

http://www.dojotoolkit.org/

Pro:
  * Good design and implementation
  * Apache licence

Con:
  * Havyweight and slow
  * Requires loading of external javascript files

## openWYSIWYG ##

http://www.openwebware.com/

Pro:
  * Simple
Con:
  * Badly written
  * Requires loading of external javascript files
  * LGPL


---


# Example Base #

The following editors can be considered as a base/examples for a pure native implementation of WYSIWYG in GWT:

  * http://code.google.com/p/gwt-html-editor/ - don't work in Debug mode
  * http://www.wymeditor.org/en/ - WYSIWYM - It would be nice to merge this approach with Axed