package truesculpt.managers;

import truesculpt.actions.ChangeToolAction;
import truesculpt.utils.Utils;
import android.content.Context;
import android.graphics.Color;



public class ToolsManager extends BaseManager
{
	public enum EPovToolSubMode {
		PAN, ROTATE, ZOOM
	};

	public enum ESculptToolSubMode {
		DRAW, GRAB, INFLATE, SMOOTH, COLOR, TEXTURE, PICK_COLOR		
	};
	
	public enum EToolMode {
		POV, SCULPT
	};
	
	public enum ESymmetryMode {
		NONE,X,Y,Z
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
		
		
		public String toString() 
		{
			String msg="";
			msg=m_toolmode+"/"+
			m_subSculptTool+"/"+
			m_subPOVTool+"/"+
			m_symmetrymode+"/"+
			Utils.ColorIntToString(m_Color)+"/"+
			Float.toString(m_Radius)+"/"+
			Float.toString(m_Strength);
			return msg;		
		}
	};

	private int mColor = Color.rgb(255, 0, 0);
	private EToolMode mMode = EToolMode.POV;
	private EPovToolSubMode mPovSubMode = EPovToolSubMode.ROTATE;
	private ESculptToolSubMode mSculptSubMode = ESculptToolSubMode.DRAW;
	private ESymmetryMode mSymmetryMode=ESymmetryMode.NONE;
	private float mRadius = 50.0f;// pct
	private float mStrength = 50.0f;// pct

	public ToolsManager(Context baseContext)
	{
		super(baseContext);
		// TODO Auto-generated constructor stub
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

	@Override
	public void onCreate()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onDestroy()
	{
		// TODO Auto-generated method stub

	}

	public void setColor(int color)
	{
		if (this.mColor!=color)
		{
			GlobalToolState prevState=GetGlobalToolState();
			this.mColor = color;
			getManagers().getActionsManager().AddUndoAction(new ChangeToolAction(GetGlobalToolState(), prevState));
			
			NotifyListeners();
		}
	}

	public void setPovSubMode(EPovToolSubMode mPovSubMode)
	{
		if (this.mPovSubMode != mPovSubMode)
		{
			GlobalToolState prevState=GetGlobalToolState();
			this.mPovSubMode = mPovSubMode;			
			//getManagers().getActionsManager().AddUndoAction(new ChangeToolAction(GetGlobalToolState(), prevState));
			
			NotifyListeners();
		}
	}

	public void setRadius(float radius)
	{
		GlobalToolState prevState=GetGlobalToolState();	
		this.mRadius = radius;
		getManagers().getActionsManager().AddUndoAction(new ChangeToolAction(GetGlobalToolState(), prevState));
		
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

	public void setSculptSubMode(ESculptToolSubMode mSculptSubMode)
	{
		if (this.mSculptSubMode != mSculptSubMode)
		{
			GlobalToolState prevState=GetGlobalToolState();			
			this.mSculptSubMode = mSculptSubMode;		
			getManagers().getActionsManager().AddUndoAction(new ChangeToolAction(GetGlobalToolState(), prevState));
			
			NotifyListeners();
		}
	}
		
	public void setSymmetryMode(ESymmetryMode mSymmetryMode)
	{
		if (this.mSymmetryMode != mSymmetryMode)
		{
			GlobalToolState prevState=GetGlobalToolState();	
			this.mSymmetryMode = mSymmetryMode;		
			getManagers().getActionsManager().AddUndoAction(new ChangeToolAction(GetGlobalToolState(), prevState));
			
			NotifyListeners();
		}
	}

	public void setStrength(float strength)
	{
		GlobalToolState prevState=GetGlobalToolState();	
		this.mStrength = strength;
		getManagers().getActionsManager().AddUndoAction(new ChangeToolAction(GetGlobalToolState(), prevState));

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
			GlobalToolState prevState=GetGlobalToolState();	
			mMode = mode;
			//getManagers().getActionsManager().AddUndoAction(new ChangeToolAction(GetGlobalToolState(), prevState));
			
			NotifyListeners();
		}
	}

	public void TakeGLScreenshot()
	{
		getManagers().getRendererManager().getMainRenderer().TakeGLScreenshotOfNextFrame();
		NotifyListeners();
	}
	
	public GlobalToolState GetGlobalToolState()
	{
		GlobalToolState state=new GlobalToolState();
		state.m_toolmode=mMode;
		state.m_subPOVTool=mPovSubMode;
		state.m_subSculptTool=mSculptSubMode;
		state.m_symmetrymode=mSymmetryMode;
		state.m_Color=mColor;
		state.m_Radius=mRadius;
		state.m_Strength=mStrength;
		return state;		
	}

	//TODO store data with this class?
	public void SetGlobalToolState(GlobalToolState state)
	{
		mMode=state.m_toolmode;
		mPovSubMode=state.m_subPOVTool;
		mSculptSubMode=state.m_subSculptTool;
		mSymmetryMode=state.m_symmetrymode;	
		mColor=state.m_Color;
		mRadius=state.m_Radius;
		mStrength=state.m_Strength;
	}

	//mesh init color
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
}
