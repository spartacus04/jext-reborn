package me.spartacus04.jext.config

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
internal annotation class ConfigField(
    val name: String,
    val description: String,
    val defaultValue: String,
    val enumValues: String = ""
)