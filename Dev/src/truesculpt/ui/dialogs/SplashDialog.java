package truesculpt.ui.dialogs;

import truesculpt.main.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class SplashDialog extends Dialog {

	private Handler mHandler = new Handler();

	
	public SplashDialog(Context context) {
		super(context);

	}
	private Runnable mCloseDlgTask = new Runnable() {
		   public void run() {
			   dismiss();
		   }
		};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ImageView image = new ImageView(getContext());
		setContentView(image);	
		
		image.setImageResource(R.drawable.flash);
		
		mHandler.postDelayed(mCloseDlgTask, 1500);
	}
}
