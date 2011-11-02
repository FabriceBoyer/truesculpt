package truesculpt.managers;

import java.util.ArrayList;

import truesculpt.actions.ChangeToolAction;
import truesculpt.tools.ColorizeTool;
import truesculpt.tools.PickColorTool;
import truesculpt.tools.RiseTool;
import truesculpt.tools.ToolsBase;
import truesculpt.utils.Utils;
import android.content.Context;
import android.graphics.Color;

public class ToolsManager extends BaseManager
{
	public enum EPovToolSubMode
	{
		PAN, ROTATE, ZOOM
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
	private ESculptToolSubMode mSculptSubMode = ESculptToolSubMode.DRAW;
	private ESymmetryMode mSymmetryMode = ESymmetryMode.NONE;
	private float mRadius = 50.0f;// pct
	private float mStrength = 50.0f;// pct
	private ToolsBase mCurrentTool = null;
	private final ArrayList<ToolsBase> mToolsLibrary = new ArrayList<ToolsBase>();

	public ToolsManager(Context baseContext)
	{
		super(baseContext);

		// TODO load from plugins or xml library
		mToolsLibrary.add(new RiseTool(getManagers()));
		mToolsLibrary.add(new ColorizeTool(getManagers()));
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
		case SMOOTH:
		case INFLATE:
		case COLOR:
		case TEXTURE:
		case PICK_COLOR:
			break;
		default:
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

	public void setColor(int color, boolean bAddUndoAction)
	{
		if (this.mColor != color)
		{
			// if (bAddUndoAction) SetUndoInitialState();
			this.mColor = color;
			// if (bAddUndoAction) AddUndoToolAction();

			// Force mode if you change color
			this.mSculptSubMode = ESculptToolSubMode.COLOR;

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
	public int getDefaultColor()
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
		if (mStrength < 0)
			sign = -1;
		setStrength(sign * Math.abs(value), bAddUndoAction);
	}

	public void setStrengthSignum(boolean bIsPositive, boolean bAddUndoAction)
	{
		float sign = -1;
		if (bIsPositive)
			sign = 1;
		setStrength(Math.abs(mStrength) * sign, bAddUndoAction);
	}

	public boolean isStrengthPositive()
	{
		return mStrength > 0;
	}
}
