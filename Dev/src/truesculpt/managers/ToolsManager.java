package truesculpt.managers;

import java.util.Vector;

import truesculpt.managers.PointOfViewManager.OnPointOfViewChangeListener;
import android.content.Context;

public class ToolsManager extends BaseManager {	
	
	public enum EToolMode { POV, SCULPT};
	
	private EToolMode mMode=EToolMode.POV;
	private int mColor=0;
	
	public EToolMode getToolMode() {
		return mMode;
	}
	public void setToolMode(EToolMode mMode) {
		this.mMode = mMode;
		
		NotifyListeners();
	}

	public ToolsManager(Context baseContext) {
		super(baseContext);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
	}

	public int getColor() {
		return mColor;
	}

	public void setColor(int mColor) {
		this.mColor = mColor;
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
	

}
