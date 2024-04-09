package org.learning.fixture

import kotlin.collections.ArrayDeque

class FakeCursor<T>(list: List<T>, private val stack: ArrayDeque<T> = ArrayDeque(list)): Iterator<T>, AutoCloseable {

    private val consumedValues = mutableListOf<T>()
    private var closed = false

    init {
        stack.addAll(list)
    }

    override fun next(): T {
        val removedValue = stack.removeFirst()
        consumedValues.add(removedValue)
        return removedValue
    }

    override fun hasNext(): Boolean = stack.isNotEmpty()

    override fun close() { closed = true }

    fun closed(): Boolean = closed

    fun consumedValues(): List<T> = consumedValues

}