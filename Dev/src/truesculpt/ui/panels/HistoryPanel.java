package truesculpt.ui.panels;

import java.util.Observable;
import java.util.Observer;

import truesculpt.actions.BaseAction;
import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.main.TrueSculptApp;
import truesculpt.ui.views.HistoryAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

//For history of action manager
public class HistoryPanel extends Activity implements Observer
{
	private ListView mHistoryListView;
	HistoryAdapter adapter;
	Button mRedoButton;
	Button mUndoButton;

	@Override
	protected void onDestroy()
	{
		getManagers().getActionsManager().deleteObserver(this);
		super.onDestroy();
	}

	public Managers getManagers()
	{
		return ((TrueSculptApp) getApplicationContext()).getManagers();
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history);

		getManagers().getActionsManager().addObserver(this);
		
		mHistoryListView = (ListView) findViewById(R.id.historyListView);

		adapter = new HistoryAdapter(getApplicationContext(), getManagers().getActionsManager());
		mHistoryListView.setAdapter(adapter);
		mHistoryListView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position, long id)
			{
				v.showContextMenu();
			}
		});
		registerForContextMenu(mHistoryListView);
		
		
		mRedoButton = (Button) findViewById(R.id.RedoBtn);
		mRedoButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				getManagers().getActionsManager().Redo();
			}
		});
		
		mUndoButton = (Button) findViewById(R.id.UndoBtn);
		mUndoButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				getManagers().getActionsManager().Undo();
			}
		});
		
		UpdateButtonsView();

	}
	
	private void UpdateButtonsView()
	{
		if (getManagers().getActionsManager().GetUndoActionCount()<=0) 
		{
			mUndoButton.setEnabled(false);			
		}
		else
		{
			mUndoButton.setEnabled(true);
		}
		
		
		if (getManagers().getActionsManager().GetRedoActionCount()<=0) 
		{
			mRedoButton.setEnabled(false);			
		}
		else
		{
			mRedoButton.setEnabled(true);
		}
		
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) 
	{
	  super.onCreateContextMenu(menu, v, menuInfo);
	  MenuInflater inflater = getMenuInflater();
	  inflater.inflate(R.menu.history_item_context_menu, menu);	  
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	  AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	  
	  switch (item.getItemId()) {
	  case R.id.undo_this_item:
		  //TODO use undo functions
		  //getManagers().getActionsManager().Remove(info.position);//update through observable		 
	    return true;
	  case R.id.undo_up_to_this_point:
		  //TODO use undo functions
		  //getManagers().getActionsManager().RemoveUpTo(info.position);		 
	    return true;
	  default:
	    return super.onContextItemSelected(item);
	  }
	}

	@Override
	public void update(Observable observable, Object data)
	{
		 adapter.notifyDataSetChanged();
		 UpdateButtonsView();
	}

}
