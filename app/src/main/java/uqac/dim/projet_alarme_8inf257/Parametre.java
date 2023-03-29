package uqac.dim.projet_alarme_8inf257;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 *  the Activity representing the parameters of the application
 */

public class Parametre extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parametre);

        Button btnLang = (Button) findViewById(R.id.param_to_languages);
        btnLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Parametre.this, LanguageActivity.class);
                startActivity(intent);
            }
        });

        Button btnTheme = (Button) findViewById(R.id.param_to_themes);
        btnTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Parametre.this, ChangeThemeActivity.class);
                startActivity(intent);
            }
        });
    }
    public void alarme(View activity_main) {
        Intent intent = new Intent(Parametre.this, MainActivity.class);
        startActivity(intent);
    }


    public void theme(View theme) {
        Intent intent = new Intent(Parametre.this, ChangeThemeActivity.class);
        startActivity(intent);
    }

}

