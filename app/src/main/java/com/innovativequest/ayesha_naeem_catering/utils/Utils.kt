package com.innovativequest.ayesha_naeem_catering.utils

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.innovativequest.ayesha_naeem_catering.data.MainDishesItem
import com.innovativequest.ayesha_naeem_catering.data.StartersItem
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

object Utils {

    private fun getJsonFromAssetPath(path: String, context: Context): String? {
        try {
            val jsonStream = context.assets.open(path)
            val r = BufferedReader(InputStreamReader(jsonStream))
            val total = StringBuilder()
            var line: String?

            do {
                line = r.readLine()
                if (line != null) total.append(line)
            } while (line != null)
            return total.toString()
        } catch (ex: IOException) {

            ex.message?.let {
                Log.e("HomeAdapter", it)
            }
            return null
        }

    }

    fun mainDishesFromLocalJson(path: String, context: Context?): List<MainDishesItem> {
        context?.let {
            val jsonObject =  JSONObject(getJsonFromAssetPath(path, it) ?: "")

            jsonObject.toJSONArray(jsonObject.names())?.let { jsonArray ->
                val builder: StringBuilder = StringBuilder(jsonArray.toString())
                builder.deleteAt(0)
                builder.deleteAt(builder.length - 1)
                return Gson().fromJson(
                    builder.toString(), object : TypeToken<ArrayList<MainDishesItem>>() {}.type)
            }
        }
        return emptyList()
    }

    fun starterDishesFromLocalJson(path: String, context: Context?): List<StartersItem> {

        context?.let {
            val jsonObject =  JSONObject(getJsonFromAssetPath(path, it) ?: "")

            jsonObject.toJSONArray(jsonObject.names())?.let { jsonArray ->
                val builder: StringBuilder = StringBuilder(jsonArray.toString())
                builder.deleteAt(0)
                builder.deleteAt(builder.length - 1)
                return Gson().fromJson(
                    builder.toString(), object : TypeToken<ArrayList<StartersItem>>() {}.type)
            }
        }
        return emptyList()
    }
}
