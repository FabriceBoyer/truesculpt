package truesculpt.ui.panels;

import truesculpt.managers.ToolsManager.EToolMode;
import truesculpt.managers.ToolsManager.OnToolChangeListener;
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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class ToolsPanel extends Activity implements OnColorChangedListener, OnToolChangeListener {
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

	private ToggleButton mViewToggle;
	private ToggleButton mSculptToggle;
	private ToggleButton mPaintToggle;
	private Button mColorButton;
	
	private TextView mRadiusText;
	private SeekBar mRadiusSeekBar;
	
	private TextView mStrengthText;
	private SeekBar mStrengthSeekBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tools);
		
		getManagers().getToolsManager().registerToolChangeListener(this);
				
		mColorButton = (Button) findViewById(R.id.color_button);
		mColorButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DIALOG_COLOR_PICKER_ID);				
			}
		});
		
		mViewToggle = (ToggleButton) findViewById(R.id.View);
		mViewToggle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {				
				getManagers().getToolsManager().setToolMode( EToolMode.POV);
				getManagers().getToolsManager().setForcedMode(true);
				
				UpdateView();
			}
		});
		
		mSculptToggle = (ToggleButton) findViewById(R.id.Sculpt);
		mSculptToggle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {				
				getManagers().getToolsManager().setToolMode( EToolMode.SCULPT);
				getManagers().getToolsManager().setForcedMode(true);
				
				UpdateView();
			}
		});
		
		mPaintToggle = (ToggleButton) findViewById(R.id.Paint);
		mPaintToggle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {				
				getManagers().getToolsManager().setToolMode( EToolMode.PAINT);
				getManagers().getToolsManager().setForcedMode(true);
				
				UpdateView();
			}
		});
		
		mRadiusSeekBar=(SeekBar)findViewById(R.id.RadiusBar);
		mRadiusSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {				
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				getManagers().getToolsManager().setRadius(progress-100);
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
		});
		mRadiusSeekBar.setMax(200);//-100 to 100 pct		
		mRadiusText=(TextView)findViewById(R.id.RadiusText);
		
		
		mStrengthSeekBar=(SeekBar)findViewById(R.id.StrengthBar);
		mStrengthSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {				
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				getManagers().getToolsManager().setStrength(progress);			
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
		});
		mStrengthSeekBar.setMax(100);//0 to 100pct		
		mStrengthText=(TextView)findViewById(R.id.StrengthText);
		
		UpdateView();
	}
	
	private void UpdateView()
	{
		mViewToggle.setChecked(getManagers().getToolsManager().getToolMode()==EToolMode.POV);
		mSculptToggle.setChecked(getManagers().getToolsManager().getToolMode()==EToolMode.SCULPT);	
		mPaintToggle.setChecked(getManagers().getToolsManager().getToolMode()==EToolMode.PAINT);	
		
		float fStrength=getManagers().getToolsManager().getStrength();
		mStrengthSeekBar.setProgress((int)fStrength);
		mStrengthText.setText("Strength = "+Integer.toString((int)fStrength)+" %");
		
		float fRadius=getManagers().getToolsManager().getRadius();
		mRadiusSeekBar.setProgress((int)fRadius+100);
		mRadiusText.setText("Radius = "+Integer.toString((int)fRadius)+" %");
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
		
		getManagers().getToolsManager().unRegisterToolChangeListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void onToolChange() {
		UpdateView();		
	}

}
