package id.kelompokungu.abzenzi.Settings;

import android.os.*;
import id.kelompokungu.abzenzi.*;
import id.kelompokungu.abzenzi.Utils.*;

public class ActivitySettings extends BaseActivity
{
	@Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
		CustomToolbar.Toolbar(this, "Setelan");
		
		getSupportFragmentManager().beginTransaction()
			.replace(R.id.settings_container, new SettingsFragment())
			.commit();
	}
	
	@Override
    public boolean onSupportNavigateUp()
	{
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
