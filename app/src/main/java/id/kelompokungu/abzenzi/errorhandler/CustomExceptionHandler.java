package id.kelompokungu.abzenzi.errorhandler;

import android.util.Log;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import android.content.*;
import java.io.*;

public class CustomExceptionHandler implements Thread.UncaughtExceptionHandler {
    private Context context;

    public CustomExceptionHandler(Context context) {
        this.context = context;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        // Handle the exception here
        exportLogcatToFile();
        // Exit the app or perform any other necessary action
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    private void exportLogcatToFile() {
		try {
			File logFile = new File(context.getExternalFilesDir(null), "logcat.txt");
			Process process = Runtime.getRuntime().exec("logcat -d *:E");
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			FileWriter fileWriter = new FileWriter(logFile);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				fileWriter.write("\t" + line + "\n");
			}
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
