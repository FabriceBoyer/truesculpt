package truesculpt.managers;

import java.util.ArrayList;

import truesculpt.actions.ChangeToolAction;
import truesculpt.tools.base.BaseTool;
import truesculpt.tools.other.GrabTool;
import truesculpt.tools.other.HighlightTool;
import truesculpt.tools.other.PickColorTool;
import truesculpt.tools.painting.ColorizeTool;
import truesculpt.tools.sculpting.ClayTool;
import truesculpt.tools.sculpting.EmbossTool;
import truesculpt.tools.sculpting.FlattenTool;
import truesculpt.tools.sculpting.InflateTool;
import truesculpt.tools.sculpting.NoiseTool;
import truesculpt.tools.sculpting.PinchTool;
import truesculpt.tools.sculpting.DrawTool;
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
		BaseTool mCurrentTool = null;
		EPovToolSubMode m_subPOVTool;
		ESymmetryMode m_symmetrymode;
		int m_Color;
		float m_Radius;
		float m_Strength;

		@Override
		public String toString()
		{
			String strToolName = "NoTool";
			if (mCurrentTool != null)
			{
				strToolName = mCurrentTool.GetName();
			}
			String msg = "";
			msg = m_toolmode + "/" + strToolName + "/" + m_subPOVTool + "/" + m_symmetrymode + "/" + Utils.ColorIntToString(m_Color) + "/" + Float.toString(m_Radius) + "/" + Float.toString(m_Strength);
			return msg;
		}
	};

	private int mColor = Color.HSVToColor(new float[] { 0f, 0.75f, 0.75f });
	private EToolMode mMode = EToolMode.POV;
	private EPovToolSubMode mPovSubMode = EPovToolSubMode.ROTATE;

	private ESymmetryMode mSymmetryMode = ESymmetryMode.NONE;
	private float mRadius = 20.0f;// pct
	private float mStrength = 30.0f;// pct
	private BaseTool mCurrentTool = null;
	private final ArrayList<BaseTool> mToolsLibrary = new ArrayList<BaseTool>();

	public ToolsManager(Context baseContext)
	{
		super(baseContext);

		InitToolsLibrary();
	}

	private void InitToolsLibrary()
	{
		// TODO load from plugins or xml library
		mToolsLibrary.clear();
		mToolsLibrary.add(new InflateTool(getManagers()));
		mToolsLibrary.add(new DrawTool(getManagers()));
		mToolsLibrary.add(new GrabTool(getManagers()));
		mToolsLibrary.add(new SmoothTool(getManagers()));
		mToolsLibrary.add(new FlattenTool(getManagers()));
		mToolsLibrary.add(new PinchTool(getManagers()));
		mToolsLibrary.add(new ClayTool(getManagers()));
		mToolsLibrary.add(new EmbossTool(getManagers()));
		mToolsLibrary.add(new NoiseTool(getManagers()));
		mToolsLibrary.add(new ColorizeTool(getManagers()));
		// mToolsLibrary.add(new TexturePaintTool(getManagers()));
		mToolsLibrary.add(new PickColorTool(getManagers()));
		// mToolsLibrary.add(new BorderTool(getManagers()));
		mToolsLibrary.add(new HighlightTool(getManagers()));

		mCurrentTool = mToolsLibrary.get(0);// inflate is default
	}

	private void setColorizeToolAsCurrentTool()
	{
		for (BaseTool tool : mToolsLibrary)
		{
			if (tool.GetName() == "Colorize")
			{
				mCurrentTool = tool;
				break;
			}
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

	public BaseTool getCurrentTool()
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
				setColorizeToolAsCurrentTool();
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
		if (mRadius < 1)
		{
			mRadius = 1;
		}
		NotifyListeners();
	}

	public BaseTool GetToolAtIndex(int nIndex)
	{
		BaseTool res = null;
		if (nIndex < mToolsLibrary.size())
		{
			res = mToolsLibrary.get(nIndex);
		}
		return res;
	}

	public void setCurrentTool(int nIndex)
	{
		if (nIndex < mToolsLibrary.size())
		{
			BaseTool tool = mToolsLibrary.get(nIndex);
			if (tool != mCurrentTool)
			{
				// SetUndoInitialState();
				this.mCurrentTool = tool;
				// AddUndoToolAction();

				NotifyListeners();
			}
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
		state.mCurrentTool = mCurrentTool;
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
		mCurrentTool = state.mCurrentTool;
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
		float sign = isStrengthPositive() ? 1 : -1;
		float absValue = Math.abs(value);
		if (absValue < 1)
		{
			absValue = 1;
		}
		setStrength(sign * absValue, bAddUndoAction);
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

	public int getCurrentToolIndex()
	{
		int nRes = -1;
		if (mCurrentTool != null)
		{
			nRes = mToolsLibrary.indexOf(mCurrentTool);
		}
		return nRes;
	}

	public int GetToolsLibrarySize()
	{
		return mToolsLibrary.size();
	}

	public void ClearAll()
	{
		InitToolsLibrary();
	}
}
