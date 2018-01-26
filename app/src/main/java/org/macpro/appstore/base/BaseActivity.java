package org.macpro.appstore.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import org.macpro.appstore.R;
import org.macpro.appstore.utils.AppActivityManager;

import java.lang.reflect.Field;

/**
 * 作者： macpro  on 2018/1/26.
 * 邮箱： xxx.com
 */

public abstract class BaseActivity extends Activity {


    protected RelativeLayout title_bar = null ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 去掉ActionBar AndroidManifest文件 android:theme="@style/AppTheme"
        // 此style 父类--> parent="Theme.AppCompat.Light.NoActionBar" 即可

        // ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE 横屏
        // ActivityInfo.SCREEN_ORIENTATION_PORTRAIT 竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Activity统一添加到栈进行管理
        AppActivityManager.getInstance().addActivity(this);

        // 填充布局
        setContentView(setContentView());

        // 当系统版本为4.4或者4.4以上时可以使用沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

            setStatusBar();
        }
        // 初始化view
        initView();
    }


    /**
     * @return 布局ID
     */
    protected abstract int setContentView();

    /**
     * 初始化view
     */
    protected abstract void initView();


    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);

        // activity切换动画
        // 参数一：新Activity进入的动画(最右侧进来)
        // 参数二：旧Activity退出的动画(最左侧出去)
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
    }


    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);

        // activity切换动画
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
    }


    // 跳转activity
    protected void openActivity(Class clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    @Override
    public void finish() {
        super.finish();

        // activity切换动画(上一个activity从左侧进入，销毁的activity往右出去)
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }

    @Override
    protected void onDestroy() {
        // Activity移出栈
        AppActivityManager.getInstance().removeActivity(this);
        super.onDestroy();

        fixInputMethodManagerLeak(this);
    }


    /**
     * 解决InputMethodManager内存泄露现象
     */
    private static void fixInputMethodManagerLeak(Context destContext) {

        if (destContext == null)
            return;

        InputMethodManager imm = (InputMethodManager) destContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm == null)
            return;

        String[] arr = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
        Field f;
        Object obj_get;
        for (String param : arr) {
            try {
                f = imm.getClass().getDeclaredField(param);
                if (!f.isAccessible()) {
                    f.setAccessible(true);
                } // author: sodino mail:sodino@qq.com
                obj_get = f.get(imm);
                if (obj_get != null && obj_get instanceof View) {
                    View v_get = (View) obj_get;
                    if (v_get
                            .getContext() == destContext) { // 被InputMethodManager持有引用的context是想要目标销毁的
                        f.set(imm, null); // 置空，破坏掉path to gc节点
                    } else {
                        // 不是想要目标销毁的，即为又进了另一层界面了，不要处理，避免影响原逻辑,也就不用继续for循环了
                        /*if (QLog.isColorLevel()) {
                            QLog.d(ReflecterHelper.class.getSimpleName(), QLog.CLR, "fixInputMethodManagerLeak break, context is not suitable, get_context=" + v_get.getContext()+" dest_context=" + destContext);
                        }*/
                        break;
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }


    /**
     * 获取状态栏的高度(反射)
     */
    protected int getStatusBarHeight() {
        try {
            // 通过反射获取到类
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            // 创建对象
            Object obj = c.newInstance();
            // 拿取到属性
            Field field = c.getField("status_bar_height");
            // 获取值
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 设置沉浸式状态栏(需要4.4以上版本)
     */
    protected void setStatusBar() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            // 获得状态栏高度
            final int statusHeight = getStatusBarHeight();

            try {
                // 获得 title_bar控件
                title_bar = (RelativeLayout) findViewById(R.id.bar_layout);

                title_bar.post(new Runnable() {
                    @Override
                    public void run() {
                        int titleHeight = title_bar.getHeight();
                        ViewGroup.LayoutParams params = title_bar.getLayoutParams();
                        params.height = statusHeight + titleHeight;
                        title_bar.setLayoutParams(params);
                    }
                });

            }catch (Exception e){

                e.printStackTrace();
            }
        }
    }
}
