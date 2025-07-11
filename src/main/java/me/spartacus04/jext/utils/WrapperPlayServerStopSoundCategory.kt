package me.spartacus04.jext.utils

import com.github.retrooper.packetevents.protocol.packettype.PacketType
import com.github.retrooper.packetevents.protocol.sound.SoundCategory
import com.github.retrooper.packetevents.wrapper.PacketWrapper
import kotlin.experimental.and
import kotlin.experimental.or

internal class WrapperPlayServerStopSoundCategory(private var category: SoundCategory?) : PacketWrapper<WrapperPlayServerStopSoundCategory>(PacketType.Play.Server.STOP_SOUND) {
    private var flags: Byte = 0

    private companion object {
        const val FLAG_CATEGORY = 0x01.toByte()
    }

    override fun read() {
        this.flags = readByte()


        if (this.flags and FLAG_CATEGORY != 0.toByte()) {
            category = SoundCategory.fromId(readVarInt())
        }
    }

    override fun write() {
        this.flags = 0

        if(this.category != null) {
            this.flags = this.flags or FLAG_CATEGORY
        }

        writeByte(this.flags.toInt())

        if(this.category != null) {
            writeByte(this.category!!.ordinal)
        }
    }

    override fun copy(wrapper: WrapperPlayServerStopSoundCategory) {
        this.flags = wrapper.flags
        this.category = wrapper.category
    }
}