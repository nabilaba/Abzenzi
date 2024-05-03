package id.kelompokungu.abzenzi.Settings;

import android.content.*;
import android.net.*;
import id.kelompokungu.abzenzi.*;
import id.kelompokungu.abzenzi.Utils.*;
import java.io.*;
import java.util.zip.*;
import android.app.*;

public class RestoreManager {
    public static void restoreData(Context context, File backupFile) {
        if (backupFile == null || !backupFile.exists()) {
            CustomNotif.showToast(context, "Backup tidak ditemukan.");
            return;
        }
        try {
            File extractionDir = context.getExternalFilesDir(null);
            if (!extractionDir.exists()) {
                extractionDir.mkdirs();
            }
            FileInputStream fis = new FileInputStream(backupFile);
            ZipInputStream zis = new ZipInputStream(fis);

            ZipEntry zipEntry;
            while ((zipEntry = zis.getNextEntry()) != null) {
                String entryName = zipEntry.getName();
                File entryFile = new File(extractionDir, entryName);

                if (zipEntry.isDirectory()) {
                    entryFile.mkdirs();
                } else {
                    FileOutputStream fos = new FileOutputStream(entryFile);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                }
                zis.closeEntry();
            }
            zis.close();
            fis.close();
			CustomNotif.showToast(context, "Sukses memulihkan cadangan");
        } catch (IOException e) {
			CustomNotif.showToast(context, "Gagal: " + e.getMessage());
        }
    }
	
	public static File createTemp(Context context, Uri uri)
	{
		try
		{
			String fileName = "restored_backup_file";
			File outputFile = new File(context.getExternalFilesDir(null), fileName);
			InputStream inputStream = context.getContentResolver().openInputStream(uri);
			if (inputStream == null)
			{
				return null;
			}
			OutputStream outputStream = new FileOutputStream(outputFile);
			byte[] buffer = new byte[4 * 1024];
			int read;
			while ((read = inputStream.read(buffer)) != -1)
			{
				outputStream.write(buffer, 0, read);
			}
			outputStream.flush();
			outputStream.close();
			inputStream.close();
			return outputFile;
		}
		catch (IOException e)
		{
			return null;
		}
	}

	public static void deleteTempFile(File file)
	{
		if (file != null && file.exists())
			file.delete();
	}
}
