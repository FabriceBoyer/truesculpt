package truesculpt.ui.panels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.main.TrueSculptApp;
import truesculpt.managers.ToolsManager.ESymmetryMode;
import truesculpt.managers.UtilsManager;
import truesculpt.tools.base.BaseTool;
import truesculpt.ui.dialogs.ColorPickerDialog.OnColorChangedListener;
import truesculpt.ui.views.ColorPickerView;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class ToolsPanel extends Activity implements Observer
{
	private ColorPickerView mColorPickerView;
	private SeekBar mRadiusSeekBar;
	private TextView mRadiusText;
	private SeekBar mStrengthSeekBar;
	private TextView mStrengthText;
	private Spinner mToolSpinner;
	private Spinner mSymmetrySpinner;

	public Managers getManagers()
	{
		return ((TrueSculptApp) getApplicationContext()).getManagers();
	}

	public static void UpdateSymmetrySpinner(Spinner symSpinner, final Context context)
	{
		ESymmetryMode mode = ((TrueSculptApp) (context.getApplicationContext())).getManagers().getToolsManager().getSymmetryMode();
		int nIndex = 0;
		switch (mode)
		{
		case NONE:
			nIndex = 0;
			break;
		case X:
			nIndex = 1;
			break;
		case Y:
			nIndex = 2;
			break;
		case Z:
			nIndex = 3;
			break;
		}
		symSpinner.setSelection(nIndex);
	}

	public static void UpdateToolSpinner(Spinner toolSpinner, final Context context)
	{
		int nToolIndex = ((TrueSculptApp) (context.getApplicationContext())).getManagers().getToolsManager().getCurrentToolIndex();
		toolSpinner.setSelection(nToolIndex);
	}

	public static void InitToolSpinner(Spinner toolSpinner, final Context context)
	{
		ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map;

		int nLibrarySize = ((TrueSculptApp) (context.getApplicationContext())).getManagers().getToolsManager().GetToolsLibrarySize();
		for (int i = 0; i < nLibrarySize; i++)
		{
			BaseTool tool = ((TrueSculptApp) (context.getApplicationContext())).getManagers().getToolsManager().GetToolAtIndex(i);
			map = new HashMap<String, String>();
			map.put("title", tool.GetName());
			map.put("description", tool.GetDescription());
			map.put("image", String.valueOf(tool.GetIcon()));
			listItem.add(map);
		}

		SimpleAdapter adapter = new SimpleAdapter(context, listItem, R.layout.reducedtoolitem, new String[] { "image", "title", "description" }, new int[] { R.id.image, R.id.title, R.id.description });
		adapter.setDropDownViewResource(R.layout.toolitem);
		toolSpinner.setAdapter(adapter);
		toolSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				((TrueSculptApp) (context.getApplicationContext())).getManagers().getToolsManager().setCurrentToolFromIndex((int) arg3);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{

			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		getManagers().getUtilsManager().updateFullscreenWindowStatus(getWindow());

		getManagers().getUsageStatisticsManager().TrackPageView("/ToolsPanel");

		setContentView(R.layout.tools);

		getManagers().getToolsManager().addObserver(this);

		mRadiusSeekBar = (SeekBar) findViewById(R.id.RadiusBar);
		mRadiusSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
		{
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
			{
				getManagers().getToolsManager().setRadius(progress, false);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar)
			{
				// getManagers().getToolsManager().SetUndoInitialState();
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar)
			{
				getManagers().getToolsManager().setRadius(seekBar.getProgress(), false);
				// getManagers().getToolsManager().AddUndoToolAction();
			}
		});
		mRadiusSeekBar.setMax(100);// 0 to 100 pct
		mRadiusText = (TextView) findViewById(R.id.RadiusText);

		mStrengthSeekBar = (SeekBar) findViewById(R.id.StrengthBar);
		mStrengthSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
		{
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
			{
				getManagers().getToolsManager().setStrength(progress - 100, false);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar)
			{
				// getManagers().getToolsManager().SetUndoInitialState();
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar)
			{
				getManagers().getToolsManager().setStrength(seekBar.getProgress() - 100, false);
				// getManagers().getToolsManager().AddUndoToolAction();
			}
		});
		mStrengthSeekBar.setMax(200);// -100 to 100 pct
		mStrengthText = (TextView) findViewById(R.id.StrengthText);

		mColorPickerView = (ColorPickerView) findViewById(R.id.ColorPickerView);
		mColorPickerView.SetColor(getManagers().getToolsManager().getColor());
		mColorPickerView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				getManagers().getUtilsManager();
				UtilsManager.ShowHSLColorPickerDialog(ToolsPanel.this);
			}
		});
		mColorPickerView.SetColorChangeListener(new OnColorChangedListener()
		{
			@Override
			public void colorChanged(int color)
			{
				getManagers().getToolsManager().setColor(color, false, true);
			}

			@Override
			public void colorChangeStart(int color)
			{
				// getManagers().getToolsManager().SetUndoInitialState();
			}

			@Override
			public void colorChangeStop(int color)
			{
				// getManagers().getToolsManager().AddUndoToolAction();
			}
		});

		Button mScreenshotButton = (Button) findViewById(R.id.screenshot);
		mScreenshotButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				getManagers().getToolsManager().TakeGLScreenshot(getManagers().getUtilsManager().CreateSnapshotFileName());
				finish();
			}
		});

		Button ResetPOVbutton = (Button) findViewById(R.id.ResetPOV);
		ResetPOVbutton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				getManagers().getPointOfViewManager().resetPOV();
				finish();
			}
		});

		Button CloseButton = (Button) findViewById(R.id.CloseBtn);
		CloseButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});

		mToolSpinner = (Spinner) findViewById(R.id.SculptToolSpinner);
		InitToolSpinner(mToolSpinner, this);

		mSymmetrySpinner = (Spinner) findViewById(R.id.SymmetrySpinner);
		InitSymmetrySpinner(mSymmetrySpinner, this);

		UpdateView();
	}

	private static void InitSymmetrySpinner(Spinner symSpinner, final Context context)
	{
		ArrayAdapter adapter = ArrayAdapter.createFromResource(context, R.array.symmetry, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		symSpinner.setAdapter(adapter);
		symSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				ESymmetryMode mode = ESymmetryMode.NONE;

				switch ((int) arg3)
				{
				case 0:
					mode = ESymmetryMode.NONE;
					break;
				case 1:
					mode = ESymmetryMode.X;
					break;
				case 2:
					mode = ESymmetryMode.Y;
					break;
				case 3:
					mode = ESymmetryMode.Z;
					break;
				}

				((TrueSculptApp) (context.getApplicationContext())).getManagers().getToolsManager().setSymmetryMode(mode);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{

			}
		});
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();

		getManagers().getToolsManager().deleteObserver(this);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		return super.onTouchEvent(event);
	}

	@Override
	public void update(Observable observable, Object data)
	{
		UpdateView();
	}

	private void UpdateView()
	{
		float fStrength = getManagers().getToolsManager().getStrength();
		mStrengthSeekBar.setProgress((int) fStrength + 100);
		mStrengthText.setText("Strength = " + Integer.toString((int) fStrength) + " %");

		float fRadius = getManagers().getToolsManager().getRadius();
		mRadiusSeekBar.setProgress((int) fRadius);
		mRadiusText.setText("Radius = " + Integer.toString((int) fRadius) + " %");

		mColorPickerView.SetColor(getManagers().getToolsManager().getColor());

		UpdateToolSpinner(mToolSpinner, this);
		UpdateSymmetrySpinner(mSymmetrySpinner, this);
	}
}
