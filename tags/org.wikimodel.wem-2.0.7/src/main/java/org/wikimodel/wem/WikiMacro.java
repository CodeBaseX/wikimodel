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

/**
 * An immutable object which holds information about a macro reference.
 * Macros are unique just by their name.
 * The content and parameters are optional.<br>
 * <br>
 * There are a few 'built in' macro names:
 * <ul>
 * <li>toc</li>
 * <li>notoc</li>
 * <li>forcetoc</li>
 * <li>footnotes</li>
 * </ul>
 * 
 * @author mkirst(at portolancs dot com)
 */
public class WikiMacro {

    public final static String MACRO_TOC       = "toc";
    public final static String MACRO_NOTOC     = "notoc";
    public final static String MACRO_FORCETOC  = "forcetoc";
    public final static String MACRO_FOOTNOTES = "footnotes";
    
    public final static String UNHANDLED_MACRO = "unhandled";
    
    private final String name;
    private final WikiParameters wikiParameters;
    private final String content;

    /**
     * @param name
     */
    public WikiMacro(String name) {
        this(name, WikiParameters.EMPTY, null);
    }
    
    /**
     * @param name
     * @param wikiParameters
     */
    public WikiMacro(String name, WikiParameters wikiParameters) {
        this(name, wikiParameters, null);
    }
    
    /**
     * @param name
     * @param wikiParameters
     * @param content
     */
    public WikiMacro(String name, WikiParameters wikiParameters, String content) {
        super();
        this.name = name;
        this.content = content;
        this.wikiParameters = wikiParameters;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @return the wikiParameters
     */
    public WikiParameters getWikiParameters() {
        return wikiParameters;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        WikiMacro other = (WikiMacro) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{{" + name + " | " + content + " | " + wikiParameters + "}}";
    }
    
}
