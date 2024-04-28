package me.spartacus04.jext.utils

import me.spartacus04.jext.JextState.GSON
import me.spartacus04.jext.JextState.PLUGIN


open class FileBind(@Transient private val filePath: String, @Transient private val clazz: Class<*>) {
    fun read() {
        if(!PLUGIN.dataFolder.exists()) PLUGIN.dataFolder.mkdirs()

        val file = PLUGIN.dataFolder.resolve(filePath)

        if(!file.exists()) {
            file.createNewFile()

            save()
        }

        val obj = GSON.fromJson(file.readText(), clazz)

        obj.javaClass.declaredFields.forEach { field ->
            field.isAccessible = true

            field.set(this, field.get(obj))
        }
    }

    fun fromText(text: String) : Boolean {
        try {
            val obj = GSON.fromJson(text, clazz)

            obj.javaClass.declaredFields.forEach { field ->
                field.isAccessible = true

                field.set(this, field.get(obj))
            }

            return true
        } catch (_: Exception) {
            return false
        }
    }

    fun save() {
        val text = GSON.toJson(this)

        PLUGIN.dataFolder.resolve(filePath).writeText(text)
    }

    companion object {
        fun <T : FileBind> create(clazz: Class<T>): T {
            val instance = clazz.getDeclaredConstructor().newInstance()

            instance.read()

            return instance
        }
    }
}
