package com.inlay.hotelroomservice.data.remote

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson

class CustomMoshiAdapter : JsonAdapter<String>() {
    @FromJson
    override fun fromJson(p0: JsonReader): String? {
        return when (p0.peek()) {
            JsonReader.Token.STRING -> p0.nextString()
            JsonReader.Token.BOOLEAN -> p0.nextBoolean().toString()
            else -> null
        }
    }

    @ToJson
    override fun toJson(p0: JsonWriter, p1: String?) {
        p0.value(p1)
    }
}