package truesculpt.managers;

import java.util.List;

import truesculpt.actions.BaseAction;
import android.content.Context;

//For undo redo and analytical descitption of sculpture
public class ActionsManager extends BaseManager {

	private List<BaseAction> mActionsList;

	public ActionsManager(Context baseContext) {
		super(baseContext);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub

	}

}
