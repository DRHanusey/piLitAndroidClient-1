Pi-Lit! Android Application

This readme file contains the following:

1. all the new features implemented

2. any known bugs in this release

3. detailed instructions to build (include makefiles if there any), install and configure the entire project on target devices.


Pi-Lit! Android Application

1. This application allows a user to control LED lights connected to a Raspberry Pi. The application contains the following features:
	- User can custom control a single light, multiple lights, or select a pre-defined light configuration
	- User can create a personal profile that contains information about he or she and a list of his or her saved light configuration
	  patterns
	- User can save light configurations to his or her personal profile and/or to a public marketplace
	- User can preview a light configuration pattern before its implementation on the physical LED strip

2. This application has several bugs.
	- When saving a configuration, it will not show on the User screen until the user logs out and in.
	- After navigating to the Marketplace and pressing android’s back button and navigating back to the Marketplace, returning to the 
	  User screen using android’s back button on the second trip will take multiple presses. The Marketplace will load several times 
	  before sending the user to the User screen. This is a problem managing the activity stack.
	- When a user registers no message is displayed letting them know if the registration was successful or not.
	- When a configuration is sent no message is displayed letting them know if the command was successful or not.


3. This project can be built using Android Studio. To run the project, Android Studio must first be downloaded and installed. Next, the project must be downloaded and then opened in Android Studio. Once the project is open in Android Studio, the Android application can 
be run on either a physical or virtual Android device (for physical, Android device must be in debugging/developer mode; for virtual, the virtual Android device must first be configured via Android Studio before running).
