package org.wikimodel.core.model;

/**
 * @author MikhailKotelnikov
 */
public interface IStringValue extends IValue {

	/**
	 * @return the language of this value
	 */
	String getLanguage();

	/**
	 * @return the string representation of this value
	 */
	String getStringValue();

}
