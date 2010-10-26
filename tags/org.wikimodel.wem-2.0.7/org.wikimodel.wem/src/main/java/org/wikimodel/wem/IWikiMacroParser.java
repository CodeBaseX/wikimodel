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
 * Instances of this type are used to transform macros found in wiki
 * documents into corresponding structured objects - {@link WikiMacro}.
 *
 * @author mkirst(at portolancs dot com)
 */
public interface IWikiMacroParser {

    /**
     * Parses the given macro, recognizes individual parts of it
     * (name, content, parameters) and returns the corresponding object.
     *
     * @param str the macro reference to parse
     * @return a wiki macro corresponding to the given string
     */
    public abstract WikiMacro parse(String str);

}