# Functionality

Dangdang is designed to help you sleep better.

# Installation
The pre-built apks are available [here](https://pan.baidu.com/s/1b_c9cdxsd4-UuEEbvhQG2g).  You can download them with passward `z2ve`.

# Prerequisites

One of these prerequisites must be satisfied before using the app.

- Disable the keyguard of your cellphone.

  It means every time you push the home button you are in the desktop, you don't have to slide or enter password to unlock the cellphone. Since we're unable to unlock screen programmatically so far. 

- or Keep your cellphone' screen on.

  For some devices, Dangdang fails to wake up the screen and thus is unable to check in correctly. The best option you can take is to keep your screen on.

Both of this two settings can be done in most of the cellphones, either in the normal setting panel or in the developer option. Take OPPO A59m for example, when we enter the developer mode, we can see these options:

<center><image src="images/oppo_a59m.png"/></center>

# Usage

1. Please see the prerequisites section before using this app.

2. Select the time range you want to check in, say 07:45:00~08:20:00, which is the default setting. Click on it will pop up a time picker:

   <center><image src="images/time_picker.png"/></center>

3. Click the `START` button to start an ever-ending background service and you're good to go.

# Releases

- Release 1:
  - Basic functionality: start Dingding in a fixed time.
- Release 2:
  - UI changes, bring in the time picker to let user select time.
  - Random check in strategy, the ability to check in randomly between a specified time range(say 08:00~08:30).
  - Disable the time picker after starting the service.

# TODOs
- [ ] Ability to unlock screen in the program
- [x] Check in at random time, say check in at any time between 07:45~08:00
- [ ] Design a better icon for the app.

# FAQs

1. What if the time range is irregular, say 09:00~08:00?

   In this case, we will just use a fixed strategy and check in at 09:00.

2. How do I know when exactly I check in since a random strategy is used?

   Well, you can but it's a little bit annoying. Since we keep all the logging in the external storage, you can view it with any text editor in your cellphone, say WPS. The logging files can be found in the directory "Dangdang/". Search for the tag `CheckInService`.

   <center><image src="images/logging.png"/></center>

3. How do I stop the background service?

   Well, it depends. In some devices(say HTC), you actually can't stop it so you'll have to restart your cellphone. But in other devices(say OPPO), you can just clear it with a slide.    