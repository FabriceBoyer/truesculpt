package truesculpt.actions;

public abstract class BaseAction
{

	public BaseAction(String strDescription)
	{
		this.mStrDescription = strDescription;
	}
	
	String mStrDescription;
	
	public String getDescription()
	{
		return mStrDescription;
	}
	public void setDescription(String strDescription)
	{
		this.mStrDescription = strDescription;
	}
	
	public abstract boolean DoAction();

	public abstract boolean UndoAction();

	
}
