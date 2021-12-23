package com.artlite.dynamicfieldlibrary.facade

import android.content.Context
import com.artlite.dynamicfieldlibrary.impl.ContextManagerImpl
import kotlin.properties.ReadOnlyProperty

/**
 * Context manager
 *
 * @constructor Create empty Context manager
 */
interface ContextManager {
    /**
     * Method which provide to get context functional.
     *
     * @return context instance.
     */
    fun getContext(): Context?

    /**
     * Method which provide to get required context functional.
     *
     * @return context instance.
     */
    fun getRequiredContext(): Context

    /**
     * Method which provide to set context functional.
     *
     * @param context instance.
     */
    fun setContext(context: Context?)

    /**
     * Method which provide to clearing functional.
     */
    fun clear()

    /**
     * Static functional.
     */
    companion object : () -> ReadOnlyProperty<Any?, ContextManager> {
        override fun invoke(): ReadOnlyProperty<Any?, ContextManager> =
            ReadOnlyProperty<Any?, ContextManager> { _, _ -> ContextManagerImpl }
    }
}