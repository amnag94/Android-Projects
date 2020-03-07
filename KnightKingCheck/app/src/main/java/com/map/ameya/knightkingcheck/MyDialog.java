package com.map.ameya.knightkingcheck;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class MyDialog extends Dialog implements
		View.OnClickListener {

	public Context context;
	int layout;
	public LayoutInflater layoutInflate;
	public View view = null;
	public int width;
	public int height;
	// private Dialog dialog;
	private TextView textView;
	private static int START_YEAR = 1990, END_YEAR = 2100;
	public Button btn_sure, btn_cancel;
	//WheelView wv_hours, wv_mins, wv_year, wv_day, wv_month;

	public MyDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public MyDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	public MyDialog(Context context, int theme, int layout) {
		super(context, theme);
		this.context = context;
		this.layout = layout;
		this.layoutInflate = LayoutInflater.from(context);
		view = layoutInflate.inflate(layout, null);
		width = (int) context.getResources().getDimension(R.dimen.dialog_width);
		height = (int) context.getResources().getDimension(
				R.dimen.dialog_height);

	}

	public MyDialog(Context context, int theme, int layout, int width,
					int height) {
		super(context, theme);
		this.context = context;
		this.layout = layout;
		this.layoutInflate = LayoutInflater.from(context);
		this.view = layoutInflate.inflate(layout, null);
		this.width = width;
		this.height = height;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (view != null) {
			this.setContentView(view);
			Window dialogWindow = getWindow();
			WindowManager.LayoutParams lp = dialogWindow.getAttributes();
			dialogWindow.setGravity(Gravity.CENTER);
			lp.width = width;
			lp.height = height;
			dialogWindow.setAttributes(lp);
		}
	}

	@Override
	public void onClick(View v) {

	}

}