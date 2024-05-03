package id.kelompokungu.abzenzi.RekapAbsensi;

import android.content.res.*;
import android.graphics.*;
import android.view.*;
import android.widget.*;
import androidx.annotation.*;
import androidx.appcompat.app.*;
import id.kelompokungu.abzenzi.*;
import id.kelompokungu.abzenzi.Utils.*;
import java.util.*;

public class CustomAdapter extends ArrayAdapter<String>
{
    private AppCompatActivity activity;
    private ArrayList<String> dataList;
	private DataListManager dataListManager;
	private DataController dc;
	private String folderKelas;

    public CustomAdapter(AppCompatActivity activity, ArrayList<String> dataList, DataListManager dataListManager)
	{
        super(activity, 0, dataList);
        this.activity = activity;
        this.dataList = dataList;
		this.dataListManager = dataListManager;

	    folderKelas = activity.getResources().getString(R.string.PATH_FOLDER);
		dc = new DataController(activity, folderKelas, TimeUtil.getDateFormat() + ".txt");
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent)
	{
        ViewHolder holder;
        if (convertView == null)
		{
            convertView = LayoutInflater.from(activity).inflate(R.layout.ra_list_item, parent, false);
            holder = new ViewHolder();
            holder.numberText = convertView.findViewById(R.id.no_text);
            holder.dataText = convertView.findViewById(R.id.item);
			holder.jamText = convertView.findViewById(R.id.jam);
            convertView.setTag(holder);
        }
		else
            holder = (ViewHolder) convertView.getTag();

        holder.numberText.setText((position + 1) + ". ");
        holder.dataText.setText(getItem(position));
		String name = getItem(position);
		switch (getBackgroundByStatus(name, holder.jamText))
		{
			case "late":
				convertView.setBackgroundColor(Color.RED);
				holder.numberText.setTextColor(Color.WHITE);
				holder.dataText.setTextColor(Color.WHITE);
				holder.jamText.setTextColor(Color.WHITE);
				break;
			case "notLate":
				convertView.setBackgroundColor(Color.GREEN);
				holder.numberText.setTextColor(Color.BLACK);
				holder.dataText.setTextColor(Color.BLACK);
				holder.jamText.setTextColor(Color.BLACK);
				break;
			case "notAbsen":
				convertView.setBackgroundColor(getContext().getResources().getColor(R.color.dayNightBg));
				int[] attrs = {android.R.attr.textColorPrimary};
				TypedArray ta = getContext().obtainStyledAttributes(R.style.AppTheme, attrs);
				int textColorPrimary = ta.getColor(0, Color.BLACK);
				ta.recycle();
				holder.numberText.setTextColor(textColorPrimary);
				holder.dataText.setTextColor(textColorPrimary);
				holder.jamText.setTextColor(textColorPrimary);
				holder.jamText.setText("Belum Absen");
				break;	
		}
        return convertView;
    }

	private String getBackgroundByStatus(String checkedData, TextView tv)
	{
		ArrayList<String> dataToCompare = dc.loadData();
		for (String data : dataToCompare)
		{
			String nama = data.split(",")[0];
			String jamMasuk = data.split(",")[1];
			String jamAbsen = data.split(",")[2];
			
			if (nama.equals(checkedData))
			{
				if (!jamAbsen.isEmpty() && !jamMasuk.isEmpty())
				{
					tv.setText(jamAbsen + "/" + jamMasuk);
					int h = Integer.parseInt(jamMasuk.split(":")[0]);
					int m = Integer.parseInt(jamMasuk.split(":")[1]);

					int hAbsen = Integer.parseInt(jamAbsen.split(":")[0]);
					int mAbsen = Integer.parseInt(jamAbsen.split(":")[1]);

					if (hAbsen > h || (hAbsen == h && mAbsen > m)) return "late";
					else return "notLate";
				}
			}
		}
		return "notAbsen";
	}

	public void setDateData(String format)
	{
		dc = new DataController(activity, folderKelas, format + ".txt");
	}

    static class ViewHolder
	{
        TextView numberText, dataText, jamText;
    }
}
