package uqac.dim.projet_alarme_8inf257;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class Alarm {
    /*
    *   Class that represents any alarm stored in the Database
    *   Alarm could be set(), unset(), display()
     */

    private int id = 0;
    private String heure;
    private int idMiniGame;
    private int idRingtone;
    private int enable;
    private int[] week = new int[]{1, 1, 1, 1, 1, 1, 1}; // Sunday -> Saturday
    private Intent intentAlarm;
    private PendingIntent pending;
    BroadcastReceiver alarmReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Alarm worked", Toast.LENGTH_LONG).show();
        }
    };

    private DBAlarmHandler db;
    static final String DATA = "data";
    static final String IDR = "idRingstone";
    static final String IDM = "idMiniGame";
    static final String HOUR = "hour";
    static final String MINUTE = "minute";

    public Alarm(int id, String h, int i_mg, int i_r, int e, int[] w, DBAlarmHandler db){
        this.id = id;
        this.heure = h;
        this.idMiniGame = i_mg;
        this.idRingtone = i_r;
        this.enable = e;
        this.db = db;
        this.week = w;

        this.intentAlarm=new Intent(Intent.ACTION_VIEW);
        this.intentAlarm.setData(Uri.parse("http://www.games-workshop.com")); // default in case of it did not work, enjoy GW
    }

    public int getId() {
        return id;
    }

    public String getHeure() {
        return heure;
    }

    public int getIdMiniGame() {
        return idMiniGame;
    }

    public int getIdRingtone() {
        return idRingtone;
    }

    public int getEnable() {
        return enable;
    }

    /**
     * This will generates the linear layout to show to the user this alarm
     * @param ctx
     * @return the LinerLayout representing one alarm
     */
    @SuppressLint("ResourceType")
    public LinearLayout display(Context ctx){
        LinearLayout res = new LinearLayout(ctx);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 20);
        res.setLayoutParams(lp);
        res.setOrientation(LinearLayout.VERTICAL);
        res.setPadding(20,20,20,20);

        /* SWITCH */
        Switch s = new Switch(ctx);
        s.setChecked(this.enable != 0);
        LinearLayout.LayoutParams lps = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        s.setLayoutParams(lps);
        s.setText(heure);
        s.setTextColor(Color.BLACK);
        s.setTextSize(50);
        s.setPadding(10*2, 10*2, 10*2, 0);
        s.setElevation(5*2);

        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                changeEnable(isChecked, ctx);
            }
        });

        /* INFOS */
        TextView tv = new TextView(ctx);
        LinearLayout.LayoutParams lpt = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lpt.setMargins(0, 0, 0, 20);
        tv.setLayoutParams(lpt);
        String txt = ctx.getString(R.string.Ringtone) +" : " + this.idRingtone +" | "+ ctx.getString(R.string.MiniGame)+ " : " + this.idMiniGame + " | ";
        String[] w = new String[]{"S", "M", "T", "W", "T", "F", "S"};

        for(int ind = 0; ind < 7; ind++){
            if (week[ind] !=0){
                txt += w[ind];
            }
        }

        tv.setText(txt);
        tv.setTextColor(Color.BLACK);

        LinearLayout.LayoutParams llpbtn = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1);

        LinearLayout lbtn = new LinearLayout(ctx);
        lbtn.setLayoutParams(llpbtn);
        lbtn.setOrientation(LinearLayout.HORIZONTAL);

        /* BUTTONS */
        Button btnM = new Button(ctx);
        btnM.setLayoutParams(llpbtn);
        btnM.setText(R.string.Edit);
        btnM.setBackground(ctx.getDrawable(R.xml.boutonsave));
        btnM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickToModify(ctx);
            }
        });

        Button btnS = new Button(ctx);
        btnS.setLayoutParams(llpbtn);
        btnS.setText(R.string.Delete);
        btnS.setBackground(ctx.getDrawable(R.xml.boutonsave));
        btnS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAlarm(ctx);
            }
        });

        lbtn.addView(btnM);
        lbtn.addView(btnS);

        res.addView(s);
        res.addView(tv);
        res.addView(lbtn);
        res.setBackground(ctx.getDrawable(R.xml.contenu));
        return res;
    }

    /**
     * When you click on modify button
     * @param ctx
     */
    public void clickToModify(Context ctx){
        Intent intent = new Intent(ctx, ModifierAlarmActivity.class);
        intent.putExtra(DATA, this.id);
        intent.putExtra(IDR, this.idRingtone);
        intent.putExtra(IDM, this.idMiniGame);
        intent.putExtra(HOUR, this.heure.substring(0, 2));
        intent.putExtra(MINUTE, this.heure.substring(3, 5));
        ctx.startActivity(intent);
    }

    public void deleteAlarm(Context ctx){
        db.deleteByID(id);
        Intent intent = new Intent(ctx, MainActivity.class);
        ctx.startActivity(intent);
        this.unsetAlarm(ctx);
    }

    /**
     * This will communicate with the DataBase to (un)set this alarm
     * @param enable either if we should set or unset this alarm
     * @param ctx Context where this alarm is (un)set
     */
    public void changeEnable(boolean enable, Context ctx){
        if (enable){
            this.enable = 1;
            this.db.enableByID(this.id);    // call to db
            this.setAlarm(ctx);
        }
        else {
            this.enable = 0;
            this.db.disableByID(this.id);   // call to db
            this.unsetAlarm(ctx);
        }
    }

    /**
     *
     * @param ctx Context where this alarm is update (will set or unset this alarm)
     */
    public void updateAlarmStatus(Context ctx){
        if(this.enable == 0){
            this.unsetAlarm(ctx);
        }
        else{
            this.setAlarm(ctx);
        }
    }

    /**
     * This will set the alarm at a precise date in the futur depending of the day in the week
     * @param ctx the Context where this object has been set
     */
    public void setAlarm(Context ctx){
        Log.v("DIM", "Alarm set");
        AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);

        /*  Creation of the notification   */
        Intent intent = new Intent(ctx, AlarmReciever.class);
        Log.v("DIM", "IDMG : " + idMiniGame);
        intent.putExtra("minigameID", idMiniGame);
        intent.putExtra("ringtoneID", idRingtone);
        intent.putExtra("data", this.toString());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        int time = 3600*Integer.parseInt(this.heure.substring(0, 2)) + 60*Integer.parseInt(this.heure.substring(3, 5))
                - 3600*Calendar.getInstance().getTime().getHours() - 60*Calendar.getInstance().getTime().getMinutes() - Calendar.getInstance().getTime().getSeconds();

        /*  Compute the timer to set the alarm  */
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int index = 0;
        boolean nextDayFound = false;

        int ind_max = 7;
        int corr = 0;

        if (time <0){   // positive time
            time += 24*60*60;
            index = 1;
            ind_max = 8;
            corr = 1;
        }

        while((!nextDayFound) && (index<ind_max)){  // compute the number of days to delay
            if(week[(day-1 + index)%7] != 0){
                nextDayFound = true;
            }
            else{
                index++;
            }
        }

        time += 24*60*60 * (index-corr);
        Log.v("AlarmSet","Alarm set in " + time + "s");

        am.setExact( AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000*time , pendingIntent );
    }

    /**
     * In order to unset this alarm in the AlarmManager
     * @param ctx the Context where this object has been unset
     */
    public void unsetAlarm(Context ctx){
        if (pending != null) {
            ctx.unregisterReceiver(alarmReceiver);
            AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pending);
            Log.v("DIM", "Alarm unset");
        }
    }

    public String toString(){
        return "Alarme : " + heure;
    }
}
