package com.hans.tab;

import java.util.ArrayList;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

public class Activity10 extends ActivityGroup{
	// Keep this in a static variable to make it accessible for all the nesten activities, lets them manipulate the view
		public static Activity10 group;

		// Need to keep track of the history if you want the back-button to work properly, don't use this if your activities requires a lot of memory.
		private ArrayList<View> history;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_10);
		this.history = new ArrayList<View>();
	    group = this;
	    
	    View view = getLocalActivityManager().startActivity("Activity10", new
				Intent(Activity10.this, Activity11.class)
				.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            .getDecorView();
	    replaceView(view);
	    /*
		Button b1 = (Button)findViewById(R.id.button1);
		b1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				View view = Activity1.group.getLocalActivityManager().startActivity("Activity11", new
						Intent(Activity10.this, Activity11.class)
						.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    .getDecorView();
				Activity1.group.replaceView(view);
			}
		});
		*/
	}
	public void replaceView(View v) {
        // Adds the old one to history
		history.add(v);
        // Changes this Groups View to the new View.
		setContentView(v);
		Log.e("replaceView", "" + history.size());
	}
	
	public void back() {
		Log.e("back1", "" + history.size());
		if(history.size() > 1) {
			history.remove(history.size()-1);
			setContentView(history.get(history.size()-1));
		}else {
			finish();
		}
		Log.e("back2", "" + history.size());
	}
	
	public void replaceContentView(String id, Intent newIntent) {
		View view = getLocalActivityManager().startActivity(id,
				newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
				.getDecorView();
		this.setContentView(view);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
	    	case KeyEvent.KEYCODE_BACK:
	    		back();
	    		break;
		}
		return true;
	}
}
