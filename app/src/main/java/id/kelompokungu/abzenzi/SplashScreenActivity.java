package id.kelompokungu.abzenzi;


import android.*;
import android.content.*;
import android.content.pm.*;
import android.os.*;
import android.provider.*;
import android.view.*;
import android.widget.*;
import androidx.annotation.*;
import androidx.appcompat.app.*;
import androidx.core.app.*;
import androidx.core.content.*;
import java.util.*;
import android.net.*;
import id.kelompokungu.abzenzi.errorhandler.*;

public class SplashScreenActivity extends BaseActivity
{
	private static final int MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE_AND_CAMERA = 201;
    private static final String[] PERMISSIONS = {
		Manifest.permission.READ_EXTERNAL_STORAGE,
		Manifest.permission.WRITE_EXTERNAL_STORAGE,
		Manifest.permission.CAMERA
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
		View decorView = getWindow().getDecorView();
		int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
			| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
			| View.SYSTEM_UI_FLAG_LAYOUT_STABLE
			| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
			| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
			| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
		decorView.setSystemUiVisibility(uiOptions);
		Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));
        requestPermissions();
    }

	void goMain()
	{
		new Handler().postDelayed(new Runnable() {
				@Override
				public void run()
				{
					Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
					startActivity(intent);
					finish();
				}
			}, 2000);
	}

	private void requestPermissions()
	{
        List<String> permissionsToRequest = new ArrayList<>();
        for (String permission : PERMISSIONS)
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) permissionsToRequest.add(permission);

        if (!permissionsToRequest.isEmpty())
		{
            String[] arrayToRequest = permissionsToRequest.toArray(new String[0]);
            ActivityCompat.requestPermissions(this, arrayToRequest, MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE_AND_CAMERA);
        }
		else
			goMain();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
	{
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE_AND_CAMERA)
		{
            boolean allPermissionsGranted = true;
            for (int i = 0; i < permissions.length; i++)
			{
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED)
				{
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (!allPermissionsGranted)
			{
                boolean shouldShowRationale = false;
                for (String permission : permissions)
				{
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission))
					{
                        shouldShowRationale = true;
                        break;
                    }
                }

                if (shouldShowRationale)
					requestPermissions();
				else
				{
                    Toast.makeText(this, "Please grant permissions manually in app settings", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
					intent.setData(Uri.fromParts("package", getPackageName(), null));
					startActivity(intent);
                }
            }
			else
				goMain();
        }
    }
}
