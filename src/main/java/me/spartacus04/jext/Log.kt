package me.spartacus04.jext

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

@Suppress("unused")
class Log @JvmOverloads constructor(title: String = "Jext") {
    private val title: String
    private var themeColor: ChatColor
    private val tokens: MutableList<Token>

    init {
        tokens = ArrayList()
        themeColor = ChatColor.WHITE
        this.title = title
    }

    /**
     * Set message theme to normal and add a header (White)
     * Return the instance with the header set
     * @return Log
     */
    fun norm(): Log {
        themeColor = ChatColor.RESET
        return head()
    }

    /**
     * Set message theme to success and add a header (Green)
     * Return the instance with the header set
     * @return Log
     */
    fun okay(): Log {
        themeColor = ChatColor.GREEN
        return head()
    }

    /**
     * Set message theme to warning and add a header (Yellow)
     * Return the instance with the header set
     * @return Log
     */
    fun warn(): Log {
        themeColor = ChatColor.YELLOW
        return head()
    }

    /**
     * Set message theme to error and add a header (Red)
     * Return the instance with the header set
     * @return Log
     */
    fun eror(): Log {
        themeColor = ChatColor.RED
        return head()
    }

    /**
     * Set message theme to infomative and add a header (Blue)
     * Return the instance with the header set
     * @return Log
     */
    fun info(): Log {
        themeColor = ChatColor.BLUE
        return head()
    }

    /**
     * Add header pattern following the theme colour
     * Return the instance itself for chaining
     * @return Log
     */
    private fun head(): Log {
        return rst("[").t(title).rst("] ")
    }

    /**
     * Add theme-coloured text
     * Return the instance itself for chaining
     * @return Log
     */
    fun t(themeColoredMessage: String): Log {
        tokens.add(Token(themeColoredMessage, themeColor))
        return this
    }

    /**
     * Add theme-coloured placeholder
     * Return the instance itself for chaining
     * @return Log
     */
    fun t(): Log {
        tokens.add(Token(themeColor))
        return this
    }

    /**
     * Add green-coloured text
     * Return the instance itself for chaining
     * @return Log
     */
    fun g(greenMessage: String): Log {
        tokens.add(Token(greenMessage, ChatColor.GREEN))
        return this
    }

    /**
     * Add green-coloured placeholder
     * Return the instance itself for chaining
     * @return Log
     */
    fun g(): Log {
        tokens.add(Token(ChatColor.GREEN))
        return this
    }

    /**
     * Add yellow-coloured text
     * Return the instance itself for chaining
     * @return Log
     */
    fun y(yellowMessage: String): Log {
        tokens.add(Token(yellowMessage, ChatColor.YELLOW))
        return this
    }

    /**
     * Add yellow-coloured placeholder
     * Return the instance itself for chaining
     * @return Log
     */
    fun y(): Log {
        tokens.add(Token(ChatColor.YELLOW))
        return this
    }

    /**
     * Add orange-coloured (gold) text
     * Return the instance itself for chaining
     * @return Log
     */
    fun o(goldenMessage: String): Log {
        tokens.add(Token(goldenMessage, ChatColor.GOLD))
        return this
    }

    /**
     * Add orange-coloured (gold) placeholder
     * Return the instance itself for chaining
     * @return Log
     */
    fun o(): Log {
        tokens.add(Token(ChatColor.GOLD))
        return this
    }

    /**
     * Add red-coloured text
     * Return the instance itself for chaining
     * @return Log
     */
    fun r(redMessage: String): Log {
        tokens.add(Token(redMessage, ChatColor.RED))
        return this
    }

    /**
     * Add red-coloured placeholder
     * Return the instance itself for chaining
     * @return Log
     */
    fun r(): Log {
        tokens.add(Token(ChatColor.RED))
        return this
    }

    /**
     * Add blue-coloured text
     * Return the instance itself for chaining
     * @return Log
     */
    fun b(blueMessage: String): Log {
        tokens.add(Token(blueMessage, ChatColor.BLUE))
        return this
    }

    /**
     * Add blue-coloured placeholder
     * Return the instance itself for chaining
     * @return Log
     */
    fun b(): Log {
        tokens.add(Token(ChatColor.BLUE))
        return this
    }

    /**
     * Add teal-coloured text
     * Return the instance itself for chaining
     * @return Log
     */
    fun a(aquaMessage: String): Log {
        tokens.add(Token(aquaMessage, ChatColor.AQUA))
        return this
    }

    /**
     * Add teal-coloured placeholder
     * Return the instance itself for chaining
     * @return Log
     */
    fun a(): Log {
        tokens.add(Token(ChatColor.AQUA))
        return this
    }

