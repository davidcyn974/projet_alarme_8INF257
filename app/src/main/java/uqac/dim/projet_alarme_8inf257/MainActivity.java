package uqac.dim.projet_alarme_8inf257;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    private DBAlarmHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Create or Open the DataBase */
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

        Log.v("DIM", "YO");
        Log.v("DIM", db.getDataDeLaBD());
        Log.v("DIM", "HEY");

        /* BUTTONS */
        Button btnCreerAlarme = (Button) findViewById(R.id.main_to_creeralarme);
        btnCreerAlarme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreerAlarmActivity.class);
                startActivity(intent);
            }
        });

        Button btnParametre = (Button) findViewById(R.id.main_to_parametre);
        btnParametre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Parametre.class);
                startActivity(intent);
            }
        });

        /* DISPLAY the alarms in the DataBase */
        db.getDataDeLaBD();
        db.selectAllAlarms();
        displayAlarms();
    }

    /**
     *  This will add the display of all the alarm stored in the DataBase (Linear Layout edited with all the linearLayout of each alarm)
     */
    public void displayAlarms(){
        LinkedList<Alarm> allAlarms = db.selectAllAlarms();
        LinearLayout ll = (LinearLayout) findViewById(R.id.show_alarms);
        for (Alarm elt : allAlarms){
            ll.addView(elt.display(this));
            elt.updateAlarmStatus(this);
        }
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