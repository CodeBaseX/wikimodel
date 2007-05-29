package org.wikimodel.core.model;

public interface IRepository {

	ISession login(String login, String password);

}
