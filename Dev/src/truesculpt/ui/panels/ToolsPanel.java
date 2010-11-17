package truesculpt.ui.panels;

import truesculpt.managers.ToolsManager.EToolMode;
import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.main.TrueSculptApp;
import truesculpt.ui.dialogs.ColorPickerDialog;
import truesculpt.ui.dialogs.ColorPickerDialog.OnColorChangedListener;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

public class ToolsPanel extends Activity implements OnColorChangedListener {
	private final int DIALOG_COLOR_PICKER_ID=0;

	
	public Managers getManagers() {	
		return ((TrueSculptApp)getApplicationContext()).getManagers();
	}
	
	@Override
	public void colorChanged(int color) {
		getManagers().getToolsManager().setColor(color);
		//String msg = "color is " + Integer.toString(color);
		//Toast.makeText(ToolsPanel.this, msg, Toast.LENGTH_SHORT).show();
	}

	private ToggleButton viewToggle;
	private ToggleButton sculptToggle;
	private  Button colorButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tools);
				
		colorButton = (Button) findViewById(R.id.color_button);
		colorButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DIALOG_COLOR_PICKER_ID);				
			}
		});
		
		viewToggle = (ToggleButton) findViewById(R.id.View);
		viewToggle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {				
				getManagers().getToolsManager().setToolMode( EToolMode.POV);
				UpdateView();
			}
		});
		
		sculptToggle = (ToggleButton) findViewById(R.id.Sculpt);
		sculptToggle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {				
				getManagers().getToolsManager().setToolMode( EToolMode.SCULPT);
				UpdateView();
			}
		});
		
		UpdateView();
	}
	
	private void UpdateView()
	{
		viewToggle.setChecked(getManagers().getToolsManager().getToolMode()==EToolMode.POV);
		sculptToggle.setChecked(getManagers().getToolsManager().getToolMode()==EToolMode.SCULPT);	
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog=null;
		switch (id) {
		case DIALOG_COLOR_PICKER_ID:
			dialog = new ColorPickerDialog(ToolsPanel.this, ToolsPanel.this, 0);
			break;
		default:
			dialog = null;
		}
		return dialog;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

}
