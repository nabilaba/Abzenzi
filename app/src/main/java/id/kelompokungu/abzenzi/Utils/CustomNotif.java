package id.kelompokungu.abzenzi.Utils;
import android.app.*;
import android.content.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import androidx.preference.*;
import com.google.android.material.snackbar.*;
import id.kelompokungu.abzenzi.*;

import id.kelompokungu.abzenzi.R;

public class CustomNotif
{
	public static void showCustomToast(final Activity a, String name) {
		final Dialog dialog = new Dialog(a);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		LayoutInflater inflater = a.getLayoutInflater();
		View layout = inflater.inflate(R.layout.custom_toast, null);

		TextView textEmot = layout.findViewById(R.id.emot);
		String emot = HelperPreference.LoadPreference(a, "emot", "😸");
		textEmot.setText(emot);
		
		TextView textUcapan = layout.findViewById(R.id.ucapan);
	    String ucapan = HelperPreference.LoadPreference(a, "ucapan", "Selamat belajar");
		textUcapan.setText(ucapan);
		
		TextView textName = layout.findViewById(R.id.name);
		textName.setText(name);

		dialog.setContentView(layout);
		
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.show();

		new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					dialog.dismiss();
				}
			}, 5000);
	}
	
	public static void showToast(Context c, String message) {
		Toast.makeText(c, message, Toast.LENGTH_LONG).show();
	}
	
	public static void showSnackbar(Activity a, String message) {
		Snackbar snackbar = Snackbar.make(a.findViewById(android.R.id.content),
										  message, Snackbar.LENGTH_SHORT);
		snackbar.show();
	}
}
