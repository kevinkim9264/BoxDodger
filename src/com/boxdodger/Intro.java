package com.boxdodger;


import com.boxdodger.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Intro extends Activity {
	Intent intent = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//System.gc();

		setContentView(R.layout.intro);

		// After one second of the splash time, it hands the control to CharacterJump.java
		Handler x = new Handler();
		x.postDelayed(new splashhandler(), 2500);
	}

	
	public class splashhandler implements Runnable {
		public void run() {
			Intent intent = new Intent(getApplicationContext(),
					MainMenu.class);
			startActivity(intent);

			
			finish();
		}
	}

	private void clearApplicationCache(java.io.File dir) {
		if (dir == null)
			dir = getCacheDir();
		else
			;
		if (dir == null)
			return;
		else
			;
		java.io.File[] children = dir.listFiles();
		try {
			for (int i = 0; i < children.length; i++)
				if (children[i].isDirectory())
					clearApplicationCache(children[i]);
				else
					children[i].delete();
		} catch (Exception e) {
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		clearApplicationCache(null);
	}
}


