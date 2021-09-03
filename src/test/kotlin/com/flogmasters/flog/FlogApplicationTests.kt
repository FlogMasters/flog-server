package com.flogmasters.flog

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import reactor.core.publisher.Flux
import java.time.Duration
import java.util.concurrent.CompletableFuture

class FlogApplicationTests: AbstractTest() {

    @Test
    fun contextLoads() {
        assert(true)
    }

    @Test
    fun createFlux_just(){
       val characterFlux = Flux.just("a","b","c").delayElements(Duration.ofMillis(500))
        val foodFlux = Flux.just("1","2","3")
                .delaySubscription(Duration.ofMillis(250))
                .delayElements(Duration.ofMillis(500))
        val mergeFlux = characterFlux.mergeWith(foodFlux)


        mergeFlux.subscribe {
            println(it)
        }
    }

    @Test
    fun fluxTest(){
        val firstFlux = Flux.just("a","bb","c","ghj")
        val secondFlux = Flux.just("1","2","3")

        val zippedFlux: Flux<String> = Flux.zip(firstFlux, secondFlux)

        zippedFlux.subscribe {
            println(it)
        }
        assert(false)
    }

    @Test
    fun firstFlux(){
        val slowFlux = Flux.just("tortoise","snail", "sloth")
        val fastFlux = Flux.just("hare", "cheetah", "squl")
        val firstFlux = Flux.first(slowFlux, fastFlux)

        firstFlux.subscribe {
            println(it)
        }
        assert(false)
    }


}
