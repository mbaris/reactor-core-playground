package io.baris.reactive.mono

import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import spock.lang.Specification

import java.util.concurrent.Callable
import java.util.concurrent.atomic.AtomicInteger

class MonoExamplesTest extends Specification {

    private static final MonoExamples monoExamples = new MonoExamples()
    private static final Random random = new Random()

    def "Mono"() {
        when:
        Mono<String> mono = monoExamples.helloWorld()

        then:
        StepVerifier.create(mono)
                .expectNext("Hello World!")
                .verifyComplete()
    }

    def "Mono empty"() {
        when:
        Mono<String> mono = monoExamples.empty()

        then:
        StepVerifier.create(mono).verifyComplete()
    }

    def "Mono with different types"() {

        when:
        Mono mono = monoExamples.monoJustWithType(input)

        then:
        StepVerifier.create(mono)
                .expectNext(input)
                .verifyComplete()

        where:
        input << ["Hello", "World", 42, Arrays.asList("listItem")]

    }

    def "Mono from error"() {

        when:
        Mono<Object> monoWithError = monoExamples.monoFromError(exception)

        then:
        StepVerifier.create(monoWithError)
                .expectError(exceptionClass)

        where:
        exception              | exceptionClass
        new RuntimeException() | RuntimeException.class
        new IOException()      | IOException.class
        new Exception()        | Exception.class
    }

    def "Mono from callable"() {
        given:
        Callable<String> callable = { return input + randomNumber } as Callable<String>

        when:
        Mono<String> mono = monoExamples.monoFromCallable(callable)

        then:
        StepVerifier.create(mono)
                .expectNext(input + randomNumber)
                .verifyComplete()

        where:
        input                  | randomNumber
        "Hello from callable " | random.nextInt()
    }

    def "Mono just captures the initial value while fromCallable is executed every time"() {
        given:
        AtomicInteger counter = new AtomicInteger(0)
        Callable<Integer> callable = { return counter.incrementAndGet() } as Callable<Integer>

        when:
        Mono<Integer> monoJust = monoExamples.monoJustWithType(counter.incrementAndGet())
        Mono<Integer> monoCallable = monoExamples.monoFromCallable(callable)

        then:
        StepVerifier.create(monoJust)
                .expectNext(1)
                .verifyComplete()

        StepVerifier.create(monoCallable)
                .expectNext(2)
                .verifyComplete()

        StepVerifier.create(monoJust)
                .expectNext(1)
                .verifyComplete()

        StepVerifier.create(monoCallable)
                .expectNext(3)
                .verifyComplete()

        StepVerifier.create(monoJust)
                .expectNext(1)
                .verifyComplete()

        StepVerifier.create(monoCallable)
                .expectNext(4)
                .verifyComplete()

    }
}
