package com.inlay.hotelroomservice.data.remote

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson

class CustomMoshiAdapter {
    @FromJson
    fun fromJson(reader: JsonReader): String {
        return when (reader.peek()) {
            JsonReader.Token.BOOLEAN -> reader.nextBoolean().toString()
            JsonReader.Token.STRING -> reader.nextString()
            else -> {
                reader.skipValue()
                " "
            }
        }
    }

    @ToJson
    fun toJson(writer: JsonWriter, value: String) {
        writer.value(value)
    }
}