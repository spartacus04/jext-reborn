package me.spartacus04.jext.geyser.extension

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.spartacus04.jext.geyser.ipc.GeyserIpc
import org.geysermc.event.subscribe.Subscribe
import org.geysermc.geyser.api.event.lifecycle.GeyserPreInitializeEvent
import org.geysermc.geyser.api.event.lifecycle.GeyserShutdownEvent
import org.geysermc.geyser.api.extension.Extension
import java.net.ServerSocket
import java.net.Socket
import java.util.Base64
import java.util.UUID
import java.util.zip.ZipFile
import kotlin.io.path.writeBytes

@Suppress("unused")
internal class GeyserExtension : Extension, GeyserIpc() {
    private var serverJob: Job? = null
    private lateinit var serverSocket: ServerSocket
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun startSocket() {
        serverSocket = ServerSocket(geyserApi().bedrockListener().port())

        serverJob = scope.launch {
            while(!serverSocket.isClosed) {
                val client = serverSocket.accept()

                val remoteIp = client.inetAddress.hostAddress
                if (remoteIp != geyserApi().defaultRemoteServer().address()) {
                    client.close()
                }

                logger().info("Client ${client.inetAddress.hostAddress} connected")

                launch { handleClient(client) }
            }
        }
    }

    private suspend fun handleClient(socket: Socket) = withContext(Dispatchers.IO) {
        socket.use { s ->
            val reader = s.inputStream.bufferedReader()
            val writer = s.outputStream.bufferedWriter()

            var line: String
            while (reader.readLine().also { line = it } != null) {
                val message = try {
                    GeyserIpcMessage.fromString(line)
                } catch (_: Exception) {
                    logger().error("Invalid message from ${s.inetAddress.hostAddress}: $line")
                    continue
                }

                when (message.command) {
                    GeyserIpcCommand.PLAYER_IS_BEDROCK -> {
                        if(message.payload == null) {
                            writer.write(
                                message.respond(false.toString())
                            )
                        } else {
                            try {
                                val uuid = UUID.fromString(message.payload)
                                writer.write(
                                    message.respond(geyserApi().isBedrockPlayer(uuid).toString())
                                )
                            } catch (e: IllegalArgumentException) {
                                writer.write(
                                    message.respond(false.toString())
                                )
                            }
                        }
                    }

                    GeyserIpcCommand.HEALTH_CHECK -> {
                        writer.write(
                            message.respond("OK")
                        )
                    }

                    GeyserIpcCommand.APPLY_RESOURCE_PACK -> {
                        if(message.payload == null) {
                            logger().error("No payload for resource pack from ${s.inetAddress.hostAddress}")
                            writer.write(
                                message.respond("ERROR: No payload")
                            )
                        } else {
                            val rpData = Base64.getDecoder().decode(message.payload)

                            val file = geyserApi().packDirectory().resolve("jext_resources.mcpack")
                            val mappingsFile = dataFolder().resolve("../../custom_mappings/mappings.json")

                            file.writeBytes(rpData)

                            val zip = ZipFile(file.toFile())
                            val entry = zip.getEntry("mappings.json")
                            mappingsFile.writeBytes(zip.getInputStream(entry).readBytes())

                            writer.write(
                                message.respond("OK")
                            )
                        }
                    }

                    GeyserIpcCommand.CLOSE_SOCKET -> {
                        logger().info("Client ${s.inetAddress.hostAddress} disconnected")
                        return@use
                    }
                }

                writer.flush()
            }


            logger().info("Client ${s.inetAddress.hostAddress} disconnected")
        }
    }

    @Subscribe
    fun onPreInitialize(event: GeyserPreInitializeEvent) {
        logger().info("JEXT Extension initializing...")
        startSocket()
        logger().info("JEXT IPC Server started on port ${serverSocket.localPort}: ${serverSocket.localSocketAddress}")
    }

    @Subscribe
    fun onClose(event: GeyserShutdownEvent) {
        serverJob?.cancel()
        serverSocket.close()
    }
}