package truesculpt.ui.debug;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.main.TrueSculptApp;
import truesculpt.utils.Utils;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DebugPanel extends Activity {
	public Managers getManagers() {	
		return ((TrueSculptApp)getApplicationContext()).getManagers();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.debug);

		final Button button = (Button) findViewById(R.id.show_sensors);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Utils.StartMyActivity(DebugPanel.this,
						truesculpt.ui.debug.DebugSensorsPanel.class);
			}
		});

		final Button button2 = (Button) findViewById(R.id.show_test);
		button2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Utils.StartMyActivity(DebugPanel.this,
						truesculpt.ui.debug.DebugTestPanel.class);
			}
		});
		
		
		//TODO update on timer
		TextView lastFrameText= (TextView) findViewById(R.id.lastFrameDurationText);
		long lLastDuration=getManagers().getmRendererManager().getmRenderer().getLastFrameDurationMs();
		String msg="Last frame duration = " + Long.toString(lLastDuration ) + " ms\n";
		msg+="Equivalent FPS is " + Float.toString(1.0f/lLastDuration*1000.0f) + " image/s\n";		
		lastFrameText.setText(msg);
		
		TextView meshStatText= (TextView) findViewById(R.id.MeshStatsText);
		int nVertex=getManagers().getmRendererManager().getmRenderer().getGeneratedObject().getVertexCount();
		int nFaces=getManagers().getmRendererManager().getmRenderer().getGeneratedObject().getFacesCount();
		msg="Number of vertex = " + Integer.toString(nVertex ) + "\n";
		msg+="Number of faces = " + Integer.toString(nFaces ) + "\n";		
		meshStatText.setText(msg);
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
