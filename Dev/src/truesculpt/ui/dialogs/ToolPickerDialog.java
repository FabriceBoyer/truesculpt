/*
 * Copyright (C) 2010 Neil Davies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * This code is base on the Android Gallery widget and was Created 
 * by Neil Davies neild001 'at' gmail dot com to be a Coverflow widget
 * 
 * @author Neil Davies
 */
package truesculpt.ui.dialogs;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.main.TrueSculptApp;
import truesculpt.tools.base.BaseTool;
import truesculpt.ui.adapters.CoverFlowImageAdapter;
import truesculpt.ui.views.CoverFlow;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

public class ToolPickerDialog extends Dialog
{
	public ToolPickerDialog(Context context, int theme)
	{
		super(context, theme);
	}

	private TextView mNameView = null;
	private TextView mDescriptionView = null;
	private CoverFlow mCoverFlow = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		getManagers().getUtilsManager().updateFullscreenWindowStatus(getWindow());

		setContentView(R.layout.toolpickerdialog);

		mNameView = (TextView) findViewById(R.id.name);

		mDescriptionView = (TextView) findViewById(R.id.description);
		mDescriptionView.setMovementMethod(new ScrollingMovementMethod());

		mCoverFlow = (CoverFlow) findViewById(R.id.coverflow);

		CoverFlowImageAdapter coverImageAdapter = new CoverFlowImageAdapter(getContext());
		coverImageAdapter.createReflectedImages();
		mCoverFlow.setAdapter(coverImageAdapter);

		mCoverFlow.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				getManagers().getToolsManager().setCurrentTool((int) arg3);
				dismiss();
			}
		});
		mCoverFlow.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				BaseTool currTool = getManagers().getToolsManager().GetToolAtIndex((int) arg3);
				if (currTool != null)
				{
					mNameView.setText(currTool.GetName());
					mDescriptionView.setText(currTool.GetDescription());
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{
				mNameView.setText("");
				mDescriptionView.setText("");
			}
		});
		mCoverFlow.setSpacing(-15);
		mCoverFlow.setSelection(getManagers().getToolsManager().getCurrentToolIndex(), true);
		mCoverFlow.setAnimationDuration(1000);
	}

	public Managers getManagers()
	{
		return ((TrueSculptApp) getContext().getApplicationContext()).getManagers();
	}

}