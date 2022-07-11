package com.frantun.peliandchill.common

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After

/**
 * Base class for ViewModels that use sealed class state object.
 */
@ExperimentalCoroutinesApi
open class BaseCoroutineViewModelStateTest<T> : BaseCoroutineTest() {

    protected var stateList = mutableListOf<T>()

    @After
    fun after() {
        stateList.clear()
    }
}
