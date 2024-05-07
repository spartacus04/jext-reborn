package me.spartacus04.jext.geyser

import java.io.File
import java.nio.file.Files

internal class GeyserIPC {
    private val ipcFile = File(System.getProperty("java.io.tmpdir") + "/geyser-jext-ipc")

    private val timeout = 5000L

    init {
        if(!ipcFile.exists()) {
            Files.createFile(ipcFile.toPath())
        }

        ipcFile.deleteOnExit()
    }

    fun listen(callback: (String) -> Unit) {
        Thread {
            var last = ipcFile.readText()
            while(true) {
                val text = ipcFile.readText()

                if(text != last) {
                    callback(text)
                    last = text
                }
            }
        }.start()
    }

    fun send(command: GeyserIPCCommand, data: String? = null) {
        ipcFile.writeText("${command.name}${if(data != null) " $data" else ""}")
    }

    fun sendAndReceive(command: GeyserIPCCommand, data: String? = null): String? {
        send(command, data)

        val current = ipcFile.readText()

        val start = System.currentTimeMillis()

        while(System.currentTimeMillis() - start < timeout) {
            val text = ipcFile.readText()
            if(text != current) {
                return text
            }
        }

        return null
    }

    internal enum class GeyserIPCCommand {
        RESOURCE_PACK,
        IS_BEDROCK,
        OK,
    }
}