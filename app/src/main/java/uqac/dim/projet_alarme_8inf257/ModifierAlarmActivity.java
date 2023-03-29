package uqac.dim.projet_alarme_8inf257;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;

import androidx.core.app.NotificationCompat;

import java.io.IOException;
import java.sql.SQLException;

/**
 *  Activity launched when the user wants the edit an alarm
 */
    public class ModifierAlarmActivity extends Activity {
        /* DATA of te Alarm */
        private int idRingtone = 0;
        private int idMiniGame = 0;
        private String hour = "00";
        private String minute = "00";
        private int idAlarm=1;
        private int[] week = new int[]{1, 1, 1, 1, 1, 1, 1}; // Monday -> Sunday
        private DBAlarmHandler db;


        /* STATIC NAME */
        static final String DATA = "data";
        static final String IDR = "idRingstone";
        static final String IDM = "idMiniGame";
        static final String HOUR = "hour";
        static final String MINUTE = "minute";
        static final int PICK_RINGTONE_REQUEST = 0;
        static final int PICK_MINIGAME_REQUEST = 1;

        private static final String TABLE_NAME_SAVED_ALARMS = "savedAlarms";
        private static final String ID_SAVED_ALARMS = "id";
        private static final String HOUR_SAVED_ALARMS = "hourSaved";
        private static final String ID_MINIGAME_SAVED_ALARMS = "idMiniGame";
        private static final String ID_RINGTONE_SAVED_ALARMS = "idRingtone";
        private static final String ENABLE_SAVED_ALARMS = "enable";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.creeralarme);

            // Get all of the Data
            idAlarm = this.getIntent().getIntExtra(DATA, idAlarm);
            minute = this.getIntent().getStringExtra(MINUTE);
            hour = this.getIntent().getStringExtra(HOUR);
            idRingtone = this.getIntent().getIntExtra(IDR, idRingtone);
            idMiniGame = this.getIntent().getIntExtra(IDM, idMiniGame);

            Log.v("DIM", idAlarm + "");

            // Create or Open the DataBase
            db = new DBAlarmHandler(this);
            try {
                db.createDatabase();

            } catch (IOException ioe) {

                throw new Error("Unable to create database");
            }

            try {
                db.openDatabase();

            }catch(SQLException sqle){
                Log.v("DIM", "Error" + sqle.getMessage());
            }

            /* BUTTONS */
            Button btnTest = (Button) findViewById(R.id.testAlarm);
            btnTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tester();
                }
            });
            Button btnSauvegarder = (Button) findViewById(R.id.saveAlarm);
            btnSauvegarder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sauvegarder();
                }
            });

            /*EDIT TEXTS */
            EditText txtH = (EditText) findViewById(R.id.creerAlarmHeure);
            txtH.setText(hour);
            txtH.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() != 0){
                        if((Integer.parseInt(s.toString()) >= 24)||(s.length()>2)){
                            txtH.setText("");
                        }
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {
                    // Verify if this has the Hour/Minute format
                    if((s.length() != 0) && (Integer.parseInt(s.toString()) < 24))
                        hour = s.toString();
                    if(s.length() == 1){
                        hour = "0" + hour;
                    }
                    Log.v("DIM",hour);
                }
            });

            EditText txtM = (EditText) findViewById(R.id.creerAlarmMinute);
            txtM.setText(minute);
            txtM.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() != 0){
                        if((Integer.parseInt(s.toString()) >= 60)||(s.length()>2)){
                            txtM.setText("");
                        }
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {
                    // Verify if this has the Hour/Minute format
                    if((s.length() != 0) && (Integer.parseInt(s.toString()) < 60))
                        minute = s.toString();
                    if(s.length() == 1){
                        minute = "0" + minute;
                    }
                    Log.v("DIM",minute);
                }
            });

            /* CHECK BOXES for the days of week */
            CheckedTextView CheckLundi = (CheckedTextView)findViewById(R.id.CheckLundi);
            CheckLundi.setChecked(true);
            CheckLundi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.v("DIM", "Lundi");
                    if(CheckLundi.isChecked()) {
                        Log.v("DIM", "Pas selectionné");
                        CheckLundi.setChecked(false);
                        CheckLundi.setTextColor(-16777216);
                        CheckLundi.setBackgroundColor(-1);
                    }else{
                        Log.v("DIM", "Selectionné");
                        CheckLundi.setChecked(true);
                        CheckLundi.setTextColor(-1);
                        CheckLundi.setBackgroundColor(-16776961);
                    }
                    modifyWeek(0, CheckLundi.isChecked());
                }
            });
            CheckedTextView CheckMardi = (CheckedTextView)findViewById(R.id.CheckMardi);
            CheckMardi.setChecked(true);
            CheckMardi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.v("DIM", "Mardi");
                    if(CheckMardi.isChecked()) {
                        Log.v("DIM", "Pas selectionné");
                        CheckMardi.setChecked(false);
                        CheckMardi.setTextColor(-16777216);
                        CheckMardi.setBackgroundColor(-1);
                    }else{
                        Log.v("DIM", "Selectionné");
                        CheckMardi.setChecked(true);
                        CheckMardi.setTextColor(-1);
                        CheckMardi.setBackgroundColor(-16776961);
                    }
                    modifyWeek(1, CheckLundi.isChecked());
                }
            });
            CheckedTextView CheckMercredi = (CheckedTextView)findViewById(R.id.CheckMercredi);
            CheckMercredi.setChecked(true);
            CheckMercredi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.v("DIM", "Mercredi");
                    if(CheckMercredi.isChecked()) {
                        Log.v("DIM", "Pas selectionné");
                        CheckMercredi.setChecked(false);
                        CheckMercredi.setTextColor(-16777216);
                        CheckMercredi.setBackgroundColor(-1);
                    }else{
                        Log.v("DIM", "Selectionné");
                        CheckMercredi.setChecked(true);
                        CheckMercredi.setTextColor(-1);
                        CheckMercredi.setBackgroundColor(-16776961);
                    }
                    modifyWeek(2, CheckLundi.isChecked());
                }
            });
            CheckedTextView CheckJeudi = (CheckedTextView)findViewById(R.id.CheckJeudi);
            CheckJeudi.setChecked(true);
            CheckJeudi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.v("DIM", "Jeudi");
                    if(CheckJeudi.isChecked()) {
                        Log.v("DIM", "Pas selectionné");
                        CheckJeudi.setChecked(false);
                        CheckJeudi.setTextColor(-16777216);
                        CheckJeudi.setBackgroundColor(-1);
                    }else{
                        Log.v("DIM", "Selectionné");
                        CheckJeudi.setChecked(true);
                        CheckJeudi.setTextColor(-1);
                        CheckJeudi.setBackgroundColor(-16776961);
                    }
                    modifyWeek(3, CheckLundi.isChecked());
                }
            });
            CheckedTextView CheckVendredi = (CheckedTextView)findViewById(R.id.CheckVendredi);
            CheckVendredi.setChecked(true);
            CheckVendredi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.v("DIM", "Vendredi");
                    if(CheckVendredi.isChecked()) {
                        Log.v("DIM", "Pas selectionné");
                        CheckVendredi.setChecked(false);
                        CheckVendredi.setTextColor(-16777216);
                        CheckVendredi.setBackgroundColor(-1);
                    }else{
                        Log.v("DIM", "Selectionné");
                        CheckVendredi.setChecked(true);
                        CheckVendredi.setTextColor(-1);
                        CheckVendredi.setBackgroundColor(-16776961);
                    }
                    modifyWeek(4, CheckLundi.isChecked());
                }
            });
            CheckedTextView CheckSamedi = (CheckedTextView)findViewById(R.id.CheckSamedi);
            CheckSamedi.setChecked(true);
            CheckSamedi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.v("DIM", "Samedi");
                    if(CheckSamedi.isChecked()) {
                        Log.v("DIM", "Pas selectionné");
                        CheckSamedi.setChecked(false);
                        CheckSamedi.setTextColor(-16777216);
                        CheckSamedi.setBackgroundColor(-1);
                    }else{
                        Log.v("DIM", "Selectionné");
                        CheckSamedi.setChecked(true);
                        CheckSamedi.setTextColor(-1);
                        CheckSamedi.setBackgroundColor(-16776961);
                    }
                    modifyWeek(5, CheckLundi.isChecked());
                }
            });
            CheckedTextView CheckDimanche = (CheckedTextView)findViewById(R.id.CheckDimanche);
            CheckDimanche.setChecked(true);
            CheckDimanche.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.v("DIM", "Dimanche");
                    if(CheckDimanche.isChecked()) {
                        Log.v("DIM", "Pas selectionné");
                        CheckDimanche.setChecked(false);
                        CheckDimanche.setTextColor(-16777216);
                        CheckDimanche.setBackgroundColor(-1);
                    }else{
                        Log.v("DIM", "Selectionné");
                        CheckDimanche.setChecked(true);
                        CheckDimanche.setTextColor(-1);
                        CheckDimanche.setBackgroundColor(-16776961);
                    }
                    modifyWeek(6, CheckLundi.isChecked());
                }
            });
        }

    /**
     *
     * @param jour day of the week (Monday = 0; Sunday = 6)
     * @param isChecked boolean if this day is checked (check box)
     */
        public void modifyWeek(int jour, boolean isChecked){
            week[jour] = isChecked ? 1 : 0;
        }

        public void creeralarme(View activity_main) {
            Intent intent = new Intent(ModifierAlarmActivity.this, uqac.dim.projet_alarme_8inf257.CreerAlarmActivity.class);
            startActivity(intent);
        }
        public void alarme(View activity_main) {
            Intent intent = new Intent(ModifierAlarmActivity.this, MainActivity.class);
            startActivity(intent);
        }
        public void parametre(View parametre) {
            Intent intent = new Intent(ModifierAlarmActivity.this, Parametre.class);
            startActivity(intent);
        }
        public void choixsonnerie(View choixsonnerie) {
            Intent intent = new Intent(ModifierAlarmActivity.this, ChoixSonnerieActivity.class);
            intent.putExtra(DATA, this.idRingtone);
            startActivityForResult(intent, PICK_RINGTONE_REQUEST);
        }
        public void desactivation(View desactivation) {
            Intent intent = new Intent(ModifierAlarmActivity.this, DesactivationActivity.class);
            intent.putExtra(DATA, this.idMiniGame);
            startActivityForResult(intent, PICK_MINIGAME_REQUEST);
        }

    /**
     *  Save the new alarm in the DataBase and launch the main activity
     */
    public void sauvegarder(){
            Log.v("DIM", "Hour chaged : " + this.hour + ":" + this.minute);

            ContentValues cv = new ContentValues();
            cv.put(HOUR_SAVED_ALARMS, this.hour + ":" + this.minute);
            cv.put(ID_RINGTONE_SAVED_ALARMS, idRingtone);
            cv.put(ID_MINIGAME_SAVED_ALARMS, idMiniGame);
            db.getMyDataBase().update(TABLE_NAME_SAVED_ALARMS, cv, ID_SAVED_ALARMS+" = ?", new String[]{String.valueOf(idAlarm)});
            Log.v("DIM", db.getDataDeLaBD());

            Log.v("DIM", "modification effecuée");
            Intent intent = new Intent(ModifierAlarmActivity.this, MainActivity.class);
            startActivity(intent);
        }

    /**
     *  allow the user to test the alarm (this alarm is saved as a deactivated alarm
     *  this will trigger the alarm as it should be triggered when the button is clicked
     */
    public void tester(){
            db.addNewAlarm(
                    this.hour + ":" + this.minute,
                    this.idMiniGame,
                    this.idRingtone,
                    0,
                    week);
            /* CREATE the notification */
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this.getApplicationContext(), "notify_001");
            Intent ii = new Intent(this.getApplicationContext(), ResultActivity.class);
            ii.putExtra("minigameID", idMiniGame);
            ii.putExtra("ringtoneID", idRingtone);
            Log.v("AlarmSet", "Intent : " + ii.getIntExtra("minigameID", 0));
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, ii, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
            bigText.setBigContentTitle("Test Alarme");
            bigText.setSummaryText("Alarme_8INF257");

            /* BUILD the notification */
            mBuilder.setContentIntent(pendingIntent);
            mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
            mBuilder.setContentTitle("Alarme");
            mBuilder.setContentText("AAaaAAaAAAaaaAAAAaaHh");
            mBuilder.setPriority(Notification.PRIORITY_MAX);
            mBuilder.setStyle(bigText);
            mBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
            mBuilder.setLights(Color.RED, 3000, 3000);
            mBuilder.setSound(Uri.parse("uri://sadfasdfasdf.mp3"));

            NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

