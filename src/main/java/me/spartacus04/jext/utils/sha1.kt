package me.spartacus04.jext.utils

import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest

internal fun getFileSha1Hash(file: File): ByteArray {
    return try {
        val md = MessageDigest.getInstance("SHA-1")
        val fis = FileInputStream(file)

        val buffer = ByteArray(4096) // Read file in chunks of 4KB
        var bytesRead: Int

        while (fis.read(buffer).also { bytesRead = it } > 0) {
            md.update(buffer, 0, bytesRead)
        }

        fis.close()
        md.digest()
    } catch (e: Exception) {
        throw RuntimeException("Error calculating SHA-1 hash", e)
    }
}