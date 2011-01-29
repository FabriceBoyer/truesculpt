package truesculpt.ui.panels;

import java.util.ArrayList;
import java.util.HashMap;

import truesculpt.main.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

//For history of action manager
public class HistoryPanel extends Activity
{
	private ListView mHistoryListView;
	 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
 
        mHistoryListView = (ListView) findViewById(R.id.historyListView);
 
      
 
    }

}
