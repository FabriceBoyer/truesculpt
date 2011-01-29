package truesculpt.ui.panels;

import truesculpt.actions.BaseAction;
import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.main.TrueSculptApp;
import truesculpt.ui.views.HistoryAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

//For history of action manager
public class HistoryPanel extends Activity
{
	private ListView mHistoryListView;

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

		HistoryAdapter adapter = new HistoryAdapter(getApplicationContext(), getManagers().getActionsManager());
		mHistoryListView.setAdapter(adapter);
		mHistoryListView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			@SuppressWarnings("unchecked")
			public void onItemClick(AdapterView<?> a, View v, int position, long id)
			{
				BaseAction action = (BaseAction) mHistoryListView.getItemAtPosition(position);
				AlertDialog.Builder adb = new AlertDialog.Builder(HistoryPanel.this);
				adb.setTitle("Selected item");
				adb.setMessage("You chose : " + action.getActionName());
				adb.setPositiveButton("Ok", null);
				adb.show();
			}
		});

		Button CloseButton = (Button) findViewById(R.id.CloseBtn);
		CloseButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
	}

}
