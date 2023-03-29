package uqac.dim.projet_alarme_8inf257;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

/**
 *  Activity to set the way of deactivation of an alarm
 */
public class DesactivationActivity extends Activity {
    private int selectedMiniGame;
    private int idRingtone = 0;

    static final String DATA = "data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.desactivation);

        this.idRingtone = getIntent().getIntExtra(DATA, this.idRingtone);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.MiniGameChoice);
        switch(idRingtone){ // if there is a default option or if an option has already been chosen
            case 0:
                radioGroup.check(R.id.MG0);
                break;
            case 1:
                radioGroup.check(R.id.MG1);
                break;
            case 2:
                radioGroup.check(R.id.MG2);
                break;
            case 3:
                radioGroup.check(R.id.MG3);
                break;
            case 4:
                radioGroup.check(R.id.MG4);
                break;
            default:
        }

        // Listener for the radio buttons
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int i;
                switch(checkedId){
                    case R.id.MG4:
                        i = 4;
                        Log.v("DIM", "change minigameid to : "+i);
                        break;
                    case R.id.MG3:
                        i = 3;
                        Log.v("DIM", "change minigameid to : "+i);
                        break;
                    case R.id.MG2:
                        i = 2;
                        Log.v("DIM", "change minigameid to : "+i);
                        break;
                    case R.id.MG1:
                        i = 1;
                        Log.v("DIM", "change minigameid to : "+i);
                        break;
                    case R.id.MG0:
                    default:
                        i=0;
                        Log.v("DIM", "change minigameid to : "+i);
                }
                setSelectedMiniGame(i);
            }
        });

        Button btn = (Button) findViewById(R.id.OkMiniGame);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendInfos();
            }
        });

    }

    public void setSelectedMiniGame(int selectedMiniGame) {
        this.selectedMiniGame = selectedMiniGame;
    }

    public void desactivation(View creeralarme) {
        Intent intent = new Intent(DesactivationActivity.this, ChoixSonnerieActivity.class);
        startActivity(intent);
    }
    public void alarme(View activity_main) {
        Intent intent = new Intent(DesactivationActivity.this, MainActivity.class);
        startActivity(intent);
    }
    public void parametre(View parametre) {
        Intent intent = new Intent(DesactivationActivity.this, Parametre.class);
        startActivity(intent);
    }


    /**
     *  gives the information back to the modification/creation of the alarm
     */
    public void sendInfos(){
        Intent res = new Intent();
        res.putExtra(DATA, selectedMiniGame);
        setResult(RESULT_OK, res);
        finish();
    }

}