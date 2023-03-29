package uqac.dim.projet_alarme_8inf257;

import static uqac.dim.projet_alarme_8inf257.ThemeChanger.loadTheme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

// Not used :(
public class ChangeThemeActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int theme = loadTheme(this);        //Load your theme here!!!!
        //CustomizationProcess.onActivityCreateSetTheme(this, theme);
        setContentView(R.layout.changetheme);

        Button btnMain = (Button) findViewById(R.id.theme_to_alarms);
        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangeThemeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
