package uqac.dim.projet_alarme_8inf257;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;

/**
 *  Used to play and stop music in an asynchronous way
 */
public class MyMediaPlayer extends AsyncTask<Void, Void, Void> {
    private static final String ACTION_PLAY = "com.example.action.PLAY";
    private MediaPlayer mediaPlayer = null;
    private int ringtoneID = 0;
    private Context ctx = null;

    public MyMediaPlayer(int ringtoneId, Context context){
        super();
        this.ringtoneID = ringtoneId;
        this.ctx = context;
    }

    /**
     *  Music is ON !
     */
    public void setMusic() {
        switch (ringtoneID) {   // there is only two Ringtone for this version
            case 1:
                mediaPlayer = MediaPlayer.create(ctx, R.raw.ringtone1);
                mediaPlayer.start();
                break;
            case 2:
                mediaPlayer = MediaPlayer.create(ctx, R.raw.ringtone2);
                mediaPlayer.start();
                break;
            /*case X:
                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.ringtoneX);
                break;*/
            default:
                break;
        }
        mediaPlayer.setLooping(true);
    }

    /**
     *  Music is OFF !
     */
    public void stopMusic() {
        mediaPlayer.stop();
    }

    /**
     *  Is Asynchronous then we must have the description of doInBackground
     */
    @Override
    protected Void doInBackground(Void... voids) {
        setMusic();
        return null;
    }
}

