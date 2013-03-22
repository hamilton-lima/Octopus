package com.example.octopus;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class MainActivity extends Activity {

	private ViewComFila view;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		view = new ViewComFila(this);
		setContentView(view);
		
	}

	protected void onDestroy() {
		view.hastaLaVista();
		super.onDestroy();
	}

}
