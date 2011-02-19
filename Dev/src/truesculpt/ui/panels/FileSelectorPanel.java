package truesculpt.ui.panels;

import truesculpt.main.R;
import android.app.Activity;
import android.os.Bundle;

//to open and close, link to website, MRU, 
public class FileSelectorPanel extends Activity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fileselector);
	}
}
