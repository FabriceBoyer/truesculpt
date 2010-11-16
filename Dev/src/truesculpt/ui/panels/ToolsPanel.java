package truesculpt.ui.panels;

import truesculpt.main.R;
import truesculpt.ui.dialogs.ColorPickerDialog;
import truesculpt.ui.dialogs.ColorPickerDialog.OnColorChangedListener;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ToolsPanel extends Activity implements OnColorChangedListener {
	private final int DIALOG_COLOR_PICKER_ID=0;

	private int mColor = 0;
	
	@Override
	public void colorChanged(int color) {
		mColor = color;
		String msg = "color is " + Integer.toString(mColor);
		Toast.makeText(ToolsPanel.this, msg, Toast.LENGTH_SHORT).show();
	}

	public int getmColor() {
		return mColor;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tools);
				
		final Button button = (Button) findViewById(R.id.color_button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DIALOG_COLOR_PICKER_ID);
				
			}
		});
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog=null;
		switch (id) {
		case DIALOG_COLOR_PICKER_ID:
			dialog = new ColorPickerDialog(ToolsPanel.this, ToolsPanel.this, 0);
			break;
		default:
			dialog = null;
		}
		return dialog;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

}
