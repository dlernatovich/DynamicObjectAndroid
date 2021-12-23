package com.artlite.dynamicfieldlibrary.extensions

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.artlite.dynamicfieldlibrary.BuildConfig
import com.artlite.dynamicfieldlibrary.facade.ContextManager
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

/**
 * Default date formatter.
 */
private val formatter: DateTimeFormatter get() = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss")

/**
 * Shared preference live data.
 * @param T type.
 * @property prefs SharedPreferences instance.
 * @property key String value.
 * @property defValue T default.
 * @property listener OnSharedPreferenceChangeListener instance.
 * @constructor
 */
private abstract class SharedPreferenceLiveData<T>(
    val prefs: SharedPreferences,
    val key: String,
    val defValue: T,
    val values: Array<T>
) : LiveData<T>() {

    /** Instance of the [OnSharedPreferenceChangeListener]. */
    private val listener = OnSharedPreferenceChangeListener { _, key ->
        if (key == this.key) value = getValue(key, defValue)
    }

    /**
     * Method which provide action when [LiveData] is active.
     */
    override fun onActive() {
        super.onActive()
        value = getValue(key, defValue)
        prefs.registerOnSharedPreferenceChangeListener(listener)
    }

    /**
     * Method which provide the action when [LiveData] is inactive.
     */
    override fun onInactive() {
        prefs.unregisterOnSharedPreferenceChangeListener(listener)
        super.onInactive()
    }

    /**
     * Method which provide value from reference.
     * @param key String value.
     * @param defValue T value.
     * @return T value.
     */
    abstract fun getValue(key: String, defValue: T): T
}

/** Integer [LiveData]. */
private class IntLiveData(prefs: SharedPreferences, key: String, defValue: Int) :
    SharedPreferenceLiveData<Int>(prefs, key, defValue, emptyArray()) {
    override fun getValue(key: String, defValue: Int): Int =
        prefs.getInt(key, defValue)
}

/** String [LiveData]. */
private class StringLiveData(prefs: SharedPreferences, key: String, defValue: String) :
    SharedPreferenceLiveData<String>(prefs, key, defValue, emptyArray()) {
    override fun getValue(key: String, defValue: String): String =
        prefs.getString(key, defValue) ?: defValue
}

/** Boolean [LiveData]. */
private class BooleanLiveData(prefs: SharedPreferences, key: String, defValue: Boolean) :
    SharedPreferenceLiveData<Boolean>(prefs, key, defValue, emptyArray()) {
    override fun getValue(key: String, defValue: Boolean): Boolean =
        prefs.getBoolean(key, defValue)
}

/** Float [LiveData] */
private class FloatLiveData(prefs: SharedPreferences, key: String, defValue: Float) :
    SharedPreferenceLiveData<Float>(prefs, key, defValue, emptyArray()) {
    override fun getValue(key: String, defValue: Float): Float =
        prefs.getFloat(key, defValue)
}

/** Long [LiveData]. */
private class LongLiveData(prefs: SharedPreferences, key: String, defValue: Long) :
    SharedPreferenceLiveData<Long>(prefs, key, defValue, emptyArray()) {
    override fun getValue(key: String, defValue: Long): Long =
        prefs.getLong(key, defValue)
}

/** String set [LiveData]. */
private class StringSetLiveData(prefs: SharedPreferences, key: String, defValue: Set<String>) :
    SharedPreferenceLiveData<Set<String>>(prefs, key, defValue, emptyArray()) {
    override fun getValue(key: String, defValue: Set<String>): Set<String> =
        prefs.getStringSet(key, defValue)?.toSet() ?: defValue
}

/** Enum [LiveData]. */
private class EnumLiveData<T : Enum<T>>(
    prefs: SharedPreferences,
    key: String,
    defValue: T,
    values: Array<T>
) : SharedPreferenceLiveData<T>(prefs, key, defValue, values) {
    override fun getValue(key: String, defValue: T): T {
        return prefs.getEnum(key, values, defValue)
    }
}

/** Local date [LiveData]. */
private class LocalDateLiveData(prefs: SharedPreferences, key: String, defValue: LocalDateTime) :
    SharedPreferenceLiveData<LocalDateTime>(prefs, key, defValue, emptyArray()) {
    override fun getValue(key: String, defValue: LocalDateTime): LocalDateTime {
        return prefs.getLocalDate(key, defValue) ?: defValue
    }
}

/**
 * Method which provide to get of the int [LiveData].
 * @receiver SharedPreferences receiver.
 * @param key String value.
 * @param def Int default value.
 * @return LiveData<Int> instance.
 */
fun SharedPreferences.int(key: String, def: Int = 0): LiveData<Int> {
    return IntLiveData(this, key, def)
}

