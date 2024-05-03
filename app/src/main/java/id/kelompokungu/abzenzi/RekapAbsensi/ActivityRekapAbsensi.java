package id.kelompokungu.abzenzi.RekapAbsensi;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.*;
import id.kelompokungu.abzenzi.*;
import id.kelompokungu.abzenzi.Utils.*;
import java.text.*;
import java.util.*;

import androidx.appcompat.app.AlertDialog;

public class ActivityRekapAbsensi extends BaseActivity
{
	private ListView listView;
	private DataListManager dataManager;
	private Calendar selectedDate;
	private TextView tanggal;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ra);
		CustomToolbar.Toolbar(this, "Rekap Absensi");

        listView = findViewById(R.id.list_view);
		dataManager = new DataListManager(this, listView);
		Button setTanggal = findViewById(R.id.button_tanggal);
        setTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
		
		tanggal = findViewById(R.id.tanggal);
		tanggal.setText("Tanggal: " + TimeUtil.getDateFormat());
    }
	
	private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year, month, day;
        if (selectedDate != null) {
            year = selectedDate.get(Calendar.YEAR);
            month = selectedDate.get(Calendar.MONTH);
            day = selectedDate.get(Calendar.DAY_OF_MONTH);
        } else {
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
			new DatePickerDialog.OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					selectedDate = Calendar.getInstance();
					selectedDate.set(Calendar.YEAR, year);
					selectedDate.set(Calendar.MONTH, monthOfYear);
					selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
					
					String format = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
					tanggal.setText("Tanggal: " + format);
					dataManager.setDateData(format);
				}
			}, year, month, day);
        datePickerDialog.show();
    }
	
	private void handleClear()
	{
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reset Data");
        builder.setMessage("Data yang sudah terhapus tidak akan bisa dikembalikan lagi! Apakah Anda yakin?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					dataManager.clearData();
					dialog.dismiss();
				}
			});
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.dismiss();
				}
			});
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
	
	@Override
    public boolean onSupportNavigateUp()
	{
        onBackPressed();
        return super.onSupportNavigateUp();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.rekap_absen, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.reset:
				handleClear();
				return true;
			case R.id.menu_exit:
				Action.showExitConfirmationDialog(this);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
