package uqac.dim.projet_alarme_8inf257;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;

public class ThemeChanger {

    public static int loadTheme(Context ctx){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        int theme = sharedPreferences.getInt("Theme", Color.RED); //RED is default color, when nothing is saved yet
        return theme;
    }

    public static void saveTheme(int theme, Context ctx) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("Theme",theme);
    }
}
