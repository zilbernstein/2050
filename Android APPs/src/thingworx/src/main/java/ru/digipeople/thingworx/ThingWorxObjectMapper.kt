package ru.digipeople.thingworx

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive

/**
 * Маппер для обеъктов ThingWorx
 */
object ThingWorxObjectMapper {
    private val gson = GsonBuilder().create()

    @Throws(IllegalArgumentException::class)
    fun map(entity: Any): Map<String, Any> {
        val jsonTree = gson.toJsonTree(entity)
        if (jsonTree.isJsonObject) {
            return jsonObjectToMap(jsonTree.asJsonObject)
        } else {
            throw IllegalArgumentException("Non primitive objects only are allowed")
        }
    }
    /**
     * преобразование json к объекту
     */
    private fun jsonObjectToMap(jsonObject: JsonObject): Map<String, Any> {
        return jsonObject.keySet().associateWith { key ->
            when (val value = jsonObject[key]) {
                is JsonPrimitive -> jsonPrimitiveToValue(value.asJsonPrimitive)
                else -> value.toString()
            }
        }
    }
    /**
     * преобразование json к примитивному типу данных
     */
    private fun jsonPrimitiveToValue(jsonPrimitive: JsonPrimitive): Any {
        return if (jsonPrimitive.isNumber) jsonPrimitive.asNumber else jsonPrimitive.asString
    }
}
