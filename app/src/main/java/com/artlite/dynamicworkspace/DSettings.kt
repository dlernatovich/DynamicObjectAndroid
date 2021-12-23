package com.artlite.dynamicworkspace

import com.artlite.dynamicfieldlibrary.core.Module
import com.artlite.dynamicfieldlibrary.extensions.*

/**
 * Settings object.
 * @constructor Create empty settings class.
 */
object DSettings {

    /**
     * Prefs instance.
     */
    private val prefs = createEncryptedSharedPreferences(name = "settings")

    /**
     * Settings name.
     */
    val name = Module.Field(default = "")
        .configureGet { _, default -> prefs.getString("name", default) }
        .configureSet { _, value -> prefs.edit().putString("name", value).apply() }
        .configureCleaner { _, _ -> prefs.edit().remove("name").apply() }
        .configureLiveData { _, default -> prefs.string("name", default!!) }

    /**
     * Settings type
     */
    val type = Module.Field(default = TType.FIRST)
        .configureGet { _, default -> prefs.getEnum("type", TType.values(), default!!) }
        .configureSet { _, value -> prefs.edit().putEnum("type", value).apply() }
        .configureCleaner { _, _ -> prefs.edit().remove("type").apply() }
        .configureLiveData { _, default -> prefs.enum("type", default!!, TType.values()) }
}