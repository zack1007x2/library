package com.framework.base;

import java.util.List;

import org.json.JSONObject;

import cn.com.framework.R;

import com.framework.net.AsyncTaskUtil;
import com.framework.net.FileDownLoad;
import com.framework.net.NetWorkUtils;
import com.framework.net.OnResultListener;
import com.framework.util.AppUtils;
import com.framework.util.DialogTools;
import com.framework.util.MyLogger;

import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * 该类加入以下功能：<br />
 * 1."再按一次退出程序"提示(只需要调用addFirstToast()方法把activity添加到退出通知提示的集合即可) <br />
 * 2.自动检测版本提示更新<br />
 * 服务器返回数据说明：例 {"flag":1,"appversion":最新版本号,"content":"更新内容","url":"最新版本的下载地址"}
 * flag: "1"数据获取成功
 * 开启自动更新版本的功能：sp.putBoolean(SPContants.AUTO_UPLOAD,true).commit();
 * 3.无需为view设置监听操作，只需要声明一个int[]类型的ids变量，把所有要监听的控件id放到该数组中即可，同时需要继承该类
 * 4.对AsyncTask做了内存优化。
 * @author lee
 * 		   gwm 在Android framework下，
 *         建议使用优化过的数据容器比如：SparseArray,SparseBooleanArray,LongSparseArray。
 *         通用的HashMap实现的内存使用率非常的低，因为他需要为每一个mapping创建一个分离的entry
 *         object。另外，SparseArray类避免了系统对有些key的自动装箱，因而带来了更高的效率。
 */
