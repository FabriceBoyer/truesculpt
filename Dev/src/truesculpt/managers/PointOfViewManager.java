package truesculpt.managers;

import truesculpt.tools.base.SculptingTool;
import android.content.Context;

public class PointOfViewManager extends BaseManager
{
	// looked from point
	private float mR = 0.0f;

	// to be recomputed to adapt to max size of object
	private float mRmax = 9.0f;
	private float mRmin = 0.1f;
	private final float mRDefault = 3.5f;// sphere is 1, zNear is 1 + margin for tool overlay
	private float mHead = 0.0f;
	private float mPitch = 0.0f;
	private float mRoll = 0.0f;

	private float mXPanOffset = 0.0f;
	private float mYPanOffset = 0.0f;

	public PointOfViewManager(Context baseContext)
	{
		super(baseContext);
	}

	public float getHeadAngle()
	{
		return mHead;
	}

	public float getRollAngle()
	{
		return mRoll;
	}

	public float getPitchAngle()
	{
		return mPitch;
	}

	public float getRmax()
	{
		return mRmax;
	}

	public float getRmin()
	{
		return mRmin;
	}

	public float getZoomDistance()
	{
		return mR;
	}

	@Override
	public void NotifyListeners()
	{
		getManagers().getRendererManager().getMainRenderer().onPointOfViewChange();
		super.NotifyListeners();
	}

	@Override
	public void onCreate()
	{
		resetPOV();
	}

	@Override
	public void onDestroy()
	{

	}

	public void resetPOV()
	{
		SetAllAngles(5, 0, 0);
		setZoomDistance(mRDefault);
		setPanOffset(0, 0);
	}

	public void SetAllAngles(float head, float pitch, float roll)
	{
		setHeadAngleInternal(head);
		setPitchAngleInternal(pitch);
		setRollAngleInternal(roll);
		NotifyListeners();
	}

	public void setPitchAngle(float angle)
	{
		setPitchAngleInternal(angle);
		NotifyListeners();
	}

	private void setPitchAngleInternal(float angle)
	{
		mPitch = angle % 360;
	}

	public void setRollAngle(float angle)
	{
		setRollAngleInternal(angle);
		NotifyListeners();
	}

	private void setRollAngleInternal(float angle)
	{
		mRoll = angle % 360;
	}

	public void setRmax(float mRmax)
	{
		this.mRmax = mRmax;
		setZoomDistance(mR);// refresh distance with saturations and notify
	}

	public void setRmin(float mRmin)
	{
		this.mRmin = mRmin + SculptingTool.MAX_DEFORMATION + 0.1f;// near is 0.1f meters
		setZoomDistance(mR);// refresh distance with saturations and notify
	}

	public void setHeadAngle(float angle)
	{
		setHeadAngleInternal(angle);
		NotifyListeners();
	}

	private void setHeadAngleInternal(float angle)
	{
		mHead = angle % 360;
	}

	public void setZoomDistance(float dist)
	{
		setZoomDistanceInternal(dist);
		NotifyListeners();
	}

	private void setZoomDistanceInternal(float dist)
	{
		mR = dist;

		if (dist > mRmax)
		{
			mR = mRmax;
		}
		if (dist < mRmin)
		{
			mR = mRmin;
		}
	}

	// TODO Saturate values
	public void setPanOffset(float x, float y)
	{
		mXPanOffset = x;
		mYPanOffset = y;
		NotifyListeners();
	}

	public float getYPanOffset()
	{
		return mYPanOffset;
	}

	public float getXPanOffset()
	{
		return mXPanOffset;
	}

	@Override
	public String toString()
	{
		return String.format("%.2f", mHead) + " / " + String.format("%.2f", mPitch) + " / " + String.format("%.2f", mRoll);
	}

}
