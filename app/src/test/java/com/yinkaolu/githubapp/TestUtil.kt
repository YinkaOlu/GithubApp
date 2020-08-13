package com.yinkaolu.githubapp

import org.mockito.ArgumentCaptor
import org.mockito.Mockito

@Suppress("UNCHECKED_CAST")
fun <T> uninitialized(): T =  null as T

object MockitoHelper {
    fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }
}

object ArgumentCaptorHelper {
    fun <T> capture(captor: ArgumentCaptor<T>): T {
        captor.capture()
        return uninitialized()
    }
}