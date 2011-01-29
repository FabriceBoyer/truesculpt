package truesculpt.managers;

import java.util.ArrayList;
import java.util.List;

import truesculpt.actions.BaseAction;
import truesculpt.actions.ColorizeAction;
import truesculpt.actions.InitialCreationAction;
import truesculpt.actions.SculptAction;
import android.content.Context;

//For undo redo and analytical description of sculpture
public class ActionsManager extends BaseManager
{
	private List<BaseAction> mActionsList=new ArrayList<BaseAction>();

	public ActionsManager(Context baseContext)
	{
		super(baseContext);
		
		//sample actions
		mActionsList.add(new InitialCreationAction("Init sphere"));
		mActionsList.add(new SculptAction("Deforming"));
		mActionsList.add(new SculptAction("Deforming +0.5"));
		mActionsList.add(new ColorizeAction("Colorize blue"));
		mActionsList.add(new SculptAction("Deforming -0.5"));		
	}

	@Override
	public void onCreate()
	{

	}

	@Override
	public void onDestroy()
	{

	}
	
	public List<BaseAction> getActionsList() { return mActionsList; }
}
