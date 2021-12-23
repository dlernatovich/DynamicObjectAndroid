package com.artlite.dynamicfieldlibrary.core

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.artlite.dynamicfieldlibrary.facade.ContextManager
import com.artlite.dynamicfieldlibrary.model.InternalLiveField

/**
 * Module for current library.
 * @constructor Create empty Module
 */
object Module {

    /**
     * Context manager instance.
     */
    private val contextManager by ContextManager()

    /**
     * Method which provide to create functional.
     * @param context Context? instance.
     */
    fun onCreate(context: Context?) = contextManager.setContext(context)

    /**
     * On destroy functional.
     */
    fun onDestroy() = contextManager.clear()

    //==============================================================================================
    //                                    MODULE COMPONENTS
    //==============================================================================================

    /**
     * Field class.
     */
    open class Field<T>(
        data: MutableLiveData<T>? = null,
        default: T? = null
    ) : InternalLiveField<T>(data, default)

}