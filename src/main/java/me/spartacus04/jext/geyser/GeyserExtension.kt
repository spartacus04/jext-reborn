package me.spartacus04.jext.geyser

import org.geysermc.api.Geyser
import org.geysermc.event.subscribe.Subscribe
import org.geysermc.geyser.api.event.lifecycle.GeyserPostInitializeEvent
import org.geysermc.geyser.api.extension.Extension
import java.util.*
import java.util.zip.ZipFile
import kotlin.io.path.writeBytes

@Suppress("unused")
internal class GeyserExtension : Extension {
    private val ipc = GeyserIPC()

    @Subscribe
    fun onPostInitialize(event: GeyserPostInitializeEvent) {
        println("JEXT Extension loaded!")

        ipc.listen {
            if(it.startsWith("IS_BEDROCK")) {
                ipc.send(
                    GeyserIPC.GeyserIPCCommand.OK,
                    Geyser.api().isBedrockPlayer(UUID.fromString(it.split(" ")[1])).toString()
                )
            } else if(it.startsWith("RESOURCE_PACK")) {
                val rpData = Base64.getDecoder().decode(it.split(" ")[1])

                val file = dataFolder().resolve("../../packs/jext_resources.mcpack")
                file.writeBytes(rpData)

                val zip = ZipFile(file.toFile())
                val entry = zip.getEntry("mappings.json")

                val mappingsFile = dataFolder().resolve("../../custom_mappings/mappings.json")
                mappingsFile.writeBytes(zip.getInputStream(entry).readBytes())
            } else if(it.startsWith("OK")) {
                println("Geyser found!")

                ipc.send(GeyserIPC.GeyserIPCCommand.OK)
            }
        }
    }
}