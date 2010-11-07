package truesculpt.ui.panels;


import truesculpt.main.R;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

public class SplashPanel extends Activity {
	
	private static final int STOPSPLASH = 0;
	//time in milliseconds
	private static final long SPLASHTIME = 2000;
	
	private ImageView splash;
	
	//handler for splash screen
	private Handler splashHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case STOPSPLASH:
				//remove SplashScreen from view
				splash.setVisibility(View.GONE);
				break;
			}
			super.handleMessage(msg);
		}
	};
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
 
        splash = new ImageView(this);
		setContentView(splash);	
		
		splash.setImageResource(R.drawable.flash);
		
			Message msg = new Message();
			msg.what = STOPSPLASH;
			splashHandler.sendMessageDelayed(msg, SPLASHTIME);
    }
}
