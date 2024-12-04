package com.tradesmanhandy.app.data.api.adapter

import com.squareup.moshi.*
import com.tradesmanhandy.app.data.model.BookingStatus
import com.tradesmanhandy.app.data.model.UserRole
import java.lang.reflect.Type

class EnumJsonAdapter<T : Enum<T>>(
    private val enumType: Class<T>,
    private val fallbackValue: T? = null,
    private val useFallbackValue: Boolean = true
) : JsonAdapter<T>() {
    private val nameStrings: Map<String, T> = enumType.enumConstants.associateBy {
        it.name.lowercase()
    }
    private val serializeNames: Map<T, String> = enumType.enumConstants.associateBy(
        { it },
        { it.name.lowercase() }
    )

    override fun fromJson(reader: JsonReader): T? {
        val string = reader.nextString().lowercase()
        return nameStrings[string] ?: if (useFallbackValue) fallbackValue else throw JsonDataException(
            "Unknown value of enum ${enumType.simpleName}: $string"
        )
    }

    override fun toJson(writer: JsonWriter, value: T?) {
        if (value != null) {
            writer.value(serializeNames[value])
        }
    }

    companion object {
        val FACTORY = JsonAdapter.Factory { type, _, _ ->
            when (type) {
                UserRole::class.java -> EnumJsonAdapter(UserRole::class.java, UserRole.CLIENT)
                BookingStatus::class.java -> EnumJsonAdapter(BookingStatus::class.java, BookingStatus.PENDING)
                else -> null
            }
        }
    }
}
