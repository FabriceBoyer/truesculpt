package truesculpt.actions;

import truesculpt.main.R;

public class ChangePOVAction extends BaseAction
{
	// TODO regroup in 1 state class with two instances init and curr
	float m_init_rotation, m_init_elevation, m_init_zoomDistance = 0.0f;
	float m_curr_rotation, m_curr_elevation, m_curr_zoomDistance = 0.0f;

	public ChangePOVAction(float init_rotation, float init_elevation, float init_zoomDistance, float curr_rotation, float curr_elevation, float curr_zoomDistance)
	{
		m_init_rotation = init_rotation;
		m_init_elevation = init_elevation;
		m_init_zoomDistance = init_zoomDistance;
		m_curr_rotation = curr_rotation;
		m_curr_elevation = curr_elevation;
		m_curr_zoomDistance = curr_zoomDistance;

		float rotOffset = curr_rotation - init_rotation;
		float elvOffset = curr_elevation - init_elevation;
		float zoomOffset = curr_zoomDistance - init_zoomDistance;

		String strDescription = "Offset [ " + Float.toString(rotOffset) + " ; " + Float.toString(elvOffset) + " ; " + Float.toString(zoomOffset) + " ] ";

		setDescription(strDescription);
	}

	@Override
	public boolean DoAction()
	{
		getManagers().getPointOfViewManager().SetAllAngles(m_curr_rotation, m_curr_elevation, m_curr_zoomDistance);
		return true;
	}

	@Override
	public String GetActionName()
	{
		return "Change point of view";
	}

	@Override
	public int GetImageResourceID()
	{
		return R.drawable.resize;
	}

	@Override
	public boolean UndoAction()
	{
		getManagers().getPointOfViewManager().SetAllAngles(m_init_rotation, m_init_elevation, m_init_zoomDistance);
		return true;
	}

	@Override
	public int GetChangeCount()
	{
		return 0;
	}

}
