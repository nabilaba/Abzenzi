package id.kelompokungu.abzenzi.ManajemenKelas;

import android.widget.*;
import androidx.appcompat.app.*;
import id.kelompokungu.abzenzi.*;
import id.kelompokungu.abzenzi.Utils.*;
import java.util.*;

public class DataListManager
 {
    private ArrayList<String> dataList;
    private CustomAdapter adapter;
    private AppCompatActivity activity;
	private DataController dc;

    public DataListManager(AppCompatActivity activity, ListView listView) {
        this.activity = activity;
        init(listView);
		loadDataFromStorage();
    }
	
	private void init(ListView listView) {
		dataList = new ArrayList<>();
        adapter = new CustomAdapter(activity, dataList, this);
        listView.setAdapter(adapter);
        
		String folderKelas = activity.getResources().getString(R.string.PATH_FOLDER);
		String fileKelas = activity.getResources().getString(R.string.FILE_KELAS);
		dc = new DataController(activity, folderKelas, fileKelas);
	}

    public void loadDataFromStorage() {
        dataList.clear();
		dataList.addAll(dc.loadData());
        Collections.sort(dataList);
        adapter.notifyDataSetChanged();
    }

    public void addData(String data) {
        if (!data.trim().isEmpty()) {
            if (!dataList.contains(data)) {
                dataList.add(data);
                Collections.sort(dataList);
                adapter.notifyDataSetChanged();
                dc.saveData(data);
				CustomNotif.showSnackbar(activity, data + " berhasil ditambahkan");
            } else {
                CustomNotif.showSnackbar(activity, "Data telah ditambahkan sebelumnya");
            }
        } else {
            CustomNotif.showSnackbar(activity, "Masukkan setidaknya 1 kata!");
        }
    }

    public void removeData(String data) {
        dataList.remove(data);
        adapter.notifyDataSetChanged();
        dc.removeData(data);
    }

    public void clearData() {
        dataList.clear();
        adapter.notifyDataSetChanged();
		dc.clearData();
    }
}
