package truesculpt.managers;

import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

import truesculpt.managers.PointOfViewManager.OnPointOfViewChangeListener;
import truesculpt.renderer.GeneratedObject;
import android.content.Context;

//for mesh storage, computation and transformation application
public class MeshManager extends BaseManager {

	private GeneratedObject mObject=null;

	public MeshManager(Context baseContext) {
		super(baseContext);
		 
			
	}

	private boolean bInitOver=false;
	
	@Override
	public void onCreate() {
		 Thread thr = new Thread(null, mInitTask, "Mesh_Init");
		 thr.start();		
	}
	
	Runnable mInitTask = new Runnable()
	{		   
		@Override
		public void run() {
			
			mObject= new GeneratedObject();	
			
	        bInitOver=true;	
	        
	        NotifyListeners();
		}
	};

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
	}
	
	private void NotifyListeners()
	{		
		for (OnMeshChangeListener listener : mListeners) 
		{
			listener.onMeshChange();		
		}	
	}
	
	public void registerPointOfViewChangeListener(OnMeshChangeListener listener)
	{
		mListeners.add(listener);	
	}
	
	public void unRegisterPointOfViewChangeListener(OnMeshChangeListener listener)
	{
		mListeners.remove(listener);	
	}

	public interface OnMeshChangeListener
	{
		void onMeshChange();
	}
	private Vector<OnMeshChangeListener> mListeners= new Vector<OnMeshChangeListener>();
	
	public void draw(GL10 gl)
	{
		if (mObject!=null && bInitOver)
		{
			mObject.draw(gl);
		}
	}

    public int getFacesCount() {
    	int nCount=-1;
    	if (mObject!=null)
		{
		 	nCount=mObject.getFacesCount();
		}
    	return nCount;
	}
    
    public int getVertexCount() {
    	int nCount=-1;
    	if (mObject!=null)
		{
		 	nCount=mObject.getVertexCount();
		}
    	return nCount;
	}

    
}
