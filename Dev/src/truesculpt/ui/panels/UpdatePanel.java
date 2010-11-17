package truesculpt.ui.panels;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.main.TrueSculptApp;
import truesculpt.managers.UpdateManager.EUpdateStatus;
import truesculpt.utils.Utils;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UpdatePanel extends Activity {

	public Managers getManagers() {	
		return ((TrueSculptApp)getApplicationContext()).getManagers();
	}
	
	private String strCurrVersion="";
	private String strLatestVersion="";
	TextView text=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.update);
		
		final Button button = (Button) findViewById(R.id.Ok_btn);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();				
			}
		});

		text = (TextView) findViewById(R.id.UpdateStatusText);
		text.setText(R.string.looking_for_updates_);
		
		Thread thr= new Thread(null,mLookUpTask,"Update_Lookup");// network access can be long
		thr.start();		
	}
	
	private Handler mHandler = new Handler();
	
	Runnable mLookUpTask= new Runnable() {
		@Override
		public void run() {
			strCurrVersion = getManagers().getUpdateManager().getCurrentVersion();
			strLatestVersion = getManagers().getUpdateManager().getLatestVersion();
			
			mHandler.post(mUpdateViewTask);	//to come back in UI thread		
		}
	};	
	
	Runnable mUpdateViewTask= new Runnable() {
		@Override
		public void run() {
			UpdateView();
		}
	};		

	public void UpdateView()
	{
		EUpdateStatus status= getManagers().getUpdateManager().getUpdateStatus(strCurrVersion,strLatestVersion);			

		String msg="";
		if (status!=EUpdateStatus.UNDEFINED)
		{		
			msg += getString(R.string.current_version_is_) + " " + strCurrVersion + " \n";
			msg += getString(R.string.latest_version_is_) + " "	+ strLatestVersion + " \n";		
						
			if (status==EUpdateStatus.IS_A_BETA) {
				msg += getString(R.string.this_version_is_a_beta_)+ " \n";
			} 
			
			if (status==EUpdateStatus.UPDATE_NEEDED) {
				msg += getString(R.string.an_update_is_needed_)+ " \n";
			} 
			if (status==EUpdateStatus.UP_TO_DATE ||
				status==EUpdateStatus.IS_A_BETA)
			{
				msg += getString(R.string.no_update_is_needed_)+ " \n";
			}
		}
		else
		{
			msg += getString(R.string.unable_to_get_update_status)+ " \n";
		}
			
		text.setText(msg);

		if (status==EUpdateStatus.UPDATE_NEEDED)
		{
			// Launch associated web page
			 String strLastestURL = strLatestVersion.replace(".", "_");
			 strLastestURL = "https://code.google.com/p/truesculpt/downloads/detail?name=TrueSculpt_"
			 + strLastestURL + ".apk";
	
			//featured download
			//String strUpdateUrl = "http://code.google.com/p/truesculpt/downloads/list?can=3";
			 
			Utils.ShowURLInBrowser(this,strLastestURL);
		}		
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}

}
