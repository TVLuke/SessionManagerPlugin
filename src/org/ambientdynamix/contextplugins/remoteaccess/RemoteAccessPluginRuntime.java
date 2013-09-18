package org.ambientdynamix.contextplugins.remoteaccess;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.ambientdynamix.api.contextplugin.*;
import org.ambientdynamix.api.contextplugin.security.PrivacyRiskLevel;
import org.ambientdynamix.api.contextplugin.security.SecuredContextInfo;

import android.os.Bundle;
import android.util.Log;


public class RemoteAccessPluginRuntime extends AutoReactiveContextPluginRuntime
{
	private final static String TAG = "LSTFM PLUGIN";
	private static RemoteAccessPluginRuntime context;
	public static String lastfmaccount="";
	public static ContextPluginSettings settings;
	private static SecureRandom random = new SecureRandom();

	@Override
	public void start() 
	{
		/*
		 * Nothing to do, since this is a pull plug-in... we're now waiting for context scan requests.
		 */
		context=this;
		Log.i(TAG, "Started!");
	}

	@Override
	public void stop() 
	{
		/*
		 * At this point, the plug-in should cancel any ongoing context scans, if there are any.
		 */
		Log.i(TAG, "Stopped!");
	}

	@Override
	public void destroy() 
	{
		/*
		 * At this point, the plug-in should release any resources.
		 */
		stop();
		Log.i(TAG, "Destroyed!");
	}

	@Override
	public void updateSettings(ContextPluginSettings settings) 
	{
		// Not supported
	}

	@Override
	public void handleContextRequest(UUID requestId, String contextInfoType) 
	{
		context=this;
	}

	@Override
	public void handleConfiguredContextRequest(UUID requestId, String contextInfoType, Bundle scanConfig) 
	{
		if(contextInfoType.equals("org.ambientdynamix.contextplugins.context.info.access.accesspermissions"))
		{
			if(scanConfig.containsKey("action_type"))
			{
				String actionType = scanConfig.getString("action_type");
				if(actionType.equals("check_sessionkey"))
				{
					if(scanConfig.containsKey("service_id"))
					{
						if(unknownService(scanConfig.getString("service_id")))
						{
							SecuredContextInfo aci= new SecuredContextInfo(new RemoteAccessContextInfo(), PrivacyRiskLevel.LOW);
							sendContextEvent(requestId, aci);
						}
						else
						{
							String newKey = getKey(scanConfig.getString("service_id"));
							SecuredContextInfo aci= new SecuredContextInfo(new RemoteAccessContextInfo(scanConfig.getString("service_id"), newKey), PrivacyRiskLevel.LOW);
							sendContextEvent(requestId, aci);							
						}
					}
				}
			}
		}
		if(contextInfoType.equals("org.ambientdynamix.contextplugins.context.action.access.accesspermissions"))
		{
			if(scanConfig.containsKey("action_type"))
			{
				String actionType = scanConfig.getString("action_type");
				if(actionType.equals("request_sessionkey"))
				{
					if(scanConfig.containsKey("service_id"))
					{
						if(unknownService(scanConfig.getString("service_id")))
						{
							String newKey = newKey();
							settings.put(scanConfig.getString("service_id"), newKey);
							SecuredContextInfo aci= new SecuredContextInfo(new RemoteAccessContextInfo(scanConfig.getString("service_id"), newKey), PrivacyRiskLevel.LOW);
							sendContextEvent(requestId, aci);
						}
						else
						{
							String newKey = getKey(scanConfig.getString("service_id"));
							SecuredContextInfo aci= new SecuredContextInfo(new RemoteAccessContextInfo(scanConfig.getString("service_id"), newKey), PrivacyRiskLevel.LOW);
							sendContextEvent(requestId, aci);							
						}
					}
				}
			}
		}
		context=this;
	}

	@Override
	public void init(PowerScheme arg0, ContextPluginSettings arg1) throws Exception 
	{
		Log.d(TAG, "init 11");
		if(arg1!=null)
		{
			Log.d(TAG, "settings are not null and now get stored as a static variable");
			settings=  arg1;
			Log.d(TAG, "they are also stored via dynamix");
			getPluginFacade().storeContextPluginSettings(getSessionId(), settings);
		}
		else
		{
			Log.d(TAG, "settings given to this method are null");
			settings =  getPluginFacade().getContextPluginSettings(getSessionId());
			if(settings!=null)
			{
				Log.d(TAG, "ok that worked");
			}
			else
			{
				ContextPluginSettings s = new ContextPluginSettings();
				getPluginFacade().storeContextPluginSettings(getSessionId(), s);
				settings = getPluginFacade().getContextPluginSettings(getSessionId());
				if(settings!=null)
				{
					Log.d(TAG, "ok, third one is the charm I guess...");
				}
				else
				{
					Log.d(TAG, "the settings are still null");
				}
			}
		}
		context=this;
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPowerScheme(PowerScheme arg0) throws Exception 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doManualContextScan() 
	{
		// TODO Auto-generated method stub
		
	}
	
	private static String newKey()
	{
		Log.d(TAG, "...");
	    String x = new BigInteger(250, random).toString(32);
	    Log.d(TAG, x);
	    x = x.substring(0, 30);
	    Log.d(TAG, x);
	    return x;
	}
	
	private static boolean unknownService(String serviceID)
	{
		return !(settings.containsKey(serviceID));
	}
	
	private static String getKey(String serviceID)
	{
		return settings.get(serviceID);
	}
}