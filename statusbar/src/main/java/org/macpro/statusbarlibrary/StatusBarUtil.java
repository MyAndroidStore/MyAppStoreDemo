package org.macpro.statusbarlibrary;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.lang.reflect.Field;

/**
 * 作者： macpro  on 2018/1/29.
 * 邮箱： xxx.com
 */

public class StatusBarUtil {

    /**
     * 设置沉浸式状态栏(需要4.4以上版本)
     *      Activity中调用此方法
     *      color == 0 不添加占位布局，但是 状态栏透明
     *      color == -1 不添加占位布局 状态栏默认
     */
    public static void setSatusBarColorActivity(final Activity activity, final int color) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {


            if (color == -1)
                return;

            // 透明导航栏
            // activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

            //透明状态栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


            try {

                final View satusBar = activity.findViewById(R.id.status_bar);

                if (color == 0)
                    return;

                satusBar.post(new Runnable() {
                    @Override
                    public void run() {

                        satusBar.setBackgroundColor(activity.getResources().getColor(color));

                        ViewGroup.LayoutParams layoutParams = satusBar.getLayoutParams();
                        layoutParams.height = getStatusBarHeight(activity);

                        satusBar.setLayoutParams(layoutParams);
                    }
                });

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }


    /**
     * 设置沉浸式状态栏(需要4.4以上版本)
     *      Fragment中调用此方法
     *      color == 0 不添加占位布局，但是 状态栏透明
     *      color == -1 不添加占位布局 状态栏默认
     */
    public static void setSatusBarColorFragment(final Activity activity, View parent, final int color) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {


            if (color == -1)
                return;

            //透明状态栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            // activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

            try {

                final View satusBar = parent.findViewById(R.id.status_bar);

                if (color == 0)
                    return;

                satusBar.post(new Runnable() {
                    @Override
                    public void run() {

                        satusBar.setBackgroundColor(color);

                        ViewGroup.LayoutParams layoutParams = satusBar.getLayoutParams();
                        layoutParams.height = getStatusBarHeight(activity);

                        satusBar.setLayoutParams(layoutParams);
                    }
                });

            } catch (Exception e) {

                e.printStackTrace();
            }

        }

    }


    /**
     * 获取状态栏的高度(反射)
     */
    public static int getStatusBarHeight(Activity context) {
        try {
            // 通过反射获取到类
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            // 创建对象
            Object obj = c.newInstance();
            // 拿取到属性
            Field field = c.getField("status_bar_height");
            // 获取值
            int x = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
