package com.boxdodger;


import com.boxdodger.basehelper.BaseHelper;
import com.boxdodger.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.ads.AdSize;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;



public class MainMenu extends BaseHelper {
	Intent intent = null;
	private EditText inputAddress = null;
	String text = null;
	String score = "";
	
	//Admob
	Handler handler = new Handler();
			
	private void loadBannerView() {
		new Thread(new Runnable() {
					
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						AdView mAdView = (AdView) findViewById(R.id.adView);
				        AdRequest adRequest = new AdRequest.Builder().build();
				        mAdView.loadAd(adRequest);
					}
				});
			}
		}).start();
	}
		
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		System.gc();

		setContentView(R.layout.main_menu);
		
		loadBannerView();
		
		//Name typing
		SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
		inputAddress = (EditText) findViewById(R.id.inputAddr);
		
		//Recalling the previously saved data.
		// First data = name.
		text = pref.getString("editText", "");
		inputAddress.setText(text);
		
		// Second data = score.
		score = pref.getString("score", "");
		if (score.trim().length() != 0) {
			score = score + " Second";
		}
		TextView scoreTxt = (TextView) findViewById(R.id.score);
		scoreTxt.setText(score);

		
		
		//Game start button
		findViewById(R.id.mark).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(getApplicationContext(), 
						MainActivity.class);
				
				startActivity(intent);
			}
		});
		
		//Record looking button
		findViewById(R.id.book).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(getApplicationContext(),
						WebForm.class);
				intent.putExtra("link", BaseHelper.URL + "rankView.php");
				startActivity(intent);
			}
		});
		
		
		
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
	
	//when the application ends, data are saved.
	@Override
	public void onStop() {
		
		super.onStop();
		SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		
		inputAddress = (EditText) findViewById(R.id.inputAddr);
		editor.putString("editText", inputAddress.getText().toString());
		editor.commit();
	}
	
	/* Comes to this method, after calling finish() in MainActivity.java class */
	@Override
	public void onResume() {
		super.onResume();
		SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
		
		//score
		score = pref.getString("score", "");
		
		
		if (score.trim().length() != 0) {
			score = score + " Seconds";
		}
		TextView scoreTxt = (TextView) findViewById(R.id.score);
		scoreTxt.setText(score);
	}
}


