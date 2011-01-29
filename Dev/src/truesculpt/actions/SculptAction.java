package truesculpt.actions;

import truesculpt.main.R;

public class SculptAction extends BaseAction
{

	public SculptAction(String strDescription)
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
	public boolean UndoAction()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getActionName()
	{		
		return "Sculpt";
	}
	@Override
	public int getImageResourceID() 
	{
		return R.drawable.grab; 
	}
	
	
}
