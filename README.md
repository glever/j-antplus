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

License is MIT.

Compatibility notes:
This project uses usb4java-javax, which underneath uses libusb. 
There seems to be a mismatch between the default windows10 WHQL driver (libusb 1.2.40.201, provided by dynastream) and libusb.
I resolved this by using the "Zadig" tool, select the ant-m usb stick (you may need to go to "Options" -> "list all devices")
and *downgrade* the driver to 1.2.6.0 (which is actually the latest libusb driver I could find on the net).

# Current status: 
unusable :) . 
Basic ant messages are implemented, placeholders for all others created under .nonimplemented packages.
Can set up ant channel to receive HRM broadcast messages, but they are not yet interpreted. See AntDeviceTest_Main for entry point.
Code will likely undergo a few heavy refactors until things stabilize a bit.
