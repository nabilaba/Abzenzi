package id.kelompokungu.abzenzi.Utils;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.*;
import androidx.appcompat.widget.*;
import id.kelompokungu.abzenzi.*;
import java.util.*;

import androidx.appcompat.widget.Toolbar;

public class CustomToolbar
{
	public static void Toolbar(AppCompatActivity a, String toolbarTitle)
	{
		Toolbar toolbar = a.findViewById(R.id.toolbar);
		toolbar.setTitle(toolbarTitle);
		a.setSupportActionBar(toolbar);
		a.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
}
