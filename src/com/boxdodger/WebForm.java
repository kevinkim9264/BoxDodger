package com.boxdodger;

import com.boxdodger.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class WebForm extends Activity{
	WebView browser;
	Intent intent = null;
	private String link = null;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.webform);
		
		Intent i = getIntent();
		Bundle extras = i.getExtras();
		if (extras != null) {
			link = extras.getString("link");
		}
		
		browser = (WebView) findViewById(R.id.webkit);
		
		browser.loadUrl(link);
		
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
	
}
