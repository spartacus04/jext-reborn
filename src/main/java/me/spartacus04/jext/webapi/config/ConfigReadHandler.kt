package me.spartacus04.jext.webapi.config

import com.google.gson.annotations.SerializedName
import com.sun.net.httpserver.HttpExchange
import me.spartacus04.jext.JextState.CONFIG
import me.spartacus04.jext.JextState.GSON
import me.spartacus04.jext.config.ConfigField
import me.spartacus04.jext.webapi.utils.JextHttpHandler

internal class ConfigReadHandler : JextHttpHandler(true) {

    override fun onGet(exchange: HttpExchange) {
        val data = CONFIG::class.java.declaredFields.map {
            val id = it.getAnnotation(SerializedName::class.java).value

            val data = it.getAnnotation(ConfigField::class.java)

            it.isAccessible = true
            val value = GSON.toJson(it.get(CONFIG))
            it.isAccessible = false

            """
                {
                    "name": "${data.name}",
                    "id": "$id",
                    "description": "${data.description}",
                    "value": $value,
                    "defaultValue": ${data.defaultValue}
                    ${if(data.enumValues.isNotBlank()) ",\"enumValues\": ${data.enumValues}" else ""}
                }
            """.trimIndent()
        }

        val response = "[${data.joinToString(",")}]"

        exchange.sendResponseHeaders(200, response.length.toLong())
        exchange.responseBody.use { output ->
            output.write(response.toByteArray())
        }
    }
}