package org.wikimodel.core.model;

public interface ITypedValue extends IValue {

	/**
	 * @return a string-serialized representation of this value
	 */
	String getAsString();

	/**
	 * @return the xml type of the value
	 */
	IId getDataType();

	/**
	 * @return the typed value object
	 */
	Object getValue();
}
