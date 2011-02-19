package truesculpt.ui.panels;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.main.TrueSculptApp;
import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

//to open and close, link to website, MRU, 
public class FileSelectorPanel extends Activity
{
	private EditText mEditNameText;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fileselector);
		
		mEditNameText=(EditText)findViewById(R.id.name_edit);
		String name=getManagers().getMeshManager().getName();
		mEditNameText.setText(name);		
	}
	
	public Managers getManagers()
	{
		return ((TrueSculptApp) getApplicationContext()).getManagers();
	}

	@Override
	protected void onPause()
	{
		getManagers().getMeshManager().setName(mEditNameText.getText().toString());
		super.onDestroy();
	}
}
