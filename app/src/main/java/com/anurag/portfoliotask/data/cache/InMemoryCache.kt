package com.anurag.portfoliotask.data.cache

import com.anurag.portfoliotask.domain.model.Holding
import java.util.concurrent.atomic.AtomicReference

object InMemoryCache {
    private val _holdings = AtomicReference<List<Holding>?>(null)

    var holdings: List<Holding>?
        get() = _holdings.get()
        set(value) = _holdings.set(value)
}