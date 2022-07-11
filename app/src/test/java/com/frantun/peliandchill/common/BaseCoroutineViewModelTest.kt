package com.frantun.peliandchill.common

import android.content.Context
import android.content.pm.ApplicationInfo
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before

/**
 * Base class for ViewModels that instantiate common dependencies.
 */
@ExperimentalCoroutinesApi
open class BaseCoroutineViewModelTest : BaseCoroutineTest() {

    protected val context = mockk<Context>()
    private val applicationInfo = mockk<ApplicationInfo>()

    @Before
    open fun before() {
        every { context.applicationInfo } returns applicationInfo
    }
}
