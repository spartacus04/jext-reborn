package me.spartacus04.jext.geyser.ipc

import java.util.UUID

internal open class GeyserIpc {
    protected val timeout = 5000

    internal data class GeyserIpcMessage(
        val command: GeyserIpcCommand,
        val id: UUID,
        var payload: String?
    ) {
        override fun toString(): String {
            return "$command $id${if(payload != null) " $payload" else ""}"
        }

        fun respond(newPayload: String): String {
            return this.apply {
                payload = newPayload
            }.toString()
        }

        companion object {
            fun fromString(message: String): GeyserIpcMessage {
                val parts = message.split(" ")

                return GeyserIpcMessage(
                    GeyserIpcCommand.valueOf(parts[0]),
                    UUID.fromString(parts[1]),
                    parts[2]
                )
            }
        }
    }

    internal enum class GeyserIpcCommand {
        APPLY_RESOURCE_PACK,
        PLAYER_IS_BEDROCK,
        HEALTH_CHECK,
        CLOSE_SOCKET
    }
}