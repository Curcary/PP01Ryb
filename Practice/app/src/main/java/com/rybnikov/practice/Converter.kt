package com.rybnikov.practice

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

class Converter {
    companion object{
        public fun convert(base64Str: String): Bitmap? {
            val decodedBytes: ByteArray = Base64.decode(
                base64Str.substring(base64Str.indexOf(",") + 1),
                Base64.DEFAULT
            )
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        }
    }
}