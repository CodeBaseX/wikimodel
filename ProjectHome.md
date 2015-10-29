# Wiki Model #

This project contains a set of wiki-related libraries, such as a parsers for various wiki syntaxes, and common wiki model (event- and object-based).

## Projects goals ##

The main goal of this project is creation of a “standard” model for wiki documents. It contains two different models:

  * Wiki Event Model (WEM) – like Simple API for XML (SAX)
  * Wiki Object Model (WOM) – like Document Object Model for XML (DOM)

## End-user benefits/features ##

  * Generates well-formed XHTML and XML. And it is very persistent for user’s errors (not closed or overlapping markup elements…)
  * Wiki pages can be stored in XML-form and converted into a well-formed wiki-syntax only for editing. So all source pages will be “autoformatted” – non closed markup elements will be explicitly closed, overlapping elements will be corrected and so on…
  * Each page can have its own “subpages” (“documents”) and each page could be splitted into multiply zones (“document sections”). For more information – see “Documents” section.
  * Each page or a subpage can contain the full formatting – block-level elements (like headers, tables, paragraphs) and inline elements (like links, style elements, special symbols).
  * In-memory representation of all elements offers the possibility of in-memory transformations/refactoring of wiki pages and additional operations with them (like generation of tables of content, extranctions of all link/images and so on)
  * Wiki-engins can cache all wiki-pages in memory in the form of objects.
  * Each block element (paragraph, table cells, list items, headers…) can have multiply lines.
  * Almost all structural elements can have its own parameters (block elements as well as inline elements).

## Providing tools ##

  * JavaCC-based parser for wiki-syntax. It generates events for Wiki Event Listeners. Usage of this parser garanties that even “bad-formed” wiki-pages will produce a “well-formed” sequence of events (all opening elements will be explicitly closed, all overlappings will be corrected)
  * XHTML event listener producing well-formed XHTML
  * XML event listener generating well formed XML documents (each structural wiki element is represented in the form of an xml element). The corresponding XML-deserialized (based on standard SAX parser) is also supplied.
  * WomToWem and WemToWom transformers. They offer the possibility to transform sequence of events (WEM) into a in-memory tree of object (WOM) and vice versa.
  * The reference implementation Wiki Object Model is also supplied.

## “Semantic Web compatibility” ##

One of the most important issues is to be compatible with the main standard/datamodel of Semantic Web – with Resource Description Framework (RDF). It means that both models (WEM as well as WOM) offer the possibility to map elements from wiki pages to RDF-statements. So each wiki page can be considered as a node in an RDF-graph. It allows to transform wiki-tools into powerful yet simple Semantic Web applications.