package com.phani.grill.sse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalTime;

@RestController
@RequestMapping(path = "/sse3")
public class SSEController3 {

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux getEventsFlux(){
        return Flux.interval(Duration.ofSeconds(1))
                .map(sequence -> "Flux - " + LocalTime.now()
                        .toString());
    }

}
