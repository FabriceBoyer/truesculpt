package com.TrueSculpt;

import com.TrueSculpt.ColorPickerDialog;
import com.TrueSculpt.ColorPickerDialog.OnColorChangedListener;

import android.app.Activity;
import android.os.Bundle;

public class TrueSculpt extends Activity implements OnColorChangedListener {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        new ColorPickerDialog(this, this, 0).show();
    }
    
    public void colorChanged(int color) {
      //  mPaint.setColor(color);
    }
}