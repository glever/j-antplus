# j-antplus
Library for speaking to select ant+ devices

I set up this project trying to talk to my personal ant+ devices.
Goal is to implement needed ant messages and ant+ datapages in order to talk to following set of devices:
* Heart rate monitor
* Speed sensor
* Cadence sensor
* Power meter
* Ant FE-c trainer

License is GPL3.

Compatibility notes:
This project uses usb4java-javax, which underneath uses libusb. 
There seems to be a mismatch between the default windows10 WHQL driver (libusb 1.2.40.201, provided by dynastream) and libusb. I resolved this by using the "Zadig" tool, select the ant-m usb stick (you may neet to go to "Options" -> "list all devices") and *downgrade* the driver to 1.2.6.0 (which is actually the latest libusb driver I could find on the net).
