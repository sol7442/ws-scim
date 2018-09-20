package com.wession.scim.agent.site.intf;

import java.sql.Connection;
import java.util.ArrayList;

public interface DBAdaptor {

	public AbstractBean getAccount(String id);
	public AbstractBean getAccountById(String id);
	public AbstractBean getAccountByScimId(String id); 
	public ArrayList<AbstractBean> getAccountList();

	public void insertAccount(AbstractBean bean);
	public void updateAccount(AbstractBean bean);
	public void deleteAccount(AbstractBean bean);
}
