package truesculpt.ui.panels;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.main.TrueSculptApp;
import truesculpt.mesh.Mesh;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;

//to open and close, link to website, MRU, 
public class FileSelectorPanel extends TabActivity
{	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.fileselector);

	    Resources res = getResources(); // Resource object to get Drawables
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, NewFilePanel.class);
	    spec = tabHost.newTabSpec("New").setIndicator("",res.getDrawable(R.drawable.filenew)).setContent(intent);
	    tabHost.addTab(spec);

	    // Do the same for the other tabs
	    intent = new Intent().setClass(this, OpenFilePanel.class);
	    spec = tabHost.newTabSpec("Open").setIndicator("",res.getDrawable(R.drawable.open)).setContent(intent);
	    tabHost.addTab(spec);

	    intent = new Intent().setClass(this, SaveFilePanel.class);
	    spec = tabHost.newTabSpec("Save").setIndicator("",res.getDrawable(R.drawable.save)).setContent(intent);
	    tabHost.addTab(spec);

	    tabHost.setCurrentTab(0);
	}
	
	public Managers getManagers()
	{
		return ((TrueSculptApp) getApplicationContext()).getManagers();
	}

	
}
