package me.spartacus04.jext.webapi.auth

import com.sun.net.httpserver.HttpExchange
import me.spartacus04.jext.Jext
import me.spartacus04.jext.webapi.utils.JextHttpHandler

internal class ConnectHandler(plugin: Jext) : JextHttpHandler(plugin, false) {
    override fun onPost(exchange: HttpExchange) {
        val addr = exchange.remoteAddress.address.address.map { it.toInt() }.joinToString(".")
        val body = exchange.requestBody.bufferedReader().readText()

        if(body == plugin.config.WEB_INTERFACE_PASSWORD) {
            val token = (0..64).map { (('a'..'z') + ('A'..'Z') + ('0'..'9')).random() }.joinToString("")

            connectedHashMap[addr] = token

            exchange.sendResponseHeaders(200, token.length.toLong())

            exchange.responseBody.use { os ->
                os.write(token.toByteArray())
            }
        } else {
            val response = "401 Unauthorized"
            exchange.sendResponseHeaders(401, response.length.toLong())

            exchange.responseBody.use { os ->
                os.write(response.toByteArray())
            }
        }
    }

    companion object {
        internal val connectedHashMap = HashMap<String, String>()
    }
}