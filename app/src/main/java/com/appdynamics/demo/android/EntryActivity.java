package com.appdynamics.demo.android;

import com.appdynamics.eumagent.runtime.AgentConfiguration;
import com.appdynamics.eumagent.runtime.Instrumentation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.appdynamics.demo.android.misc.AsyncTaskListener;
import com.appdynamics.demo.android.misc.Constants;
import com.appdynamics.demo.android.misc.UserLoginTask;

public class EntryActivity extends  Activity implements AsyncTaskListener {

	 @Override
	    public void onCreate(Bundle savedInstanceState)
	    {
            super.onCreate(savedInstanceState);

            // Wei added:
			AgentConfiguration config;
			config = AgentConfiguration.builder()
					.withAppKey("EUM-AAB-AUA") // or use PreferenceConstants.EUM_APP_KEY
					.withContext(getApplicationContext())
					.withLoggingLevel(Instrumentation.LOGGING_LEVEL_VERBOSE)
					.withCollectorURL("http://wcallmrumcertlevel1lab-1qflbwue.appd-sales.com:7001")
					.withCompileTimeInstrumentationCheck(true)
					.withScreenshotsEnabled(true)
//                        .withAutoInstrument(true)
//                    .withApplicationName("com.example.android.xxxxxxxx")
					.withScreenshotURL("http://wcallmrumcertlevel1lab-1qflbwue.appd-sales.com:7001")
//                    .withExcludedUrlPatterns(excludedURLPatterns)
					.build();

			Instrumentation.start(config);

	      //See if the user credentials are already stored in the system
			SharedPreferences settings = getSharedPreferences(Constants.COMMON_PREFS_FILE_NAME,
                    Context.MODE_PRIVATE);
            String mUser = settings.getString("username",null);

            // Wei added:
			Instrumentation.setUserData("userID", mUser);

		       if (mUser==null || mUser.trim().equals("")) {
		    	   showLogin();
		       } else {
		    	  UserLoginTask task = new UserLoginTask(this);
		    	  task.execute(mUser,settings.getString("password",null));
		       }

	        finish();
	    }

	private void showLogin() {
		//Go to Login screen if the user has not been registered previously
		Intent listIntent = new Intent(this, LoginActivity.class);
		startActivity(listIntent);
	}

	@Override
	public void cancelled() {
		showLogin();
	}

	@Override
	public void onPostExecute(boolean success, boolean error,
			String exceptionMessage) {
		if (success) {
	    	   showBookList();
		} else {
			showLogin();
		}

	}

	private void showBookList() {
		Intent loginIntent = new Intent(this, ItemListActivity.class);
		startActivity(loginIntent);
	}

}
