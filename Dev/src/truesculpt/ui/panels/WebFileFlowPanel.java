package truesculpt.ui.panels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.main.TrueSculptApp;
import truesculpt.parser.WebEntry;
import truesculpt.tools.base.BaseTool;
import truesculpt.ui.adapters.CoverFlowImageAdapter;
import truesculpt.ui.adapters.StreamingCoverFlowAdapter;
import truesculpt.ui.panels.WebFileFlowPanel.SortKind;
import truesculpt.ui.views.CoverFlow;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

public class WebFileFlowPanel extends Activity
{	
	private TextView mNameView = null;
	private TextView mDescriptionView = null;
	private CoverFlow mCoverFlow = null;
	private ArrayList<WebEntry> mEntries=null;
	private Button mDownloadBtn =null;
	private Spinner mSortKindSpinner= null;
	private ToggleButton mSortOrderToggle= null;
	private StreamingCoverFlowAdapter mCoverImageAdapter =null;
	enum SortKind {
		DATE("Date"),
		NAME("Name"),
		SIZE("Size"),
		FEATURED("Featured"),
		DOWNLOAD("Download");		

	    private String friendlyName;

	    private SortKind(String friendlyName){
	        this.friendlyName = friendlyName;
	    }

	    @Override public String toString(){
	        return friendlyName;
	    }
	};
	enum SortOrder { 
		ASCENDING("Ascending"),
		DESCENDING("Descending");
		
	    private String friendlyName;

	    private SortOrder(String friendlyName){
	        this.friendlyName = friendlyName;
	    }

	    @Override public String toString(){
	        return friendlyName;
	    }};
	private SortKind mSortKind = SortKind.DATE;
	private SortOrder mSortOrder = SortOrder.ASCENDING;
	
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
		
		mDownloadBtn = (Button) findViewById(R.id.download);
		mDownloadBtn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
								
			}
		});
		
		mSortKindSpinner = (Spinner) findViewById(R.id.sortKind);
		ArrayAdapter<SortKind> sortKindAdapter = new ArrayAdapter<SortKind>(this, android.R.layout.simple_spinner_item, SortKind.values());
		sortKindAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSortKindSpinner.setAdapter(sortKindAdapter);
		mSortKindSpinner.setSelection(0);
		mSortKindSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				mSortKind=(SortKind)mSortKindSpinner.getSelectedItem();
				UpdateWebEntries();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
								
			}
		});
		
		mSortOrderToggle = (ToggleButton) findViewById(R.id.sortOrder);	
		mSortOrderToggle.setChecked(mSortOrder==SortOrder.ASCENDING);
		mSortOrderToggle.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				mSortOrder=mSortOrderToggle.isChecked()?SortOrder.ASCENDING:SortOrder.DESCENDING;
				UpdateWebEntries();				
			}
		});

		mCoverImageAdapter = new StreamingCoverFlowAdapter(this);
		UpdateWebEntries();
		
		mCoverFlow.setAdapter(mCoverImageAdapter);

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
				SetNameAndDescription(position);				
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
		//mCoverFlow.setAnimationDuration(1000);
	}
	
	private void SetNameAndDescription(int position)
	{
		if (mEntries!=null)
		{
			WebEntry entry = mEntries.get(position);
			mNameView.setText(entry.getTitle());
			mDescriptionView.setText(entry.getDescription() + 
					"\nDownloaded " + entry.getDownloadCount() +" times\n" +
					"Created on " + entry.getCreationTime() + "\n" +
					Math.round(entry.getObjectSizeKo()) +" Ko\n");
		}
	}

	
	private void UpdateWebEntries()
	{
		mEntries=getManagers().getWebManager().getWebEntries();
		Collections.sort(mEntries,new Comparator<WebEntry>() {
			@Override
			public int compare(WebEntry lhs, WebEntry rhs) {
				int nRes=0;
				//supposed ascending
				switch (mSortKind)
				{
				case DATE:
					nRes=lhs.getCreationTime().compareTo(rhs.getCreationTime());
					break;
				case FEATURED:
					nRes=lhs.getIsFeatured().compareTo(rhs.getIsFeatured());
					break;
				case NAME:
					nRes=lhs.getTitle().compareTo(rhs.getTitle());
					break;
				case SIZE:
					nRes=lhs.getObjectSizeKo().compareTo(rhs.getObjectSizeKo());
					break;		
				case DOWNLOAD:
					nRes=lhs.getDownloadCount().compareTo(rhs.getDownloadCount());
					break;
				}
				if (mSortOrder==SortOrder.DESCENDING)
				{
					nRes=-1*nRes;
				}
				return nRes;
			}
		});
		ArrayList<String> stringEntries= new ArrayList<String>();
		if (mEntries!=null && mEntries.size()>0)
		{
			for(WebEntry entry : mEntries)
			{
				stringEntries.add(entry.getImageThumbnailURL().toString());
			}
		
			mCoverImageAdapter.setEntries(stringEntries);
			mCoverImageAdapter.notifyDataSetChanged();	
			
			mCoverFlow.setSelection(0);
			SetNameAndDescription(0);
		}
	}
	

	public Managers getManagers()
	{
		return ((TrueSculptApp) getApplicationContext()).getManagers();
	}

}