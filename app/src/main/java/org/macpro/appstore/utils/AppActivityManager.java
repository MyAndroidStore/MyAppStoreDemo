package org.macpro.appstore.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.util.Stack;

/**
 * 作者： macpro  on 2018/1/26.
 * 邮箱： xxx.com
 * <p>
 * 统一管理Activity
 */

public class AppActivityManager {

    // 栈<Activity>
    private static Stack<Activity> mActivityStack;
    private static AppActivityManager mAppManager;

    // 构造方法私有
    private AppActivityManager() {
    }

    // 单例
    public static AppActivityManager getInstance() {

        if (mAppManager == null)
            mAppManager = new AppActivityManager();

        return mAppManager;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {

        if (mActivityStack == null)
            mActivityStack = new Stack<>();

        mActivityStack.add(activity);
    }

    /**
     * 移除Activity到堆外
     */
    public void removeActivity(Activity activity) {
        mActivityStack.remove(activity);
    }

    /**
     * 获取栈顶Activity
     */
    public Activity getTopActivity() {
        return mActivityStack.lastElement();
    }

    /**
     * 结束栈顶Activity
     */
    public void killTopActivity() {
        killActivity(mActivityStack.lastElement());
    }

    /**
     * 结束指定的Activity
     */
    private void killActivity(Activity activity) {
        if (activity != null) {
            mActivityStack.remove(activity);
            activity.finish();
        }
    }


    /**
     * 结束指定类名的Activity
     */
    public void killActivity(Class<?> cls) {
        for (Activity activity : mActivityStack) {
            if (activity.getClass().equals(cls)) {
                killActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    private void killAllActivity() {

        for (int i = 0, size = mActivityStack.size(); i < size; i++) {
            if (null != mActivityStack.get(i)) {
                mActivityStack.get(i).finish();
            }
        }
        mActivityStack.clear();
    }

    /**
     * 退出应用程序
     */
    @SuppressWarnings("deprecation")
    public void AppExit(Context context) {
        try {
            killAllActivity();
            System.exit(0);
        } catch (Exception e) {
            Log.e("AppActivityManager", "" + e);
        }
    }
}
