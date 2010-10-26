/*
 * Copyright (c) 2010 mkirst(at portolancs dot com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wikimodel.wem;

import junit.framework.TestCase;

/**
 * @author mkirst(at portolancs dot com)
 */
public class ReferenceHandlerTest extends TestCase {

    private TestReferenceHandler clazz;

    protected void setUp() throws Exception {
        super.setUp();
        this.clazz = new TestReferenceHandler(true, true);
    }

    public void testHandleImageUppercase() {
        WikiReference ref = new WikiReference("Image:foo.png", "bar");
        clazz.handle(ref);
        assertEquals("foo.png", clazz.getImgRef());
        assertEquals("bar", clazz.getImgLabel());
    }

    public void testHandleImageLowercase() {
        WikiReference ref = new WikiReference("image:bar.png", "foo");
        clazz.handle(ref);
        assertEquals("bar.png", clazz.getImgRef());
        assertEquals("foo", clazz.getImgLabel());
    }

    /*
     * ========================================================================
     */

    /**
     * @author mkirst(at portolancs dot com)
     */
    private static class TestReferenceHandler extends ReferenceHandler {

        private String imgRef;
        private String imgLabel;

        protected TestReferenceHandler(boolean supportImage,
                boolean supportDownload) {
            super(supportImage, supportDownload);
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.wikimodel.wem.ReferenceHandler#handleImage(java.lang.String,
         * java.lang.String, org.wikimodel.wem.WikiParameters)
         */
        @Override
        protected void handleImage(String ref, String label,
                WikiParameters params) {
            imgRef = ref;
            imgLabel = label;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.wikimodel.wem.ReferenceHandler#handleReference(java.lang.String,
         * java.lang.String, org.wikimodel.wem.WikiParameters)
         */
        @Override
        protected void handleReference(String ref, String label,
                WikiParameters params) {
            // not interested in.
        }

        /**
         * @return the imgRef
         */
        public String getImgRef() {
            return imgRef;
        }

        /**
         * @return the imgLabel
         */
        public String getImgLabel() {
            return imgLabel;
        }

    }
}