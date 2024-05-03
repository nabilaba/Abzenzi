package id.kelompokungu.abzenzi.Settings;

import android.content.*;
import android.net.*;
import id.kelompokungu.abzenzi.Utils.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.zip.*;

public class BackupManager {
    public static void backupData(Context context, Uri uri) {
        File sourceFolder = context.getExternalFilesDir(null);
        try {
			FileOutputStream fos = new FileOutputStream(context.getContentResolver().openFileDescriptor(uri, "w").getFileDescriptor());
			ZipOutputStream zos = new ZipOutputStream(fos);
			zipFolder(sourceFolder.getPath(), sourceFolder, zos);
			zos.close();
			CustomNotif.showToast(context, "Pencadangan sukses");
		} catch (Exception e) {
			e.printStackTrace();
			CustomNotif.showToast(context, "Pencadangan gagal");
		}
    }
	
    private static void zipFolder(String sourceFolderPath, File sourceFolder, ZipOutputStream zos) throws Exception {
        File[] files = sourceFolder.listFiles();
        byte[] buffer = new byte[1024];
        int length;

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    zipFolder(sourceFolderPath, file, zos);
                } else {
                    FileInputStream fis = new FileInputStream(file);
                    zos.putNextEntry(new ZipEntry(file.getPath().substring(sourceFolderPath.length() + 1)));
                    while ((length = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, length);
                    }
                    zos.closeEntry();
                    fis.close();
                }
            }
        }
    }
	
    public static String getDefaultName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        return "backup-abzenzi-" + sdf.format(new Date());
    }
}
