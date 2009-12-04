/**
 * 
 */
package org.wikimodel.fsm.grammar;

import java.io.InputStream;

import junit.framework.TestCase;

import org.wikimodel.fsm.FsmEvent;
import org.wikimodel.fsm.FsmProcess;
import org.wikimodel.fsm.FsmProcessListener;
import org.wikimodel.fsm.FsmState;
import org.wikimodel.fsm.FsmStateDescriptorConfigurator;
import org.wikimodel.fsm.IFsmEvent;
import org.wikimodel.fsm.IFsmProcessListener;
import org.wikimodel.fsm.config.DescriptionReader;
import org.wikimodel.graph.AbstractNodeWalker;

/**
 * @author kotelnikov
 */
public class GrammarTest extends TestCase {

    class T {
    }

    private IFsmProcessListener fListener = new FsmProcessListener() {
        private int fDepth;

        @Override
        public void beginState(FsmState node) throws Exception {
            String name = node.getKey();
            if (!name.startsWith("#")) {
                println("<" + name + ">");
                fDepth++;
            }
        }

        @Override
        public void endState(FsmState node) throws Exception {
            String name = node.getKey();
            if (!name.startsWith("#")) {
                fDepth--;
                println("</" + name + ">");
            }
        }

        private void println(String string) {
            for (int i = 0; i < fDepth; i++) {
                System.out.print("    ");
            }
            System.out.println(string);
        }
    };

    /**
     * @param name
     */
    public GrammarTest(String name) {
        super(name);
    }

    private FsmStateDescriptorConfigurator readConfig(String str)
        throws Exception {
        Class<?> cls = getClass();
        String path = "/"
            + cls.getPackage().getName().replace(".", "/")
            + "/"
            + str;
        InputStream input = cls.getResourceAsStream(path);
        try {
            FsmStateDescriptorConfigurator config = new FsmStateDescriptorConfigurator();
            DescriptionReader descriptorReader = new DescriptionReader(config);
            descriptorReader.parse(input);
            return config;
        } finally {
            input.close();
        }
    }

    public void test() throws Exception {
        FsmStateDescriptorConfigurator config = readConfig("GrammarTest_test.xml");
        FsmProcess process = config.newProcess("MAIN", fListener);
        test(process, "x", "MAIN/DOC/#INIT");
        test(process, "x", "MAIN/DOC/#B/P/TEXT");
        test(process, "table.tr.th", "MAIN/DOC/#B/TABLE/TR/TH/#IB/TEXT");
        test(process, "list.li", "MAIN/DOC/#B/LIST/LI/#IB/TEXT");
        test(process, "list.li", "MAIN/DOC/#B/LIST/LI/#IB/TEXT");

        // Default cell
        test(process, "table", "MAIN/DOC/#B/TABLE/TR/TD/#IB/TEXT");
        // New table line
        test(process, "table.tr", "MAIN/DOC/#B/TABLE/TR/TD/#IB/TEXT");
        // New header cell in the same line
        test(process, "table.tr.th", "MAIN/DOC/#B/TABLE/TR/TH/#IB/TEXT");
        // New table with a default cell
        test(process, "table", "MAIN/DOC/#B/TABLE/TR/TD/#IB/TEXT");

        test(process, "list.li", "MAIN/DOC/#B/LIST/LI/#IB/TEXT");
        test(process, "list", "MAIN/DOC/#B/LIST/LI/LIST/LI/#IB/TEXT");
        test(process, "list.li", "MAIN/DOC/#B/LIST/LI/LIST/LI/#IB/TEXT");
        test(process, "list.li", "MAIN/DOC/#B/LIST/LI/LIST/LI/#IB/TEXT");
        test(process, "list.end", "MAIN/DOC/#B/LIST/LI/#IB/TEXT");

        test(process, "doc.begin", "MAIN/DOC/#B/LIST/LI/#IB/DOC/#INIT");
        test(process, "header", "MAIN/DOC/#B/LIST/LI/#IB/DOC/#B/HEADER/TEXT");
        test(process, "doc.end", "MAIN/DOC/#B/LIST/LI/#IB/TEXT");

        test(process, "p", "MAIN/DOC/#B/P/TEXT");
        test(process, "doc.end", null);
    }

    private void test(FsmProcess process, String event, String control)
        throws Exception {
        IFsmEvent e = FsmEvent.newEvent(event);
        process.setEvent(e);
        AbstractNodeWalker.Mode mode = AbstractNodeWalker.Mode.LEAF;
        boolean ok = process.nextStep(mode);
        assertEquals(control != null, ok);
        FsmState state = process.getCurrentState();
        String path = state != null ? state.getPath() : null;
        assertEquals(control, path);
    }

    public void testX() throws Exception {
        FsmStateDescriptorConfigurator config = new FsmStateDescriptorConfigurator();
        {
            config.beginState("DOC");
            config.onTransition(null, "", "BLOCK");
            config.onTransition("*", "exit", null);
            config.onTransition("BLOCK", "", "{initial}");
            {
                config.beginState("BLOCK");
                config.onTransition(null, "table", "TABLE");
                config.onTransition(null, "ul", "UL");
                config.onTransition(null, "ol", "UL");
                config.onTransition(null, "", "P");
                {
                    config.beginState("TABLE");
                    config.onTransition(null, "", "TR");
                    config.onTransition("TR", "table.tr", "TR");
                    {
                        config.beginState("TR");
                        config.onTransition(null, "table.tr.th", "TH");
                        config.onTransition(null, "", "TD");
                        config.onTransition("TD", "table.tr.th", "TH");
                        config.onTransition("TD", "table.tr.td", "TD");
                        config.onTransition("TH", "table.tr.th", "TH");
                        config.onTransition("TH", "table.tr.td", "TD");
                        config.endState();
                    }
                    config.endState();
                }
                {
                    config.beginState("UL");
                    config.onTransition(null, "", "LI");
                    config.endState();
                }
                config.endState();
            }
            config.endState();
        }

        FsmProcess process = config.newProcess("DOC", fListener);
        test(process, "x", "DOC/BLOCK/P");
        test(process, "table.tr", "DOC/BLOCK/TABLE/TR/TD");
        test(process, "table.tr.th", "DOC/BLOCK/TABLE/TR/TH");
        test(process, "table.tr", "DOC/BLOCK/TABLE/TR/TD");
        test(process, "ul.li", "DOC/BLOCK/UL/LI");
        test(process, "exit", null);
    }

}
