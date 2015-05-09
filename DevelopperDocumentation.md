# Building #

  * Get the necessary build software requirements from the following wiki page : https://code.google.com/p/truesculpt/wiki/Requirements
  * Checkout the project with the following command "hg clone https://truesculpt.googlecode.com/hg/ truesculpt"
  * Import the project into eclipse
  * Project build is automatic and should compile without errors
  * Run project on your phone or export the apk with the ADT export tool

That's all

# Release checklist #

  * Set debuggable to false in manifest
  * Set increasing version name and version code in manifest
  * Export APK using provided manifest and correct password with the Android Tools/Export signed application
  * Version apk under mercurial Release directory
  * Commit everything and Tag version under mercurial (hg tag "Release X.X")
  * Publish to website and market with correct name (Trusculpt\_X\_X). Deprecate previous versions and features current one
  * Update website latest version number page at http://code.google.com/p/truesculpt/wiki/Version. Update main welcome page shortcuts
  * Increment version number/code for beta development. Set debuggable to true
  * Publish new screenshots (versionned under mercurial and referenced on web page)
  * Update Android Market apk and screenshots
  * Publish message in mailing list

# Libraries used #
  * Google Analytics for mobile (http://code.google.com/intl/fr/mobile/analytics/)
  * Android color picker (http://code.google.com/p/android-color-picker/) : Apache License
  * Interfuser coverflow widget (http://www.inter-fuser.com/2010/02/android-coverflow-widget-v2.html)
  * Universal Image Loader (https://github.com/nostra13/Android-Universal-Image-Loader)

# Icons used #
  * Must have icons (http://www.visualpharm.com) : Creative Commons Attribution No Derivative 3.0
  * Codex Icons (http://iliadraznin.com) : Creative Commons Attribution Share Alike 3.0
  * Android developer icons 2.0 : Creative Commons Attribution 3.0
  * Blender sculpting tools icons (http://www.blender.org) : GPL

# Permissions details #
  * android.permission.INTERNET : Needed for Web library and update check
  * android.permission.WRITE\_EXTERNAL\_STORAGE : Needed to store sculptures on disk
  * android.permission.ACCESS\_NETWORK\_STATE : Needed for google analytics tracking
  * android.permission.WAKE\_LOCK : Needed for prevent sleeping mode option
  * android.permission.SET\_WALLPAPER : Needed for the set sculpture image as wallpaper option

# Tips #
  * Modify the eclipse ADB timeout to a very large value to avoid phone disconections (Window/Preferences/Android/DDMS/ADB TimeOut)
  * Import the TrueSculpt task list into eclipse from TrueSculpt/Tasks/tasks.xml.zip (Window/Preferences/Mylyn/Tasks/Advanced)
  * Update yout eclipse plugins and android sdk frequently with the tools provided (eclipse updater and android package manager)
  * Setup your Mercurial plugin with correct path and connection information
  * Setup correct android version and jdk/jre versions inside eclipse preferences (Window/Preferences/Android and Window/Preferences/Java/Installed JREs)
  * Modify your root mercurial .hg/hgrc file with your connection information for easier access
-paths
default = https://truesculpt.googlecode.com/hg/
-ui
username = fabrice.boyer@gmail.com
-auth
truesculpt.prefix = https://truesculpt.googlecode.com/hg/
truesculpt.username = fabrice.boyer@gmail.com
truesculpt.password = XXXX