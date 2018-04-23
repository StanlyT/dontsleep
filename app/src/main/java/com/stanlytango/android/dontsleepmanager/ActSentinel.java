package com.stanlytango.android.dontsleepmanager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stanlytango.android.dontsleepmanager.databasestructure.Sentinel;

import java.util.List;


public class ActSentinel extends BaseActivity {
    RecyclerView mRecyclerView;
    SentinelViewAdapter adapter;

    Sentinel sentinel = new Sentinel();
    List<Sentinel> list = sentinel.makeList();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sentinel_view);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        adapter = new SentinelViewAdapter(list);
        mRecyclerView.setAdapter(adapter);

    }

    // класс view holder-а с помощью которого мы получаем ссылки
    // на каждый виджет из отдельного пункта списка
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

        // Создает новые views (вызывается layout manager-ом)
        public SentinelViewHolder onCreateViewHolder(ViewGroup container, int viewType){
            View view = LayoutInflater.from(container.getContext())
                    .inflate(R.layout.item_of_sentinel_table, container,false);
            //Log.d("onCreateViewHlder "," container = "+container.getClass().toString());
            SentinelViewHolder vh = new SentinelViewHolder(view);
            return vh;
        }

        //Связываем данные с виджетами каждого item'a во ViewHolder'е
        public void onBindViewHolder(SentinelViewHolder sentinelVH, int position){
            Log.d("onBindViewHolder"," myViewHolder = position "+position);
            Sentinel sentinel = mSentinels.get(position);
            sentinelVH.bind(sentinel);
        }

        // Последний обязательный для реализации метод
        // Возвращает размер данных (вызывается layout manager-ом)
        public int getItemCount(){
            return mSentinels.size();
        }
    }
}

