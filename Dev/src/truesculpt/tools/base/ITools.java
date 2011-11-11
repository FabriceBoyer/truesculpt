package truesculpt.tools.base;


public interface ITools
{
	abstract public void Start(float xScreen, float yScreen);

	abstract public void Pick(float xScreen, float yScreen);

	abstract public void Stop(float xScreen, float yScreen);

	abstract public String GetName();

	abstract public String GetDescription();

	abstract public int GetIcon();

	abstract public boolean RequiresToolOverlay();
}
