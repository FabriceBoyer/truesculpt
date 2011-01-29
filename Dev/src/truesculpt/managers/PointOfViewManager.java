package truesculpt.managers;

import android.content.Context;

public class PointOfViewManager extends BaseManager
{
	private float mPhi = 0.0f;

	// looked from point
	private float mR = 0.0f;

	// to be recomputed to adapt to max size of object
	private float mRmax = 9.0f;
	private float mRmin = 2.0f;

	private float mTheta = 0.0f;

	public PointOfViewManager(Context baseContext)
	{
		super(baseContext);

	}

	public float getElevationAngle()
	{
		return mPhi;
	}

	public float getRmax()
	{
		return mRmax;
	}

	public float getRmin()
	{
		return mRmin;
	}

	public float getRotationAngle()
	{
		return mTheta;
	}

	public float getZoomDistance()
	{
		return mR;
	}

	private void NotifyListeners()
	{
		getManagers().getRendererManager().onPointOfViewChange();

		setChanged();
		notifyObservers(this);
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
		mR = 3.0f;
		mTheta = 0.0f;
		mPhi = 0.0f;

		NotifyListeners();
	}

	public void SetAllAngles(float rotation, float elevation, float zoomDistance)
	{
		setElevationAngleInternal(elevation);
		setRotationAngleInternal(rotation);
		setZoomDistanceInternal(zoomDistance);
		NotifyListeners();
	}

	public void SetAllAngles(float[] angles)
	{
		SetAllAngles(angles[0], angles[1], angles[2]);
	}

	// +90 to -90
	public void setElevationAngle(float angle)
	{
		setElevationAngleInternal(angle);
		NotifyListeners();
	}

	private void setElevationAngleInternal(float angle)
	{
		mPhi = angle;

		if (angle > 90.0f)
		{
			mPhi = 90.0f;
		}

		if (angle < -90.0f)
		{
			mPhi = -90.0f;
		}
	}

	public void setRmax(float mRmax)
	{
		this.mRmax = mRmax;
		setZoomDistance(mR);// refresh distance with saturations and notify
	}

	public void setRmin(float mRmin)
	{
		this.mRmin = mRmin;
		setZoomDistance(mR);// refresh distance with saturations and notify
	}

	// 180 to 180
	public void setRotationAngle(float angle)
	{
		setRotationAngleInternal(angle);
		NotifyListeners();
	}

	private void setRotationAngleInternal(float angle)
	{
		mTheta = angle % 180;
		if (angle > 180.0f)
		{
			mTheta -= 180.0f;
		}
		if (angle < -180.0f)
		{
			mTheta += 180.0f;
		}
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

}
