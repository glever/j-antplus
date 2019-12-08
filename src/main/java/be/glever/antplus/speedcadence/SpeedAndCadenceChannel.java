package be.glever.antplus.speedcadence;

import be.glever.ant.channel.AntChannel;
import be.glever.ant.channel.AntChannelId;
import be.glever.ant.channel.AntChannelNetwork;
import be.glever.ant.channel.AntChannelTransmissionType;
import be.glever.ant.constants.AntChannelType;
import be.glever.ant.constants.AntNetworkKeys;
import be.glever.ant.constants.AntPlusDeviceType;
import be.glever.ant.message.AntMessage;
import be.glever.ant.message.channel.ChannelEventOrResponseMessage;
import be.glever.ant.message.data.BroadcastDataMessage;
import be.glever.ant.usb.AntUsbDevice;
import be.glever.ant.util.ByteUtils;
import be.glever.antplus.FluxProvider;
import be.glever.util.logging.Log;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;

public class SpeedAndCadenceChannel extends AntChannel implements FluxProvider {
    public static final byte CHANNEL_FREQUENCY = 0x39;  // RF Channel 57 (2457MHz)
    public static final byte[] DEVICE_NUMBER_WILDCARD = {0x00, 0x00};
    public static final byte DEFAULT_PUBLIC_NETWORK = 0x00;
    private static final Log LOG = Log.getLogger(SpeedAndCadenceChannel.class);
    private final AntUsbDevice device;

    /**
     * Foo
     *
     * Allowed values:
     *  8086/32768 seconds (~4.05 Hz)
     * 16172/32768 seconds (~2.03 Hz)
     * 32344/32768 seconds (~1.01 Hz)
     *
     * TODO: Allow setting the frequency
     */
    public static final byte[] CHANNEL_PERIOD = ByteUtils.toUShort(8086);

    private Flux<AntMessage> eventFlux;

    /**
     * Pair with any (the first found) SpeedCadence.
     *
     * @param device
     */
    public SpeedAndCadenceChannel(AntUsbDevice device) {
        this(device, DEVICE_NUMBER_WILDCARD);
    }

    /**
     * Pair with a known device.
     *
     * > The device number needs to be as unique as possible across production units. An example of achieving this specification is
     * to use the lowest two bytes of the serial number of the device for the device number of the ANT channel parameter; ensure
     * that the device has a set serial number.
     *
     * @param device
     * @param deviceNumber
     */
    public SpeedAndCadenceChannel(AntUsbDevice device, byte[] deviceNumber) {
        this.device = device;
        setChannelType(AntChannelType.BIDIRECTIONAL_SLAVE);
        setNetwork(new AntChannelNetwork(DEFAULT_PUBLIC_NETWORK, AntNetworkKeys.ANT_PLUS_NETWORK_KEY));
        setRfFrequency(CHANNEL_FREQUENCY);
        setChannelId(new AntChannelId(AntChannelTransmissionType.PAIRING_TRANSMISSION_TYPE, AntPlusDeviceType.SpeedCadence, deviceNumber));
        setChannelPeriod(CHANNEL_PERIOD);

        this.device.openChannel(this).block(Duration.ofSeconds(10));
    }

    @Override
    public void subscribeTo(Flux<AntMessage> messageFlux) {
        eventFlux = messageFlux
                .filter(this::isMatchingAntMessage)
                .distinctUntilChanged(AntMessage::toByteArray, Arrays::equals);
    }

    @Override
    public Flux<AntMessage> getEvents() {
        return eventFlux;
    }

    private boolean isMatchingAntMessage(AntMessage message) {
        if (message.getMessageId() == BroadcastDataMessage.MSG_ID) {
            return ((BroadcastDataMessage) message).getChannelNumber() == super.getChannelNumber();
        } else if (message.getMessageId() == ChannelEventOrResponseMessage.MSG_ID) {
            return ((ChannelEventOrResponseMessage) message).getChannelNumber() == super.getChannelNumber();
        }
        return false;
    }
}
