package com.artlite.dynamicfieldlibrary.extensions

import android.content.SharedPreferences
import android.os.Bundle

/**
 * Method which provide to get enum from Bundle.
 * @receiver Bundle receiver.
 * @param key String key.
 * @param default T default value.
 * @return T value.
 */
inline fun <reified T : Enum<T>> Bundle.getEnum(key: String, default: T): T =
    getInt(key).let { if (it >= 0) enumValues<T>()[it] else default }

/**
 * Method which provide to put enum to bundle.
 * @receiver Bundle receiver.
 * @param key String key.
 * @param value T? instance.
 */
fun <T : Enum<T>> Bundle.putEnum(key: String, value: T?) =
    putInt(key, value?.ordinal ?: -1)

/**
 * Method which provide the getting enum from the [SharedPreferences].
 * @receiver SharedPreferences receiver.
 * @param key String key.
 * @return T value.
 */
fun <T : Enum<T>> SharedPreferences.getEnum(key: String, values: Array<T>, default: T): T {
    return this.getInt(key, -1).let { if (it >= 0) values[it] else default }
}


/**
 * Method which provide the getting enum from the [SharedPreferences].
 * @receiver SharedPreferences receiver.
 * @param key String key.
 * @param default T default value.
 * @return T value.
 */
inline fun <reified T : Enum<T>> SharedPreferences.getEnum(key: String, default: T): T {
    return this.getInt(key, -1).let { if (it >= 0) enumValues<T>()[it] else default }
}


/**
 * Method which provide to put enum to [SharedPreferences].
 * @receiver SharedPreferences.Editor receiver.
 * @param key String key.
 * @param value T? instance.
 * @return SharedPreferences.Editor editor.
 */
fun <T : Enum<T>> SharedPreferences.Editor.putEnum(key: String, value: T?):
        SharedPreferences.Editor = this.putInt(key, value?.ordinal ?: -1)