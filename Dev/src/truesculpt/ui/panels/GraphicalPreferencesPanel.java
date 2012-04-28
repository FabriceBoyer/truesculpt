package truesculpt.ui.panels;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.main.TrueSculptApp;
import truesculpt.ui.views.ColorShowView;
import android.app.Activity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.SeekBar;

public class GraphicalPreferencesPanel extends Activity
{
	private ColorShowView backgroundGradientColorUpperLeft;
	private ColorShowView backgroundGradientColorUpperRight;
	private ColorShowView backgroundGradientColorLowerLeft;
	private ColorShowView backgroundGradientColorLowerRight;
	private SeekBar backgroundGradientAngle;
	private ColorShowView AmbientLightColor;
	private ColorShowView DiffuseLightColor;
	private ColorShowView SpecularLightColor;
	private CheckBox showSymmetryPane;
	private ColorShowView symmetryPlaneColor;
	private SeekBar symmetryPlaneTransparency;
	private CheckBox showWireframe;
	private CheckBox showNormals;
	private CheckBox showReferenceAxis;
	private ColorShowView helpTextColor;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		getManagers().getUtilsManager().updateFullscreenWindowStatus(getWindow());

		getManagers().getUsageStatisticsManager().TrackPageView("/GraphicalPreferencePanel");

		setContentView(R.layout.graphicalpreferences);

		backgroundGradientColorUpperLeft=(ColorShowView) findViewById(R.id.backgroundGradientColorUpperLeft);
		backgroundGradientColorUpperRight=(ColorShowView) findViewById(R.id.backgroundGradientColorUpperRight);
		backgroundGradientColorLowerLeft=(ColorShowView) findViewById(R.id.backgroundGradientColorLowerLeft);
		backgroundGradientColorLowerRight=(ColorShowView) findViewById(R.id.backgroundGradientColorLowerRight);
		backgroundGradientAngle=(SeekBar) findViewById(R.id.backgroundGradientAngle);
		AmbientLightColor=(ColorShowView) findViewById(R.id.AmbientLightColor);
		DiffuseLightColor=(ColorShowView) findViewById(R.id.DiffuseLightColor);
		SpecularLightColor=(ColorShowView) findViewById(R.id.SpecularLightColor);
		showSymmetryPane=(CheckBox) findViewById(R.id.showSymmetryPane);
		symmetryPlaneColor=(ColorShowView) findViewById(R.id.symmetryPlaneColor);
		symmetryPlaneTransparency=(SeekBar) findViewById(R.id.symmetryPlaneTransparency);
		showWireframe=(CheckBox) findViewById(R.id.showWireframe);
		showNormals=(CheckBox) findViewById(R.id.showNormals);
		showReferenceAxis=(CheckBox) findViewById(R.id.showReferenceAxis);
		helpTextColor=(ColorShowView) findViewById(R.id.helpTextColor);	

		UpdateView();
	}

	private void UpdateView()
	{


	}

	public Managers getManagers()
	{
		return ((TrueSculptApp) getApplicationContext()).getManagers();
	}

}
