package me.spartacus04.jext.geyser.plugin

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import me.spartacus04.jext.geyser.ipc.GeyserIpc
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.IOException
import java.net.Socket
import java.util.UUID
import kotlin.io.encoding.Base64


internal class GeyserStandalone(val ipPort: String) : GeyserMode, GeyserIpc() {
    private val socket: Socket
    private val reader: BufferedReader
    private val writer: BufferedWriter

    init {
        val parts = ipPort.split(":")

        socket = Socket(parts[0], parts[1].toInt()).apply {
            soTimeout = timeout
        }

        reader = socket.getInputStream().bufferedReader()
        writer = socket.getOutputStream().bufferedWriter()

        val response = runBlockingSend(GeyserIpcCommand.HEALTH_CHECK, null)

        if(response != "OK") {
            throw IOException("Health check failed: $response")
        }
    }

    suspend fun sendAndReceive(command: GeyserIpcCommand, data: String? = null): String? {
        val request = GeyserIpcMessage(
            command,
            UUID.randomUUID(),
            data
        )

        return withContext(Dispatchers.IO) {
            withTimeout(timeout.toLong()) {
                synchronized(this@GeyserStandalone) {
                    writer.write(request.toString())

                    val responseLine = reader.readLine() ?: throw IOException("Connection closed")
                    val response = GeyserIpcMessage.fromString(responseLine)

                    if (response.id != request.id) {
                        throw IOException("Mismatched response ID")
                    }

                    response.payload
                }
            }
        }
    }

    fun runBlockingSend(command: GeyserIpcCommand, data: String?): String? {
        val request = GeyserIpcMessage(command, UUID.randomUUID(), data)
        writer.write(request.toString())

        val responseLine = reader.readLine() ?: throw IOException("Connection closed")
        val response = GeyserIpcMessage.fromString(responseLine)

        if (response.id != request.id) {
            throw IOException("Mismatched response ID")
        }

        return response.payload
    }

    override fun isBedrockPlayer(player: UUID): Boolean {
        return try {
            val result = runBlocking {
                sendAndReceive(
                    GeyserIpcCommand.PLAYER_IS_BEDROCK, player.toString()
                )
            }

            result?.toBoolean() ?: false
        } catch (_: Exception) {
            false
        }
    }

    override fun applyResourcePack(buffer: ByteArray) {
        runBlocking {
            sendAndReceive(GeyserIpcCommand.APPLY_RESOURCE_PACK, Base64.encode(buffer))
        }
    }

    override fun close() {
        val request = GeyserIpcMessage(
            GeyserIpcCommand.CLOSE_SOCKET,
            UUID.randomUUID(),
            null
        )

        synchronized(this) {
            try {
                writer.write(request.toString())
            } catch (_: IOException) { }

            try {
                socket.close()
            } catch (_: IOException) { }
        }
    }
}