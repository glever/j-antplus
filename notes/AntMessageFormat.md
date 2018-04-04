All Ant messages are a byte array with following content:
	[0] SYNC 10100100 or 10100101 to signal start of message
	[1] MSG LEN: number of data bytes in the message
	[2] MSG ID: Data type identifier (?)
	[3..N+2] PAYLOAD content of the message with length N, with N PROBABLY being 8 (= ANT+ datapage?)
	[N+3] CHECKSUM: XOR of all preceding bytes including the SYNC byte

Ant Message can be extended with additional info (transmission type, device type and device number), here there are 2 types:
	Legacy, adds following bytes between MSG ID and PAYLOAD:
		[0] Channel Number
		[1..2] Device Number
		[3] Device Type
		[4] Transmission Type
	Flagged Extended Data Message, adds following bytes between PAYLOAD and CHECKSUM
		[0] flag byte 0x80
		[1..2] device number
		[3] device type
		[4] transmission type
	TODO CHANNEL NUMBER IS NEVER EXPLAINED: NOT DESCRIBED IN BASIC MESSAGE AND APPEARS IN XTND MESSAGE WITHOUT EXPLANATION