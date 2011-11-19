package truesculpt.managers;

import java.util.ArrayList;
import java.util.List;

import truesculpt.actions.BaseAction;
import android.content.Context;

//For undo redo and analytical description of sculpture
public class ActionsManager extends BaseManager
{
	private final int MAX_CHANGE_COUNT = (int) 2e4;
	private int mCurrChangeCount = 0;
	private final List<BaseAction> mUndoActionsList = new ArrayList<BaseAction>();
	private final List<BaseAction> mRedoActionsList = new ArrayList<BaseAction>();

	public ActionsManager(Context baseContext)
	{
		super(baseContext);
	}

	@Override
	public void onCreate()
	{

	}

	public void Remove(int position)
	{
		BaseAction removedAction = mUndoActionsList.get(position);
		mCurrChangeCount -= removedAction.GetChangeCount();
		mUndoActionsList.remove(position);
		NotifyListeners();
	}

	public void RemoveUpTo(int position)
	{
		if (position < GetUndoActionCount())
		{
			for (int i = 0; i <= position; i++)
			{
				BaseAction removedAction = mUndoActionsList.get(0);
				mCurrChangeCount -= removedAction.GetChangeCount();
				mUndoActionsList.remove(0);
			}
		}
		NotifyListeners();
	}

	@Override
	public void onDestroy()
	{

	}

	public int GetUndoActionCount()
	{
		return mUndoActionsList.size();
	}

	public int GetRedoActionCount()
	{
		return mRedoActionsList.size();
	}

	public BaseAction GetUndoActionAt(int position)
	{
		BaseAction res = null;

		if (position < GetUndoActionCount())
		{
			res = mUndoActionsList.get(position);
		}

		return res;
	}

	public void AddUndoAction(BaseAction action)
	{
		action.setManagers(getManagers());

		// TODO optimize by avoiding offsetting (reverse list)
		mUndoActionsList.add(0, action);// add at the top
		mCurrChangeCount += action.GetChangeCount();

		// history stripping
		while (mCurrChangeCount > MAX_CHANGE_COUNT)
		{
			int nIndex = GetUndoActionCount() - 1;
			BaseAction removedAction = mUndoActionsList.get(nIndex);
			mCurrChangeCount -= removedAction.GetChangeCount();
			mUndoActionsList.remove(nIndex);
		}

		for (BaseAction removedAction : mRedoActionsList)
		{
			mCurrChangeCount -= removedAction.GetChangeCount();
		}
		mRedoActionsList.clear();// new branch no more needed
		NotifyListeners();
	}

	public void Redo()
	{
		if (GetRedoActionCount() > 0)
		{
			BaseAction action = mRedoActionsList.get(0);
			mRedoActionsList.remove(0);
			mUndoActionsList.add(0, action);// add at the top
			action.DoAction();
			NotifyListeners();
		}
	}

	public void Undo()
	{
		if (GetUndoActionCount() > 0)
		{
			BaseAction action = mUndoActionsList.get(0);
			mUndoActionsList.remove(0);
			mRedoActionsList.add(0, action);
			action.UndoAction();
			NotifyListeners();
		}
	}

	public void ClearAll()
	{
		mUndoActionsList.clear();
		mRedoActionsList.clear();
		mCurrChangeCount = 0;
		NotifyListeners();
	}

	public int GetCurrentChangeCount()
	{
		return mCurrChangeCount;
	}

	public int GetMaxChangeCount()
	{
		return MAX_CHANGE_COUNT;
	}

}
