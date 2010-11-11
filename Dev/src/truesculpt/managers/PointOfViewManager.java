package truesculpt.managers;

import java.util.Vector;

import truesculpt.managers.SensorsManager.OnSensorChangeListener;

import android.content.Context;

public class PointOfViewManager extends BaseManager implements OnSensorChangeListener {

	public interface OnPointOfViewChangeListener
	{
		void onPointOfViewChange();
	}
	private Vector<OnPointOfViewChangeListener> mListeners= new Vector<OnPointOfViewChangeListener>();
	//camera pos
	private float mX=0.0f;
	
	//looked at point
	private float mXOrig=0.0f;
	private float mY=0.0f;
	private float mYOrig=0.0f;
	
	private float mZ=10.0f;
	
	private float mZOrig=0.0f;
	
	public PointOfViewManager(Context baseContext) {
		super(baseContext);

		
	}		

	@Override
	public void onCreate() {
		super.onCreate();
		getManagers().getmSensorsManager().registerOnSensorChangeListener(this);
		
	}
	
	@Override
	public void onDestroy() {
		
		super.onDestroy();
	}

	public void addElevationAngle(float angle)
	{
		NotifyListeners();
	}
	
	public void addRotationAngle(float angle)
	{
		NotifyListeners();
	}	
	public void addZoomDistance(float dist)
	{
		NotifyListeners();
	}
	
	private void NotifyListeners()
	{
		for (OnPointOfViewChangeListener listener : mListeners) 
		{
			listener.onPointOfViewChange();		
		}	
	}
	
	public void registerPointOfViewChangeListener(OnPointOfViewChangeListener listener)
	{
		mListeners.add(listener);	
	}

	@Override
	public void onSensorChanged() {
		NotifyListeners();		
	}	


}
