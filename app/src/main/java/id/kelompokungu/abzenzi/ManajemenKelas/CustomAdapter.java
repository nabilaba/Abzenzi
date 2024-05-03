package id.kelompokungu.abzenzi.ManajemenKelas;

import android.content.*;
import android.graphics.*;
import android.view.*;
import android.widget.*;
import androidx.annotation.*;
import androidx.appcompat.app.*;
import id.kelompokungu.abzenzi.*;
import id.kelompokungu.abzenzi.QRSiswa.*;
import id.kelompokungu.abzenzi.Utils.*;
import java.text.*;
import java.util.*;

public class CustomAdapter extends ArrayAdapter<String>
{
    private AppCompatActivity activity;
    private ArrayList<String> dataList;
	private DataListManager dataListManager;
	private DataController dc;	
	private QRGenerator QRGenerator;
	
    public CustomAdapter(AppCompatActivity activity, ArrayList<String> dataList, DataListManager dataListManager)
	{
        super(activity, 0, dataList);
        this.activity = activity;
        this.dataList = dataList;
		this.dataListManager = dataListManager;

		String folderKelas = activity.getResources().getString(R.string.PATH_FOLDER);
        String fileKelas = activity.getResources().getString(R.string.FILE_KELAS);
		dc = new DataController(activity, folderKelas, fileKelas + ".txt");
		
		QRGenerator = new QRGenerator();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent)
	{
        final ViewHolder holder;
		final String data = getItem(position);
		
        if (convertView == null)
		{
            convertView = LayoutInflater.from(activity).inflate(R.layout.mk_list_item, parent, false);
            holder = new ViewHolder();
            holder.numberText = convertView.findViewById(R.id.no_text);
            holder.dataText = convertView.findViewById(R.id.item);
            holder.barcode = convertView.findViewById(R.id.barcode);
			holder.delete = convertView.findViewById(R.id.delete);
            convertView.setTag(holder);
        }
		else
            holder = (ViewHolder) convertView.getTag();

        holder.numberText.setText((position + 1) + ". ");
        holder.dataText.setText(data);
        convertView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View p1)
				{
					Intent intent = new Intent(activity, ActivityQRSiswa.class);
					intent.putExtra("data", data);
					activity.startActivity(intent);
				}
			});
			
		holder.delete.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View p1)
				{
					dataListManager.removeData(getItem(position));
				}
			});
			
		QRGenerator.generateQRAsync(activity, data, holder.barcode);
			
        return convertView;
    }

    static class ViewHolder
	{
        TextView numberText, dataText;
        ImageView barcode, delete;
    }
}
