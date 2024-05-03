package id.kelompokungu.abzenzi.Utils;

import android.app.*;
import android.content.*;
import id.kelompokungu.abzenzi.*;
import java.io.*;
import java.util.*;
import androidx.appcompat.app.*;

public class DataController
{
	private Activity activity;
	private String folder;
	private String file;

	public DataController(Activity activity, String folder, String file)
	{
		this.activity = activity;
		this.folder = folder;
		this.file = file;
	}

	public ArrayList<String> loadData()
	{
		{
			ArrayList<String> dataList = new ArrayList<>();
			BufferedReader reader = null;
			try
			{
				File mFolder = new File(activity.getExternalFilesDir(null), folder);
				if (!mFolder.exists())
				{
					mFolder.mkdirs();
				}
				File mFile = new File(mFolder, file);
				if (mFile.exists())
				{
					reader = new BufferedReader(new FileReader(mFile));
					String line;
					while ((line = reader.readLine()) != null)
					{
						dataList.add(line);
					}
					Collections.sort(dataList);
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			finally
			{
				if (reader != null)
				{
					try
					{
						reader.close();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
				return dataList;
			}
		}
	}

	public void saveData(String data)
	{
		FileWriter fileWriter = null;
		try
		{
			File mFolder = new File(activity.getExternalFilesDir(null), folder);
			if (!mFolder.exists())
			{
				mFolder.mkdirs();
			}
			File mFile = new File(mFolder, file);
			fileWriter = new FileWriter(mFile, true);
			fileWriter.write(data + "\n");
		}
		catch (IOException e)
		{
			e.printStackTrace();
			CustomNotif.showSnackbar(activity, "Gagal menyimpan data");
		}
		finally
		{
			if (fileWriter != null)
			{
				try
				{
					fileWriter.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	public void removeData(String itemToRemove)
	{
        List<String> dataList = new ArrayList<>();
		dataList.addAll(loadData());
        dataList.remove(itemToRemove);
        FileWriter writer = null;
        try
		{
            File mFolder = new File(activity.getExternalFilesDir(null), folder);
			File mFile = new File(mFolder, file);
            List<String> lines = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(mFile));
            String line;
            while ((line = reader.readLine()) != null)
			{
                if (!line.equals(itemToRemove))
				{
                    lines.add(line);
                }
            }
            reader.close();
            writer = new FileWriter(mFile, false);
            for (String updatedLine : lines)
			{
                writer.write(updatedLine + "\n");
            }
			CustomNotif.showSnackbar(activity, "Berhasil menghapus " + itemToRemove);
        }
		catch (IOException e)
		{
            e.printStackTrace();
			CustomNotif.showSnackbar(activity, "Gagal menghapus");
        }
		finally
		{
            try
			{
                if (writer != null)
				{
                    writer.close();
                }
            }
			catch (IOException e)
			{
                e.printStackTrace();
            }
        }
    }

	public void clearData()
	{
		FileWriter fileWriter = null;
		try
		{
			File mFolder = new File(activity.getExternalFilesDir(null), folder);
			File mFile = new File(mFolder, file);
			if (mFile.exists())
			{
				fileWriter = new FileWriter(mFile, false);
				fileWriter.write("");
			}
			CustomNotif.showSnackbar(activity, "Reset data sukses");
		}
		catch (IOException e)
		{
			CustomNotif.showSnackbar(activity, "Gagal menghapus data");
		}
		finally
		{
			try
			{
				if (fileWriter != null)
				{
					fileWriter.close();
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}


