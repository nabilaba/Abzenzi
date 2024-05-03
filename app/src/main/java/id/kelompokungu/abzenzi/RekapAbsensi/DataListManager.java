package id.kelompokungu.abzenzi.RekapAbsensi;

import android.widget.*;
import androidx.appcompat.app.*;
import id.kelompokungu.abzenzi.*;
import id.kelompokungu.abzenzi.Utils.*;
import java.text.*;
import java.util.*;

public class DataListManager
{
    private ArrayList<String> dlSiswa;
    private CustomAdapter adapter;
    private AppCompatActivity activity;
	private DataController dcSiswa, dcRekap;
	private String folderKelas;
	
    public DataListManager(AppCompatActivity activity, ListView listView) {
        this.activity = activity;
		init(listView);
		loadDataFromStorage();
    }
	
	private void init(ListView listView) {
		dlSiswa = new ArrayList<>();
        adapter = new CustomAdapter(activity, dlSiswa, this);
        listView.setAdapter(adapter);
        
		folderKelas = activity.getResources().getString(R.string.PATH_FOLDER);
		String fileKelas = activity.getResources().getString(R.string.FILE_KELAS);
		
		dcSiswa = new DataController(activity, folderKelas, fileKelas);
		dcRekap = new DataController(activity, folderKelas, TimeUtil.getDateFormat() + ".txt");
	}
	
	public void setDateData(String format) {
		dcRekap = new DataController(activity, folderKelas, format + ".txt");
		adapter.setDateData(format);
		adapter.notifyDataSetChanged();
	}

    private void loadDataFromStorage() {
        dlSiswa.clear();
		dlSiswa.addAll(dcSiswa.loadData());
        Collections.sort(dlSiswa);
        adapter.notifyDataSetChanged();
    }

    public void addData(String data) {
        if (!data.trim().isEmpty()) {
            if (!dlSiswa.contains(data)) {
                adapter.notifyDataSetChanged();
                dcSiswa.saveData(data);
            } else {
                CustomNotif.showSnackbar(activity, "Data telah ditambahkan sebelumnya");
            }
        } else {
            CustomNotif.showSnackbar(activity, "Masukkan setidaknya 1 kata!");
        }
    }

    public void removeData(String data) {
        adapter.notifyDataSetChanged();
		dcRekap.removeData(data);
    }

    public void clearData() {
        adapter.notifyDataSetChanged();
		dcRekap.clearData();
    }
}
