package com.stanlytango.android.dontsleepmanager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stanlytango.android.dontsleepmanager.databasestructure.SentinelFirebaseCallback;
import com.stanlytango.android.dontsleepmanager.databasestructure.Sentinel;
import com.stanlytango.android.dontsleepmanager.databasestructure.SentinelStorage;

import java.util.ArrayList;
import java.util.List;


public class ActSentinel extends AppCompatActivity implements SentinelFirebaseCallback {
    public static final String EXTRA = "extra";
    private static final String TAG = "# ActSentinel";
    private static final int MENU_ADD_GUARD = 123;
    private static final int REQUEST_CODE = 11;

    RecyclerView mRecyclerView;
    SentinelViewAdapter adapter;
    private final String DBSentintelName = "sentinel";

    Sentinel sentinel = new Sentinel();
    FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference dbRef = mFirebaseDatabase.getReference(DBSentintelName);
    List<Sentinel> list = new ArrayList<>();
    SentinelStorage sentinelStorage;

    public static Intent newIntent (Context context, Class<?> cls, Boolean b){
        Intent i = new Intent (context, cls);
        i.putExtra(EXTRA, b);
        return i;
    }
// ready
    public void updateUI(){
        //SentinelStorage.get().readSentinelsListFromDB(dbRef, this);
        Log.d(TAG, "updateUI :: "+SentinelStorage.get().getList().toString());
        if (adapter == null){
            SentinelStorage.get().readSentinelsListFromDB(dbRef, this);
            //sentinelStorage.readSentinelsListFromDB(dbRef, this);
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_sentinel, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.new_sentinel:
//                startActivity(ActNewSentinel.newIntent(getApplicationContext(),
//                              ActNewSentinel.class, true));
                Intent intent = new Intent ();
                intent = ActNewSentinel.newIntent(getApplicationContext(),
                        ActNewSentinel.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.quit_main:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != AppCompatActivity.RESULT_OK)
            return;
        if (REQUEST_CODE == requestCode){
            if (data == null){
                Log.d(TAG, "onActivityResult: Intent data EQUALS null");
                return;
            }
        }
    }

    // method implementation of SentinelFirebaseCallback
    @Override
    public void onCallback(List<Sentinel> lst) {
        list.addAll(lst);
        Log.d(TAG, "  !!! onCallback list empty :: "+list.isEmpty());
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() call :: "+SentinelStorage.get().getList().toString());
        updateUI();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sentinel_view);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new SentinelViewAdapter(list);
        Log.d(TAG, "onCreate() list :: "+list.toString());

        sentinelStorage = SentinelStorage.get();

        // if db Sentinel exists then read that
        // in other case start ActNewSentinel
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    // callback method of SentinelFirebaseCallback interface
                    sentinelStorage.readSentinelsListFromDB(dbRef, new SentinelFirebaseCallback() {
                        @Override
                        public void onCallback(List<Sentinel> lst) {
                            list.addAll(lst);
                            Log.d(TAG, "readSentinelsList. List is empty :: "+list.isEmpty());
                            adapter.notifyDataSetChanged();
                        }
                    });
                } else {
                    Log.d(TAG, "DataBase "+ DBSentintelName + " doesn't exist");
                    Intent intent = new Intent ();
                    intent = ActNewSentinel.newIntent(getApplicationContext(),
                            ActNewSentinel.class);
                    startActivityForResult(intent, REQUEST_CODE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG,"onCancelled :: "+databaseError.getMessage());
            }
        });

        mRecyclerView.setAdapter(adapter);  // Log.d(TAG, "!!!! list empty :: "+list.isEmpty());
    }

    public class SentinelViewHolder extends RecyclerView.ViewHolder{
        Sentinel sentinel;
        TextView tvLogin;
        TextView tvPassword;
        TextView tvName;
        TextView tvSurname;

        SentinelViewHolder(View view){
            super(view);
            tvLogin = (TextView) view.findViewById(R.id.login_value);
            tvPassword = (TextView) view.findViewById(R.id.password_value);
            tvName = (TextView) view.findViewById(R.id.name);
            tvSurname = (TextView) view.findViewById(R.id.surname);
        }

        public void bind (Sentinel sentinel){
            this.sentinel = sentinel;
            tvLogin.setText(sentinel.login);
            tvPassword.setText(sentinel.password);
            tvName.setText(sentinel.name);
            tvSurname.setText(sentinel.surname);
        }
    }

    public class SentinelViewAdapter extends RecyclerView.Adapter<SentinelViewHolder>{
        List<Sentinel> mSentinels;

        public SentinelViewAdapter(List<Sentinel> guards){
            mSentinels = guards;
        }

        public SentinelViewHolder onCreateViewHolder(ViewGroup container, int viewType){
            View view = LayoutInflater.from(container.getContext())
                    .inflate(R.layout.item_of_sentinel_table, container,false);
            //Log.d("onCreateVH "," container = "+container.getClass().toString());
            SentinelViewHolder vh = new SentinelViewHolder(view);
            return vh;
        }


        public void onBindViewHolder(SentinelViewHolder sentinelVH, int position){
            //Log.d("onBindViewHolder"," myViewHolder = position "+position);
            Sentinel sentinel = mSentinels.get(position);
            sentinelVH.bind(sentinel);
        }

        public int getItemCount(){
            return mSentinels.size();
        }
    }
}

