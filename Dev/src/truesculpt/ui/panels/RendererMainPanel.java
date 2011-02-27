package truesculpt.ui.panels;

import java.util.Observable;
import java.util.Observer;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.main.TrueSculptApp;
import truesculpt.managers.ToolsManager.ESymmetryMode;
import truesculpt.ui.dialogs.ColorPickerDialog.OnColorChangedListener;
import truesculpt.ui.views.ColorShowView;
import truesculpt.ui.views.SliderPickView;
import truesculpt.ui.views.SliderPickView.OnDoubleClickListener;
import truesculpt.ui.views.SliderPickView.OnSliderPickChangedListener;
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
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SlidingDrawer;
import android.widget.Spinner;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;

public class RendererMainPanel extends Activity implements Observer
{
	private GLSurfaceView mGLSurfaceView = null;
	private SlidingDrawer mToolsSlidingDrawer;
	private ImageButton mRedoButton;
	private ImageButton mUndoButton;
	private ColorShowView mColorShow;
	private Spinner mToolSpinner;
	private SliderPickView mRadius;
	private SliderPickView mStrength;
	private ImageButton mResetPOVbutton;	
	private ToggleButton mSymmetrySwitcher;

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
		
		ShowSplashScreen();
		getManagers().getUtilsManager().CheckUpdate(getBaseContext());
		ShowTutorial();

		updateFullscreenWindowStatus();

		setContentView(R.layout.main);

		mGLSurfaceView = (GLSurfaceView) findViewById(R.id.glview);
		mGLSurfaceView.setDebugFlags(GLSurfaceView.DEBUG_CHECK_GL_ERROR);
		mGLSurfaceView.setRenderer(getManagers().getRendererManager().getMainRenderer());
		mGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

		UpdateGLView();

		getManagers().getPointOfViewManager().addObserver(RendererMainPanel.this);
		getManagers().getMeshManager().addObserver(RendererMainPanel.this);
		getManagers().getToolsManager().addObserver(RendererMainPanel.this);
		getManagers().getActionsManager().addObserver(this);
	
		mToolsSlidingDrawer = (SlidingDrawer) findViewById(R.id.toolsSlidingDrawer);
		mToolsSlidingDrawer.setOnDrawerOpenListener(new OnDrawerOpenListener() 
		{
			@Override
			public void onDrawerOpened() {
				//mToolsSlideHandleButton.setBackgroundResource(R.drawable.down_arrow);
			}
		});
		mToolsSlidingDrawer.setOnDrawerCloseListener(new OnDrawerCloseListener()
		{
			@Override
			public void onDrawerClosed() {
				//mToolsSlideHandleButton.setBackgroundResource(R.drawable.up_arrow);
			}
		});
		
