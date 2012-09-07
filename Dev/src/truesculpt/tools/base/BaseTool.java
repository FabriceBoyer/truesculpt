package truesculpt.tools.base;

import truesculpt.actions.BaseAction;
import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.managers.ToolsManager.ESymmetryMode;
import truesculpt.mesh.Mesh;
import android.os.SystemClock;
import android.util.FloatMath;

abstract public class BaseTool implements ITools
{
	protected long mLastSculptDurationMs = -1;
	protected long tSculptStart = -1;
	private Managers mManagers = null;
	protected Mesh mMesh = null;
	protected BaseAction mAction = null;

	protected final float MIN_RADIUS = 0.01f;// meters
	protected final float MAX_RADIUS = 1f;// meters
	protected float mSquareMaxDistance = -1;
	protected float mMaxDistance = -1;

	protected float mSigma = -1;
	protected float mMaxGaussian = -1;
	protected final float FWHM = (float) (2f * Math.sqrt(2 * Math.log(2f)));// full width at half maximum
	protected final static float oneoversqrttwopi = (float) (1f / Math.sqrt(2f * Math.PI));

	public BaseTool(Managers managers)
	{
		mManagers = managers;
	}

	@Override
	public void Start(float xScreen, float yScreen)
	{
		mMesh = getManagers().getMeshManager().getMesh();

		mAction = null;

		mSquareMaxDistance = (float) Math.pow((MAX_RADIUS - MIN_RADIUS) * getManagers().getToolsManager().getRadius() / 100f + MIN_RADIUS, 2);
		mMaxDistance = FloatMath.sqrt(mSquareMaxDistance);

		mSigma = (mMaxDistance / 1.2f) / FWHM;
		mMaxGaussian = Gaussian(mSigma, 0);
	}

	abstract protected void PickInternal(float xScreen, float yScreen, ESymmetryMode mode);

	@Override
	public void Pick(float xScreen, float yScreen)
	{
		tSculptStart = SystemClock.uptimeMillis();

		// symmetry handling
		switch (getManagers().getToolsManager().getSymmetryMode())
		{
		case NONE:
			// nop
			break;
		case X:
			PickInternal(xScreen, yScreen, ESymmetryMode.X);
			break;
		case Y:
			PickInternal(xScreen, yScreen, ESymmetryMode.Y);
			break;
		case Z:
			PickInternal(xScreen, yScreen, ESymmetryMode.Z);
			break;
		}

		// Regular pick always done
		PickInternal(xScreen, yScreen, ESymmetryMode.NONE);

		mLastSculptDurationMs = SystemClock.uptimeMillis() - tSculptStart;
	}

	@Override
	public void Stop(float xScreen, float yScreen)
	{
		if (mAction != null)
		{
			getManagers().getActionsManager().AddUndoAction(mAction);
			mAction.DoAction();
			mAction = null;
		}

		mMesh = null;
	}

	protected static float Gaussian(float sigma, float sqDist)
	{
		return (float) (oneoversqrttwopi / sigma * Math.exp(-sqDist / (2 * sigma * sigma)));
	}

	protected Managers getManagers()
	{
		return mManagers;
	}

	public long getLastSculptDurationMs()
	{
		return mLastSculptDurationMs;
	}

	@Override
	public String GetName()
	{
		return "BaseTool";
	}

	@Override
	public String GetDescription()
	{
		String res = "A detailled description of the " + GetName() + " tool"; // "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed non risus. Suspendisse lectus tortor, dignissim sit amet, adipiscing nec, ultricies sed, dolor. Cras elementum ultrices diam. Maecenas ligula massa, varius a, semper congue, euismod non, mi. Proin porttitor, orci nec nonummy molestie, enim est eleifend mi, non fermentum diam nisl sit amet erat. Duis semper. Duis arcu massa, scelerisque vitae, consequat in, pretium a, enim. Pellentesque congue. Ut in risus volutpat libero pharetra tempor. Cras vestibulum bibendum augue. Praesent egestas leo in pede. Praesent blandit odio eu enim. Pellentesque sed dui ut augue blandit sodales. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Aliquam nibh. Mauris ac mauris sed pede pellentesque fermentum. Maecenas adipiscing ante non diam sodales hendrerit. Ut velit mauris, egestas sed, gravida nec, ornare ut, mi. Aenean ut orci vel massa suscipit pulvinar. Nulla sollicitudin. Fusce varius, ligula non tempus aliquam, nunc turpis ullamcorper nibh, in tempus sapien eros vitae ligula. Pellentesque rhoncus nunc et augue. Integer id felis. Curabitur aliquet pellentesque diam. Integer quis metus vitae elit lobortis egestas. Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Morbi vel erat non mauris convallis vehicula. Nulla et sapien. Integer tortor tellus, aliquam faucibus, convallis id, congue eu, quam. Mauris ullamcorper felis vitae erat. Proin feugiat, augue non elementum posuere, metus purus iaculis lectus, et tristique ligula justo vitae magna. Aliquam convallis sollicitudin purus. Praesent aliquam, enim at fermentum mollis, ligula massa adipiscing nisl, ac euismod nibh nisl eu lectus. Fusce vulputate sem at sapien. Vivamus leo. Aliquam euismod libero eu enim. Nulla nec felis sed leo placerat imperdiet. Aenean suscipit nulla in justo. Suspendisse cursus rutrum augue. Nulla tincidunt tincidunt mi. Curabitur iaculis, lorem vel rhoncus faucibus, felis magna fermentum augue, et ultricies lacus lorem varius purus. Curabitur eu amet.";
		String strClassName = this.getClass().getSimpleName();
		// String id=GetName()+ " tool description";
		int nStringID = getManagers().getActionsManager().getbaseContext().getResources().getIdentifier(strClassName, "string", "truesculpt.main");
		if (nStringID > 0)
		{
			String value = getManagers().getActionsManager().getbaseContext().getResources().getString(nStringID);
			if (value != "")
			{
				res = value;
			}
		}
		return res;
	}

	@Override
	public int GetIcon()
	{
		return R.drawable.flash;
	}

	@Override
	public boolean RequiresToolOverlay()
	{
		return false;
	}
}
