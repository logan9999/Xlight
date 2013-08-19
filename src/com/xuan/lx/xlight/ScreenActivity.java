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
	/* ��һ�޶���menuѡ��identifier������ʶ���¼� */
	static final private int M_COLOR = Menu.FIRST;
	static final private int M_SET = Menu.FIRST + 1;
	static final private int M_EXIT = Menu.FIRST + 2;
	/* ��ɫ�˵�����ɫ���������� */
	private int[] color = { R.drawable.white, R.drawable.blue,
			R.drawable.pink, R.drawable.green, R.drawable.orange,
			R.drawable.yellow };
	private int[] text = { R.string.str_white, R.string.str_blue,
			R.string.str_pink, R.string.str_green, R.string.str_orange,
			R.string.str_yellow };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ȫ�����ã����ش�������װ��
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // ������Ļ��ʾ�ޱ��⣬����������Ҫ���úã��������ٴα�����
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD,
				WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.screen_main);
		/* ��ʼ��mLinearLayout */
		mLinearLayout = (LinearLayout) findViewById(R.id.linearLayout_screen);
		/* ȡ��PowerManager */
		mPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		/* ȡ��WakeLock */
		mWakeLock = mPowerManager.newWakeLock(
				PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "BackLight");

		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.screenBrightness = 1.0f;
		getWindow().setAttributes(lp);
	}

	@Override
	protected void onResume() {
		/* onResume()ʱ����wakeLock() */
		wakeLock();
		super.onResume();
	}

	@Override
	protected void onPause() {
		/* onPause()ʱ����wakeUnlock() */
		wakeUnlock();
		super.onPause();
	}

	/* ����WakeLock��method */
	private void wakeLock() {
		if (!ifLocked) {
			ifLocked = true;
			mWakeLock.acquire();
		}
	}

	/* �ͷ�WakeLock��method */
	private void wakeUnlock() {
		if (ifLocked) {
			mWakeLock.release();
			ifLocked = false;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		/* menu��ID */
		int idGroup1 = 0;
		/* menuItemID */
		int orderMenuItem1 = Menu.NONE;
		int orderMenuItem2 = Menu.NONE + 1;
		/* ����menu */
		menu.add(idGroup1, M_COLOR, orderMenuItem1, R.string.str_color);
		menu.add(idGroup1, M_EXIT, orderMenuItem2, R.string.str_exit);
		menu.setGroupCheckable(idGroup1, true, true);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case (M_COLOR):
			/* ����ѡ�񱳺���ɫ��AlertDialog */
			new AlertDialog.Builder(this)
					.setTitle(getResources().getString(R.string.str_color))
					.setAdapter(new XAdapter(this, color, text), listener1)
					.setPositiveButton("ȡ��",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
								}
							}).show();
			break;
		case (M_EXIT):
			/* �뿪���� */
			this.finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/* ѡ�񱳺���ɫ��AlertDialog��OnClickListener */
	OnClickListener listener1 = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			/* ���ı�����ɫ */
			mLinearLayout.setBackgroundResource(color[which]);
			/* ��Toast��ʾ�趨����ɫ */
			Toast.makeText(ScreenActivity.this,
					getResources().getString(text[which]), Toast.LENGTH_LONG)
					.show();
		}
	};

}
