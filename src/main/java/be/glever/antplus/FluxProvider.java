package be.glever.antplus;

import be.glever.ant.message.AntMessage;
import reactor.core.publisher.Flux;

public interface FluxProvider {
    Flux<AntMessage> getEvents();
}
