package uqac.dim.projet_alarme_8inf257;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReciever extends BroadcastReceiver {
    private MediaPlayer mediaPlayer;
    private int ringtoneId;
    private Context ctx;

    /**
     * Called when receive an alarm, it will create the notification to call the resultActivity (Mini-Game)
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.v("DIM", "Alarm triggered");

        NotificationManager mNotificationManager;

        /* CREATE THE NOTIFICATION */
        int minigameId= intent.getIntExtra("minigameID",0);
        int ringtoneId= intent.getIntExtra("ringtoneID",0);
        Log.v("AlarmSet", "ID_MG_AlarmReceiver : " + minigameId +" | "+ringtoneId);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context.getApplicationContext(), "notify_001");
        Intent ii = new Intent(context.getApplicationContext(), ResultActivity.class);
        ii.putExtra("minigameID", minigameId);
        ii.putExtra("ringtoneID", ringtoneId);
        Log.v("AlarmSet", "Intent : " + ii.getIntExtra("minigameID", 0));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, ii, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.setBigContentTitle(intent.getStringExtra("data"));
        bigText.setSummaryText("Alarme_8INF257");

        /* BUILD NOTIFICATION */
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
        mBuilder.setContentTitle("Alarme");
        mBuilder.setContentText("AAaaAAaAAAaaaAAAAaaHh");
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setStyle(bigText);
        mBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        mBuilder.setLights(Color.RED, 3000, 3000);
        mBuilder.setSound(Uri.parse("uri://sadfasdfasdf.mp3"));

        mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

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

        MyMediaPlayer mmp = new MyMediaPlayer(ringtoneId, context);
        Log.v("DIM", "***** CONTEXT : " + context);
        CommonMyMediaPlayer.player = mmp;
        mmp.execute((Void) null);
    }

}
