package io.github.paletteLens.util

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import io.github.paletteLens.exceptions.JsonException
import java.io.IOException

class MoshiJSONConverterImp : JSONConverter {
    private val moshi = Moshi.Builder().build()

    override fun <T> fromJson(
        json: String,
        clazz: Class<T>,
    ): T {
        val adapter: JsonAdapter<T> = moshi.adapter(clazz)
        try {
            val result = adapter.nonNull().fromJson(json)
            return result!!
        } catch (e: JsonDataException) {
            throw JsonException.ReadError("Error loading JSON for class $clazz", e)
        } catch (e: IOException) {
            throw JsonException.LoadError("Error reading JSON for class $clazz", e)
        }
    }

    override fun <T> toJson(obj: T?): String {
        if (obj == null) {
            throw IllegalArgumentException("Object to serialize cannot be null")
        }
        val adapter: JsonAdapter<T> = moshi.adapter(obj.javaClass as Class<T>)
        try {
            return adapter.toJson(obj)
        } catch (e: JsonDataException) {
            throw JsonException.ConvertError("Error writing JSON for object $obj", e)
        }
    }
}
