package com.TrueSculpt;

import com.TrueSculpt.ColorPickerDialog;
import com.TrueSculpt.ColorPickerDialog.OnColorChangedListener;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewDebug.IntToString;
import android.widget.Button;
import android.widget.Toast;

public class TrueSculpt extends Activity implements OnColorChangedListener {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);        
        
        
        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	
            	new ColorPickerDialog(TrueSculpt.this, TrueSculpt.this, 0).show();
            }
        });

    }
    
    public void colorChanged(int color) {
     String msg="color is" + color;
    Toast.makeText(TrueSculpt.this, msg, Toast.LENGTH_SHORT).show();
    }
}