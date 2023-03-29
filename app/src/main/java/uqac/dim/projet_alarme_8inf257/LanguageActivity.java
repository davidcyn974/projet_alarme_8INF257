package uqac.dim.projet_alarme_8inf257;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.Locale;

public class LanguageActivity extends Activity {
    TextView selectLangText;
    Button btnFrench, btnEnglish, btn1 ,btn2;
    Context context;
    Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lang_page);

        // referencing the text and button views
        selectLangText = findViewById(R.id.TitleTheme);
        btnFrench = findViewById(R.id.RadioButtonFrench);
        btnEnglish = findViewById(R.id.RadioButtonEnglish);
        btn1 = findViewById(R.id.AlarmeButton);
        btn2 = findViewById(R.id.ParametersButton);
        // setting up on click listener event over the button
        // in order to change the language with the help of
        // LocaleHelper class
        btnEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Locale locale = new Locale("en");
                Locale.setDefault(locale);
                Resources resources = LanguageActivity.this.getResources();
                Configuration config = resources.getConfiguration();
                config.setLocale(locale);
                resources.updateConfiguration(config, resources.getDisplayMetrics());
                finish();
                startActivity(getIntent());
            }
        });

        btnFrench.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Locale locale = new Locale("fr");
                Locale.setDefault(locale);
                Resources resources = LanguageActivity.this.getResources();
                Configuration config = resources.getConfiguration();
                config.setLocale(locale);
                resources.updateConfiguration(config, resources.getDisplayMetrics());
                finish();
                startActivity(getIntent());
            }
        });

    }
    public void alarme(View activity_main) {
        Intent intent = new Intent(LanguageActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
