package org.wikimodel.core.model.tmp;

import org.wikimodel.core.model.IId;
import org.wikimodel.core.model.INode;

public interface INodeManager {

	INode getNode(IId id, boolean create);

	IId newId();

	void setUri(IId id, String[] uris);

	IId getId(String uri);

	String[] getUri(IId id);

}
