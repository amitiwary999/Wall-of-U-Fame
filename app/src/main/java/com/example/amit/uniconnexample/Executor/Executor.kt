package com.example.amit.uniconnexample.Executor

import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by Meera on 13,December,2019
 */
class Executor {
    companion object{
        val BOUNDED = Executors.newFixedThreadPool(Math.max(2, Math.min(Runtime.getRuntime().availableProcessors() - 1, 4)), NumberedThreadFactory("bounded"))
    }

    private class NumberedThreadFactory(private val baseName: String) : ThreadFactory {
        private val counter: AtomicInteger
        override fun newThread(r: Runnable): Thread {
            return Thread(r, baseName + "-" + counter.getAndIncrement())
        }

        init {
            counter = AtomicInteger()
        }
    }
}