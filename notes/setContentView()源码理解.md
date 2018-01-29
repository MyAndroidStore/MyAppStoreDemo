### WindowManager(接口) 中定义了Window的类型
/**
         * @see #TYPE_BASE_APPLICATION
         * @see #TYPE_APPLICATION
         * @see #TYPE_APPLICATION_STARTING
         * @see #TYPE_DRAWN_APPLICATION
         * @see #TYPE_APPLICATION_PANEL
         * @see #TYPE_APPLICATION_MEDIA
         * @see #TYPE_APPLICATION_SUB_PANEL
         * @see #TYPE_APPLICATION_ABOVE_SUB_PANEL
         * @see #TYPE_APPLICATION_ATTACHED_DIALOG
         * @see #TYPE_STATUS_BAR
         * @see #TYPE_SEARCH_BAR
         * @see #TYPE_PHONE
         * @see #TYPE_SYSTEM_ALERT
         * @see #TYPE_TOAST
         * @see #TYPE_SYSTEM_OVERLAY
         * @see #TYPE_PRIORITY_PHONE
         * @see #TYPE_STATUS_BAR_PANEL
         * @see #TYPE_SYSTEM_DIALOG
         * @see #TYPE_KEYGUARD_DIALOG
         * @see #TYPE_SYSTEM_ERROR
         * @see #TYPE_INPUT_METHOD
         * @see #TYPE_INPUT_METHOD_DIALOG
         */

### Activity.setContentView()
	
```
// activity
public void setContentView(int layoutResID) {
        // Window(抽象类)-->最终调用的是new PhoneWindow().setContentView();
        getWindow().setContentView(layoutResID);
        initWindowDecorActionBar();
}
```

### PhoneWindow.setContentView()

```
@Override
public void setContentView(int layoutResID){

	// 其实只做了两件事
	installDecor(); // 初始化DectorView(继承FrameLayout),向DectorView中添加系统布局，获取其中的帧布局
	mLayoutInflator.inflate(layoutResID,mContentParent);// 将我们activity中的布局添加到帧布局中
}

```


```
private void installDector(){

	if (mDecor == null) {
		mDecor = generateDecor(); // new DecorView();
		....
	}
	
	if(mContentParent == null){
		mContentParent = generateLayout(mDecor);//其实返回的是一个(R.id.content)帧布局
		....
	}
}
```	
```	
R.layout.screen_simple;资源
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:fitsSystemWindows="true"
    android:orientation="vertical">
    <ViewStub android:id="@+id/action_mode_bar_stub"
              android:inflatedId="@+id/action_mode_bar"
              android:layout="@layout/action_mode_bar"
              android:layout_width="match_parent"
              android:layout_height="wrap_content"
              android:theme="?attr/actionBarTheme" />
    <FrameLayout
         android:id="@android:id/content"
         android:layout_width="match_parent"
         android:layout_height="match_parent"
         android:foregroundInsidePadding="false"
         android:foregroundGravity="fill_horizontal|top"
         android:foreground="?android:attr/windowContentOverlay" />
</LinearLayout>
```	