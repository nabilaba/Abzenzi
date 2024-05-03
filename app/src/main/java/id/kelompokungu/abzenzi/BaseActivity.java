package id.kelompokungu.abzenzi;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

public class BaseActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setNightMode();
    }

    @Override
    protected void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
		setNightMode();
    }

    @Override
    protected void onPause() {
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }
	
    private void setNightMode() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String mode = prefs.getString("theme", "system");
        int nightMode = AppCompatDelegate.MODE_NIGHT_NO;
        switch (mode) {
            case "light":
                nightMode = AppCompatDelegate.MODE_NIGHT_NO;
                break;
            case "dark":
                nightMode = AppCompatDelegate.MODE_NIGHT_YES;
                break;
        }
        getDelegate().setLocalNightMode(nightMode);
    }

	@Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("theme")) {
            recreate();
        }
    }
}
