package org.wikimodel.fsm.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.wikimodel.fsm.FsmStateDescriptor;
import org.wikimodel.fsm.FsmStateDescriptorConfigurator;
import org.wikimodel.fsm.xml.ConfigParser;
import org.xml.sax.Attributes;

/**
 * This handler is used to read state descriptions from xml-config file. It
 * takes two parameters: map with state handlers and resulting map which will be
 * filled by topmost state descriptions when a <code>parse(...)</code> is
 * called. <br />
 * Example of state description in xml-config file:
 * 
 * <pre><code>
 *      ...
 *      &lt;state key=&quot;mainState&quot; handler=&quot;mainHandlerKey&quot;&gt;
 *          &lt;transitions initial=&quot;helloState&quot;&gt;
 *             &lt;transition from=&quot;helloState&quot; to=&quot;totoState&quot;&gt;
 *                &lt;guard event=&quot;errorEvent&quot; to=&quot;errorState&quot;/&gt;
 *                &lt;guard event=&quot;*&quot; to=&quot;helloState&quot;/&gt;
 *             &lt;/transition&gt;
 *             ...
 *          &lt;/transitions&gt;
 *          &lt;substates&gt;
 *            &lt;state key=&quot;helloState&quot; handler=&quot;helloHandlerKey&quot;/&gt;
 *            &lt;state key=&quot;totoState&quot;&gt;
 *               &lt;transitions initial=&quot;...&quot;&gt;
 *               ...
 *               &lt;/transitions&gt;
 *               &lt;substates&gt;
 *               ...
 *               &lt;/substates&gt;
 *            &lt;/state&gt;
 *          &lt;/substates&gt;
 *      &lt;/state&gt;
 *      ...     	
 * </code></pre>
 * 
 * @author kotelnikov
 */
public class DescriptionReader extends ConfigParser {

    static Set fExcludedKeys = new HashSet();

    protected static final String KEY_STATE_KEY = "key";

    protected static final String KEY_T_EVENT = "event";

    protected static final String KEY_T_FROM = "from";

    protected static final String KEY_T_TO = "to";

    static {
        fExcludedKeys.add(KEY_STATE_KEY);
        fExcludedKeys.add(KEY_T_FROM);
        fExcludedKeys.add(KEY_T_EVENT);
        fExcludedKeys.add(KEY_T_TO);
    }

    FsmStateDescriptorConfigurator fConfig;

    /**
     * 
     *
     */
    public DescriptionReader() {
        this(new FsmStateDescriptorConfigurator());
    }

    public DescriptionReader(FsmStateDescriptorConfigurator config) {
        fConfig = config;
        addHandlers();
    }

    protected void addAttributes(Map map, Attributes attributes) {
        int len = attributes.getLength();
        for (int i = 0; i < len; i++) {
            String name = attributes.getQName(i);
            if (fExcludedKeys.contains(name))
                continue;
            String value = attributes.getValue(i);
            map.put(name, value);
        }
    }

    /**
     * Adds all element handlers to the given parser
     * 
     * @param parser
     */
    public void addHandlers() {
        NodeHandler stateConfigurator = new ConfigParser.NodeHandler() {

            /**
             * Creates a new state descriptor and puts it on the top of the
             * description stack in the parent descriptor. If there is no parent
             * state then this method calls the method
             * {@link DescriptionReader#addTopLevelStateDescriptor()}.
             * 
             * @throws Exception
             */
            public void beginNode(
                String uri,
                String localName,
                String name,
                Attributes attributes) throws Exception {
                String stateKey = attributes.getValue(KEY_STATE_KEY);
                if (stateKey == null)
                    return;

                Map map = new HashMap();
                addAttributes(map, attributes);
                fConfig.beginState(stateKey, map);
            }

            public void endNode(
                String uri,
                String localName,
                String name,
                Attributes attributes) {
                fConfig.endState();
            }
        };

        NodeHandler transitionConfigurator = new ConfigParser.NodeHandler() {

            public void beginNode(
                String uri,
                String localName,
                String name,
                Attributes attributes) {
                String from = attributes.getValue(KEY_T_FROM);
                String event = attributes.getValue(KEY_T_EVENT);
                String to = attributes.getValue(KEY_T_TO);
                fConfig.onTransition(from, event, to);
            }
        };

        registerHandler(".*/process", stateConfigurator);
        registerHandler(".*/state", stateConfigurator);
        registerHandler(".*/extension", stateConfigurator);
        registerHandler(
            ".*/state/transitions/transition",
            transitionConfigurator);
        registerHandler(
            ".*/process/transitions/transition",
            transitionConfigurator);
        registerHandler(
            ".*/extension/transitions/transition",
            transitionConfigurator);
    }

    public FsmStateDescriptorConfigurator getConfig() {
        return fConfig;
    }

    /**
     * This method should be overloaded in subclasses to associate an extension
     * with the given key with add the description.
     * 
     * @param stateKey
     * @param descriptor
     * @throws Exception
     */
    protected void setExtensionDescription(
        String stateKey,
        FsmStateDescriptor descriptor) throws Exception {
        //
    }

    /**
     * This method should be overloaded in subclasses to associate the given
     * description with the process key.
     * 
     * @param processKey
     * @param descriptor
     * @throws Exception
     */
    protected void setProcessDescription(
        String processKey,
        FsmStateDescriptor descriptor) throws Exception {
        //
    }

}