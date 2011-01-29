package truesculpt.actions;

import truesculpt.main.R;

public class InitialCreationAction extends BaseAction
{

	public InitialCreationAction(String strDescription)
	{
		super(strDescription);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean DoAction()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getActionName()
	{
		return "Init";
	}

	@Override
	public int getImageResourceID()
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
