package truesculpt.ui.panels;

import java.util.ArrayList;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.main.TrueSculptApp;
import truesculpt.parser.WebEntry;
import truesculpt.tools.base.BaseTool;
import truesculpt.ui.adapters.CoverFlowImageAdapter;
import truesculpt.ui.adapters.StreamingCoverFlowAdapter;
import truesculpt.ui.views.CoverFlow;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

public class WebFileFlowPanel extends Activity
{	
	private TextView mNameView = null;
	private TextView mDescriptionView = null;
	private CoverFlow mCoverFlow = null;
	ArrayList<WebEntry> mEntries=null;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		getManagers().getUsageStatisticsManager().TrackPageView("/WebFileFlowPanel");
		getManagers().getUtilsManager().updateFullscreenWindowStatus(getWindow());

		setContentView(R.layout.webfileflow);

		mNameView = (TextView) findViewById(R.id.name);
		mDescriptionView = (TextView) findViewById(R.id.description);
		mDescriptionView.setMovementMethod(new ScrollingMovementMethod());
		mCoverFlow = (CoverFlow) findViewById(R.id.coverflow);

		mEntries=getManagers().getWebManager().getWebEntries();
		ArrayList<String> stringEntries= new ArrayList<String>();
		if (mEntries!=null)
		{
			for(WebEntry entry : mEntries)
			{
				stringEntries.add(entry.getImageThumbnailURL().toString());
			}
		}
		StreamingCoverFlowAdapter coverImageAdapter = new StreamingCoverFlowAdapter(this,stringEntries);
		
		mCoverFlow.setAdapter(coverImageAdapter);

		mCoverFlow.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0,  View v, int position, long id)
			{
								
			}
		});
		mCoverFlow.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> arg0,  View v, int position, long id)
			{				
				WebEntry entry = mEntries.get(position);
				mNameView.setText(entry.getTitle());
				mDescriptionView.setText(entry.getDescription() + 
						"\nDownloaded " + entry.getDownloadCount() +" times\n" +
						"Created on " + entry.getCreationTime());				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{
				mNameView.setText("");
				mDescriptionView.setText("");
			}
		});
		//mCoverFlow.setSpacing(-15);
		//mCoverFlow.setSelection(0, true);
		mCoverFlow.setAnimationDuration(1000);
	}

	public Managers getManagers()
	{
		return ((TrueSculptApp) getApplicationContext()).getManagers();
	}

}