// === Removed some obsoletes
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                String channelId = "Your_channel_id";
                NotificationChannel channel = new NotificationChannel(
                        channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_HIGH);
                mNotificationManager.createNotificationChannel(channel);
                mBuilder.setChannelId(channelId);
            }

            mNotificationManager.notify(0, mBuilder.build());

            MyMediaPlayer mmp = new MyMediaPlayer(idRingtone, this);
            Log.v("DIM", "***** CONTEXT : " + this);
            CommonMyMediaPlayer.player = mmp;
            mmp.execute((Void) null);
        }

    /**
     *  Receive the data from the two children-activities to set the alarm
     */
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            // call-back function
            Log.v("DIM", "INFOS received");
            super.onActivityResult(requestCode, resultCode, data);
            Log.v("DIM", "oui");

            if (requestCode == PICK_RINGTONE_REQUEST) { // choose ringtone activity
                if (resultCode == RESULT_OK) {
                    this.idRingtone = data.getIntExtra(DATA, this.idRingtone);
                    Log.v("DIM", "change Ringtone : "+data.getStringExtra(DATA));
                }
                else{Log.v("DIM", "failed");}
            }
            else if (requestCode == PICK_MINIGAME_REQUEST) {    //  choose minigame activity
                if (resultCode == RESULT_OK) {
                    this.idMiniGame = data.getIntExtra(DATA, this.idMiniGame);
                    Log.v("DIM", "change Minigame : "+data.getStringExtra(DATA));
                }
                else{Log.v("DIM", "failed");}
            }
            else{
                Log.v("DIM", "wrong pickup");
            }
            Log.v("DIM", "infos : " + hour + idRingtone + idMiniGame);
        }

        @Override
        protected void onPause() {
            db.close();
            super.onPause();
        }

        @Override
        protected void onResume() {
            try {
                db.openDatabase();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            super.onResume();
        }
    }
