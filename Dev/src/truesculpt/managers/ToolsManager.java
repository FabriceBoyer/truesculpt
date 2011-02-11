package truesculpt.managers;

import android.content.Context;
import android.graphics.Color;

public class ToolsManager extends BaseManager
{

	public enum EPovToolSubMode {
		PAN, ROTATE, ZOOM
	};

	public enum ESculptToolSubMode {
		DRAW, GRAB, INFLATE, SMOOTH
	};

	public enum EToolMode {
		PAINT, POV, SCULPT
	};
	
	public enum ESymmetryMode {
		NONE,X,Y,Z
	};

	private int mColor = Color.rgb(150, 150, 150);
	private boolean mForcedMode = false;
	private EToolMode mLastMode = EToolMode.SCULPT;

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

	public boolean getForcedMode()
	{
		return mForcedMode;
	}

	public EToolMode getLastMode()
	{
		return mLastMode;
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
		this.mColor = color;
		NotifyListeners();
	}

	public void setForcedMode(boolean mForcedMode)
	{
		this.mForcedMode = mForcedMode;
	}

	public void setLastToolMode()
	{
		if (mMode != mLastMode)
		{
			mMode = mLastMode;
			NotifyListeners();
			// don't update mLastMode otherwise oscillate
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

	public void setRadius(float radius)
	{
		this.mRadius = radius;
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
			this.mSculptSubMode = mSculptSubMode;		
			NotifyListeners();
		}
	}
	
	public void setSymmetryMode(ESymmetryMode mSymmetryMode)
	{
		if (this.mSymmetryMode != mSymmetryMode)
		{
			this.mSymmetryMode = mSymmetryMode;		
			NotifyListeners();
		}
	}

	public void setStrength(float strength)
	{
		this.mStrength = strength;

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
		if (mMode != mode)
		{
			mLastMode = mMode;
			mMode = mode;
			NotifyListeners();
		}
	}

	public void TakeGLScreenshot()
	{
		getManagers().getRendererManager().getMainRenderer().TakeGLScreenshotOfNextFrame();
		NotifyListeners();
	}
}
