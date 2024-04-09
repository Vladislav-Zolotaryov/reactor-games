package org.learning

import io.kotest.core.spec.style.StringSpec
import reactor.core.publisher.Flux
import reactor.test.StepVerifier

class FluxCombinationPractice : StringSpec({

    data class User(
        val id: String,
        val name: String,
    )

    data class UserAge(
        val id: String,
        val age: Int,
    )

    data class UserCreditScore(
        val id: String,
        val score: Int,
    )

    data class UserDto(
        val id: String,
        val name: String,
        val age: Int,
        val creditScore: Int? = null,
    )

    "combine user names with their respective salaries" {
        val users = Flux.just(
            User(id = "1", name = "Tommy"),
            User(id = "2", name = "Jim"),
            User(id = "3", name =  "Jane")
        )

        val userAge = Flux.just(
            UserAge(id = "1", age = 18),
            UserAge(id = "2", age = 30),
            UserAge(id = "3", age = 24)
        )

        val userCreditScore = Flux.just(
            UserCreditScore(id = "1", score = 100),
        )

        /** Implement **/

        StepVerifier.create(Flux.empty<UserDto>())
            .expectNextSequence(listOf(
                UserDto(id = "1", name = "Tommy", age = 18, creditScore = 100),
                UserDto(id = "2", name = "Jim", age = 30,),
                UserDto(id = "3", name = "Jane", age = 24,),
            ))
    }
})