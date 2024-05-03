package id.kelompokungu.abzenzi.Utils;

import android.content.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.provider.*;
import androidx.appcompat.app.*;
import id.kelompokungu.abzenzi.*;
import java.io.*;
import android.view.*;
import android.app.Activity;

public class Action
{
	public static void showExitConfirmationDialog(final Activity a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(a);
        builder.setTitle("Keluar");
        builder.setMessage("Apakah Anda yakin ingin keluar dari aplikasi?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					a.finishAffinity();
					System.exit(0);
				}
			});
        builder.setNegativeButton("Tidak", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
	
	public static void saveImage(AppCompatActivity a, Bitmap bitmap, String name) {
		try
		{
			File dcimDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
			File subDirectory = new File(dcimDirectory, a.getResources().getString(R.string.FOLDER_QR));
			if (!subDirectory.exists())
			{
				subDirectory.mkdirs();
			}
			File file = new File(subDirectory, name + ".jpg");
			OutputStream outStream = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
			outStream.flush();
			outStream.close();
			ContentValues values = new ContentValues();
			values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
			values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
			a.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			CustomNotif.showSnackbar(a, "Tersimpan:" + file.getPath());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void setOnClick(final View view, final Class<? extends Activity> activityClass)
	{
        view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v)
				{
					Context context = view.getContext();
					Intent intent = new Intent(context, activityClass);
					context.startActivity(intent);
				}
			});
    }
}
