package me.spartacus04.jext.utils

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import me.spartacus04.jext.State.PLUGIN
import me.spartacus04.jext.config.Config
import me.spartacus04.jext.config.ConfigTypeAdapter


open class FileBind(@Transient private val filePath: String, @Transient private val resourcePath: String, @Transient private val typeToken: TypeToken<*>) {
    @Transient
    private val gson = GsonBuilder()
        .setLenient()
        .setPrettyPrinting()
        .registerTypeAdapter(object : TypeToken<Config>() {}.type, ConfigTypeAdapter())
        .create()

    constructor(filePath: String, typeToken: TypeToken<*>) : this(filePath, filePath, typeToken)

    fun read() {
        if(!PLUGIN.dataFolder.exists()) PLUGIN.dataFolder.mkdirs()

        val file = PLUGIN.dataFolder.resolve(filePath)

        if(!file.exists()) {
            file.createNewFile()

            PLUGIN.getResource(resourcePath)!!.bufferedReader().use {
                file.writeText(it.readText())
            }
        }

        gson.fromJson(file.readText(), typeToken)
    }

    fun save() {
        val text = gson.toJson(this)

        println(text)

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
