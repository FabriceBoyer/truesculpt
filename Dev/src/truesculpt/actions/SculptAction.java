package truesculpt.actions;

import truesculpt.main.R;

public class SculptAction extends BaseAction
{

	public SculptAction(String strDescription)
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
		return "Sculpt";
	}

	@Override
	public int GetImageResourceID()
	{
		return R.drawable.grab;
	}

	@Override
	public boolean UndoAction()
	{
		// TODO Auto-generated method stub
		return false;
	}

}
