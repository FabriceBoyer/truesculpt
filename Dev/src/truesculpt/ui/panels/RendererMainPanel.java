package truesculpt.ui.panels;

import java.util.Observable;
import java.util.Observer;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.main.TrueSculptApp;
import truesculpt.managers.ToolsManager.ESymmetryMode;
import truesculpt.managers.UtilsManager;
import truesculpt.tools.base.BaseTool;
import truesculpt.ui.adapters.CoverFlowImageAdapter;
import truesculpt.ui.dialogs.ColorPickerDialog.OnColorChangedListener;
import truesculpt.ui.views.ColorShowView;
import truesculpt.ui.views.CoverFlow;
import truesculpt.ui.views.SliderPickView;
import truesculpt.ui.views.SliderPickView.OnDoubleClickListener;
import truesculpt.ui.views.SliderPickView.OnSliderPickChangedListener;
import truesculpt.ui.views.ToolPickerView;
import truesculpt.ui.views.ToolPickerView.OnToolPickChangedListener;
import truesculpt.utils.Utils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class RendererMainPanel extends Activity implements Observer
{
	private GLSurfaceView mGLSurfaceView;
	private SlidingDrawer mToolsSlidingDrawer;
	private ImageButton mRedoButton;
	private ImageButton mUndoButton;
	private ColorShowView mColorShow;
	private ToolPickerView mToolPicker;
	private SliderPickView mRadius;
	private SliderPickView mStrength;
	private ImageButton mResetPOVbutton;
	private ToggleButton mSymmetrySwitcher;
	private ImageButton mFilesBtn;
	private TextView mBigTextOverlay;
	private CoverFlow mCoverFlow;
	private SeekBar mRadiusSeekBar;
	private SeekBar mStrengthSeekBar;

	private final int mRadiusBkColor = Color.rgb(0, 140, 0);// dk green

	public Managers getManagers()
	{
		return ((TrueSculptApp) getApplicationContext()).getManagers();
	}

	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		default:
			return super.onContextItemSelected(item);
		}
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// StrictMode.enableDefaults();

		super.onCreate(savedInstanceState);

		getManagers().getUsageStatisticsManager().TrackPageView("/RendererMainPanel");
		getManagers().getUtilsManager().InitHandler();

		// ShowSplashScreen();
		// getManagers().getUpdateManager().CheckUpdate(getBaseContext());
		// ShowTutorial();

		getManagers().getUtilsManager().updateFullscreenWindowStatus(getWindow());

		setContentView(R.layout.main);

		mGLSurfaceView = (GLSurfaceView) findViewById(R.id.glview);
		mGLSurfaceView.setDebugFlags(GLSurfaceView.DEBUG_CHECK_GL_ERROR);
		mGLSurfaceView.setRenderer(getManagers().getRendererManager().getMainRenderer());
		mGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

		getManagers().getPointOfViewManager().addObserver(this);
		getManagers().getMeshManager().addObserver(this);
		getManagers().getToolsManager().addObserver(this);
		getManagers().getActionsManager().addObserver(this);

		mToolsSlidingDrawer = (SlidingDrawer) findViewById(R.id.toolsSlidingDrawer);
		mToolsSlidingDrawer.setOnDrawerOpenListener(new OnDrawerOpenListener()
		{
			@Override
			public void onDrawerOpened()
			{
				// mToolsSlideHandleButton.setBackgroundResource(R.drawable.down_arrow);
			}
		});
		mToolsSlidingDrawer.setOnDrawerCloseListener(new OnDrawerCloseListener()
		{
			@Override
			public void onDrawerClosed()
			{
				// mToolsSlideHandleButton.setBackgroundResource(R.drawable.up_arrow);
			}
		});

		mBigTextOverlay = (TextView) findViewById(R.id.bigTextValueOverlay);
		mBigTextOverlay.setTextSize(25);
		HideBigTextOverlay();

		mToolPicker = (ToolPickerView) findViewById(R.id.SculptToolPicker);
		mToolPicker.setElemCount(getManagers().getToolsManager().GetToolsLibrarySize());
		mToolPicker.setToolChangeListener(new OnToolPickChangedListener()
		{
			@Override
			public void ToolChangeStart(int value)
			{
				SetBigTextOverlayToDragMe();
				mCoverFlow.setVisibility(mCoverFlow.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
			}

			@Override
			public void ToolValueChanged(int value)
			{
				if (value != getManagers().getToolsManager().getCurrentToolIndex())
				{
					getManagers().getToolsManager().setCurrentTool(value);
					UpdateToolBigTextOverlay(value);
				}
			}

			@Override
			public void ToolChangeStop(int value)
			{
				HideBigTextOverlay();
			}
		});
		mToolPicker.setDoubleClickListener(new ToolPickerView.OnDoubleClickListener()
		{
			@Override
			public void onDoubleClick(float value)
			{
				mCoverFlow.setVisibility(View.GONE);
				HideAllOptionnalTools();
				getManagers().getUtilsManager().ShowToolPickerDialog(RendererMainPanel.this);
			}
		});

		mCoverFlow = (CoverFlow) findViewById(R.id.coverflow);
		CoverFlowImageAdapter coverImageAdapter = new CoverFlowImageAdapter(this);
		coverImageAdapter.createReflectedImages();
		mCoverFlow.setAdapter(coverImageAdapter);
		mCoverFlow.setSpacing(-15);
		mCoverFlow.setAnimationDuration(1000);
		mCoverFlow.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				getManagers().getToolsManager().setCurrentTool((int) arg3);
				mCoverFlow.setVisibility(View.GONE);
				HideBigTextOverlay();
			}
		});
		mCoverFlow.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				UpdateToolBigTextOverlay((int) arg3);
				getManagers().getToolsManager().setCurrentTool((int) arg3);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{

			}
		});

		mRedoButton = (ImageButton) findViewById(R.id.RedoBtn);
		mRedoButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				getManagers().getActionsManager().Redo();
			}
		});
		mRedoButton.setOnLongClickListener(new View.OnLongClickListener()
		{
			@Override
			public boolean onLongClick(View v)
			{
				Utils.StartMyActivity(RendererMainPanel.this, truesculpt.ui.panels.HistoryPanel.class, false);
				return false;
			}
		});
		mRedoButton.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1)
			{
				if ((arg1.getAction() & MotionEvent.ACTION_MASK) != MotionEvent.ACTION_UP)
				{
					mBigTextOverlay.setTextColor(Color.WHITE);
					mBigTextOverlay.setText("Redo");
				}
				else
				{
					HideBigTextOverlay();
				}
				return false;
			}
		});

		mUndoButton = (ImageButton) findViewById(R.id.UndoBtn);
		mUndoButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				getManagers().getActionsManager().Undo();
			}
		});
		mUndoButton.setOnLongClickListener(new View.OnLongClickListener()
		{
			@Override
			public boolean onLongClick(View v)
			{
				// Utils.StartMyActivity(RendererMainPanel.this, truesculpt.ui.panels.HistoryPanel.class, false);
				return false;
			}
		});
		mUndoButton.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1)
			{
				if ((arg1.getAction() & MotionEvent.ACTION_MASK) != MotionEvent.ACTION_UP)
				{

					mBigTextOverlay.setTextColor(Color.WHITE);
					mBigTextOverlay.setText("Undo");
				}
				else
				{
					HideBigTextOverlay();
				}
				return false;
			}
		});

		mResetPOVbutton = (ImageButton) findViewById(R.id.ResetPOVBtn);
		mResetPOVbutton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				getManagers().getPointOfViewManager().resetPOV();
			}
		});
		mResetPOVbutton.setOnLongClickListener(new View.OnLongClickListener()
		{
			@Override
			public boolean onLongClick(View v)
			{
				// Utils.StartMyActivity(RendererMainPanel.this, truesculpt.ui.panels.PointOfViewPanel.class, false);
				return false;
			}
		});
		mResetPOVbutton.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1)
			{
				if ((arg1.getAction() & MotionEvent.ACTION_MASK) != MotionEvent.ACTION_UP)
				{
					mBigTextOverlay.setTextColor(Color.WHITE);
					mBigTextOverlay.setText("Reset view\n\nUse two fingers\nTo zoom and pan\n\nDrag in black to rotate");
				}
				else
				{
					HideBigTextOverlay();
				}
				return false;
			}
		});

		mSymmetrySwitcher = (ToggleButton) findViewById(R.id.SymmetrySwitcher);
		mSymmetrySwitcher.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (getManagers().getToolsManager().getSymmetryMode() != ESymmetryMode.NONE)
				{
					getManagers().getToolsManager().setSymmetryMode(ESymmetryMode.NONE);
				}
				else
				{
					getManagers().getToolsManager().setSymmetryMode(ESymmetryMode.X);
				}
			}
		});
		mSymmetrySwitcher.setOnLongClickListener(new View.OnLongClickListener()
		{
			@Override
			public boolean onLongClick(View v)
			{
				// Utils.StartMyActivity(RendererMainPanel.this, truesculpt.ui.panels.ToolsPanel.class, false);
				return false;
			}
		});
		mSymmetrySwitcher.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1)
			{
				if ((arg1.getAction() & MotionEvent.ACTION_MASK) != MotionEvent.ACTION_UP)
				{

					mBigTextOverlay.setTextColor(Color.WHITE);
					String text = "Enable symmetry";
					if (getManagers().getToolsManager().getSymmetryMode() != ESymmetryMode.NONE)// new mode not set yet
					{
						text = "Disable symmetry";
					}
					mBigTextOverlay.setText(text);
				}
				else
				{
					HideBigTextOverlay();
				}
				return false;
			}
		});

		mColorShow = (ColorShowView) findViewById(R.id.ColorShowView);
		mColorShow.SetDoubleClickListener(new ColorShowView.OnDoubleClickListener()
		{
			@Override
			public void onDoubleClick(int color)
			{
				HideAllOptionnalTools();
				getManagers().getToolsManager().setColor(color, false, true);
				UtilsManager.ShowHSLColorPickerDialog(RendererMainPanel.this);
			}
		});
		mColorShow.SetColorChangeListener(new OnColorChangedListener()
		{
			@Override
			public void colorChangeStart(int color)
			{
				SetBigTextOverlayToDragMe();
				mBigTextOverlay.setTextColor(color);
				// getManagers().getToolsManager().SetUndoInitialState();
			}

			@Override
			public void colorChanged(int color)
			{
				if (getManagers().getToolsManager().getColor() != color)
				{
					mBigTextOverlay.setText("Color");
					mBigTextOverlay.setTextColor(color);
					getManagers().getToolsManager().setColor(color, false, true);
				}
			}

			@Override
			public void colorChangeStop(int color)
			{
				HideBigTextOverlay();
				// getManagers().getToolsManager().AddUndoToolAction();
			}
		});

		mRadius = (SliderPickView) findViewById(R.id.RadiusPicker);
		mRadius.setText("Radius");
		mRadius.setMaxValue(100);
		mRadius.setMinValue(0);
		mRadius.SetCircleBackColor(mRadiusBkColor);
		mRadius.setSliderChangeListener(new OnSliderPickChangedListener()
		{
			@Override
			public void sliderChangeStart(float value)
			{
				SetBigTextOverlayToDragMe();
				mRadiusSeekBar.setVisibility(mRadiusSeekBar.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
				// getManagers().getToolsManager().SetUndoInitialState();
			}

			@Override
			public void sliderValueChanged(float value)
			{
				UpdateRadiusBigTextOverlay(value);
				getManagers().getToolsManager().setRadius(value, false);
			}

			@Override
			public void sliderChangeStop(float value)
			{
				HideBigTextOverlay();
				// getManagers().getToolsManager().AddUndoToolAction();
			}
		});
		mRadius.SetDoubleClickListener(new OnDoubleClickListener()
		{
			@Override
			public void onDoubleClick(float value)
			{
				HideAllOptionnalTools();
				getManagers().getToolsManager().setRadius(value, false);
				Utils.StartMyActivity(RendererMainPanel.this, truesculpt.ui.panels.ToolsPanel.class, false);
			}
		});

		mRadiusSeekBar = (SeekBar) findViewById(R.id.RadiusBar);
		mRadiusSeekBar.setMax(100);// 0 to 100 pct
		mRadiusSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
		{
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
			{
				UpdateRadiusBigTextOverlay(progress);
				getManagers().getToolsManager().setRadius(progress, false);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar)
			{

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar)
			{
				getManagers().getToolsManager().setRadius(seekBar.getProgress(), false);
				mRadiusSeekBar.setVisibility(View.GONE);
				HideBigTextOverlay();
			}
		});

		mStrength = (SliderPickView) findViewById(R.id.StrengthPicker);
		mStrength.setText("Strength");
		mStrength.setMaxValue(100);
		mStrength.setMinValue(0);
		mStrength.setSliderChangeListener(new OnSliderPickChangedListener()
		{
			@Override
			public void sliderChangeStart(float value)
			{

				SetBigTextOverlayToDragMe();
				mStrengthSeekBar.setVisibility(mStrengthSeekBar.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
				// getManagers().getToolsManager().SetUndoInitialState();
			}

			@Override
			public void sliderValueChanged(float value)
			{
				UpdateStrengthBigTextOverlay(value);
				getManagers().getToolsManager().setStrengthAbsoluteValue(value, false);
			}

			@Override
			public void sliderChangeStop(float value)
			{
				HideBigTextOverlay();
				// getManagers().getToolsManager().AddUndoToolAction();
			}
		});
		mStrength.SetDoubleClickListener(new OnDoubleClickListener()
		{
			@Override
			public void onDoubleClick(float value)
			{
				HideAllOptionnalTools();
				getManagers().getToolsManager().setStrengthAbsoluteValue(value, false);
				boolean bIsPositive = getManagers().getToolsManager().isStrengthPositive();
				getManagers().getToolsManager().setStrengthSignum(!bIsPositive, false);
			}
		});

		mStrengthSeekBar = (SeekBar) findViewById(R.id.StrengthBar);
		mStrengthSeekBar.setMax(200);// -100 to 100 pct
		mStrengthSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
		{
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
			{
				UpdateStrengthBigTextOverlay(progress - 100);
				getManagers().getToolsManager().setStrength(progress - 100, false);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar)
			{

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar)
			{
				getManagers().getToolsManager().setStrength(seekBar.getProgress() - 100, false);
				mStrengthSeekBar.setVisibility(View.GONE);
				HideBigTextOverlay();
			}
		});

		mFilesBtn = (ImageButton) findViewById(R.id.FilesBtn);
		mFilesBtn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Utils.StartMyActivity(RendererMainPanel.this, truesculpt.ui.panels.FileSelectorPanel.class, false);
			}
		});
		mFilesBtn.setOnLongClickListener(new View.OnLongClickListener()
		{
			@Override
			public boolean onLongClick(View v)
			{
				Utils.StartMyActivity(RendererMainPanel.this, truesculpt.ui.panels.DebugPanel.class, false);
				return false;
			}
		});

		mToolsSlidingDrawer.open();

		mCoverFlow.setVisibility(View.GONE);
		HideAllOptionnalTools();

		UpdateGLView();
		UpdateButtonsView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();

		getManagers().getPointOfViewManager().deleteObserver(RendererMainPanel.this);
		getManagers().getMeshManager().deleteObserver(RendererMainPanel.this);
		getManagers().getToolsManager().deleteObserver(RendererMainPanel.this);
		getManagers().getActionsManager().deleteObserver(this);

		getManagers().Destroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		// Handle the back button
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			/*
			 * if (mToolsSlidingDrawer.isOpened()) { mToolsSlidingDrawer.close(); } else
			 */
			{
				// Ask the user if they want to quit
				new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle(R.string.quit).setMessage(R.string.really_quit).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						// save last used file
						getManagers().getOptionsManager().setLastUsedFile(getManagers().getMeshManager().getName());

						finish();
						System.exit(0);
					}
				}).setNegativeButton(R.string.no, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
					}
				}).setNeutralButton(R.string.save, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						Utils.StartMyActivity(RendererMainPanel.this, truesculpt.ui.panels.SaveFilePanel.class, false);
					}
				}).show();
			}
			return true;
		}
		else
		{
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.show_files:
		{
			Utils.StartMyActivity(this, truesculpt.ui.panels.FileSelectorPanel.class, false);
			return true;
		}

		case R.id.show_options:
		{
			Utils.StartMyActivity(this, truesculpt.ui.panels.OptionsPanel.class, false);
			return true;
		}

		case R.id.show_about_panel:
		{
			Utils.StartMyActivity(this, truesculpt.ui.panels.AboutPanel.class, false);
			return true;
		}
		/*
		 * case R.id.show_tools_panel: { commuteSliderState(); //Utils.StartMyActivity(this, truesculpt.ui.panels.ToolsPanel.class, false); return true; } case R.id.show_history: { Utils.StartMyActivity(this, truesculpt.ui.panels.HistoryPanel.class, false); return true; } case R.id.show_point_of_view_panel: { Utils.StartMyActivity(this, truesculpt.ui.panels.PointOfViewPanel.class, false); return true; } case R.id.quit: { this.finish(); return true; }
		 */
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onPause()
	{
		super.onPause();

		getManagers().getSleepPowerManager().stop();

		// not needed for glsurfaceView since render is on request
		// TODO pause sensors
		// if (mGLSurfaceView != null)
		// mGLSurfaceView.onPause();
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		getManagers().getSleepPowerManager().restart();

		if (mGLSurfaceView != null)
		{
			mGLSurfaceView.onResume();
			UpdateGLView();
		}
	}

	@Override
	protected void onStop()
	{
		super.onStop();

		getManagers().getSleepPowerManager().stop();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		HideAllOptionnalTools();
		int nRes = getManagers().getTouchManager().onTouchEvent(event);
		switch (nRes)
		{
		case 1:// Tap tap commuteSliderState();
			break;
		}
		return super.onTouchEvent(event);
	}

	private void commuteSliderState()
	{
		if (mToolsSlidingDrawer.isOpened())
		{
			mToolsSlidingDrawer.close();
		}
		else
		{
			mToolsSlidingDrawer.open();
		}
	}

	public void ShowSplashScreen()
	{
		if (getManagers().getOptionsManager().getDisplaySplashScreenAtStartup() == true)
		{
			Utils.StartMyActivity(this, truesculpt.ui.panels.SplashPanel.class, false);
		}
	}

	public void ShowTutorial()
	{
		if (getManagers().getOptionsManager().getViewTutorialAtStartup() == true)
		{
			Utils.StartMyActivity(this, truesculpt.ui.panels.TutorialOverlayPanel.class, false);
		}
	}

	@Override
	public void update(Observable observable, Object data)
	{
		UpdateGLView();
		UpdateButtonsView();
	}

	private void UpdateButtonsView()
	{
		if (getManagers().getActionsManager().GetUndoActionCount() <= 0)
		{
			mUndoButton.setEnabled(false);
			mUndoButton.setVisibility(View.GONE);
		}
		else
		{
			mUndoButton.setEnabled(true);
			mUndoButton.setVisibility(View.VISIBLE);
		}

		if (getManagers().getActionsManager().GetRedoActionCount() <= 0)
		{
			mRedoButton.setEnabled(false);
			mRedoButton.setVisibility(View.GONE);
		}
		else
		{
			mRedoButton.setEnabled(true);
			mRedoButton.setVisibility(View.VISIBLE);
		}

		BaseTool currTool = getManagers().getToolsManager().getCurrentTool();

		mColorShow.setColor(getManagers().getToolsManager().getColor());
		mColorShow.setVisibility(currTool.RequiresColor() ? View.VISIBLE : View.GONE);

		mToolPicker.setCurrentValue(getManagers().getToolsManager().getCurrentToolIndex(), getManagers().getToolsManager().getCurrentTool().GetIcon(), getManagers().getToolsManager().getCurrentTool().GetName());
		mCoverFlow.setSelection(getManagers().getToolsManager().getCurrentToolIndex(), true);

		mRadius.setCurrentValue(getManagers().getToolsManager().getRadius());
		mRadius.setVisibility(currTool.RequiresRadius() ? View.VISIBLE : View.GONE);

		mRadiusSeekBar.setProgress((int) getManagers().getToolsManager().getRadius());
		if (!currTool.RequiresRadius()) mRadiusSeekBar.setVisibility(View.GONE);

		mStrength.setCurrentValue(getManagers().getToolsManager().getStrengthAbsoluteValue());
		int strengthColor = Color.RED;
		if (getManagers().getToolsManager().isStrengthPositive()) strengthColor = Color.BLUE;
		mStrength.SetCircleBackColor(strengthColor);
		mStrength.setVisibility(currTool.RequiresStrength() ? View.VISIBLE : View.GONE);

		mStrengthSeekBar.setProgress((int) getManagers().getToolsManager().getStrength() + 100);
		if (!currTool.RequiresStrength()) mStrengthSeekBar.setVisibility(View.GONE);

		mSymmetrySwitcher.setChecked(getManagers().getToolsManager().getSymmetryMode() != ESymmetryMode.NONE);
		mSymmetrySwitcher.setVisibility(currTool.RequiresSymmetry() ? View.VISIBLE : View.GONE);

		mToolsSlidingDrawer.requestLayout();
	}

	public void HideAllOptionnalTools()
	{
		mStrengthSeekBar.setVisibility(View.GONE);
		mRadiusSeekBar.setVisibility(View.GONE);
		HideBigTextOverlay();
	}

	private void HideBigTextOverlay()
	{
		mBigTextOverlay.setText("");
	}

	private void UpdateGLView()
	{
		mGLSurfaceView.requestRender();
	}

	private void SetBigTextOverlayToDragMe()
	{
		mBigTextOverlay.setTextColor(Color.WHITE);

		mBigTextOverlay.setText("Drag to adjust\nOr\nDouble click");
	}

	private void UpdateRadiusBigTextOverlay(float value)
	{
		if (value != getManagers().getToolsManager().getRadius())
		{
			mBigTextOverlay.setTextColor(mRadiusBkColor);
			mBigTextOverlay.setText(Integer.toString((int) getManagers().getToolsManager().getRadius()) + " %\nRadius");
		}
	}

	private void UpdateStrengthBigTextOverlay(float value)
	{
		if (value != getManagers().getToolsManager().getStrength())
		{
			int textColor = Color.RED;
			if (getManagers().getToolsManager().isStrengthPositive())
			{
				textColor = Color.BLUE;
			}
			mBigTextOverlay.setTextColor(textColor);

			String strDirection = getManagers().getToolsManager().isStrengthPositive() ? "Additive" : "Subtractive";
			mBigTextOverlay.setText(Integer.toString((int) getManagers().getToolsManager().getStrengthAbsoluteValue()) + " %\n" + strDirection + "\nStrength");
		}
	}

	private void UpdateToolBigTextOverlay(int value)
	{
		mBigTextOverlay.setTextColor(Color.WHITE);
		mBigTextOverlay.setText(getManagers().getToolsManager().GetToolAtIndex(value).GetName());
	}
}