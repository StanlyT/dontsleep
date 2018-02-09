package com.stanlytango.android.dontsleepmanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class BaseActivity extends AppCompatActivity {
    public static final String EXTRA = "extra";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTitle(getResources().getString(R.string.app_name) + " : " + getLocalClassName());
        super.onCreate(savedInstanceState);
    }

    public static Intent newIntent (Context context, Class<?> cls, Boolean b){
        Intent i = new Intent (context, cls);
        i.putExtra(EXTRA, b);
        return i;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                // Toast.makeText(getApplicationContext(),
                //                "pressed R.id.home",
                //                  Toast.LENGTH_SHORT)
                //             .show();
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
