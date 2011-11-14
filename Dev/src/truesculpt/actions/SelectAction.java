package truesculpt.actions;

import truesculpt.main.R;

public class SelectAction extends BaseAction
{

	public SelectAction()
	{
		super();
		setDescription("Selecting");
	}

	@Override
	public boolean DoAction()
	{
		return false;
	}

	@Override
	public boolean UndoAction()
	{
		return false;
	}

	@Override
	public String GetActionName()
	{
		return "Select";
	}

	@Override
	public int GetImageResourceID()
	{
		return R.drawable.flash;
	}

	@Override
	public int GetChangeCount()
	{
		return 0;
	}

}
