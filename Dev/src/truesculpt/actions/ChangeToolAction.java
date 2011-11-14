package truesculpt.actions;

import truesculpt.main.R;
import truesculpt.managers.ToolsManager.GlobalToolState;

public class ChangeToolAction extends BaseAction
{
	private GlobalToolState m_CurrState;
	private GlobalToolState m_InitState;

	public ChangeToolAction(GlobalToolState CurrState, GlobalToolState InitState)
	{
		m_CurrState = CurrState;
		m_InitState = InitState;

		String strDescription = m_CurrState.toString();

		setDescription(strDescription);
	}

	@Override
	public boolean DoAction()
	{
		getManagers().getToolsManager().SetGlobalToolState(m_CurrState);
		return true;
	}

	@Override
	public String GetActionName()
	{
		return "Change tool";
	}

	@Override
	public int GetImageResourceID()
	{
		return R.drawable.equalizer;
	}

	@Override
	public boolean UndoAction()
	{
		getManagers().getToolsManager().SetGlobalToolState(m_InitState);
		return true;
	}

	@Override
	public int GetChangeCount()
	{
		return 0;
	}

}
