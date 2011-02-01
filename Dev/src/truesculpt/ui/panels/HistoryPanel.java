package truesculpt.ui.panels;

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
public class HistoryPanel extends Activity
{
	private ListView mHistoryListView;
	HistoryAdapter adapter;

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

//		Button CloseButton = (Button) findViewById(R.id.CloseBtn);
//		CloseButton.setOnClickListener(new View.OnClickListener()
//		{
//			@Override
//			public void onClick(View v)
//			{
//				finish();
//			}
//		});
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
		  getManagers().getActionsManager().getActionsList().remove(info.position);
		  adapter.notifyDataSetChanged();
	    return true;
	  case R.id.undo_up_to_this_point:
		  for (int i=0;i<=info.position;i++)
		  {
			  getManagers().getActionsManager().getActionsList().remove(0);
		  }
		  adapter.notifyDataSetChanged();
	    return true;
	  default:
	    return super.onContextItemSelected(item);
	  }
	}

}
