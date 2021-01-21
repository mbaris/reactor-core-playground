package io.baris.reactive.mono;

import reactor.core.publisher.Mono;

import java.util.concurrent.Callable;

public class MonoExamples {

    Mono<String> helloWorld() {
        return Mono.just("Hello World!");
    }

    Mono<String> empty() {
        return Mono.empty();
    }

    <T> Mono<T> monoJustWithType(T input) {
        return Mono.just(input);
    }

    Mono<Object> monoFromError(Exception e) {
        return Mono.error(e);
    }

    <T> Mono<T> monoFromCallable(Callable<T> callable) {
        return Mono.fromCallable(callable);
    }

}
