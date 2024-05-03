package id.kelompokungu.abzenzi.ManajemenKelas;

import android.content.*;
import android.os.*;
import android.text.*;
import android.view.*;
import android.view.inputmethod.*;
import android.widget.*;
import androidx.appcompat.app.*;
import id.kelompokungu.abzenzi.*;
import id.kelompokungu.abzenzi.Utils.*;
import com.google.android.material.floatingactionbutton.*;
import id.kelompokungu.abzenzi.Settings.*;

public class ActivityManajemenKelas extends BaseActivity
{
	private ListView listView;
	private DataListManager dataManager;
	private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
		setTheme(R.style.AppTheme);
        setContentView(R.layout.mk);
		CustomToolbar.Toolbar(this, "Manajemen Kelas");
        listView = findViewById(R.id.list_view);
		dataManager = new DataListManager(this, listView);

		fab = findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View p1)
				{
					showAddItemDialog();
				}	
			});
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

	private void showAddItemDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = getLayoutInflater();
		View dialogView = inflater.inflate(R.layout.dialog_mk, null);
		builder.setView(dialogView);

	    final EditText name = dialogView.findViewById(R.id.name);
		Button add = dialogView.findViewById(R.id.add);
		Button cancel = dialogView.findViewById(R.id.cancel);
		final AlertDialog dialog = builder.create();

		add.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v)
				{
					String data = name.getText().toString().trim();
					if (!data.isEmpty())
					{
						dataManager.addData(data);
						dialog.dismiss();
					}
					else
						CustomNotif.showToast(getApplicationContext(), "Mohon melengkapi formulir");
				}
			});
		cancel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View p1)
				{
					dialog.dismiss();
				}			
			});
		dialog.show();
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
		getMenuInflater().inflate(R.menu.manajemen_menu, menu);
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
