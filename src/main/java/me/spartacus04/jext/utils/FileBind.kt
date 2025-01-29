package me.spartacus04.jext.utils

import me.spartacus04.jext.JextState.GSON
import me.spartacus04.jext.JextState.PLUGIN

/**
 * The class `FileBind` is used to bind a file to a class.
 * 
 * @param filePath The path of the file.
 * @param clazz The class to bind the file to.
 */
open class FileBind(@Transient private val filePath: String, @Transient private val clazz: Class<*>) {
    /**
     * Reads the file and binds it to the class.
     */
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

    /**
     * Reads the file from the specified text and binds it to the class.
     * 
     * @param text The text to read from.
     * 
     * @return Returns true if the text was successfully read, otherwise false.
     */
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

    /**
     * Saves the class to the file.
     */
    fun save() {
        val text = GSON.toJson(this)

        PLUGIN.dataFolder.resolve(filePath).writeText(text)
    }

    companion object {
        /**
         * Creates a new instance of the specified class and binds it to the file.
         * 
         * @param clazz The class to bind the file to.
         * 
         * @return The instance of the class.
         */
        fun <T : FileBind> create(clazz: Class<T>): T {
            val instance = clazz.getDeclaredConstructor().newInstance()

            instance.read()

            return instance
        }
    }
}
