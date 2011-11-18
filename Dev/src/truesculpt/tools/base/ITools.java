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

	abstract public boolean RequiresStrength();

	abstract public boolean RequiresRadius();

	abstract public boolean RequiresColor();

	abstract public boolean RequiresSymmetry();
}
