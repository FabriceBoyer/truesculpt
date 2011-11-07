package truesculpt.managers;

import java.util.ArrayList;

import truesculpt.actions.ChangeToolAction;
import truesculpt.tools.base.ToolsBase;
import truesculpt.tools.other.PickColorTool;
import truesculpt.tools.other.SelectionTool;
import truesculpt.tools.painting.ColorizeTool;
import truesculpt.tools.sculpting.GrabTool;
import truesculpt.tools.sculpting.InflateTool;
import truesculpt.tools.sculpting.RiseTool;
import truesculpt.tools.sculpting.SmoothTool;
import truesculpt.utils.Utils;
import android.content.Context;
import android.graphics.Color;

public class ToolsManager extends BaseManager
{
	public enum EPovToolSubMode
	{
		ROTATE, ZOOM_AND_PAN
	};

	public enum ESculptToolSubMode
	{
		DRAW, GRAB, SMOOTH, INFLATE, COLOR, TEXTURE, PICK_COLOR
	};

	public enum EToolMode
	{
		POV, SCULPT
	};

	public enum ESymmetryMode
	{
		NONE, X, Y, Z, XY, XZ, YZ
	};

	public class GlobalToolState
	{
		EToolMode m_toolmode;
		ESculptToolSubMode m_subSculptTool;
		EPovToolSubMode m_subPOVTool;
		ESymmetryMode m_symmetrymode;
		int m_Color;
		float m_Radius;
		float m_Strength;

		@Override
		public String toString()
		{
			String msg = "";
			msg = m_toolmode + "/" + m_subSculptTool + "/" + m_subPOVTool + "/" + m_symmetrymode + "/" + Utils.ColorIntToString(m_Color) + "/" + Float.toString(m_Radius) + "/" + Float.toString(m_Strength);
			return msg;
		}
	};

	private int mColor = Color.rgb(255, 0, 0);
	private EToolMode mMode = EToolMode.POV;
	private EPovToolSubMode mPovSubMode = EPovToolSubMode.ROTATE;
	private ESculptToolSubMode mSculptSubMode = ESculptToolSubMode.INFLATE;
	private ESymmetryMode mSymmetryMode = ESymmetryMode.NONE;
	private float mRadius = 20.0f;// pct
	private float mStrength = 30.0f;// pct
	private ToolsBase mCurrentTool = null;
	private final ArrayList<ToolsBase> mToolsLibrary = new ArrayList<ToolsBase>();

	public ToolsManager(Context baseContext)
	{
		super(baseContext);

		// TODO load from plugins or xml library
		mToolsLibrary.add(new RiseTool(getManagers()));
		mToolsLibrary.add(new GrabTool(getManagers()));
		mToolsLibrary.add(new SmoothTool(getManagers()));
		mToolsLibrary.add(new InflateTool(getManagers()));
		mToolsLibrary.add(new ColorizeTool(getManagers()));
		mToolsLibrary.add(new SelectionTool(getManagers()));// temp for test, replace texture
		mToolsLibrary.add(new PickColorTool(getManagers()));

		UpdateCurrentTool();
	}

	private void UpdateCurrentTool()
	{
		switch (mSculptSubMode)
		{
		case DRAW:
			mCurrentTool = mToolsLibrary.get(0);
			break;
		case GRAB:
			mCurrentTool = mToolsLibrary.get(1);
			break;
		case SMOOTH:
			mCurrentTool = mToolsLibrary.get(2);
			break;
		case INFLATE:
			mCurrentTool = mToolsLibrary.get(3);
			break;
		case COLOR:
			mCurrentTool = mToolsLibrary.get(4);
			break;
		case TEXTURE:
			mCurrentTool = mToolsLibrary.get(5);
			break;
		case PICK_COLOR:
			mCurrentTool = mToolsLibrary.get(6);
			break;
		}

	}

	public int getColor()
	{
		return mColor;
	}

	public EPovToolSubMode getPovSubMode()
	{
		return mPovSubMode;
	}

	public float getRadius()
	{
		return mRadius;
	}

	public ESculptToolSubMode getSculptSubMode()
	{
		return mSculptSubMode;
	}

	public ESymmetryMode getSymmetryMode()
	{
		return mSymmetryMode;
	}

	public float getStrength()
	{
		return mStrength;
	}

	public EToolMode getToolMode()
	{
		return mMode;
	}

	public ToolsBase getCurrentTool()
	{
		return mCurrentTool;
	}

	@Override
	public void onCreate()
	{

	}

	@Override
	public void onDestroy()
	{

	}

