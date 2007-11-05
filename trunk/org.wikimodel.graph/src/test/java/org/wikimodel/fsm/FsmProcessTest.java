/**
 * 
 */
package org.wikimodel.fsm;

import junit.framework.TestCase;

/**
 * @author kotelnikov
 */
public class FsmProcessTest extends TestCase {

    IFsmProcessListener listener = new FsmProcessListener();

    /**
     * @param name
     */
    public FsmProcessTest(String name) {
        super(name);
    }

    public void testOne() throws Exception {
        FsmStateDescriptorConfigurator config = new FsmStateDescriptorConfigurator();
        config.beginState("main");
        {
            config.onTransition(null, "", "off");
            config.onTransition("off", "", "off");
            config.onTransition("off", "okEvent", "on");
            config.onTransition("off", "exitEvent", null);
            config.onTransition("on", "", "off");
        }
        config.endState();

        FsmProcess process = config.newProcess("main", listener);
        test(process, "abcEvent", "main/off");
        test(process, "abcEvent", "main/off");
        test(process, "okEvent", "main/on");
        test(process, "okEvent", "main/off");
        test(process, "totoEvent", "main/off");
        test(process, "exitEvent", null);
    }

    public void testTwo() throws Exception {
        FsmStateDescriptorConfigurator config = new FsmStateDescriptorConfigurator();
        {
            config.beginState("main");
            config.onTransition(null, "", "off");
            config.onTransition("off", "", "off");
            config.onTransition("off", "okEvent", "on");
            config.onTransition("off", "exitEvent", null);
            config.onTransition("on", "", "off");
            {
                config.beginState("on");
                config.onTransition(null, "", "init");
                config.onTransition("init", "", "done");
                config.onTransition(
                    "done",
                    "okEvent",
                    IFsmProcessConst.STATE_INITIAL);
                config.onTransition("done", "", null);
                config.endState();
            }
            config.endState();
        }

        FsmProcess process = config.newProcess("main", listener);
        test(process, "abcEvent", "main/off");
        test(process, "abcEvent", "main/off");
        test(process, "okEvent", "main/on/init");
        test(process, "abcEvent", "main/on/done");
        test(process, "okEvent", "main/on/init");
        test(process, "abcEvent", "main/on/done");
        test(process, "abcEvent", "main/off");
        test(process, "totoEvent", "main/off");
        test(process, "exitEvent", null);
    }

    /**
     * Checks that the state declarations can be inherited using the dynamic
     * state stacks.
     * 
     * @throws Exception
     */
    public void testStateInheritance() throws Exception {
        FsmStateDescriptorConfigurator config = new FsmStateDescriptorConfigurator();
        {
            config.beginState("main");
            config.onTransition(null, "", "off");
            config.onTransition("off", "", "off");
            config.onTransition("off", "okEvent", "on");
            config.onTransition("off", "exitEvent", null);
            config.onTransition("on", "", "off");
            {
                config.beginState("test");
                config.onTransition(null, "", "abc");
                config.endState();
            }
            {
                config.beginState("on");
                config.onTransition(null, "", "init");
                config.onTransition("init", "", "test");
                config.endState();
            }
            config.endState();
        }

        FsmProcess process = config.newProcess("main", listener);
        test(process, "abcEvent", "main/off");
        test(process, "abcEvent", "main/off");
        test(process, "okEvent", "main/on/init");
        test(process, "abcEvent", "main/on/test/abc");
        test(process, "abcEvent", "main/off");
        test(process, "totoEvent", "main/off");
        test(process, "exitEvent", null);
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
