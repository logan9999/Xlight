package com.xuan.lx.xlight;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class XlightActivity extends Activity {
	private ScheduledExecutorService scheduler = null;
	private ImageView imageView;
	public static boolean status = true;
	public Camera camera = null;
	private Parameters parameters = null;
	private int back = 0;// �жϰ�����back
	/* ��һ�޶���menuѡ��identifier������ʶ���¼� */
	static final private int M_SCREEN = Menu.FIRST;
	static final private int M_EXIT = Menu.FIRST + 1;
	private static long DELAY = 360;

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
		setContentView(R.layout.main);
		imageView = (ImageView) findViewById(R.id.imageView);
		imageView.setBackgroundResource(R.drawable.shou_off);
		scheduler = Executors.newSingleThreadScheduledExecutor();
		imageView.setOnClickListener(new LightButton());
	}

	class LightButton implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (status) {
				Log.i("XlightActivity", "turnOn");
				turnOn();
 				startTimer(DELAY);
			} else {
				Log.i("XlightActivity", "turnOff");
				mTimer.cancel();
				mTimer = null;
				mTimerTask = null;
				turnOff();
			}

		}

	}

	public void CloseApp() { // �رճ���
		if (status) {// ���عر�ʱ
			finish();
			android.os.Process.killProcess(android.os.Process.myPid());// �رս���
		} else if (!status) {// ���ش�ʱ
			camera.release();
			finish();
			android.os.Process.killProcess(android.os.Process.myPid());// �رս���
			status = true;// ���⣬�򿪿��غ��˳������ٴν��벻�򿪿���ֱ���˳�ʱ���������
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			back++;
			switch (back) {
			case 1:
				Toast.makeText(this, "�ٰ�һ���˳�����", Toast.LENGTH_LONG).show();
				break;
			case 2:
				back = 0;// ��ʼ��backֵ
				CloseApp();
				break;
			}
			return true;// ���ó�false��backʧЧ ��true��ʾ ��ʧЧ
		} else {
			return super.onKeyDown(keyCode, event);
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
		menu.add(idGroup1, M_SCREEN, orderMenuItem1, R.string.str_screen);
		menu.add(idGroup1, M_EXIT, orderMenuItem2, R.string.str_exit);
		menu.setGroupCheckable(idGroup1, true, true);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case (M_SCREEN):
			Intent mIntent = new Intent();
			mIntent.setClass(this, ScreenActivity.class);
			startActivity(mIntent);
			break;
		case (M_EXIT):
			/* �뿪���� */
			CloseApp();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/*@Override
	protected void onPause() {
		super.onPause();
		CloseApp();
	}*/

	Runnable task = new Runnable() {
		@Override
		public void run() {
			if (!status) {
				Message msg = new Message();
				msg.what = CLOSED;
				handler.sendMessage(msg);
			}
		}

	};

	public void scheduler(long delay) {
		Log.i("XlightActivity", "scheduler()");
		scheduler.schedule(task, delay, TimeUnit.SECONDS);
	}

	public void turnOn() {
		camera = Camera.open();
		imageView.setBackgroundResource(R.drawable.shou_on);
		parameters = camera.getParameters();
		parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);// ����
		camera.setParameters(parameters);
		camera.startPreview(); // ��ʼ����
		status = false;
	}

	public void turnOff() {
		Log.i("XlightActivity", "turnOff()");
		imageView.setBackgroundResource(R.drawable.shou_off);
		parameters.setFlashMode(Parameters.FLASH_MODE_OFF);// �ر�
		camera.setParameters(parameters);
		camera.stopPreview(); // �ص�����
		status = true;
		camera.release();
	}

	public void startTimer(long delay) {
		Log.i("XlightActivity", "startTimer()");
		mTimerTask = new TimerTask() {
			@Override
			public void run() {
				if (!status) {
					Message msg = new Message();
					msg.what = CLOSED;
					handler.sendMessage(msg);
				}
			}

		};
		delay = 1000 * delay;
		mTimer = new Timer();
		mTimer.schedule(mTimerTask, delay);

	}

	/** timer���� **/
	Timer mTimer = null;

	/** TimerTask���� **/
	TimerTask mTimerTask = null;
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CLOSED:
				turnOff();
				break;
			case OPEN:
				turnOn();
				break;

			}
			super.handleMessage(msg);
		}
	};

	private final static int CLOSED = 0;
	private final static int OPEN = 1;
}
