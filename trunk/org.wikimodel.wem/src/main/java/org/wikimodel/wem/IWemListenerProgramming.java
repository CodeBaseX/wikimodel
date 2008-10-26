package org.wikimodel.wem;

/**
 * This interface re-groups all listener methods related to document elements
 * which should be interpreted by the client code. The meaning of extensions and
 * macros is not defined by the WikiModel. The general recommended semantic of
 * macros - macros can be used to define interpreted/executable code in the
 * handled document. Extensions are used mostly to <em>call</em> already defined
 * somewhere code to insert back the results of these calls in the document.
 * handled document. But the exact semantic and associated actions of these
 * elements should be defined by the client code in an implementation-specific
 * way.
 * 
 * @author kotelnikov
 */
public interface IWemListenerProgramming {

    /**
     * This method is used to notify about a new extension which CAN generate
     * block elements as a result of its interpretation.
     * 
     * @param extensionName the name of the extension
     * @param params parameters for the extension
     */
    void onExtensionBlock(String extensionName, WikiParameters params);

    /**
     * This method is used to notify about a new extension which CAN generate
     * in-line elements as a result of its interpretation. This method CAN NOT
     * generate block elements.
     * 
     * @param extensionName the name of the extension
     * @param params parameters for the extension
     */
    void onExtensionInline(String extensionName, WikiParameters params);

    /**
     * This method is used to notify about a new in-line macro which CAN
     * generate block elements as a result of its interpretation.
     * 
     * @param macroName the name of the macro
     * @param params parameters of the macro
     * @param content the content of the macro
     */
    void onMacroBlock(String macroName, WikiParameters params, String content);

    /**
     * This method is used to notify about a new in-line macro which CAN
     * generate only in-line elements as a result of its interpretation. This
     * method CAN NOT generate block elements.
     * 
     * @param macroName the name of the macro
     * @param params parameters of the macro
     * @param content the content of the macro
     */
    void onMacroInline(String macroName, WikiParameters params, String content);

}
