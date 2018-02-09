package com.stanlytango.android.dontsleepmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityManagerMainPanel extends BaseActivity implements View.OnClickListener{
    public Button btn_accounts_manager, btn_account_sett, btn_report_results, btn_pass_changing;
    public Intent intent;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.manager_panel);
        btn_accounts_manager = (Button)findViewById(R.id.btn_account_manager);
        btn_account_sett  = (Button)findViewById(R.id.btn_account_sett);
        btn_report_results = (Button)findViewById(R.id.btn_rep_res);
        btn_pass_changing  = (Button)findViewById(R.id.btn_pass_changing);

        btn_accounts_manager.setOnClickListener(this);
        btn_account_sett.setOnClickListener(this);
        btn_report_results.setOnClickListener(this);
        btn_pass_changing.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
//        switch (view.getId()){
//            case R.id.btn_account_manager:
////                если ни одного охранника нет, тогда
////                  открываем страницу с всплывающим окном создать нового охранника
////                  в противном случае просто список с созданными аккаунтами
//                intent = ActivityAdmGuardsList.newIntent(ActivityAdminMainPanel.this, ActivityAdmGuardsList.class, true);
//                startActivity(intent);
//                break;
//            case R.id.btn_account_sett:
//                intent = ActivityAdmSettings.newIntent(ActivityAdminMainPanel.this, ActivityAdmSettings.class, true);
//                startActivity(intent);
//                break;
//            case R.id.btn_rep_res:
//                intent = ActivityAdminReport.newIntent(ActivityAdminMainPanel.this, ActivityAdminReport.class, true);
//                startActivity(intent);
//                break;
//            case R.id.btn_pass_changing:
//                intent = ActivityAdminReport.newIntent(ActivityAdminMainPanel.this, ActivityAdminChangePass.class, true);
//                startActivity(intent);
//        }
    }
}