/**
 * Method which provide to get of the string [LiveData].
 * @receiver SharedPreferences receiver.
 * @param key String value.
 * @param def Int default value.
 * @return LiveData<String> instance.
 */
fun SharedPreferences.string(key: String, def: String = ""): LiveData<String> {
    return StringLiveData(this, key, def)
}

/**
 * Method which provide to get of the bool [LiveData].
 * @receiver SharedPreferences receiver.
 * @param key String value.
 * @param def Int default value.
 * @return LiveData<Boolean> instance.
 */
fun SharedPreferences.bool(key: String, def: Boolean = false): LiveData<Boolean> {
    return BooleanLiveData(this, key, def)
}

/**
 * Method which provide to get of the float [LiveData].
 * @receiver SharedPreferences receiver.
 * @param key String value.
 * @param def Int default value.
 * @return LiveData<Float> instance.
 */
fun SharedPreferences.float(key: String, def: Float = 0.0f): LiveData<Float> {
    return FloatLiveData(this, key, def)
}

/**
 * Method which provide to get of the long [LiveData].
 * @receiver SharedPreferences receiver.
 * @param key String value.
 * @param def Int default value.
 * @return LiveData<Long> instance.
 */
fun SharedPreferences.long(key: String, def: Long = 0L): LiveData<Long> {
    return LongLiveData(this, key, def)
}

/**
 * Method which provide to get of the string set [LiveData].
 * @receiver SharedPreferences receiver.
 * @param key String value.
 * @param def Int default value.
 * @return LiveData<Set<String>> instance.
 */
fun SharedPreferences.stringSet(key: String, def: Set<String> = emptySet()): LiveData<Set<String>> {
    return StringSetLiveData(this, key, def)
}

/**
 * Method which provide to get of the string set [LiveData].
 * @receiver SharedPreferences receiver.
 * @param key String value.
 * @param def Int default value.
 * @return LiveData<Set<String>> instance.
 */
@Suppress("UNCHECKED_CAST")
fun <K : Enum<K>> SharedPreferences.enum(key: String, def: K, values: Array<K>): LiveData<K> {
    return EnumLiveData(this, key, def, values)
}

/**
 * Method which provide to get of the local date [LiveData].
 * @receiver SharedPreferences receiver.
 * @param key String value.
 * @param def LocalDateTime instance.
 * @return LiveData<LocalDateTime> instance.
 */
fun SharedPreferences.localDate(
    key: String,
    def: LocalDateTime = LocalDateTime.now()
): LiveData<LocalDateTime> {
    return LocalDateLiveData(this, key, def)
}

/**
 * Method which provide to put [LocalDateTime]
 * @receiver SharedPreferences.Editor receiver.
 * @param key String value.
 * @param value LocalDateTime? instance.
 * @return Boolean if it saved.
 */
fun SharedPreferences.Editor.putLocalDate(
    key: String,
    value: LocalDateTime?
): SharedPreferences.Editor {
    val text = value?.toString(formatter) ?: return this
    this.putString(key, text)
    return this
}

/**
 * Method which provide to read [LocalDateTime]
 * @receiver SharedPreferences receiver.
 * @param key String value.
 * @param defValue LocalDateTime? default value.
 * @return LocalDateTime? instance.
 */
fun SharedPreferences.getLocalDate(key: String, defValue: LocalDateTime? = null): LocalDateTime? {
    return getString(key, null)?.toLocalDate(formatter) ?: defValue
}

/**
 * Method which provide the clear preferences functional.
 * @receiver SharedPreferences receiver.
 */
fun SharedPreferences.clear() {
    this.edit().clear().apply()
}

/**
 * Method which provide to create of the encrypted shared preferences.
 * @param context Context instance.
 * @param name String value.
 * @param needClean if need to clean [SharedPreferences].
 * @return SharedPreferences instance.
 */
fun createEncryptedSharedPreferences(
    context: Context? = null,
    name: String,
    needClean: Boolean = false,
    needFormatted: Boolean = true
): SharedPreferences {
    val contextManager by ContextManager()
    val requiredContext = context ?: contextManager.getRequiredContext()
    val formattedName = when (needFormatted) {
        true -> "${BuildConfig.LIBRARY_PACKAGE_NAME}.$name"
        else -> name
    }
    val masterKey = MasterKey.Builder(requiredContext, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()
    val preferences = EncryptedSharedPreferences.create(
        requiredContext,
        formattedName,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    if (needClean) preferences.clear()
    return preferences
}

/**
 * Method which provide to convert [String] to [LocalDateTime]
 * @receiver String receiver.
 * @return LocalDateTime value.
 */
private fun String.toLocalDate(formatter: DateTimeFormatter): LocalDateTime? {
    return try {
        LocalDateTime.parse(this, formatter)
    } catch (ex: Exception) {
        Log.e(this, "Can\'t convert string to local date with format $formatter")
        null
    }
}