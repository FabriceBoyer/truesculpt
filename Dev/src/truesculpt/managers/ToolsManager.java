package truesculpt.managers;

import java.util.Vector;
import android.content.Context;

public class ToolsManager extends BaseManager {	
	
	public enum EToolMode { POV, SCULPT};
	
	private EToolMode mMode=EToolMode.POV;
	private int mColor=0;
	private float mStrength=50.0f;//pct
	private float mRadius=50.0f;//pct

	public ToolsManager(Context baseContext) {
		super(baseContext);
		// TODO Auto-generated constructor stub
	}

	private void NotifyListeners()
	{		
		for (OnToolChangeListener listener : mListeners) 
		{
			listener.onToolChange();		
		}	
	}
	
	public void registerToolChangeListener(OnToolChangeListener listener)
	{
		mListeners.add(listener);	
	}
	
	public void unRegisterToolChangeListener(OnToolChangeListener listener)
	{
		mListeners.remove(listener);	
	}

	public interface OnToolChangeListener
	{
		void onToolChange();
	}
	private Vector<OnToolChangeListener> mListeners= new Vector<OnToolChangeListener>();

	
	
	public EToolMode getToolMode() {
		return mMode;
	}
	public void setToolMode(EToolMode mMode) {
		this.mMode = mMode;		
		NotifyListeners();
	}

	public float getStrength() {
		return mStrength;
	}
	public void setStrength(float mStrength) {
		this.mStrength = mStrength;
		NotifyListeners();
	}
	
	public float getRadius() {
		return mRadius;
	}
	public void setRadius(float mRadius) {
		this.mRadius = mRadius;
		NotifyListeners();
	}
	
	public int getColor() {
		return mColor;
	}
	public void setColor(int mColor) {
		this.mColor = mColor;
		NotifyListeners();
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
	}	

}
