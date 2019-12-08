# j-antplus
Java Library for speaking with ANT / ANT+ devices through a usb dongle.

I set up this project trying to talk to my personal ant+ devices.
Goal is to implement needed ant messages and ant+ datapages in order to talk to following set of devices:
* Heart rate monitor:
	* Basic support. Integrates with Garmin HRM3SS. Check out HrmTest_Main under src/test
	* No support for legacy format (yet, because I don't own a legacy device). If you need it, check out HrmChannel to sniff the datapage 1st bit alternating every 4 packages.
* Speed sensor (TODO)
* Cadence sensor(TODO)
* Power meter(TODO)
* Ant FE-c trainer(TODO)


# Current status: 
Codebase still unstable but foundation is shaping up. Expect a few heavy refactors until things stabilize.
Check out the Hrm/SpeedTest/Cadence/Fec examples to get started. Many thanks to https://github.com/JohnAZoidberg for help with implementing most of the devices.

If you want to re-use this library in another project run `gradle publishToMavenLocal`. 
Then you can add a maven dependency to `be.glever:j-antplus:0.0.1-SNAPSHOT` in order to use it.

# Compatibility notes
## Windows
This project uses usb4java-javax, which underneath uses libusb.
There seems to be a mismatch between the default windows10 WHQL driver (libusb 1.2.40.201, provided by dynastream) and libusb.
I resolved this by using the "Zadig" tool, select the ant-m usb stick (you may need to go to "Options" -> "list all devices")
and *downgrade* the driver to 1.2.6.0 (which is actually the latest libusb driver I could find on the net).

## Linux
Tested on Ubuntu: you need to give access to the usb interface for current user.
I followed this approach (which may give too wide permissions to your taste): https://askubuntu.com/a/930338  
```
echo 'SUBSYSTEM=="usb", MODE="0660", GROUP="plugdev"' > /etc/udev/rules.d/00-usb-permissions.rules
udevadm control --reload-rules
```
After this, unplug and re-plug the dongle.
