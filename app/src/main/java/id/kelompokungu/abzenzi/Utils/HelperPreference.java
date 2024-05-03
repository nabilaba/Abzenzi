package id.kelompokungu.abzenzi.Utils;

import android.content.*;
import androidx.preference.*;

public class HelperPreference
{
	public static void SavePreference(Context context, String key, String newValue) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(key, newValue);
		editor.apply();
	}
	
	public static String LoadPreference(Context context, String key, String defaultValue){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(key, defaultValue);
	}
}
