package com.flogmasters.flog.common.model

import java.lang.reflect.InvocationTargetException

object FlogContext {
    private val flogHeader: ThreadLocal<FlogHeader> = ThreadLocal()

    private val exception = ThreadLocal<Exception>()

    val contentType: String?
        get() = flogHeader.get().contentType

    val host: String?
        get() = flogHeader.get().host

    val isTest: Boolean?
        get() = flogHeader.get().isTest

    fun getFlogHeader() = flogHeader.get()

    @Throws(IllegalAccessException::class, InstantiationException::class, NoSuchMethodException::class, InvocationTargetException::class)
    fun getFlogHeader(clazz: Class<out FlogHeader?>?): FlogHeader {
        if (flogHeader.get() == null && clazz != null) {
            flogHeader.set(clazz.getDeclaredConstructor().newInstance())
        }
        return getFlogHeader()
    }

    fun setFlogHeader(flogHeader: FlogHeader) {
        FlogContext.flogHeader.set(flogHeader)
    }

    fun getException(): Exception? {
        return exception.get()
    }

    val exceptionStackTraceList: List<StackTraceElement>
        get() = (getException()?.stackTrace?.toMutableList() ?: mutableListOf()).filter { it.className.startsWith("com.flogmasters.flog") }

    fun setException(exception: Exception) {
        FlogContext.exception.set(exception)
    }

    fun removeFlogHeader() = flogHeader.remove()

    fun removeException() = exception.remove()

    fun removeAll() {
        removeFlogHeader()
        removeException()
    }
}