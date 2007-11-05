/**
 * 
 */
package org.wikimodel.fsm.grammar;

import junit.framework.TestCase;

import org.wikimodel.fsm.FsmProcess;
import org.wikimodel.fsm.FsmProcessListener;
import org.wikimodel.fsm.FsmState;
import org.wikimodel.fsm.FsmStateDescriptorConfigurator;
import org.wikimodel.fsm.IFsmProcessListener;

/**
 * @author kotelnikov
 */
public class GrammarTest extends TestCase {

    private IFsmProcessListener fListener = new FsmProcessListener() {
        private int fDepth;

        public void beginState(FsmState node) throws Exception {
            println("<" + node.getKey() + ">");
            fDepth++;
        }

        private void println(String string) {
            for (int i = 0; i < fDepth; i++) {
                System.out.print("    ");
            }
            System.out.println(string);
        }

        public void endState(FsmState node) throws Exception {
            fDepth--;
            println("</" + node.getKey() + ">");
        }
    };

    /**
     * @param name
     */
    public GrammarTest(String name) {
        super(name);
    }

    public void test() throws Exception {
        FsmStateDescriptorConfigurator config = new FsmStateDescriptorConfigurator();
        config.beginState("MAIN");
        config.onTransition(null, "", "DOC");
        config.onTransition("DOC", "", "DOC");
        config.onTransition("DOC", "doc.end", null);
        config.beginState("DOC");
        {
            config.onTransition(null, "", "BLOCK");
            config.onTransition("BLOCK", "", "BLOCK");
            config.onTransition("BLOCK", "doc.end", null);
            config.beginState("BLOCK");
            {
                config.onTransition(null, "block.property", "BLOCK_PROPERTY");
                config.onTransition(null, "block.info", "BLOCK_INFO");
                config.onTransition(null, "block.quot", "BLOCK_QUOT");
                config.onTransition(null, "ul", "UL");
                config.onTransition(null, "ol", "OL");
                config.onTransition(null, "dl", "DL");
                config.onTransition(null, "table", "TABLE");
                config.onTransition(null, "header", "HEADER");
                config.onTransition(null, "hr", "HR");
                config.onTransition(null, "empty", "EMPTY_PARAGRAPH");
                config.onTransition(null, "", "P");
                // begin of #block
                // config.onTransition(null, "doc.begin", "DOC");
                config.onTransition(null, "block.verbatim", "BLOCK_VERBATIM");
                config.onTransition(null, "block.macro", "BLOCK_MACRO");
                config.onTransition(null, "block.extension", "BLOCK_EXTENSION");
                // end of #block

                config.beginState("TABLE");
                {
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
                }
                config.endState(); // TABLE

                config.beginState("UL");
                {
                    config.onTransition("LI", "ul.li", "LI");
                    config.onTransition(null, "", "LI");
                }
                config.endState(); // UL
            }
            config.endState(); // BLOCK
        }
        config.endState(); // DOC

        FsmProcess process = config.newProcess("MAIN", fListener);
        test(process, "x", "MAIN/DOC/BLOCK/P");
        test(process, "table.tr.th", "MAIN/DOC/BLOCK/TABLE/TR/TH");
        test(process, "ul.li", "MAIN/DOC/BLOCK/UL/LI");
        test(process, "ul.li", "MAIN/DOC/BLOCK/UL/LI");

        // Default cell
        test(process, "table", "MAIN/DOC/BLOCK/TABLE/TR/TD");
        // New table line
        test(process, "table.tr", "MAIN/DOC/BLOCK/TABLE/TR/TD");
        // New header cell in the same line
        test(process, "table.tr.th", "MAIN/DOC/BLOCK/TABLE/TR/TH");
        // New table with a default cell
        test(process, "table", "MAIN/DOC/BLOCK/TABLE/TR/TD");

        test(process, "ul.li", "MAIN/DOC/BLOCK/UL/LI");
        test(process, "p", "MAIN/DOC/BLOCK/P");
        test(process, "doc.end", null);
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

    private void test(FsmProcess process, String event, String control)
        throws Exception {
        process.setEvent(event);
        boolean ok = process.nextStep(FsmProcess.DEBUG_TERMINAL);
        assertEquals(control != null, ok);
        FsmState state = process.getCurrentState();
        String path = state != null ? state.getPath() : null;
        assertEquals(control, path);
    }

}
