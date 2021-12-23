package com.artlite.dynamicworkspace

import android.app.Application
import com.artlite.dynamicfieldlibrary.core.Module

/**
 * Current application instance.
 * @constructor Create empty Current application
 */
class CurrentApplication : Application() {

    /**
     * On create functional.
     */
    override fun onCreate() {
        super.onCreate()
        Module.onCreate(this)
    }

    /**
     * On terminate functional.
     */
    override fun onTerminate() {
        Module.onDestroy()
        super.onTerminate()
    }

}