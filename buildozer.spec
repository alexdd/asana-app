[app]

# (str) Title of your application
title = Yoga Asana Timer

# (str) Package name
package.name = yogaasanatimer

# (str) Package domain (needed for android/ios packaging)
package.domain = com.yoga

# (str) Source code where the main.py live
source.dir = .

# (list) Source files to include (let empty to include all the files)
source.include_exts = py,png,jpg,kv,atlas,json

# (list) List of inclusions using pattern matching
#source.include_patterns = assets/*,images/*.png

# (list) Source files to exclude (let empty to not exclude anything)
#source.exclude_exts = spec

# (list) List of directory to exclude (let empty to not exclude anything)
#source.exclude_dirs = tests, bin

# (list) List of exclusions using pattern matching
#source.exclude_patterns = license,images/*/*.jpg

# (str) Application versioning (method 1)
version = 1.0

# (str) Application versioning (method 2)
# version.regex = __version__ = ['\"](\d+\.\d+\.\d+\.\d+)['\"]
# version.filename = %(source.dir)s/main.py

# (list) Application requirements
# comma separated e.g. requirements = sqlite3,kivy
requirements = python3,kivy

# (str) Custom source folders for requirements
#requirements.source.kivy = ../../kivy

# (str) Presplash of the application
presplash.filename = %(source.dir)s/data/icon.png

# (str) Icon of the application
#icon.filename = %(source.dir)s/data/icon.png

# (str) Supported orientation (one of landscape, sensorLandscape, portrait or all)
orientation = landscape

# (list) List of service to declare
#services = NAME:ENTRYPOINT_TO_PY,NAME2:ENTRYPOINT2_TO_PY

#
# OSX Specific
#

#
# author = © Copyright Info

# change the major version of python used by the app
osx.python_version = 3

# Kivy version to use
osx.kivy_version = 2.1.0

#
# Android specific
#

# (bool) Indicate if the application should be fullscreen or not
fullscreen = 0

# (string) Presplash animation using Lottie format.
# See https://lottiefiles.com/ for examples and https://airbnb.design/lottie/
# for general documentation.
# Lottie files can be created using various tools, like Adobe After Ex
# (use Bodymovin export) or online tools like 'sottie'. (string)
# presplash.lottie = "path/to/lottie/file.json"

# (str) Adaptive icon of the application (used if Android API level is 26+ at runtime)
#icon.adaptive.icon = %(source.dir)s/data/icon.png

# (str) The application's updateable data (erases cache when updated on Google Play)
# You can use /data for this folder. Note that this will replace all your data folder.
#app_data_dir = %(source.dir)s/data

# (str) The application's cacheable data (preserved on update)
# You can use /cache for this folder
#app_cache_dir = %(source.dir)s/cache

#
# Python for android (p4a) specific
#

# (str) The directory in which python-for-android should look for your own build recipes (if any)
#p4a.local_recipes =

# (str) The directory in which python-for-android should look for your own build modules (if any)
#p4a.local_modules =

# (str) The default packaging format (apk / aab). Use 'aab' for Google Play
# release.
#android.release_artifact = apk

# (list) python-for-android whitelist of permissions
#android.permissions = INTERNET,READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE

# (str) Presplash is animated using Kivy's animation system
# android.presplash_animation = &lt;duration&gt;:&lt;kv_anim_file&gt;

# (list) Presplash animation lottie file, ie. 'path/to/animation.json', animated for
# for as long as specified in &lt;duration&gt;.
# android.presplash_lottie = &lt;duration&gt;:&lt;path/to/animation.json&gt;

# (int) target Android API, should be as high as possible. Default: 31
android.api = 33

# (int) Minimum API your APK will support. Default: 21
#android.minapi = 21

# (str) Android NDK version to use
#android.ndk = 23b

# (int) Android SDK version to use
#android.sdk = 30

# (bool) Use --private data storage (True) or --dir public storage (False)
#android.private_storage = True

# (str) Android app theme, default is ok for Kivy-based app
# android.apptheme = "@android:style/Theme.NoTitleBar"

# (list) Pattern to whitelist for the whole project
#android.whitelist =

# (str) Path to a custom whitelist file
#android.whitelist_src =

# (str) Path to a custom blacklist file
#android.blacklist_src =

# (list) List of Java .jar files to add to the libs so that pyjnius can access
# their classes. Don't add jars that you do not need, since extra jars can slow
# down the build process. Allows wildcards matching, for example:
# OUYA-ODK/libs/*.jar
#android.add_jars = foo.jar,bar.jar,path/to/more/*.jar

# (list) List of Java files to add to the android project (can be java or a
# directory containing the files)
#android.add_src =

# (str) OUYA Console category. Should be one of GAME or APP
# If you leave this blank, OUYA support will not be enabled
#android.ouya.category = GAME

