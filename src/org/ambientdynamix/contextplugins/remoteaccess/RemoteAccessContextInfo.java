package org.ambientdynamix.contextplugins.remoteaccess;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.ambientdynamix.api.application.IContextInfo;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class RemoteAccessContextInfo implements IContextInfo
{

	private final String TAG = Constants.TAG;
	String id="";
	String key="";
	
	public static Parcelable.Creator<RemoteAccessContextInfo> CREATOR = new Parcelable.Creator<RemoteAccessContextInfo>() 
			{
			public RemoteAccessContextInfo createFromParcel(Parcel in) 
			{
				return new RemoteAccessContextInfo(in);
			}

			public RemoteAccessContextInfo[] newArray(int size) 
			{
				return new RemoteAccessContextInfo[size];
			}
		};
		
	RemoteAccessContextInfo(String id, String key)
	{
		this.id=id;
		this.key=key;
	}
	
	public RemoteAccessContextInfo(Parcel in) 
	{
		id= in.readString();
		key = in.readString();
	}

	public RemoteAccessContextInfo() 
	{
	}

	@Override
	public String toString() 
	{
		return this.getClass().getSimpleName();
	}
	
	@Override
	public int describeContents() 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) 
	{
		out.writeString(id);
		out.writeString(key);
	}

	@Override
	public String getContextType() 
	{
		return "org.ambientdynamix.contextplugins.context.info.access.sessionkey";
	}

	@Override
	public String getImplementingClassname() 
	{
		return this.getClass().getName();
	}

	@Override
	public String getStringRepresentation(String format) 
	{
		String result=id+" "+key;
		return result;
	}

	@Override
	public Set<String> getStringRepresentationFormats() 
	{
		Set<String> formats = new HashSet<String>();
		formats.add("text/plain");
		formats.add("XML");
		formats.add("JSON");
		return formats;
	}
}