		mToolSpinner = (Spinner) findViewById(R.id.SculptToolSpinner);
		ToolsPanel.InitToolSpinner(mToolSpinner,this);
		
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
				Utils.StartMyActivity(RendererMainPanel.this, truesculpt.ui.panels.HistoryPanel.class, false);
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
				Utils.StartMyActivity(RendererMainPanel.this, truesculpt.ui.panels.PointOfViewPanel.class, false);
				return false;
			}
		});
		
		mSymmetrySwitcher= (ToggleButton) findViewById(R.id.SymmetrySwitcher);
		mSymmetrySwitcher.setOnClickListener(new View.OnClickListener()
		{	
			@Override
			public void onClick(View v)
			{
				if (getManagers().getToolsManager().getSymmetryMode()!=ESymmetryMode.NONE)
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
				Utils.StartMyActivity(RendererMainPanel.this, truesculpt.ui.panels.ToolsPanel.class, false);
				return false;
			}
		});
		
		
		mColorShow = (ColorShowView) findViewById(R.id.ColorShowView);
		mColorShow.SetDoubleClickListener(new ColorShowView.OnDoubleClickListener()
		{
			@Override
			public void onDoubleClick(int color)
			{
				getManagers().getToolsManager().setColor(color,false);	
				getManagers().getUtilsManager().ShowHSLColorPickerDialog(RendererMainPanel.this);
			}
		});
		mColorShow.SetColorChangeListener(new OnColorChangedListener()
		{			
			@Override
			public void colorChanged(int color)
			{
				getManagers().getToolsManager().setColor(color,false);				
			}

			@Override
			public void colorChangeStart()
			{			
				getManagers().getToolsManager().SetUndoInitialState();
			}

			@Override
			public void colorChangeStop(int color)
			{
				getManagers().getToolsManager().AddUndoToolAction();
			}
		});
		
		mRadius = (SliderPickView)findViewById(R.id.RadiusPicker);
		mRadius.setText("Radius");
		mRadius.setMaxValue(100);
		mRadius.setMinValue(0);
		mRadius.setSliderChangeListener(new OnSliderPickChangedListener()
		{			
			@Override
			public void sliderValueChanged(float value)
			{
				getManagers().getToolsManager().setRadius(value,false);				
			}

			@Override
			public void sliderChangeStart()
			{			
				getManagers().getToolsManager().SetUndoInitialState();
			}

			@Override
			public void sliderChangeStop(float value)
			{
				getManagers().getToolsManager().setRadius(value,false);	
				getManagers().getToolsManager().AddUndoToolAction();
			}
		});
		
		mStrength = (SliderPickView)findViewById(R.id.StrengthPicker);
		mStrength.setText("Strength");
		mStrength.setMaxValue(100);
		mStrength.setMinValue(0);
		mStrength.setSliderChangeListener(new OnSliderPickChangedListener()
		{			
			@Override
			public void sliderValueChanged(float value)
			{
				getManagers().getToolsManager().setStrengthAbsoluteValue(value,false);				
			}

			@Override
			public void sliderChangeStart()
			{				
				getManagers().getToolsManager().SetUndoInitialState();
			}

			@Override
			public void sliderChangeStop(float value)
			{
				getManagers().getToolsManager().AddUndoToolAction();
			}
		});
		mStrength.SetDoubleClickListener(new OnDoubleClickListener()
		{			
			@Override
			public void onDoubleClick()
			{
				boolean bIsPositive=getManagers().getToolsManager().isStrengthPositive();
				getManagers().getToolsManager().setStrengthSignum(!bIsPositive, false);				
			}
		});
		
		mToolsSlidingDrawer.open();
		
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
			if (mToolsSlidingDrawer.isOpened())
			{
				mToolsSlidingDrawer.close();
			}
			else
			{
				// Ask the user if they want to quit
				new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle(R.string.quit).setMessage(R.string.really_quit).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						finish();
						System.exit(0);
					}
				}).setNegativeButton(R.string.no, null).show();
			}
			return true;
		} else
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
		case R.id.show_tools_panel:
		{
			commuteSliderState();
			//Utils.StartMyActivity(this, truesculpt.ui.panels.ToolsPanel.class, false);
			return true;
		}
		case R.id.show_history:
		{
			Utils.StartMyActivity(this, truesculpt.ui.panels.HistoryPanel.class, false);
			return true;
		}			
	 	case R.id.show_point_of_view_panel: 
	 	{ 
	 		Utils.StartMyActivity(this, truesculpt.ui.panels.PointOfViewPanel.class, false); 
	 		return true; 
	 	} 
	 	case R.id.quit:
	 	{ 
	 		this.finish();
	 		return true;
	 	}
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
		int nRes = getManagers().getTouchManager().onTouchEvent(event);
		switch (nRes)
		{
		case 1://Tap tap
			commuteSliderState();
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
			Utils.StartMyActivity(this, truesculpt.ui.panels.TutorialWizardPanel.class, false);
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
		if (getManagers().getActionsManager().GetUndoActionCount()<=0) 
		{
			mUndoButton.setEnabled(false);			
		}
		else
		{
			mUndoButton.setEnabled(true);
		}		
		
		if (getManagers().getActionsManager().GetRedoActionCount()<=0) 
		{
			mRedoButton.setEnabled(false);			
		}
		else
		{
			mRedoButton.setEnabled(true);
		}
		
		mColorShow.setColor(getManagers().getToolsManager().getColor());
		
		ToolsPanel.UpdateToolSpinner(mToolSpinner,this);
		
		mRadius.setCurrentValue(getManagers().getToolsManager().getRadius());
		
		mStrength.setCurrentValue(getManagers().getToolsManager().getStrengthAbsoluteValue());
		int strengthColor=Color.RED;
		if (getManagers().getToolsManager().isStrengthPositive()) strengthColor=Color.BLUE;
		mStrength.SetCircleBackColor(strengthColor);
		
		mSymmetrySwitcher.setChecked(getManagers().getToolsManager().getSymmetryMode()!=ESymmetryMode.NONE);
	}	

	public void updateFullscreenWindowStatus()
	{
		if (getManagers().getOptionsManager().getFullScreenApplication())
		{
			this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
	}

	private void UpdateGLView()
	{
		mGLSurfaceView.requestRender();
	}
}