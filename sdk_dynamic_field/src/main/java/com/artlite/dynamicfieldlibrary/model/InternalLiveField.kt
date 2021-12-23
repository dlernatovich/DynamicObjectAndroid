package com.artlite.dynamicfieldlibrary.model

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * Live field cast typealias.
 */
typealias LiveFieldCast<T> = () -> T?

/**
 * Live field class.
 * @param T type.
 * @property data MutableLiveData<T>? instance.
 * @property default T? value.
 * @property setter Function2<[@kotlin.ParameterName] MutableLiveData<T>?, [@kotlin.ParameterName] T?, Unit>? callback.
 * @property getter Function2<[@kotlin.ParameterName] MutableLiveData<T>?, [@kotlin.ParameterName] T?, T?>? callback.
 * @property cleaner Function2<[@kotlin.ParameterName] MutableLiveData<T>?, [@kotlin.ParameterName] T?, Unit>? callback.
 * @property liveData Function2<[@kotlin.ParameterName] MutableLiveData<T>?, [@kotlin.ParameterName] T?, LiveData<T>?>? callback.
 * @property liveDataObject LiveData<T>? instance.
 * @constructor
 */
open class InternalLiveField<T>(
    private val data: MutableLiveData<T>? = null,
    private val default: T? = null
) : LiveFieldCast<T> {

    /** Setter. */
    private var setter: ((data: MutableLiveData<T>?, value: T?) -> Unit)? = null

    /** Getter. */
    private var getter: ((data: MutableLiveData<T>?, default: T?) -> T?)? = null

    /** Getter. */
    private var cleaner: ((data: MutableLiveData<T>?, default: T?) -> Unit)? = null

    /** Live data. */
    private var liveData: ((data: MutableLiveData<T>?, default: T?) -> LiveData<T>?)? = null

    /** Observer value. */
    val liveDataObject: LiveData<T>? get() = liveData?.invoke(data, default)

    /**
     * Method which provide the invoke functional.
     * @return T? instance.
     */
    override fun invoke(): T? = getter?.invoke(data, default)

    /**
     * Method which provide configure setter.
     * @param setter Function1<[@kotlin.ParameterName] T?, Unit> setter function.
     */
    fun configureSet(
        setter: (data: MutableLiveData<T>?, value: T?) -> Unit
    ): InternalLiveField<T> {
        this.setter = setter
        return this
    }

    /**
     * Method which provide configure setter.
     * @param getter Function0<T?>
     */
    fun configureGet(
        getter: (data: MutableLiveData<T>?, default: T?) -> T?
    ): InternalLiveField<T> {
        this.getter = getter
        return this
    }

    /**
     * Method which provide configure cleaner.
     * @param cleaner Function0<T?>
     */
    fun configureCleaner(
        cleaner: (data: MutableLiveData<T>?, default: T?) -> Unit
    ): InternalLiveField<T> {
        this.cleaner = cleaner
        return this
    }

    /**
     * Method which provide configure setter.
     * @param liveData Function0<LiveData<T>?>
     * @return LiveField<T>
     */
    fun configureLiveData(
        liveData: (data: MutableLiveData<T>?, default: T?) -> LiveData<T>?
    ): InternalLiveField<T> {
        this.liveData = liveData
        return this
    }

    /**
     * Method which provide the update functional.
     * @param value T? instance.
     * @return LiveField<T>
     */
    fun update(value: T?): InternalLiveField<T> {
        this.setter?.invoke(data, value)
        return this
    }

    /**
     * Method which provide the update functional.
     * @param updater Function0<T?> callback.
     * @return LiveField<T>
     */
    inline fun update(
        crossinline updater: () -> T?
    ): InternalLiveField<T> = update(updater())

    /**
     * Method which provide the fetch functional
     * @param owner LifecycleOwner
     * @param observer Observer<T>
     */
    fun fetch(owner: LifecycleOwner, observer: Observer<T>) {
        this.liveDataObject?.observeOnce(owner, observer)
    }

    /**
     * Method which provide the observe functional
     * @param owner LifecycleOwner
     * @param observer Observer<T>
     */
    fun observe(owner: LifecycleOwner, observer: Observer<T>) {
        this.liveDataObject?.observe(owner, observer)
    }

    /**
     * Method which provide to observe forever functional.
     * @param observer Observer<T> instance.
     */
    fun observeForever(observer: Observer<T>) {
        this.liveDataObject?.observeForever(observer)
    }

    /**
     * Method which provide to remove of the observer.
     * @param observer Observer<T> instance.
     */
    fun removeObserver(observer: Observer<T>) {
        this.liveDataObject?.removeObserver(observer)
    }

    /**
     * Method which provide to remove of the observers.
     * @param owner LifecycleOwner instance.
     */
    fun removeObservers(owner: LifecycleOwner) {
        this.liveDataObject?.removeObservers(owner)
    }

    /**
     * Method which provide the clean functional.
     * @return Unit? result.
     */
    fun clear() = cleaner?.invoke(data, default)

}

/** Method which provide observe once functional. */
private fun <T> LiveData<T>.observeOnce(owner: LifecycleOwner, observer: Observer<T>) {
    observe(owner, object : Observer<T> {
        override fun onChanged(t: T?) {
            observer.onChanged(t)
            removeObserver(this)
        }
    })
}

/*
HOW TO USE.

/** Instance of [String]. */
    private val filterSettingsVersion = LiveField(default = "")
        .configureGet { _, it -> prefs.getString("filterSettingsVersion", it!!) }
        .configureSet { _, it -> prefs.edit().putString("filterSettingsVersion", it).apply() }
        .configureLiveData { _, it -> prefs.string("filterSettingsVersion", it!!) }
        .configureCleaner { _, it -> prefs.edit().putString("filterSettingsVersion", it).apply() }
 */