# (str) Filename of OUYA Console icon. It must be a 732x412 png image.
#android.ouya.icon.filename = %(source.dir)s/data/ouya_icon.png

# (str) XML file to include as an intent filters in &lt;activity&gt; tag
#android.manifest.intent_filters =

# (str) launchMode to set for the main activity
#android.manifest.launch_mode = standard

# (list) Android additionnal libraries to copy into libs/armeabi
#android.add_compiled_py =

#
# iOS specific
#

# (str) Path to a custom kivy-ios folder
#ios.kivy_ios_dir = ../kivy-ios
# Alternately, specify the URL and branch of a git checkout:
ios.kivy_ios_url = https://github.com/kivy/kivy-ios
ios.kivy_ios_branch = master

# Another platform to try is master, if master doesn't work
#ios.kivy_ios_branch = master

# (str) Name of the certificate to use for signing the debug version
# Get a list of available identities: buildozer ios list_identities
#ios.codesign.debug = "iPhone Developer: <lastname> <firstname> (<hexstring>)"

# (str) The development team that you've been enrolled with is in here:
#ios.codesign.release = %(ios.codesign.debug)s

# (str) Name of the certificate to use for signing the release version
#ios.codesign.release = %(ios.codesign.debug)s

#
# Android NDK Bootstrap
#

# (str) Bootstrap name
# See https://github.com/kivy/python-for-android/blob/master/pythonforandroid/bootstrap.py
# for the list of available bootstraps.
# Android typically uses (pygame, sdl2, webview, service_only, or empty string for blank (and to create your own))
#android.bootstrap = sdl2

# (str) Entry point for SDL2 bootstrap
# See https://github.com/kivy/python-for-android/blob/master/doc/source/create-package.rst
# for SDL2 main.py entry point examples
# android.entrypoint = %(source.dir)s/main.py

# (str) entrypoint for service_only bootstrap
# See https://github.com/kivy/python-for-android/blob/master/pythonforandroid/recipes/service_only/__init__.py
# android.service.entrypoint = %(source.dir)s/main.py

# (list) The build.gradle file to read Python version from
# See: https://github.com/kivy/python-for-android/blob/master/pythonforandroid/toolchain.py
#android.build_gradle_py_version = %(source.dir)s/gradle.properties

# (list) Additionnal gradle repositories to add
# See: https://wiki.landata.de/index.php?title=Buildozer
#android.gradle_repositories =

# (list) Additionnal gradle dependencies to add, e.g.:
#android.gradle_dependencies =

#
# Cosmetic
#

# (str) Presplash image (default: None)
# Presplash is used when the app is launched. Be careful not to upload a presplash
# that's too heavy as it will increase the app's startup time.
# presplash.filename = %(source.dir)s/data/presplash.png

# (str) Icon filename (default: None)
# icon.filename = %(source.dir)s/data/icon.png

# (list) Permissions
#android.permissions = INTERNET,READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE

#
# Author information
#

# (str) Author name
#author = Author's Name

# (str) Author email
#author.email = author@email.com

# (str) Website
#url =

# (str) Application description
#description = My application description

# (str) Application license
#license = MIT

# (list) Multiple lines of description
#description.file = %(source.dir)s/README.md

# (str) Title in Google Play for example
#title = Open Source Project
#
# (list) Additional Android test files
#android.add_test_apk = [<apk>,<apk>,...]
#android.add_test_sources = [<filename>, <filename>, ...]

# (str) Android version code
#android.version_code = 1

# (str) Android version name
#android.version_name = 1.0

#
# iOS specific
#

# (str) Path to a custom kivy-ios folder
#ios.kivy_ios_dir = ../kivy-ios
# Alternately, specify the URL and branch of a git checkout:
#ios.kivy_ios_url = https://github.com/kivy/kivy-ios
#ios.kivy_ios_branch = master

# Another platform to try is master, if master doesn't work
#ios.kivy_ios_branch = master

# (str) Name of the certificate to use for signing the debug version
# Get a list of available identities: buildozer ios list_identities
#ios.codesign.debug = "iPhone Developer: <lastname> <firstname> (<hexstring>)"

# (str) The development team that you've been enrolled with is is in here:
#ios.codesign.release = %(ios.codesign.debug)s

# (str) Name of the certificate to use for signing the release version
#ios.codesign.release = %(ios.codesign.debug)s

# (str) How to show the build logs in the app logs
#ios.xcodeproj.log_level = 0

#
# Python for iOS specific
#

# (str) Path to your project's root folder
#osx.root = /Users/MyUser/Kivy.ios

# (str) Path to your project's IOS folder
#osx.ios.folder = iOS

# (list) the commands to trigger after the app has been built and is on the device
#ios.xcode_extra_commands =

# (str) Output format for iOS, either 'app' or 'ipa'
#ios.output_format = app

# (str) the format of the description in the apps
#ios.description_format = <%s

