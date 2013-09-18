package org.ambientdynamix.contextplugins.remoteaccess;

import org.ambientdynamix.api.contextplugin.ContextPluginRuntime;
import org.ambientdynamix.api.contextplugin.ContextPluginSettings;
import org.ambientdynamix.api.contextplugin.IContextPluginConfigurationViewFactory;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RemoteAccessConfigurationActivity extends Activity implements IContextPluginConfigurationViewFactory
{

	LinearLayout rootLayout;
	private final static String TAG = "LSTFM PLUGIN";
	private Context ctx;
	Activity activity;
	
	@Override
	public void destroyView() throws Exception 
	{
		
	}

	@Override
	public View initializeView(Context context, final ContextPluginRuntime arg1, int arg2) throws Exception 
	{

		Log.i(TAG, "version 11");
		ctx=context;
		activity=this;
		// Discover our screen size for proper formatting 
		DisplayMetrics met = context.getResources().getDisplayMetrics();
		
		// Access our Locale via the incoming context's resource configuration to determine language
		String language = context.getResources().getConfiguration().locale.getDisplayLanguage();
		
        TextView text = new TextView(ctx);
        text.setText("Username");
        
        final EditText username = new EditText(ctx);
        ContextPluginSettings settings = arg1.getPluginFacade().getContextPluginSettings(arg1.getSessionId());
        if(settings!=null)
        {
            	Log.d(TAG, "got Settings");
            	String u = settings.get(Constants.USERNAME);
            	if(u!=null)
            	{
            		username.setText(u);
            	}
            	else
            	{
            		username.setText("");
            	}
            }
            else
            {
            	settings = RemoteAccessPluginRuntime.settings;
                if(settings!=null)
                {
                	Log.d(TAG, "got Settings");
                	String u = settings.get(Constants.USERNAME);
                	if(u!=null)
                	{
                		username.setText(u);
                	}
                	else
                	{
                		username.setText("");
                	}
                }
                else
                {
                	Log.d(TAG, "settings are zero");
                }
            }
        	        
        Button b2 = new Button(context);
       	b2.setText("Save");
        b2.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v)
            {
            	String x = username.getEditableText().toString();
            	 ContextPluginSettings settings = arg1.getPluginFacade().getContextPluginSettings(arg1.getSessionId());
            	settings.put(Constants.USERNAME, x);
            	//TODO: finish()
            	arg1.getPluginFacade().setPluginConfiguredStatus(arg1.getSessionId(), true);
            	activity.finish();
            }
        });
        
		// Main layout. 
		rootLayout = new LinearLayout(context);
		rootLayout.setOrientation(LinearLayout.VERTICAL);
		
	       rootLayout.addView(text,  new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
	        		FrameLayout.LayoutParams.WRAP_CONTENT));
	 
	       rootLayout.addView(username,  new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
	        		FrameLayout.LayoutParams.WRAP_CONTENT));
	 
	       rootLayout.addView(b2,  new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
	        		FrameLayout.LayoutParams.WRAP_CONTENT));
	 
		return rootLayout;
  
	}

}
