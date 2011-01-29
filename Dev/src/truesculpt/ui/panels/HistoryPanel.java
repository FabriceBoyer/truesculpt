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
 
        ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
 
        HashMap<String, String> map;
 
        map = new HashMap<String, String>();
        map.put("title", "Draw");
        map.put("description", "Draw");
        map.put("image", String.valueOf(R.drawable.draw));
        listItem.add(map);
 
        map = new HashMap<String, String>();
        map.put("title", "Grab");
        map.put("description", "Grab");
        map.put("image", String.valueOf(R.drawable.grab));
        listItem.add(map);
 
        map = new HashMap<String, String>();
        map.put("title", "Smooth");
        map.put("description", "Smooth");
        map.put("image", String.valueOf(R.drawable.smooth));
        listItem.add(map);
        
        map = new HashMap<String, String>();
        map.put("title", "Inflate");
        map.put("description", "Inflate");
        map.put("image", String.valueOf(R.drawable.inflate));
        listItem.add(map);
      
        SimpleAdapter mSchedule = new SimpleAdapter (this.getBaseContext(), listItem, R.layout.historyitem,
               new String[] {"image", "title", "description"}, new int[] {R.id.image, R.id.title, R.id.description});
  
        mHistoryListView.setAdapter(mSchedule);
  
        mHistoryListView.setOnItemClickListener(new OnItemClickListener() {
			
         	@SuppressWarnings("unchecked")
			public void onItemClick(AdapterView<?> a, View v, int position, long id) {
        		HashMap<String, String> map = (HashMap<String, String>) mHistoryListView.getItemAtPosition(position);
        		AlertDialog.Builder adb = new AlertDialog.Builder(HistoryPanel.this);
        		adb.setTitle("Selected Item");
        		adb.setMessage("You chose : " + map.get("title"));
        		adb.setPositiveButton("Ok", null);
        		adb.show();
        	}

		
         });
 
    }

}
