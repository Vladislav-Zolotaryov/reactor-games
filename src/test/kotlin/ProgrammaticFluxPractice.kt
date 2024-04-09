package org.learning

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.learning.fixture.FakeCursor
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.toJavaDuration


class ProgrammaticFluxPractice : StringSpec({
    "emit integers until a limit is met" {
        val limit = 100

        val generator: Flux<Int> = Flux.create {
            var runningCount = 0
            while (runningCount < limit) {
                runningCount += 1
                it.next(runningCount)
            }
            it.complete()
        }

        StepVerifier.create(generator)
            .expectNextSequence(1..limit)
            .verifyComplete()
    }

    "emit doubled integers, starting with base value of 2 and until infinity" {
        val initialValue = 2

        val generator: Flux<Int> = Flux.generate({ initialValue }, { state, generator ->
            val newState = state * 2
            generator.next(newState)
            newState
        })

        StepVerifier.create(generator)
            .expectNext(4)
            .expectNext(8)
            .expectNext(16)
            .thenCancel()
            .verifyThenAssertThat()
            .hasNotDroppedElements()
    }

    "consume a cursor as a flux with cancellation and resource management in mind" {
        val fakeCursor = FakeCursor(listOf(5, 10, 15, 20, 99))

        val generator = Flux.generate { sink ->
            if (fakeCursor.hasNext()) {
                sink.next(fakeCursor.next())
            } else {
                sink.complete()
            }
        }.doFinally { fakeCursor.close() }

        StepVerifier.create(generator)
            .expectNext(5)
            .expectNext(10)
            .thenCancel()
            .verifyThenAssertThat()
            .hasNotDroppedElements()

        fakeCursor.closed() shouldBe true
        fakeCursor.consumedValues() shouldBe listOf(5, 10)
    }

    "trigger backpressure protection with drop elements strategy" {
        val testValue = "something"

        val producerRate = 100.milliseconds.toJavaDuration()
        val consumerLag = 150.milliseconds.toJavaDuration()

        /** Create a timed producer that emits a value each 100 ms **/
        val producer = Flux.interval(producerRate).map { testValue }

        /** Add a slow consumer that consumes values with a delay of 150 ms **/
        val consumer = producer
            .onBackpressureDrop {
                println("Dropping")
            }
            .concatMap {
                Mono.delay(consumerLag).then(Mono.just(it)).doOnNext { println("Consuming") }
            }

        StepVerifier
            .create(consumer)
            .expectNext(testValue)
            .expectNext(testValue)
            .thenCancel()
            .verify()
    }
})