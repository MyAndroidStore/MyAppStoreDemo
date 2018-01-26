package org.macpro.appstore;

import android.content.Intent;
import android.view.View;

import org.macpro.appstore.base.BaseActivity;

public class MainActivity extends BaseActivity {


    @Override
    protected int setContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {


    }

    public void click(View view) {

        startActivity(new Intent(this,Main2Activity.class));
    }
}
