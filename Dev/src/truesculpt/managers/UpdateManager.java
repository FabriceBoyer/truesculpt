package truesculpt.managers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import truesculpt.main.R;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.widget.Toast;

public class UpdateManager extends BaseManager {

	public UpdateManager(Activity mBaseActivity) {
		super(mBaseActivity);
		// TODO Auto-generated constructor stub
	}

	public String getCurrentVersion() {
		String strCurrVersion = "0.0";

		PackageManager pm = getBaseActivity().getPackageManager();
		try {
			PackageInfo info = pm.getPackageInfo(getBaseActivity()
					.getPackageName(), 0);
			strCurrVersion = info.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return strCurrVersion;
	}

	public String getLatestVersion() {
		String strLatestVersion = "-1.0";

		try {
			// TODO check alternative url list (register url)
			URL url = new URL(
					"http://code.google.com/p/truesculpt/wiki/Version");
			InputStream stream = url.openStream();
			InputStreamReader reader = new InputStreamReader(stream);
			BufferedReader buffReader = new BufferedReader(reader);
			String strTemp = buffReader.readLine();
			String strFileVersion = "";
			while (strTemp != null) {
				strFileVersion += strTemp;
				strTemp = buffReader.readLine();
			}
			buffReader.close();

			// <p>LATEST_STABLE_VERSION=0_1 </p>
			// LATEST_BETA_VERSION=0_1
			// UPDATE_BASE_URL=http://truesculpt.googlecode.com/files/TrueSculpt_

			Pattern p = Pattern.compile(
					"<p>LATEST_STABLE_VERSION=[0-9]+_[0-9]+ </p>",
					Pattern.CASE_INSENSITIVE);

			Matcher m = p.matcher(strFileVersion);
			if (m.find()) {
				String elem = m.group();
				elem = elem.replace("<p>LATEST_STABLE_VERSION=", "");
				elem = elem.replace("</p>", "");
				elem = elem.replace("_", ".");
				elem = elem.trim();

				strLatestVersion = elem;
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return strLatestVersion;
	}

	public String getUpdateStatus() {
		
		String msg="";
		
		String strCurrVersion = getCurrentVersion();
		String[] tempVer = strCurrVersion.split("\\.");
		int majCurr = -1;
		int minCurr = -1;
		if (tempVer.length == 2) {
			majCurr = Integer.parseInt(tempVer[0]);
			minCurr = Integer.parseInt(tempVer[1]);
		}

		String strLatestVersion = getLatestVersion();
		tempVer = strLatestVersion.split("\\.");
		int majLat = -1;
		int minLat = -1;
		if (tempVer.length == 2) {
			majLat = Integer.parseInt(tempVer[0]);
			minLat = Integer.parseInt(tempVer[1]);
		}

		if (majCurr >= 0 && minCurr >= 0 && majLat >= 0 && minLat >= 0) {
			boolean bUpdateNeeded = false;
			boolean bIsBeta = false;
			if (majLat > majCurr) {
				bUpdateNeeded = true;
			} else if (majLat == majCurr) {
				if (minLat > minCurr) {
					bUpdateNeeded = true;
				} else if (minLat < minCurr) {
					bIsBeta = true;
				}
			} else if (majLat < majCurr) {
				bIsBeta = true;
			}

			msg = getBaseActivity().getString(
					R.string.current_version_is_)
					+ strCurrVersion
					+ getBaseActivity().getString(R.string._latest_version_is_)
					+ strLatestVersion + ". ";
			if (bIsBeta) {
				msg += R.string.this_version_is_a_beta_;
			} else {
				msg += R.string.this_version_is_not_a_beta_;
			}

			if (bUpdateNeeded) {
				msg += R.string.an_update_is_needed_;
			} else {
				msg += R.string.no_update_is_needed_;
			}
		}
		
		return msg;
	}

}
