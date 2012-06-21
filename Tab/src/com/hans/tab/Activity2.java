package com.hans.tab;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class Activity2 extends Activity {
	Uri imageUri;
	File tmpFile;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity2);
		Button b1 = (Button) findViewById(R.id.button1);
	    b1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				View view = Activity1.group.getLocalActivityManager().startActivity("Activity3", new
							Intent(Activity2.this, Activity3.class)
							.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        .getDecorView();
				Activity1.group.replaceView(view);
			}
		});
	}
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        Log.e("Activity2","onKeyDown");
        return false;
    }
}
