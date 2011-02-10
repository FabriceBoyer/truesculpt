package truesculpt.actions;

import truesculpt.main.R;

public class ColorizeAction extends BaseAction
{
	public ColorizeAction(String strDescription)
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
		return "Paint";
	}

	@Override
	public int GetImageResourceID()
	{
		return R.drawable.colorpicker;
	}

	@Override
	public boolean UndoAction()
	{
		// TODO Auto-generated method stub
		return false;
	}

}