    /**
     * Add dark teal-coloured text
     * Return the instance itself for chaining
     * @return Log
     */
    fun da(darkAquaMessage: String): Log {
        tokens.add(Token(darkAquaMessage, ChatColor.DARK_AQUA))
        return this
    }

    /**
     * Add dark teal-coloured placeholder
     * Return the instance itself for chaining
     * @return Log
     */
    fun da(): Log {
        tokens.add(Token(ChatColor.DARK_AQUA))
        return this
    }

    /**
     * Add purple-coloured text
     * Return the instance itself for chaining
     * @return Log
     */
    fun p(purpleMessage: String): Log {
        tokens.add(Token(purpleMessage, ChatColor.LIGHT_PURPLE))
        return this
    }

    /**
     * Add purple-coloured placeholder
     * Return the instance itself for chaining
     * @return Log
     */
    fun p(): Log {
        tokens.add(Token(ChatColor.LIGHT_PURPLE))
        return this
    }

    /**
     * Add dark purple-coloured text
     * Return the instance itself for chaining
     * @return Log
     */
    fun dp(darkPurpleMessage: String): Log {
        tokens.add(Token(darkPurpleMessage, ChatColor.DARK_PURPLE))
        return this
    }

    /**
     * Add dark purple-coloured placeholder
     * Return the instance itself for chaining
     * @return Log
     */
    fun dp(): Log {
        tokens.add(Token(ChatColor.DARK_PURPLE))
        return this
    }

    /**
     * Add gray-coloured text
     * Return the instance itself for chaining
     * @return Log
     */
    fun gr(grayMessage: String): Log {
        tokens.add(Token(grayMessage, ChatColor.GRAY))
        return this
    }

    /**
     * Add gray-coloured placeholder
     * Return the instance itself for chaining
     * @return Log
     */
    fun gr(): Log {
        tokens.add(Token(ChatColor.GRAY))
        return this
    }

    /**
     * Add magic-coloured text
     * Return the instance itself for chaining
     * @return Log
     */
    fun mag(magicMessage: String): Log {
        tokens.add(Token(magicMessage, ChatColor.MAGIC))
        return this
    }

    /**
     * Add magic-coloured placeholder
     * Return the instance itself for chaining
     * @return Log
     */
    fun mag(): Log {
        tokens.add(Token(ChatColor.MAGIC))
        return this
    }

    /**
     * Reset colour and add text
     * Return the instance itself for chaining
     * @return Log
     */
    fun rst(plainColoredMessage: String): Log {
        tokens.add(Token(plainColoredMessage, ChatColor.RESET))
        return this
    }

    /**
     * Reset colour and add placeholder
     * Return the instance itself for chaining
     * @return Log
     */
    fun rst(): Log {
        tokens.add(Token(ChatColor.RESET))
        return this
    }

    /**
     * Send the message to the console with parameter
     * set to replace the placeholders.
     * @return void
     */
    fun send(vararg objects: Any?) {
        val objectQueue: Queue<Any> = LinkedList(listOf(*objects))
        Bukkit.getConsoleSender().sendMessage(constructMessage(objectQueue))
    }

    /**
     * Send the message to specified target with parameter
     * set to replace the placeholders.
     * @return void
     */
    fun send(target: CommandSender?, vararg objects: Any?) {
        val objectQueue: Queue<Any> = LinkedList(listOf(*objects))
        val message = constructMessage(objectQueue)
        if (target is Player) {
            target.sendMessage(message)
        } else {
            Bukkit.getConsoleSender().sendMessage(message)
        }
    }

    /**
     * Generate string with parameter set to replace
     * the placeholders.
     * @return String
     */
    fun text(vararg objects: Any?): String {
        val objectQueue: Queue<Any> = LinkedList(listOf(*objects))
        return constructMessage(objectQueue)
    }

    private fun constructMessage(parameters: Queue<Any>): String {
        var message = ""
        for (token in tokens) {
            message += token.toString(parameters)
        }
        return message
    }

    private inner class Token private constructor(
        private val message: String,
        private val color: ChatColor,
        private val parameter: Boolean
    ) {
        constructor(message: String, color: ChatColor) : this(message, color, false)
        constructor(color: ChatColor) : this("", color, true)

        fun toString(objects: Queue<Any>): String {
            return if (parameter) {
                val `object` = objects.poll()
                var replacement = ""
                if (`object` != null) replacement = `object`.toString()
                color.toString() + replacement
            } else {
                color.toString() + message
            }
        }
    }
}