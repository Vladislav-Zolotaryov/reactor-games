package org.learning

import io.kotest.core.spec.style.StringSpec
import reactor.core.publisher.Flux
import reactor.test.StepVerifier

class NestingHandlingPractice : StringSpec({
    "flatten nested integer stream and reduce to a sum" {
        val data = Flux.just(
            Flux.just(1, 2, 3),
            Flux.just(4),
            Flux.just(5, 6, 7),
            Flux.just(8, 9),
        )

        val result = data.flatMap { it.reduce(Int::plus) }.reduce(Int::plus)

        StepVerifier.create(result).expectNext(45).verifyComplete()
    }
})