public abstract class BaseActivity extends Activity implements OnClickListener,
		OnItemClickListener, OnItemLongClickListener,
		OnLongClickListener, OnItemSelectedListener {
	private List<String> activitys;
	/** 版本更新提示 */
	private final String url = ""; // 获取服务器上apk最新版本的url
	private final String path = ""; // 保存到本地的文件路径
	private SharedPreferences sp;
	private AppUtils app_util;
	private FragmentManager manager;
	public MyLogger Log;

	@Override
	protected final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log = MyLogger.kLog();
		sp = getSharedPreferences(SPContants.CONFIG,Context.MODE_PRIVATE);
		manager = getFragmentManager();
		app_util = new AppUtils(getApplicationContext());
		if (getApplication() instanceof BaseAppcation) { // 将activity添加到自定义界面集合
			((BaseAppcation) getApplication()).addActivity(this);
			activitys = ((BaseAppcation) getApplication()).getUIS();
		}
		onCreate(sp, manager, savedInstanceState);
		// 自动检测版本更新提示
		autoLoad();
		setListener();
	}
	/**
	 * 监听事件重写了，只需要你手动声明一个以ids为变量名的int[] 该方法会自动为在这个数组里的控件设置上事件监听
	 */
	private void setListener() {
		try {
			Field field = getClass().getField("ids");
			int[] ids = (int[]) field.get(this);
			if (ids != null && ids.length > 0)
				for (int id : ids) {
					View view = findViewById(id);
					if (view instanceof AdapterView) {
						if (view instanceof Spinner) {
							((Spinner) view).setOnItemSelectedListener(this);
							continue;
						}
						((AdapterView<?>) view).setOnItemClickListener(this);
						((AdapterView<?>) view).setOnItemLongClickListener(this);
						continue;
					}
					view.setOnClickListener(this);
					view.setOnLongClickListener(this);
				}
		} catch (Exception e) {
		}
	}

	private void autoLoad() {
		boolean flag = ((BaseAppcation) getApplication()).isFlag();
		if (flag) { // 判断是否未检测过新版本
			if (NetWorkUtils.getNetConnecState(this) && sp.getBoolean(SPContants.AUTO_UPLOAD,true)) { // 判断当前是否有网络和是否已开启自动更新版本的功能
				((BaseAppcation) getApplication()).setFlag(false); // 修改标识，标识已经检测过新版本
				AsyncTaskUtil.sendGet(url,new OnResultListener() {
							@Override
							public void onGetResult(Object result,int iError) throws Exception {
								JSONObject json = new JSONObject(result.toString());
								int flags = json.getInt("flag");
								if(flags == 1) {
									double version = json.getDouble("appversion");
									String content = json.getString("content");
									String downurl = json.getString("url"); // 获取下载最新版本的url
									double currentVersion = Double.parseDouble(getVersionName());
									if (currentVersion < version) {
										showUploadDialog(content,downurl);
									}
								}
							}
						}, this);
			} else {
				DialogTools.showNoNetWork(this);
			}
		}
	}

	/**
	 * 显示更新提示对话框
	 * 
	 * @param content
	 *                 更新提示内容
	 * @param url
	 *                 最新版本的下载地址
	 */
	protected void showUploadDialog(String content, final String downurl) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(content);
		builder.setPositiveButton("取消",new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog,int which) {
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("确定",new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog,int which) {
				// 下载最新版本并安装
				FileDownLoad.down(path,url,getApplicationContext(),new OnResultListener() {
					@Override
					public void onGetResult(Object result,int iError) throws Exception {
						installApk(path);
					}
				});
			}
		});
		builder.show();
	}
	/**
	 * 安装指定文件路径的apk文件
	 * @param path
	 */
	protected void installApk(String path) {
		app_util.installApk(path);
	}
	/**
	 * 将Activity添加到退出通知
	 * @param activityName
	 *                 activity的类名
	 */
	public void addFirstToast(String activityName) {
		activitys.add(activityName);
	}
	private long exitTime = 0;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000&& activitys.contains(getActivityName())) {
				Toast.makeText(getApplicationContext(),"再按一次退出程序",Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else if (activitys.contains(getActivityName())) {
				if (getApplication() instanceof BaseAppcation) {
					((BaseAppcation) getApplication()).exit();
				}
			}
		}
		if (keyCode == KeyEvent.KEYCODE_BACK&& activitys.contains(getActivityName())) {
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (getApplication() instanceof BaseAppcation) {
				((BaseAppcation) getApplication()).delActivity(this);
			}
			return true;
		}
		return onKeyDownMethod(keyCode, event);
	}

	/**
	 * 无需重写onKeyDown()方法,需要时应该重写该方法 已经屏蔽了返回键，如需处理返回键，请重写onKeyDown()方法
	 * @param keyCode
	 * @param event
	 * @return
	 */
	protected boolean onKeyDownMethod(int keyCode, KeyEvent event) {
		//如果启用了BasePage类，下面两行代码需要复制在activity的onKeyDown()方法中
//		BaseFragment frag = (BaseFragment) manager.findFragmentByTag("");
//		return frag.getPager().onKeyDownMethod(keyCode,event);
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		if (getApplication() instanceof BaseAppcation) {
			((BaseAppcation) getApplication()).clearAsyncTask();
		}
		super.onDestroy();
	}

	/**
	 * 获取当前正在运行的Activity
	 * 
	 * @return
	 */
	private String getActivityName() {
		return app_util.getActivityName();
	}

	/**
	 * 获取当前应用的版本号
	 * 
	 * @return
	 * @throws Exception
	 */
	private String getVersionName() {
		return app_util.getVersionName();
	}

	public <T extends BaseFragment> T getFragment(String tag) {
		return (T) manager.findFragmentByTag(tag);
	}

	public <D extends View> D findViewToId(int id) {
		return (D) (getWindow().findViewById(id));
	}

	/**
	 * 普通控件点击事件
	 */
	@Override
	public void onClick(View v) {

	}

	/**
	 * 普通控件长按点击事件
	 */
	@Override
	public boolean onLongClick(View v) {
		return false;
	}

	/**
	 * ListView的item点击事件
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view,
			int position, long id) {
	}

	public void startActivity(Class<BaseActivity> clz) {
		Intent intent = new Intent(getApplicationContext(), clz);
		startActivity(intent);
	}

	public void startActivity(Class<BaseActivity> clz, int enterAnim,
			int exitAnim) {
		Intent intent = new Intent(getApplicationContext(), clz);
		startActivity(intent, enterAnim, exitAnim);
	}

	public void startActivity(String action) {
		Intent intent = new Intent(action);
		startActivity(intent);
	}

	/**
	 * activity之间的切换，默认渐入渐出动画
	 */
	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		overridePendingTransition(R.anim.fade, R.anim.hold);
		// overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit); //缩放动画
	}

	@Override
	public Context getApplicationContext() {
		return new SoftReference<Context>(super.getApplicationContext()).get();
	}

	/**
	 * 自定义activity之间的切换动画
	 * 
	 * @param intent
	 * @param enterAnim
	 *                 进入时动画
	 * @param exitAnim
	 *                 出去时动画
	 */
	public void startActivity(Intent intent, int enterAnim, int exitAnim) {
		super.startActivity(intent);
		overridePendingTransition(enterAnim, exitAnim);
	}

	/**
	 * ListView的item长按事件监听
	 */
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		return false;
	}

	/**
	 * Spinner控件的事件监听
	 */
	@Override
	public void onItemSelected(AdapterView<?> parent, View view,
			int position, long id) {
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	/**
	 * Activity的生命周期onCreate()方法
	 * @param sp
	 *                 SharedPreferences文件对象
	 * @param manager
	 *                 管理Fragment的对象，如果使用Fragment可以跟本框架做到无缝的结合
	 * @param savedInstanceState
	 *                 Activity的状态保存
	 */
	public abstract void onCreate(SharedPreferences sp,FragmentManager manager, Bundle savedInstanceState);
	/**
	 * Fragment之间的切换方法，加载Fragment方法.<br />
	 * 重写该方法时要注意的问题：
	 * <ol>
	 * <li>该方法尽量放在activity中。避免Fragment的嵌套出现id资源找不到的问题,最好Fragment里面不要在嵌套Fragment.</li>
	 * <li>在加载之前需判断是否已经加载了，如果是 请先移除，在加载。避免报该Fragment已经加载了的问题.</li>
	 * <li>该方法不能在activity的生命周期中使用。避免回退栈的问题.<br />
	 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * (原因：addToBackStack()方法不能在activity的生命周期中使用)</li>
	 * </ol>
	 * @param frag
	 *                 中间容器显示的Fragment
	 * @param tag
	 *                 中间容器的标识
	 */
	public void jumpFragment(BaseFragment frag, String tag) {
	}
	/**
	 * @param frag
	 * @param tag
	 * @param isHiddenNav  是否隐藏导航栏，至于如何实现隐藏需开发者自己考虑
	 */
	public void jumpFragment(BaseFragment frag, String tag,boolean isHiddenNav) {
	}
}