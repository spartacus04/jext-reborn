package me.spartacus04.jext.utils

fun <T> listOfCatching(
    vararg elements: () -> T,
    onError: (Throwable) -> Unit = {},
) : List<T> {
    val list = mutableListOf<T>()

    for (element in elements) {
        runCatching { list.add(element()) }
            .onFailure { onError(it) }
    }

    return list
}