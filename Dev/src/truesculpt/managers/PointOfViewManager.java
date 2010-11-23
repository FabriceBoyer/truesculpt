package truesculpt.managers;

import java.util.Vector;

import truesculpt.managers.SensorsManager.OnSensorChangeListener;
import truesculpt.utils.Utils;

import android.content.Context;

public class PointOfViewManager extends BaseManager {

	//looked from point
	private float mR=0.0f;
	private float mTheta=0.0f;
	private float mPhi=0.0f;
	
	private float mRmax=10.0f;
	private float mRmin=1.0f;//to be recomputed to adapt to max size of object
	
	public PointOfViewManager(Context baseContext) {
		super(baseContext);
				
	}		

	@Override
	public void onCreate() {		
		
		ResetPOV();				
	}
	
	@Override
	public void onDestroy() {	
		
		
	}

	public void SetAllAngles(float [] angles)
	{		
		SetAllAngles(angles[0],angles[1],angles[2]);
	}
	
	public void SetAllAngles(float rotation, float elevation, float zoomDistance)
	{
		setElevationAngleInternal(elevation);
		setRotationAngleInternal(rotation);
		setZoomDistanceInternal(zoomDistance);
		NotifyListeners();
	}
	//+90 to -90
	public void setElevationAngle(float angle)
	{		
		setElevationAngleInternal(angle);
		NotifyListeners();
	}

	private void setElevationAngleInternal(float angle) {
		mPhi=angle;
		
		if (angle>90.0f)
		{
			mPhi=90.0f;
		}
		
		if (angle<-90.0f)
		{
			mPhi=-90.0f;
		}
	}
	
	//180 to 180
	public void setRotationAngle(float angle)
	{
		setRotationAngleInternal(angle);
		NotifyListeners();
	}

	private void setRotationAngleInternal(float angle) {
		mTheta=angle%180;	
		if (angle>180.0f)
		{
			mTheta-=180.0f;
		}		
		if (angle<-180.0f)
		{
			mTheta+=180.0f;
		}
	}	
	
	public void setZoomDistance(float dist)
	{		
		setZoomDistanceInternal(dist);
		NotifyListeners();
	}

	private void setZoomDistanceInternal(float dist) {
		mR=dist;
		
		if (dist>mRmax)
		{
			mR=mRmax;
		}
		if (dist < mRmin)
		{
			mR=mRmin;
		}
	}
	
	public float getElevationAngle()
	{		
		return mPhi;
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
		
		for (OnPointOfViewChangeListener listener : mListeners) 
		{
			listener.onPointOfViewChange();		
		}	
	}
	
	public void registerPointOfViewChangeListener(OnPointOfViewChangeListener listener)
	{
		mListeners.add(listener);	
	}
	
	public void unRegisterPointOfViewChangeListener(OnPointOfViewChangeListener listener)
	{
		mListeners.remove(listener);	
	}

	public interface OnPointOfViewChangeListener
	{
		void onPointOfViewChange();
	}
	private Vector<OnPointOfViewChangeListener> mListeners= new Vector<OnPointOfViewChangeListener>();
	
	
	public void onSensorChanged() {
		NotifyListeners();		
	}

	public float getRmax() {
		return mRmax;
	}	

	public float getRmin() {
		return mRmin;
	}	

	public void ResetPOV()
	{
		mR=4.0f;
		mTheta=0.0f;
		mPhi=0.0f;
		
		NotifyListeners();
	}

}
