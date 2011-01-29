package truesculpt.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import truesculpt.actions.BaseAction;
import truesculpt.actions.ColorizeAction;
import truesculpt.actions.InitialCreationAction;
import truesculpt.actions.SculptAction;
import android.content.Context;

//For undo redo and analytical description of sculpture
public class ActionsManager extends BaseManager
{
	private List<BaseAction> mActionsList = new ArrayList<BaseAction>();

	public ActionsManager(Context baseContext)
	{
		super(baseContext);

		// sample actions
		mActionsList.add(new InitialCreationAction("Init sphere"));
		mActionsList.add(new SculptAction("Deforming"));
		mActionsList.add(new SculptAction("Deforming +0.5"));
		mActionsList.add(new ColorizeAction("Colorize blue"));
		mActionsList.add(new SculptAction("Deforming -0.5"));

		for (int i = 0; i < 1000; i++)
		{
			Random rand = new Random();
			int val = rand.nextInt(100);
			if (val < 33)
			{
				mActionsList.add(new SculptAction("Deforming " + Integer.toString(val)));
			}
			if (val >= 33 && val <= 66)
			{
				mActionsList.add(new InitialCreationAction("Init " + Integer.toString(val)));
			}
			if (val > 66)
			{
				mActionsList.add(new ColorizeAction("Colorize " + Integer.toString(val)));
			}
		}

	}

	public List<BaseAction> getActionsList()
	{
		return mActionsList;
	}

	@Override
	public void onCreate()
	{

	}

	@Override
	public void onDestroy()
	{

	}
}
