package io.kairos.moviecatalogservice.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestController
public class MaintenanceController {

    private final Sinks.Many<ServerSentEvent<String>> sink;

    public MaintenanceController() {
        this.sink = Sinks.many().unicast().onBackpressureBuffer();
    }

    @PostMapping(value = "/pollEmitter", consumes = MediaType.ALL_VALUE, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @CrossOrigin
    public Flux<ServerSentEvent<String>> stream() {
        Flux<ServerSentEvent<String>> flux = sink.asFlux();

        return flux.doFinally(signalType -> {
                    if (signalType == SignalType.ON_COMPLETE) {
                        log.info("SseEmitter is completed");
                    }
                })
                .timeout(Duration.ofMinutes(5))
                .doOnError(error -> log.error("SseEmitter got error", error));
    }

    public void sendMessage(String message) {
        ServerSentEvent<String> event = ServerSentEvent.builder(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss"))).build();
        sink.tryEmitNext(ServerSentEvent.builder(message).build());
    }

}
