package uqac.dim.projet_alarme_8inf257;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

/**
 * That's the activity called when an alarm is triggered. It will open the right mini-game activity after a click on the Button
 */
public class ResultActivity extends Activity {
    MediaPlayer mediaPlayer;
    private int minigameId;
    private int ringtoneId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.desactiver);

        minigameId = getIntent().getIntExtra("minigameID",0);
        Log.v("AlarmSet", "onCreate ResultatActivity : IDMG = " + minigameId);
        ringtoneId = getIntent().getIntExtra("ringtoneID", 0);


        Button dAlarme = (Button) findViewById(R.id.desactiverAlarme);
        dAlarme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                desactiver();
            }
        });
    }
    public void desactiver(){
        Log.v("AlarmSet", "Playing minigame : " + minigameId);
        Intent i;
        switch(minigameId){
            case 1:
                i = new Intent(this, MiniJeuxCalcul.class);
                break;
            case 2:
                i = new Intent(this, MiniJeuxCliqueActivity.class);
                break;
            case 3:
                i = new Intent(this, ShakeActivity.class);
                break;
            case 4:
                i = new Intent(this, PassphraseActivity.class);
                break;
            default:
                CommonMyMediaPlayer.player.stopMusic();
                i = new Intent(this, MainActivity.class);
                break;
        }
        startActivity(i);
    }
}
