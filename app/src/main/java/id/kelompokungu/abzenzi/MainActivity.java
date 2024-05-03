package id.kelompokungu.abzenzi;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.hardware.*;
import android.net.*;
import android.os.*;
import android.provider.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import androidx.annotation.*;
import com.google.zxing.*;
import com.google.zxing.common.*;
import com.journeyapps.barcodescanner.*;
import com.journeyapps.barcodescanner.camera.*;
import id.kelompokungu.abzenzi.ManajemenKelas.*;
import id.kelompokungu.abzenzi.RekapAbsensi.*;
import id.kelompokungu.abzenzi.Settings.*;
import id.kelompokungu.abzenzi.Utils.*;
import java.io.*;
import java.text.*;
import java.util.*;

import android.hardware.Camera;

public class MainActivity extends BaseActivity
{
    private DecoratedBarcodeView barcodeView;
	private boolean usingFrontCamera = false, isTorch = false;
	ImageView lampu, switchCameraButton, manajemenKelas, rekap, gallery, settings;
	private static final int REQUEST_IMAGE_PICK = 100;
	private ArrayList<String> dataList;
	private String lastBarcodeScan = "";
	private DataController dc, dcRekap;

    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupScanner();
    }

    private void setupScanner()
	{
		dataList = new ArrayList<>();
        barcodeView = findViewById(R.id.barcode_scanner);
		barcodeView.setStatusText("");
        barcodeView.decodeContinuous(callback);
		setToggleLampu();
		setToggleSwitchKamera();
		goGallery();
		goManajemenKelas();
		goRekap();
		goSettings();
		dataManajemen();
	}

	void dataManajemen()
	{
		String folderKelas = getResources().getString(R.string.PATH_FOLDER);
		String fileKelas = getResources().getString(R.string.FILE_KELAS);
		dc = new DataController(this, folderKelas, fileKelas);
        
		dcRekap = new DataController(this, folderKelas, TimeUtil.getDateFormat() + ".txt");
	}

	void goManajemenKelas()
	{
		manajemenKelas = findViewById(R.id.manajemen_kelas);
		Action.setOnClick(manajemenKelas, ActivityManajemenKelas.class);
	}

	void goRekap()
	{
		rekap = findViewById(R.id.rekap);
		Action.setOnClick(rekap, ActivityRekapAbsensi.class);
	}
	
	void goSettings() {
		settings = findViewById(R.id.settings);
		Action.setOnClick(settings, ActivitySettings.class);
	}

	void goGallery()
	{
		gallery = findViewById(R.id.gallery);
		gallery.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1)
				{
					Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(intent, REQUEST_IMAGE_PICK);
				}
			});
	}

	void setToggleLampu()
	{
		lampu = findViewById(R.id.lampu);
		lampu.setColorFilter(Color.WHITE);
		lampu.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View p1)
				{
					isTorch = !isTorch;
					lampu.setImageResource(isTorch ? R.drawable.flash_on : R.drawable.flash_off);
					if (isTorch)
						barcodeView.setTorchOn();
					else
						barcodeView.setTorchOff();
				}
			});
	}

	void setToggleSwitchKamera()
	{
		switchCameraButton = findViewById(R.id.pilihKamera);
		switchCameraButton.setColorFilter(Color.WHITE);
        switchCameraButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v)
				{
					barcodeView.pause();
					usingFrontCamera = !usingFrontCamera;
					switchCameraButton.setImageResource(usingFrontCamera ? R.drawable.camera_rear : R.drawable.camera_front);
					CameraSettings cameraSettings = new CameraSettings();
					cameraSettings.setRequestedCameraId(usingFrontCamera ? Camera.CameraInfo.CAMERA_FACING_FRONT : Camera.CameraInfo.CAMERA_FACING_BACK);
					barcodeView.getBarcodeView().setCameraSettings(cameraSettings);
					barcodeView.resume();
				}
			});
	}

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result)
		{
			String barcode = result.getText();
            if (barcode != null)
				if (!barcode.equals(lastBarcodeScan))
				{
					cekQR(result.getText());
					lastBarcodeScan = barcode;
				}
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints)
		{}
    };

	private void cekQR(String result)
	{
		dataList.clear();
		dataList.addAll(dc.loadData());
		if (dataList.contains(result))
			if (cekAbsen(result))
				CustomNotif.showSnackbar(this, "Sudah Absen");
			else {
				String jamMasuk = HelperPreference.LoadPreference(getApplicationContext(), "jam_masuk", "07:00");
				
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
				String currentTime = sdf.format(new Date());
				dcRekap.saveData(result + "," + jamMasuk + "," + currentTime);
				CustomNotif.showCustomToast(this, result);
			}
		else
			CustomNotif.showSnackbar(this, "Maaf, " + result + " tidak terdaftar dalam kelas");
	}

	public boolean cekAbsen(String checkedData)
	{
		ArrayList<String> dataToCompare = dcRekap.loadData();
		for (String data : dataToCompare) {
			if (data.split(",")[0].equals(checkedData)) return true;
		}
		return false;
	}

    private String scanQRCode(Bitmap bitmap)
	{
        try
		{
            com.google.zxing.Reader reader = new MultiFormatReader();
            int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
            bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
            com.google.zxing.LuminanceSource source = new com.google.zxing.RGBLuminanceSource(bitmap.getWidth(), bitmap.getHeight(), pixels);
            com.google.zxing.BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));
            Result result = reader.decode(binaryBitmap);
            return result.getText();
        }
		catch (ReaderException e)
		{
            e.printStackTrace();
            return null;
        }
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
	{
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK && data != null)
		{
            Uri imageUri = data.getData();
            try
			{
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                String result = scanQRCode(bitmap);
                if (result != null) cekQR(result);
				else
                    CustomNotif.showSnackbar(this, "Maaf, tidak ada QR code ditemukan");
            }
			catch (IOException e)
			{
                e.printStackTrace();
            }
        }
    }

	@Override
    protected void onResume()
	{
        super.onResume();
		barcodeView.resume();
    }

    @Override
    protected void onPause()
	{
        super.onPause();
		barcodeView.pause();
    }

	@Override
	public void onBackPressed()
	{
		Action.showExitConfirmationDialog(this);
	}
}
