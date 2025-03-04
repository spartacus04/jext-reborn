package me.spartacus04.jext.geyser

import java.util.UUID
import java.util.Base64

internal class GeyserStandalone : GeyserMode {
    private val ipcCommunicator = GeyserIPC()

    init {
        ipcCommunicator.sendAndReceive(GeyserIPC.GeyserIPCCommand.OK) ?: throw IllegalStateException("Geyser not found")
    }

    override fun isBedrockPlayer(player: UUID): Boolean {
        ipcCommunicator.sendAndReceive(GeyserIPC.GeyserIPCCommand.IS_BEDROCK, player.toString())?.let {
            return it.toBoolean()
        } ?: return false
    }

    override fun applyResourcePack(buffer: ByteArray) {
        ipcCommunicator.send(GeyserIPC.GeyserIPCCommand.RESOURCE_PACK, Base64.getEncoder().encodeToString(buffer))
    }
}