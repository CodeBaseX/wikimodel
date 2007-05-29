package org.wikimodel.core.model;

import java.util.Collection;

public interface INode {

	String[] getAllProperties();

	IId getNodeId();

	IValue getPropertyValue(IId propertyId);

	Collection<IValue> getPropertyValues(IId propertyId);

	boolean removePropertyValues(IId propertyId);

	void setPropertyValue(IId propertyId, IValue value);

	void setPropertyValues(IId propertyId, Collection<IValue> value);
	
	Collection<INode> getBackReferences(IId propertyId);
	
	ISession getSession();

}
