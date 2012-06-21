package com.hans.tab;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import java.io.File;
import java.util.ArrayList;

import android.app.ActivityGroup;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
//aaaaaaaaaaaaaaa
public class Activity1 extends ActivityGroup {
	// Keep this in a static variable to make it accessible for all the nesten activities, lets them manipulate the view
	public static Activity1 group;

	// Need to keep track of the history if you want the back-button to work properly, don't use this if your activities requires a lot of memory.
	ArrayList<View> history;

	static Uri imageUri;
	static File tmpFile;
	private ListView listView;
	private String[] listText;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity1);
		this.history = new ArrayList<View>();
	    group = this;
	    
//	    if(savedInstanceState != null)
//	    	Log.e("Activity1", "ABC" + savedInstanceState.getInt("ABC"));
//	    else
//	    	Log.e("Activity1", "is null");
	    if(TabActivity.currentTab == 0){
	        goList();
	    }
	}
	private void goList() {
        // TODO Auto-generated method stub
        setContentView(R.layout.list);
        
        listText = new String[]{"滷肉飯","蕃茄","巧克力","沙沙醬","葡萄汁"};
        listView = (ListView)findViewById(R.id.listView);
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listText);
        listView.setAdapter(listAdapter);
        listView.setFocusable(true);
       
        listView.setOnItemClickListener(new OnItemClickListener() {

           @Override
           public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                   long arg3) {
               View view = getLocalActivityManager().startActivity("Activity2", new
                       Intent(Activity1.this, Activity2.class)
                       .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                   .getDecorView();
               replaceView(view);
           }
       });
       
    }
	public void replaceView(View v) {
		// animation of activitygroup transition
		
		Animation hyperspaceJump = 
		AnimationUtils.loadAnimation(this, R.anim.translate_left_in);
		v.startAnimation(hyperspaceJump);
        // Adds the old one to history
		Log.e("Activity1","replaceView");
		history.add(v);
        // Changes this Groups View to the new View.
		setContentView(v);
		//v.setVisibility(View.GONE);
		
		//v.setVisibility(View.VISIBLE);
		Log.e("Activity1 replaceView()->history.size():", "" + history.size());
	}
	
	public void back() {
		Log.e("Activity1 back1()->history.size():", "" + history.size());
		if(history.size() > 1) {
			history.remove(history.size()-1);
			View v = history.get(history.size()-1);
			Animation hyperspaceJump = 
					AnimationUtils.loadAnimation(this, R.anim.translate_right_out);
					v.startAnimation(hyperspaceJump);
			group.setContentView(v);
		}else {
			finish();
		}
		Log.e("Activity1 back2()->history.size():", "" + history.size());
	}
	
	public void replaceContentView(String id, Intent newIntent) {
		View view = getLocalActivityManager().startActivity(id,
				newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
				.getDecorView();
		setContentView(view);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    Log.e("Activity1","onKeyDown");
		switch (keyCode) {
	    	case KeyEvent.KEYCODE_BACK:
	    	    Log.e("Activity1","onKeyDown KEYCODE_BACK");
	    		back();
	    		break;
		}
		return true;
	}
	@Override  
	public boolean dispatchKeyEvent(KeyEvent event) {  
	    Log.e("Activity1","dispatchKeyEvent");
	    if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {  
	        //do something
	        Log.e("Activity1","dispatchKeyEvent2");
	        back();
	    }  
	    return true;
	}  
//	@Override
//	protected void onPause() {
//		// TODO Auto-generated method stub
//		super.onPause();
//	}
//	@Override
//	protected void onSaveInstanceState(Bundle outState) {
//		outState.putSerializable("ABC", history);
//		Log.e("Activity1", "onSaveInstanceState");
//		super.onSaveInstanceState(outState);
//	}
//	@Override
//	protected void onRestoreInstanceState(Bundle savedInstanceState) {
//		Log.e("Activity1", "onRestoreInstanceState");
//		super.onRestoreInstanceState(savedInstanceState);
//	}
//	@Override
//	public void onConfigurationChanged(Configuration newConfig) {
//		Log.e("Activity1", "onConfigurationChanged");
//		super.onConfigurationChanged(newConfig);
//	}
}
