package com.xuan.lx.xlight;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ScreenActivity extends Activity {
	private boolean ifLocked = false;
	private PowerManager.WakeLock mWakeLock;
	private PowerManager mPowerManager;
	private LinearLayout mLinearLayout;
	/* 独一无二的menu选项identifier，用以识别事件 */
	static final private int M_COLOR = Menu.FIRST;
	static final private int M_SET = Menu.FIRST + 1;
	static final private int M_EXIT = Menu.FIRST + 2;
	/* 颜色菜单的颜色与文字数组 */
	private int[] color = { R.drawable.white, R.drawable.blue,
			R.drawable.pink, R.drawable.green, R.drawable.orange,
			R.drawable.yellow };
	private int[] text = { R.string.str_white, R.string.str_blue,
			R.string.str_pink, R.string.str_green, R.string.str_orange,
			R.string.str_yellow };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 全屏设置，隐藏窗口所有装饰
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置屏幕显示无标题，必须启动就要设置好，否则不能再次被设置
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD,
				WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.screen_main);
		/* 初始化mLinearLayout */
		mLinearLayout = (LinearLayout) findViewById(R.id.linearLayout_screen);
		/* 取得PowerManager */
		mPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		/* 取得WakeLock */
		mWakeLock = mPowerManager.newWakeLock(
				PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "BackLight");

		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.screenBrightness = 1.0f;
		getWindow().setAttributes(lp);
	}

	@Override
	protected void onResume() {
		/* onResume()时调用wakeLock() */
		wakeLock();
		super.onResume();
	}

	@Override
	protected void onPause() {
		/* onPause()时调用wakeUnlock() */
		wakeUnlock();
		super.onPause();
	}

	/* 唤起WakeLock的method */
	private void wakeLock() {
		if (!ifLocked) {
			ifLocked = true;
			mWakeLock.acquire();
		}
	}

	/* 释放WakeLock的method */
	private void wakeUnlock() {
		if (ifLocked) {
			mWakeLock.release();
			ifLocked = false;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		/* menu组ID */
		int idGroup1 = 0;
		/* menuItemID */
		int orderMenuItem1 = Menu.NONE;
		int orderMenuItem2 = Menu.NONE + 1;
		/* 建立menu */
		menu.add(idGroup1, M_COLOR, orderMenuItem1, R.string.str_color);
		menu.add(idGroup1, M_EXIT, orderMenuItem2, R.string.str_exit);
		menu.setGroupCheckable(idGroup1, true, true);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case (M_COLOR):
			/* 弹出选择背后颜色的AlertDialog */
			new AlertDialog.Builder(this)
					.setTitle(getResources().getString(R.string.str_color))
					.setAdapter(new XAdapter(this, color, text), listener1)
					.setPositiveButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
								}
							}).show();
			break;
		case (M_EXIT):
			/* 离开程序 */
			this.finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/* 选择背后颜色的AlertDialog的OnClickListener */
	OnClickListener listener1 = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			/* 更改背景颜色 */
			mLinearLayout.setBackgroundResource(color[which]);
			/* 北Toast显示设定的颜色 */
			Toast.makeText(ScreenActivity.this,
					getResources().getString(text[which]), Toast.LENGTH_LONG)
					.show();
		}
	};

}
