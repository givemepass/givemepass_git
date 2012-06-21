package com.hans.tab;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

import java.io.File;

public class TabActivity extends Activity{
	LocalActivityManager lam;
	TabHost tabHost;
	File tmpFile;
	Uri imageUri;
	public static int currentTab = 0;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Log.e("TabActivity","onCreate");
        
        tabHost = (TabHost)findViewById(android.R.id.tabhost);
        lam = new LocalActivityManager(TabActivity.this, false);
        lam.dispatchCreate(savedInstanceState);
        tabHost.setup(lam);
        
        tabHost.addTab(tabHost.newTabSpec("Tab1").setIndicator("Tab1").setContent(new Intent(TabActivity.this, Activity1.class)));
        tabHost.addTab(tabHost.newTabSpec("Tab2").setIndicator("Tab2").setContent(new Intent(TabActivity.this, Activity10.class)));
        tabHost.setCurrentTab(0);
        
        View view = lam.startActivity("Activity2", new
                Intent(TabActivity.this, Activity1.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            .getDecorView();
        Activity1.group.replaceView(view);
        
        tabHost.setOnTabChangedListener(new OnTabChangeListener(){

            @Override
            public void onTabChanged(String arg0) {
                // TODO Auto-generated method stub
                currentTab = tabHost.getCurrentTab();
            }
            
        });
    }
//	@Override
//	protected void onStart() {
//		// TODO Auto-generated method stub
//		Log.e("TabActivity","onStart");
//		super.onStart();
//	}
//	@Override
//	protected void onRestart() {
//		// TODO Auto-generated method stub
//		Log.e("TabActivity","onRestart");
//		super.onRestart();
//	}
//	@Override
//	protected void onStop() {
//		// TODO Auto-generated method stub
//		Log.e("TabActivity","onStop");
//		super.onStop();
//	}
//	@Override
//	public void onConfigurationChanged(Configuration newConfig) {
//		// TODO Auto-generated method stub
//		Log.e("TabActivity","onConfigurationChanged");
//		super.onConfigurationChanged(newConfig);
//	}
//	@Override
//	protected void onResume() {
//		//lam.dispatchResume();
//		Log.e("TabActivity","onResume");
//		super.onResume();
//	}
//	@Override
//	protected void onPause() {
//		//lam.dispatchPause(isFinishing());
//		Log.e("TabActivity","onPause");
//		super.onPause();
//	}
//	@Override
//	protected void onRestoreInstanceState(Bundle savedInstanceState) {
//		Log.e("TabActivity","onRestoreInstanceState");
//		//tabHost.setCurrentTab(savedInstanceState.getInt("which"));
//		super.onRestoreInstanceState(savedInstanceState);
//	}
//	@Override
//	protected void onSaveInstanceState(Bundle outState) {
//		Log.e("TabActivity","onSaveInstanceState");
//		//outState.putInt("which", tabHost.getCurrentTab());
//		super.onSaveInstanceState(outState);
//	}
//	@Override
//	protected void onDestroy() {
//		Log.e("TabActivity","onDestroy");
//		super.onDestroy();
//	}
}