package com.artlite.dynamicfieldlibrary.impl

import android.content.Context
import com.artlite.dynamicfieldlibrary.facade.ContextManager
import java.lang.ref.WeakReference

/**
 * Context manager impl functional.
 *
 * @constructor Create empty Context manager impl
 */
internal object ContextManagerImpl : ContextManager {

    /**
     * Instance of the [WeakReference].
     */
    private var contextRef: WeakReference<Context>? = null

    /**
     * Method which provide to get context functional.
     *
     * @return context instance.
     */
    override fun getContext(): Context? = contextRef?.get()

    /**
     * Method which provide to get required context functional.
     *
     * @return context instance.
     */
    override fun getRequiredContext(): Context = getContext()!!

    /**
     * Method which provide to set context functional.
     *
     * @param context instance.
     */
    override fun setContext(context: Context?) {
        if (context == null) return
        contextRef = WeakReference(context)
    }

    /**
     * Method which provide to clearing functional.
     */
    override fun clear() {
        contextRef = null
    }
}