	public void setColor(int color, boolean bAddUndoAction, boolean bChangeMode)
	{
		if (this.mColor != color)
		{
			// if (bAddUndoAction) SetUndoInitialState();
			this.mColor = color;
			// if (bAddUndoAction) AddUndoToolAction();

			if (bChangeMode)
			{
				// Force mode if you change color
				setSculptSubMode(ESculptToolSubMode.COLOR);
			}

			NotifyListeners();
		}
	}

	GlobalToolState prevState = null;

	public void SetUndoInitialState()
	{
		prevState = GetGlobalToolState();
	}

	public void AddUndoToolAction()
	{
		if (prevState != null)
		{
			getManagers().getActionsManager().AddUndoAction(new ChangeToolAction(GetGlobalToolState(), prevState));
		}
	}

	public void setPovSubMode(EPovToolSubMode mPovSubMode)
	{
		if (this.mPovSubMode != mPovSubMode)
		{
			this.mPovSubMode = mPovSubMode;

			NotifyListeners();
		}
	}

	public void setRadius(float radius, boolean bAddUndoAction)
	{
		// if (bAddUndoAction) SetUndoInitialState();
		this.mRadius = radius;
		// if (bAddUndoAction) AddUndoToolAction();

		if (mRadius > 100)
		{
			mRadius = 100;
		}
		if (mRadius < 0)
		{
			mRadius = 0;
		}
		NotifyListeners();
	}

	public void setSculptSubMode(ESculptToolSubMode sculptSubMode)
	{
		if (this.mSculptSubMode != sculptSubMode)
		{
			// SetUndoInitialState();
			this.mSculptSubMode = sculptSubMode;
			// AddUndoToolAction();

			UpdateCurrentTool();

			NotifyListeners();
		}
	}

	public void setSymmetryMode(ESymmetryMode symmetryMode)
	{
		if (this.mSymmetryMode != symmetryMode)
		{
			// SetUndoInitialState();
			this.mSymmetryMode = symmetryMode;
			// AddUndoToolAction();

			NotifyListeners();
		}
	}

	public void setStrength(float strength, boolean bAddUndoAction)
	{
		// if (bAddUndoAction) SetUndoInitialState();
		this.mStrength = strength;
		// if (bAddUndoAction) AddUndoToolAction();

		if (mStrength > 100)
		{
			mStrength = 100;
		}
		if (mStrength < -100)
		{
			mStrength = -100;
		}

		NotifyListeners();
	}

	public void setToolMode(EToolMode mode)
	{
		if (this.mMode != mode)
		{
			mMode = mode;

			NotifyListeners();
		}
	}

	public void TakeGLScreenshot(String strSnapshotName)
	{
		getManagers().getRendererManager().getMainRenderer().TakeGLScreenshotOfNextFrame(strSnapshotName);
		NotifyListeners();
	}

	public GlobalToolState GetGlobalToolState()
	{
		GlobalToolState state = new GlobalToolState();
		state.m_toolmode = mMode;
		state.m_subPOVTool = mPovSubMode;
		state.m_subSculptTool = mSculptSubMode;
		state.m_symmetrymode = mSymmetryMode;
		state.m_Color = mColor;
		state.m_Radius = mRadius;
		state.m_Strength = mStrength;
		return state;
	}

	// TODO store data with this class?
	public void SetGlobalToolState(GlobalToolState state)
	{
		mMode = state.m_toolmode;
		mPovSubMode = state.m_subPOVTool;
		mSculptSubMode = state.m_subSculptTool;
		mSymmetryMode = state.m_symmetrymode;
		mColor = state.m_Color;
		mRadius = state.m_Radius;
		mStrength = state.m_Strength;

		NotifyListeners();
	}

	// mesh init color
	public static int getDefaultColor()
	{
		return Color.rgb(150, 150, 150);
	}

	@Override
	public void NotifyListeners()
	{
		getManagers().getRendererManager().getMainRenderer().onToolChange();
		super.NotifyListeners();
	}

	public float getStrengthAbsoluteValue()
	{
		return Math.abs(mStrength);
	}

	public void setStrengthAbsoluteValue(float value, boolean bAddUndoAction)
	{
		float sign = 1;
		if (mStrength < 0) sign = -1;
		setStrength(sign * Math.abs(value), bAddUndoAction);
	}

	public void setStrengthSignum(boolean bIsPositive, boolean bAddUndoAction)
	{
		float sign = -1;
		if (bIsPositive) sign = 1;
		setStrength(Math.abs(mStrength) * sign, bAddUndoAction);
	}

	public boolean isStrengthPositive()
	{
		return mStrength > 0;
	}
}
