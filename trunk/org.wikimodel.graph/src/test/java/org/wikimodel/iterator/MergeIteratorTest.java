/**
 * 
 */
package org.wikimodel.iterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

/**
 * @author kotelnikov
 */
public class MergeIteratorTest extends TestCase {

    /**
     * @param name
     */
    public MergeIteratorTest(String name) {
        super(name);
    }

    /**
     * 
     */
    public void test() {
        test(new String[] { "" }, "");
        test(new String[] { "1ab" }, "1ab");
        test(new String[] { "1ab", "" }, "1ab");
        test(new String[] { "", "23c" }, "23c");
        test(new String[] { "1ab", "23c" }, "123abc");
        test(new String[] { "1ab", "23cde" }, "123abcde");
        test(new String[] { "1a", "2b", "3c" }, "123abc");
        test(new String[] { "1a", "2bde", "3c" }, "123abcde");

        test(new String[] { "1b3", "a2c" }, "1a2b3c");

        test(new String[] { "123", "abc456" }, "123abc456");
        test(new String[] { "123", "abc", "456" }, "123456abc");
    }

    @SuppressWarnings("unchecked")
    private void test(String[] array, String control) {
        Iterator<String>[] iterators = new Iterator[array.length];
        for (int i = 0; i < array.length; i++) {
            List<String> list = new ArrayList<String>();
            for (int j = 0; j < array[i].length(); j++) {
                list.add("" + array[i].charAt(j));
            }
            iterators[i] = list.iterator();
        }
        MergeIterator<String> iterator = new MergeIterator<String>(iterators);
        StringBuffer buf = new StringBuffer();
        while (iterator.hasNext()) {
            String str = iterator.next();
            buf.append(str);
        }
        assertEquals(control, buf.toString());
    }

}
