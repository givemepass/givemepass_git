package com.hans.tab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Activity3 extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity3);
		Button b1 = (Button) findViewById(R.id.button2);
		b1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				View view = Activity1.group.getLocalActivityManager().startActivity("Activity4", new
						Intent(Activity3.this, Activity4.class)
						.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    .getDecorView();
				Activity1.group.replaceView(view);
				
			}
		});
	}
}
