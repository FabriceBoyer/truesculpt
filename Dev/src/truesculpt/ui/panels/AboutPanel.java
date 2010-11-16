package truesculpt.ui.panels;

import truesculpt.main.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AboutPanel extends Activity {

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		getWindow().setBackgroundDrawableResource(R.drawable.transparentbackground);
		

		final Button button = (Button) findViewById(R.id.Ok_btn);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();				
			}
		});
	}

}
