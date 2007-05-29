/**
 * 
 */
package org.wikimodel.core.model;

import java.io.InputStream;

/**
 * @author sebastocha
 * 
 */
public interface IBinaryValue extends IValue {

	String getMimeType();

	InputStream getContent();

}
