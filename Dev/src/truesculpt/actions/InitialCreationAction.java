package truesculpt.actions;

import truesculpt.main.R;

public class InitialCreationAction extends BaseAction
{

	public InitialCreationAction(String strDescription)
	{
		super();
		setDescription(strDescription);
	}

	@Override
	public boolean DoAction()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String GetActionName()
	{
		return "Init";
	}

	@Override
	public int GetImageResourceID()
	{
		return R.drawable.flash;
	}

	@Override
	public boolean UndoAction()
	{
		// TODO Auto-generated method stub
		return false;
	}

}
