package uqac.dim.projet_alarme_8inf257;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 *  100 clicks mini-game
 */
public class MiniJeuxCliqueActivity extends Activity {
    private final int NOMBRE = 100;
    private int cliqueRestant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.minijeuxclique);

       Button soustraire = findViewById(R.id.soustraire);
        cliqueRestant = NOMBRE;
        soustraire.setText(String.valueOf(cliqueRestant));

        soustraire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decrementer();
            }
        });
    }

    /**
     * this will count the number of clicks
     */
    private void decrementer() {
        cliqueRestant --;
        if (cliqueRestant > 0) {
            ((Button) findViewById(R.id.soustraire)).setText(String.valueOf(cliqueRestant));
        }
        else {
            CommonMyMediaPlayer.player.stopMusic();

            Toast.makeText(getApplicationContext(), "Well done ;)", Toast.LENGTH_LONG).show();

            Intent i = new Intent(MiniJeuxCliqueActivity.this, MainActivity.class);
            Bundle bundle  = ActivityOptions.makeSceneTransitionAnimation(MiniJeuxCliqueActivity.this).toBundle();
            startActivity(i,bundle);
        }
    }
}
