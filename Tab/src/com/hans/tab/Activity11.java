package com.hans.tab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Activity11 extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity11);
		Button b1 = (Button)findViewById(R.id.button1);
		b1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				View view = Activity10.group.getLocalActivityManager().startActivity("Activity13", new
						Intent(Activity11.this, Activity13.class)
						.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    .getDecorView();
				Activity10.group.replaceView(view);
			}
		});
	}
}
