package com.android.nitelights.login;



import java.io.IOException;
import java.net.MalformedURLException;

import redstone.xmlrpc.XmlRpcClient;
import redstone.xmlrpc.XmlRpcException;
import redstone.xmlrpc.XmlRpcFault;
import redstone.xmlrpc.XmlRpcStruct;


/**
 * Class, that performs authentication and communications with Drupal. 
 * @author Moskvichev Andrey V.
 *
 */
public class DrupalConnect {
	/** Base url of Drupal installation */
	final static private String URLBASE = "http://niteflow.com";
	
	/** XML-RPC url */
	final static private String XMLRPC = URLBASE + "/?q=androidrpc";

	
	/** Singleton instance */
	static private DrupalConnect instance;

	// session params, returned by user.login, and then passed in all 
	// subsequent calls as cookie
	private String sessid;
	private String session_name;
	
	
	private DrupalConnect() {
	}
	
	/**
	 * Perform authentication.
	 * @param username user name
	 * @param password password
	 * @throws IOException
	 * @throws XmlRpcException
	 * @throws XmlRpcFault
	 */
	public String login(String username, String password) throws IOException, XmlRpcException, XmlRpcFault {
		if (isAuthenticated())
			logout();
		
		XmlRpcClient xmlrpc = new XmlRpcClient(XMLRPC, false);
		
		XmlRpcStruct res = (XmlRpcStruct) xmlrpc.invoke("user.login", new Object[] { username, password });
		
		sessid = res.getString("sessid");
		session_name = res.getString("session_name");
		XmlRpcStruct user = res.getStruct("user");
		return user.getString("uid");
	}
	
	/**
	 * Close session.
	 * @throws MalformedURLException
	 * @throws XmlRpcException
	 * @throws XmlRpcFault
	 */
	public void logout() throws MalformedURLException, XmlRpcException, XmlRpcFault {
		if (!isAuthenticated())
			return ;
					
		try {
			// create xml-rpc client
			XmlRpcClient xmlrpc = new XmlRpcClient(XMLRPC, false);
			// set session cookie		
			xmlrpc.setRequestProperty("Cookie", getSessionCookieString());

			// remote call
			xmlrpc.invoke("user.logout", new Object[] { });
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
		sessid = null;
		session_name = null;
	}

	/**
	 * Checks if user is authenticated.
	 * @return <code>true</code> if authenticated
	 */
	public boolean isAuthenticated() {
		if (sessid == null)
			return false;
		return true;
	}


	private String getSessionCookieString() {
		if (sessid == null || session_name == null)
			return null;
		
		return session_name+"="+sessid;
	}

	public static DrupalConnect getInstance() {
		if (instance == null)
			instance = new DrupalConnect();
		return instance;
	}
}

