package org.wikimodel.core.model;

import java.io.InputStream;

import org.wikimodel.core.model.tmp.INodeManager;

public interface ISession {

	INodeManager getINodeManager();

	IId getId(String uri, boolean create);

	INode getNode(IId id, boolean create);


	IId newId();

	String[] getUri(IId id);
	
	void setUri(IId id, String[] uris);

	/**
	 * Returns a newly created literal instance with the given string value and
	 * language
	 * 
	 * @param value
	 *            the literal value
	 * @param language
	 *            the language of the given literal value
	 * @return a new literal instance with the given string value and language @
	 */
	IStringValue newStringValue(String value, String language);

	/**
	 * Returns a newly created typed value
	 * 
	 * @param value
	 *            the internal object represenation of the typed value
	 * @param dataType
	 *            the type of the value
	 * @return a newly created typed value @
	 */
	ITypedValue newTypedValue(Object value, IId dataType);

	/**
	 * Returns a newly created typed value
	 * 
	 * @param value
	 *            the serialized form of a typed object
	 * @param dataType
	 *            the type of the value
	 * @return a newly created typed value @
	 */
	IValue newTypedValueFromString(String value, IId dataType);

	IBinaryValue newBinaryValue(InputStream input, String mimeType);

}
