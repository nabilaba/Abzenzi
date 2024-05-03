package id.kelompokungu.abzenzi.Utils;

import android.content.*;
import android.graphics.*;
import android.os.*;
import androidx.appcompat.app.*;
import androidx.collection.*;
import com.google.zxing.*;
import com.google.zxing.common.*;
import com.journeyapps.barcodescanner.*;
import java.util.*;
import android.widget.*;

public class QRGenerator
{
	private LruCache<String, Bitmap> qrCache;

	public QRGenerator()
	{
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		final int cacheSize = maxMemory / 8;
		qrCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap)
			{
				return bitmap.getByteCount() / 1024;
			}
		};
	}

	public void generateQRAsync(final AppCompatActivity activity, final String data, final ImageView target)
	{
		Bitmap cachedQR = getQRFromCache(data);
		if (cachedQR != null)
		{
			target.setImageBitmap(cachedQR);
		}
		else
		{
			new AsyncTask<Void, Void, Bitmap>() {
				@Override
				protected Bitmap doInBackground(Void... voids)
				{
					return getQR(activity, data);
				}

				@Override
				protected void onPostExecute(Bitmap bitmap)
				{
					if (bitmap != null)
					{
						cacheQR(data, bitmap);
						target.setImageBitmap(bitmap);
					}
				}
			}.execute();
		}
	}

	private Bitmap getQRFromCache(String data)
	{
		if (qrCache != null)
		{
			return qrCache.get(data);
		}
		return null;
	}

	private void cacheQR(String data, Bitmap barcode)
	{
		if (getQRFromCache(data) == null)
		{
			qrCache.put(data, barcode);
		}
	}

	private Bitmap getQR(Context context, String text)
	{
		int size = context.getResources().getDisplayMetrics().widthPixels;
		Bitmap bitmap = createBlankBitmap(size, size);
		MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

		Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.MARGIN, 1);
		try
		{
			BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, size, size, hints);
			BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
		    bitmap = barcodeEncoder.createBitmap(bitMatrix);
		}
		catch (WriterException e)
		{ }

		return bitmap;
	}

	private Bitmap createBlankBitmap(int width, int height)
	{
		return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
	}
}
