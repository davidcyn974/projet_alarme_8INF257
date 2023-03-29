package uqac.dim.projet_alarme_8inf257;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 *  We hope you're awake enough, it's time to do some basic math
 */
public class MiniJeuxCalcul extends Activity {
    private int nombre1 = 0;
    private int nombre2 = 0;
    private  int operateur = 0;
    private int resultat = 0;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.minijeuxcalcul);
        commencer();
        Button commencer = (Button) findViewById(R.id.commencer);
        commencer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valider();
                score();

            }
        });
    }

    /**
     * create an equation to solve and store the result
     */
    public void commencer(){
        TextView calcul = (TextView) findViewById(R.id.CalculTextView);
        Random random = new Random();
        operateur = random.nextInt(3);
        if(operateur == 0){
            Log.v("DIM", "Addition");
            nombre1 = random.nextInt(100);
            nombre2 = random.nextInt(100);
            resultat = nombre1+nombre2;
            calcul.setText(nombre1+" + "+nombre2);
        }if (operateur == 1){
            Log.v("DIM", "Soustraction");
            nombre1 = random.nextInt(100);
            nombre2 = random.nextInt(100);
            if(nombre2>nombre1){
                int changenombre = nombre2;
                nombre2 = nombre1;
                nombre1 = changenombre;
            }
            resultat = nombre1-nombre2;
            calcul.setText(nombre1+" - "+nombre2);
        }if(operateur == 2){
            Log.v("DIM", "Multiplication");
            nombre1 = random.nextInt(10);
            nombre2 = random.nextInt(10);
            resultat = nombre1*nombre2;
            calcul.setText(nombre1+" * "+nombre2);
        }
    }

    /**
     *  score > 5 -> you won, it stops the alarm
     */
    public void score(){
        TextView scoreTextView = (TextView) findViewById(R.id.scoreTextView);
        scoreTextView.setText(score+"/5");
        if(score>5){
            CommonMyMediaPlayer.player.stopMusic();

            Toast.makeText(getApplicationContext(), "Well done ;)", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    /**
     * this will verify the answer and launch a new round if the score is under or equal to 5
     */
    public void valider(){
        EditText reponse = (EditText) findViewById(R.id.reponse);
        String numero = reponse.getText().toString();
        String result = Integer.toString(resultat);
        Log.v("DIM", result);
        Log.v("DIM", numero);
        TextView calcul = (TextView) findViewById(R.id.CalculTextView);
        Timer t = new Timer();
        if(result.equals(numero)){  // Great job + timed green answer
            Log.v("DIM", "Bonne réponse");
            calcul.setBackgroundColor(-16711936);
            TimerTask task = new TimerTask() {
                public void run()
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            calcul.setBackgroundColor(getColor(R.color.theme_default_principal));
                            reponse.setText("");
                            commencer();
                            score+=1;
                            score();
                        }
                    });

                }
            };
            t.schedule(task, 1000);
        }
        else{  // Bad answer + timed red answer
            Log.v("DIM", "Mauvaise réponse");
            calcul.setBackgroundColor(-65536);
            TimerTask task = new TimerTask() {
                public void run()
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            calcul.setBackgroundColor(getColor(R.color.theme_default_principal));
                            reponse.setText("");
                            commencer();
                        }
                    });

                }
            };
            t.schedule(task, 1000);
        }

    }

}

