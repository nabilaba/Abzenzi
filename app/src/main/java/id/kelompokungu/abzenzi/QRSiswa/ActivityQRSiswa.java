package id.kelompokungu.abzenzi.QRSiswa;

import android.content.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.provider.*;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.*;
import id.kelompokungu.abzenzi.*;
import java.io.*;
import id.kelompokungu.abzenzi.Utils.*;

public class ActivityQRSiswa extends BaseActivity
{
	private View save, frame;
	private QRGenerator QRGenerator;
	private ImageView qr;
	private TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_siswa);	
		CustomToolbar.Toolbar(this, "QR");

        qr = findViewById(R.id.qr);
		name = findViewById(R.id.name);
		frame = findViewById(R.id.frame);

        String textToEncode = getIntent().getStringExtra("data");
		name.setText(textToEncode);

		QRGenerator = new QRGenerator();
		QRGenerator.generateQRAsync(this, textToEncode, qr);

		handleSave(textToEncode);
    }

    private Bitmap frameQR(View view)
	{
		view.setBackgroundColor(Color.WHITE);
		Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
		view.setBackgroundColor(Color.TRANSPARENT);
        return bitmap;
	}

    private void handleSave(final String name)
	{
		save = findViewById(R.id.save);
		save.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View p1)
				{
					Bitmap bitmap = frameQR(frame);
					Action.saveImage(ActivityQRSiswa.this, bitmap, name);
				}
			});
    }

	@Override
    public boolean onSupportNavigateUp()
	{
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
