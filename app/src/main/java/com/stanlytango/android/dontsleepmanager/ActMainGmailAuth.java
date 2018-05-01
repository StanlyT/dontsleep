package com.stanlytango.android.dontsleepmanager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Data;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.*;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.stanlytango.android.dontsleepmanager.databasestructure.SecuredZone;
import com.stanlytango.android.dontsleepmanager.databasestructure.Sentinel;
import com.stanlytango.android.dontsleepmanager.databasestructure.SentintelShiftID;
import com.stanlytango.android.dontsleepmanager.databasestructure.Shift;
import com.stanlytango.android.dontsleepmanager.databasestructure.ShiftSettings;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.*;
import android.support.annotation.NonNull;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class ActMainGmailAuth extends BaseActivity
        implements EasyPermissions.PermissionCallbacks {
    GoogleAccountCredential mCredential;

    private TextView mOutputText;

    private Button mButtonSentinels;
    private Button mButtonSecuredZones;
    private Button mButtonSettings;
    private Button mButtonReport;
    private Button mButtonStartExchange;

    ProgressDialog mProgress;

    private static final String TAG = "#";

    Gmail.Users.Messages msgs;

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String BUTTON_TEXT = "Call Gmail API";
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = { GmailScopes.GMAIL_LABELS,
                                             GmailScopes.GMAIL_READONLY,
                                             GmailScopes.MAIL_GOOGLE_COM,
                                             GmailScopes.GMAIL_MODIFY
    };

    private final String DBSecuredZoneName = "securedzone";
    private final String DBSentintelName = "sentinel";
    static final String DBSentintelNm = "sentinel";
    private final String DBShiftSettingsName = "shiftsettings";
    private final String DBShiftName = "shift";
    private final String DBSentinelShiftIDName = "sentintel_shift_id";

  //  private static final Set<String> SCOPES = GmailScopes.all();


//    private static final Collection<String> SCOPES = Arrays.asList("https://www.googleapis.com/auth/userinfo.profile;https://www.googleapis.com/auth/userinfo.email;https://www.googleapis.com/auth/gmail.modify".split(";"));


    /**
     * Create the ActMainGmailAuth activity.
     * @param savedInstanceState previously saved instance data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_panel);

        mOutputText = (TextView) findViewById(R.id.output_tv);
        mOutputText.setPadding(16, 16, 16, 16);
        mOutputText.setVerticalScrollBarEnabled(true);
        mOutputText.setMovementMethod(new ScrollingMovementMethod());
        mOutputText.setText("TextView");

        // проверяем существует ли БД НАСТРОЕК смены
        // если нет, то создаем дефолтный элемент
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference dbRef = firebaseDatabase.getReference();
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.child(DBShiftSettingsName).exists()){
                    ShiftSettings shiftSettings = new ShiftSettings();
                    shiftSettings.addDefaultSetting(dbRef.child(DBShiftSettingsName));
                    Toast.makeText(getApplicationContext(),
                            " ЕВРИКА!!! ",Toast.LENGTH_LONG).show();
                } else {
                    Log.d(TAG,DBShiftSettingsName+" exists");
                    Toast.makeText(getApplicationContext(),
                            " ChildrenCount "+dataSnapshot.getChildrenCount(),Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

/////////// кнопка ОБМЕН ДАННЫМИ ////////////////////////////////////////////////////////
        mButtonStartExchange = (Button) findViewById(R.id.btn_start_exchange);
        mButtonStartExchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

        // получаем результат API из почтового ящика
                mButtonStartExchange.setEnabled(false);
                mOutputText.setText("");
                getResultsFromApi(); // !!!!!!!!!!!!!!!!!!!!!!!!
                mButtonStartExchange.setEnabled(true);
////////////////////////////////////////////////////////////////////////////////////////
        // =====================================================

                //ТЕСТируем
                // БД SentinelShiftId
                // допустим уже есть какая-то история, и теперь при нажатии DataExchange
                // должно произойти обновление текущей БД
                final SentintelShiftID sentintelShiftID = new SentintelShiftID();
                sentintelShiftID.addNewSentinelShiftID(dbRef.child(DBSentinelShiftIDName),"brooks",Arrays.asList("id11","id12","id13"));


        // #BC3 проверяем есть ли DBSentinel и DBSecZone для дальнейшего обмена данными
                // получаем  ссылку на базу данных
                final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

                   //  разовая проверка БД методом
                  // addListenerForSingleValueEvent
                dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(DBSecuredZoneName)){
                            if (dataSnapshot.hasChild(DBSentintelName)){
                                // все необходимые БД есть, можно двигаться дальше
                                Toast.makeText(getApplicationContext(),"All neccassary DB exist, data exchange can be continued",Toast.LENGTH_LONG).show();

 //ТЕСТируем
 // БД SentinelShiftId
 /**/
                            SentintelShiftID sentintelShiftID = new SentintelShiftID();
 /* получаем */             List<String> lst = sentintelShiftID.getShiftIDList(dataSnapshot, DBSentinelShiftIDName,"brooks");
                            lst.add("NEW_SHIFT_ID");
                            sentintelShiftID.addNewSentinelShiftID(dbRef.child(DBSentinelShiftIDName),"brooks",lst);

               /*  */       Log.d(TAG, "!!!!!!!!!!!!!!!!"+ lst.toString());


                            } else {
                                    Toast.makeText(getApplicationContext(),"You have to create at least one Sentinel",Toast.LENGTH_LONG).show();
                                }
                            } else
                               Toast.makeText(getApplicationContext(),
                                "You have to create at least one Secured Zone for monitoring",Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        // #BC3 проверили есть ли DBSentinel и DBSecZone ===================================

        // кнопка добавление Охраняемой Зоны
        mButtonSecuredZones = (Button) findViewById(R.id.btn_sec_zone);
        mButtonSecuredZones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference dbSecuredZoneRef = FirebaseDatabase.getInstance().getReference(DBSecuredZoneName);
                SecuredZone securedZone = new SecuredZone();
                // допустим у нас есть база данных SecuredZone
                securedZone.letsSayThereIsSecuredZoneDB(dbSecuredZoneRef);
            }
        });

        // кнопка добавление Охранника
        mButtonSentinels = (Button) findViewById(R.id.btn_sentinels);
        mButtonSentinels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ActSentinel.newIntent(getApplicationContext(),ActSentinel.class, true));

                DatabaseReference dbSentinelRef = FirebaseDatabase.getInstance().getReference(DBSentintelName);
                Sentinel sentinel = new Sentinel();

                // допустим у нас есть база данных Sentinel
                sentinel.letsSayThereIsSentinelDB(dbSentinelRef);

            }
        });

        // кнопка создать Отчет
        mButtonReport = (Button) findViewById(R.id.btn_report);
        mButtonReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Shift shift = new Shift();
                shift.letsSayThereIsShift(dbRef.child(DBShiftName));

            }
        });

        // кнопка Default Settings
        mButtonSettings = (Button) findViewById(R.id.btn_settings);
        mButtonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShiftSettings shiftSettings = new ShiftSettings();
                shiftSettings.writeNewSetting(dbRef.child(DBShiftSettingsName),
                        1, 2, 2, true, false);
            }
        });

        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Calling Gmail API ...");

        // Initialize credentials and service object.
        mCredential = GoogleAccountCredential
                .usingOAuth2(getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
    }


    /** ************************************************************************
     * Attempt to call the API, after verifying that all the preconditions are
     * satisfied. The preconditions are: Google Play Services installed, an
     * account was selected and the device currently has online access. If any
     * of the preconditions are not satisfied, the app will prompt the user as
     * appropriate.
     *
     * Попытка вызвать API, после проверки того, что все предварительные
     * условия выполнены. А именно: установлены службы Google Play,
     * выбрана учетная запись, и устройство имеет доступ в Интернет.
     * Если какое-либо из предварительных условий не выполняется, приложение
     * подскажет пользователю по мере необходимости.
     */
    private void getResultsFromApi() {
        if (! isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (! isDeviceOnline()) {
            mOutputText.setText("No network connection available.");
        } else {
            new MakeRequestTask(mCredential).execute();
        }
    }


    /**  **************************************************************************
     * Attempts to set the account used with the API credentials. If an account
     * name was previously saved it will use that one; otherwise an account
     * picker dialog will be shown to the user. Note that the setting the
     * account to use with the credentials object requires the app to have the
     * GET_ACCOUNTS permission, which is requested here if it is not already
     * present. The AfterPermissionGranted annotation indicates that this
     * function will be rerun automatically whenever the GET_ACCOUNTS permission
     * is granted.
     *
     * Попытка установить учетную запись, используемую с учетными данными API.
     * Если имя учетной записи было ранее сохранено, оно будет использовать ее;
     * в противном случае пользователю будет показан диалог выбора учетной записи.
     * Обратите внимание, что для настройки учетной записи, используемой для объекта
     * учетных данных, требуется, чтобы приложение имело разрешение GET_ACCOUNTS,
     * которое запрашивается здесь, если оно еще не присутствует.
     * Аннотации AfterPermissionGranted показывают, что эта функция будет повторно
     * запущена автоматически при предоставлении разрешения GET_ACCOUNTS.
     */
    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi();
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);

            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }


    /** *************************************************************************
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode code indicating the result of the incoming
     *     activity result.
     * @param data Intent (containing result data) returned by incoming
     *     activity result.
     *
     * Вызывается, когда действие, запущенное здесь (в частности, AccountPicker
     * и авторизация) завершается, предоставляя вам код запроса, с которого вы
     * его начали, возвращаемый resultCode и любые дополнительные данные из него.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    mOutputText.setText(
                            "This app requires Google Play Services. Please install " +
                                    "Google Play Services on your device and relaunch this app.");
                } else {
                    getResultsFromApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                        getResultsFromApi();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }
                break;
        }
    }


    /** ****************************************************************************
     * Respond to requests for permissions at runtime for API 23 and above.
     * @param requestCode The request code passed in
     *     requestPermissions(android.app.Activity, String, int, String[])
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }


    /** ****************************************************************************
     * Callback for when a permission is granted using the EasyPermissions library.
     * @param requestCode The request code associated with the requested permission
     * @param list The requested permission list. Never null.
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Do nothing.
    }


    /** ******************************************************************************
     * Callback for when a permission is denied using the EasyPermissions
     * library.
     * @param requestCode The request code associated with the requested
     *         permission
     * @param list The requested permission list. Never null.
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Do nothing.
    }


    /**  *****************************************************************************
     * Checks whether the device currently has a network connection.
     * @return true if the device has a network connection, false otherwise.
     */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }


    /** ****************************************************************************
     * Check that Google Play services APK is installed and up to date.
     * @return true if Google Play Services is available and up to
     *     date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }


    /** ***********************************************************************************
     * Attempt to resolve a missing, out-of-date, invalid or disabled Google
     * Play Services installation via a user dialog, if possible.
     *
     * Попытка устранить отсутствующий, устаревший, недействительный или
     * отключенный Google Play Services через пользовательский диалог.
     */
    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        final int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }


    /** ****************************************************************************
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     * @param connectionStatusCode code describing the presence (or lack of)
     *     Google Play Services on this device.
     *
     * Отображает диалоговое окно с ошибкой, показывающее, что сервис Google Play
     * отсутствует или устарел
     * *@param connectionStatusCode код, описывающий наличие (или отсутствие)
     *     Google Play Служб на этом устройстве
     */
    void showGooglePlayServicesAvailabilityErrorDialog(final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                ActMainGmailAuth.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }


    /** *******************************************************************************
     * An asynchronous task that handles the Gmail API call.
     * Placing the API calls in their own task ensures the UI stays responsive.
     *
     * Асинхронная задача, которая обрабатывает вызов API Gmail. Размещение вызовова API
     * в их собственной задаче гарантирует, что UI останется отзывчивым.
     */
    private class MakeRequestTask extends AsyncTask<Void, Void, List<com.google.api.services.gmail.model.Message>> {
        private com.google.api.services.gmail.Gmail mService = null;
        private Exception mLastError = null;

        MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.gmail.Gmail.Builder(transport, jsonFactory, credential)
                    .setApplicationName("Gmail API Android Quickstart")
                    .build();
        }

        /**
         * Background task to call Gmail API.
         * @param params no parameters needed for this task.
         */
        @Override
        protected void onPreExecute() {
            mOutputText.setText("");
            mProgress.show();
        }

        @Override
        protected List<com.google.api.services.gmail.model.Message> doInBackground(Void... params) {
            try {
                // получаем папки из user'a, конвертим их
                // в строковый список и ретёрним обратно
                return getMessagesList();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        /**
         * List all Messages of the user's mailbox matching the query.
         * @param mService Authorized Gmail API instance.
         * @param user User's email address. The special value "me"
         * can be used to indicate the authenticated user.
         * @param query String used to filter the Messages listed.
         * @param labelIds Only return Messages with these labelIds applied.
         * @throws IOException
         */
        private List<com.google.api.services.gmail.model.Message> getMessagesList() throws IOException {
            String user = "me";
            String query = "from:55555.gold@gmail.com is:unread";

            ListLabelsResponse listLabelsResponse =
                    mService.users().labels().list(user).execute();
            List<String> labelIds = new ArrayList<>();
            labelIds = Arrays.asList("INBOX");

            ListMessagesResponse listMessagesResponse = mService.users().messages()
                                                        .list(user)
                                                        //.setLabelIds(labelIds)
                                                        .setQ(query)
                                                        .execute();

            List<com.google.api.services.gmail.model.Message> messages = new ArrayList<>();

            while(listMessagesResponse.getMessages() != null){
                messages.addAll(listMessagesResponse.getMessages());
                if(listMessagesResponse.getNextPageToken() != null){
                    String pageToken = listMessagesResponse.getNextPageToken();

                    listMessagesResponse = mService.users().messages()
                                            .list(user)
                                           // .setLabelIds(labelIds)
                                            .setQ(query)
                                            .setPageToken(pageToken)
                                            .execute();
                } else {
                    break;
                }
            }

            for (com.google.api.services.gmail.model.Message message : messages)
                Log.d(TAG, message.toPrettyString()+"");

            return messages;
        }


        @Override
        protected void onPostExecute(List<com.google.api.services.gmail.model.Message> output) {
            mProgress.hide();
            if (output == null || output.size() == 0) {
                mOutputText.setText("No results returned.");
            } else {
                //try {
                    Toast.makeText(getApplicationContext(),
                            "List<Message> size = "+ output.size(),Toast.LENGTH_LONG).show();
                    int i = 0;
                    for (com.google.api.services.gmail.model.Message message : output){
                        Log.d(TAG, " # "+i++);
                        Log.d(TAG, ""+message.getSnippet());
                    }

                //} catch (IOException e) {e.printStackTrace();}

            }
        }

        // Обрабатываем разные исключения соответствующим уведомлением
        @Override
        protected void onCancelled() {
            mProgress.hide();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            ActMainGmailAuth.REQUEST_AUTHORIZATION);
                } else {
                    Log.e(TAG, "The following error occurred:\n"+mLastError.getMessage());
                    mOutputText.setText("The following error occurred:\n"
                            + mLastError.getMessage());
                }
            } else {
                mOutputText.setText("Request cancelled.");
            }
        }
    }
}
