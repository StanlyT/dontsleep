package com.stanlytango.android.dontsleepmanager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.stanlytango.android.dontsleepmanager.databasestructure.Sentinel;

public class ActNewSentinel extends AppCompatActivity {
    public static final String EXTRA = "extra";
    private static final String TAG = "# ActSentinel";
    private static final int MENU_ADD_GUARD = 123;

    private EditText edtName, edtSurname, edtLogin, edtPassword;

    public static Intent newIntent (Context context, Class<?> cls){
        Intent i = new Intent (context, cls);
        return i;
    }

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
                String strName, strSurname, strLogin, strPassword;
                strName = edtName.getText().toString();
                strSurname = edtSurname.getText().toString();
                strLogin = edtLogin.getText().toString();
                strPassword = edtPassword.getText().toString();

                new Sentinel().writeNewSentinel(FirebaseDatabase.getInstance().getReference(ActMainGmailAuth.DBSentintelNm),//ActMainGmailAuth.DBSentintelNm
                                                strLogin, strPassword, strName, strSurname, Long.valueOf(1));
                setResult(RESULT_OK);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_sentinel);

        edtName = (EditText)findViewById(R.id.name);
        edtSurname = (EditText)findViewById(R.id.surname);
        edtLogin = (EditText)findViewById(R.id.login);
        edtPassword = (EditText)findViewById(R.id.password);




    }
}
