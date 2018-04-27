package com.stanlytango.android.dontsleepmanager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class ActNewSentinel extends AppCompatActivity {
    public static final String EXTRA = "extra";
    private static final String TAG = "# ActSentinel";
    private static final int MENU_ADD_GUARD = 123;

    public static Intent newIntent (Context context, Class<?> cls, Boolean b){
        Intent i = new Intent (context, cls);
        i.putExtra(EXTRA, b);
        return i;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sentinel_create, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.create_sentinel:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_sentinel);
    }
}
