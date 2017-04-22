package spencer.cn.finalproject.acview;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import spencer.cn.finalproject.R;
import spencer.cn.finalproject.application.BaseApplication;

public class BaseActionBarActivity extends BaseActivity {
    private BaseApplication mBaseApp = null;
    private WindowManager mWindowManager = null;
    private View mNightView = null;
    private WindowManager.LayoutParams mNightViewParam;
    private boolean mIsAddedView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mBaseApp = (BaseApplication) getApplication();
        if (mBaseApp.isNightMode())
            setTheme(R.style.AppTheme_night);
        else
            setTheme(R.style.AppTheme_day);
        super.onCreate(savedInstanceState);
        mIsAddedView = false;
        if (mBaseApp.isNightMode()) {
            initNightView();
            mNightView.setBackgroundResource(R.color.night_mask);
        }
    }
    @Override
    protected void onDestroy() {
        if (mIsAddedView) {
            mBaseApp = null;
            mWindowManager.removeViewImmediate(mNightView);
            mWindowManager = null;
            mNightView = null;
        }
        super.onDestroy();
    }
    public void ChangeToDay() {
        mBaseApp.setIsNightMode(false);
        mNightView.setBackgroundResource(android.R.color.transparent);
    }
    public void ChangeToNight() {
        mBaseApp.setIsNightMode(true);
        initNightView();
        mNightView.setBackgroundResource(R.color.night_mask);
    }
    /**
     * wait a time until the onresume finish
     */
    public void recreateOnResume() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                recreate();
            }
        }, 100);
    }
    private void initNightView() {
        if (mIsAddedView == true)
            return;
        mNightViewParam = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSPARENT);
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mNightView = new View(this);
        mWindowManager.addView(mNightView, mNightViewParam);
        mIsAddedView = true;
    }
}