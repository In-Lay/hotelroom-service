package com.inlay.hotelroomservice.data.remote

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
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
                reader.skipValue() // skip if the value is neither Boolean nor String
                "Invalid type" // return a default string or throw an exception
            }
        }
    }

    @ToJson
    fun toJson(writer: JsonWriter, value: String) {
        writer.value(value)
    }
}