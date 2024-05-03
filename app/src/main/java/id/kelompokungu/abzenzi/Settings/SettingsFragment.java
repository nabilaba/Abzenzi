package id.kelompokungu.abzenzi.Settings;

import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.widget.*;
import androidx.preference.*;
import id.kelompokungu.abzenzi.*;
import id.kelompokungu.abzenzi.Utils.*;
import java.io.*;
import java.util.zip.*;

import id.kelompokungu.abzenzi.R;
import java.util.*;
import android.text.format.*;

public class SettingsFragment extends PreferenceFragmentCompat
{
	private static final int REQUEST_CODE_PICK_BACKUP_FILE = 1001;
	private static final int CREATE_DOCUMENT_REQUEST_CODE = 1002;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey)
	{
        setPreferencesFromResource(R.xml.preferences, rootKey);

		Preference jamMasukPref = findPreference("jam_masuk");
		String jamMasuk = HelperPreference.LoadPreference(requireContext(), "jam_masuk", "07:00");
		jamMasukPref.setSummary(jamMasuk);
		jamMasukPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(Preference preference) {
					showTimePickerDialog(preference);
					return true;
				}
			});
		EditTextPreference ucapanPref = findPreference("ucapan");
		String ucapan = HelperPreference.LoadPreference(requireContext(), "ucapan", "Selamat belajar");
		ucapanPref.setSummary(ucapan);
		ucapanPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
				@Override
				public boolean onPreferenceChange(Preference preference, Object newValue)
				{	
					HelperPreference.SavePreference(requireContext(), "ucapan", (String) newValue);
					preference.setSummary((CharSequence) newValue);
					return true;
				}	
			});

		ListPreference emotPref = findPreference("emot");
		emotPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
				@Override
				public boolean onPreferenceChange(Preference preference, Object newValue)
				{
					HelperPreference.SavePreference(requireContext(), "emot", (String) newValue);
					return true;
				}			
			});

        ListPreference themePref = findPreference("theme");
        themePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
				@Override
				public boolean onPreferenceChange(Preference preference, Object newValue)
				{
					HelperPreference.SavePreference(requireContext(), "theme", (String) newValue);
					return true;
				}
			});

		Preference exitPref = findPreference("exit");
		exitPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(Preference p1)
				{
					Action.showExitConfirmationDialog(getActivity());
					return false;
				}
			});

		Preference backupPref = findPreference("backup");
        backupPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(Preference preference)
				{

					Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
					intent.addCategory(Intent.CATEGORY_OPENABLE);
					intent.setType("application/zip");
					intent.putExtra(Intent.EXTRA_TITLE, BackupManager.getDefaultName());
					startActivityForResult(intent, CREATE_DOCUMENT_REQUEST_CODE);
					return true;
				}
			});
		Preference restorePref = findPreference("restore");
        restorePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(Preference preference)
				{
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
					intent.setType("*/*");
					startActivityForResult(intent, REQUEST_CODE_PICK_BACKUP_FILE);
					return true;
				}
			});
    }

	private void showTimePickerDialog(final Preference pref) {
      	String savedTime = HelperPreference.LoadPreference(requireContext(), "jam_masuk", "07:00");
        int hour = Integer.parseInt(savedTime.split(":")[0]);
        int minute = Integer.parseInt(savedTime.split(":")[1]);
		
        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(),
			new TimePickerDialog.OnTimeSetListener() {
				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					String selectedTime = String.format("%02d:%02d", hourOfDay, minute);
					HelperPreference.SavePreference(requireContext(), "jam_masuk", selectedTime);
					if (pref != null) {
						pref.setSummary(selectedTime);
					}
				}
			}, hour, minute, DateFormat.is24HourFormat(requireContext()));
        timePickerDialog.show();
    }
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, final Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_PICK_BACKUP_FILE)
		{
			if (resultCode == Activity.RESULT_OK)
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("Terapkan cadangan?");
				builder.setMessage("Semua data yang lama akan tergantikan oleh data yang baru?");
				builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							if (data != null && data.getData() != null)
							{
								Uri uri = data.getData();
								File tempFile = RestoreManager.createTemp(getContext(), uri);
								if (tempFile != null)
								{
									RestoreManager.restoreData(getContext(), tempFile);
									RestoreManager.deleteTempFile(tempFile);
								}
							}
						}
					});
				builder.setNegativeButton("No", null);
				AlertDialog dialog = builder.create();
				dialog.show();
			}
			else if (resultCode == Activity.RESULT_CANCELED)
			{
				CustomNotif.showToast(getContext(), "Pemulihan dibatalkan");
			}
		}

		if (requestCode == CREATE_DOCUMENT_REQUEST_CODE)
		{
            if (resultCode == Activity.RESULT_OK)
			{
                if (data != null && data.getData() != null)
				{
                    Uri uri = data.getData();
                    BackupManager.backupData(getContext(), uri);
                }
            }
			else if (resultCode == Activity.RESULT_CANCELED)
			{
                CustomNotif.showToast(getContext(), "Pencadangan Dibatalkan");
            }
        }
	}

}

