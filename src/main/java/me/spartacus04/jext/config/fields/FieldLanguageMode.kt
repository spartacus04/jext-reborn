package me.spartacus04.jext.config.fields

import com.google.gson.annotations.SerializedName

enum class FieldLanguageMode {
    @SerializedName("auto")
    AUTO,

    @SerializedName("silent")
    SILENT,

    @SerializedName("custom")
    CUSTOM,

    // locales

    af_za, ar_sa, ca_es, cs_cz, da_dk, de_de,
    el_gr, en_us, es_es, fi_fi, fr_fr, he_il,
    hu_hu, it_it, ja_jp, ko_kr, nl_nl, no_no,
    pl_pl, pt_br, pt_pt, ro_ro, ru_ru, sr_sp,
    sv_se, tr_tr, uk_ua, vi_vn, zh_cn, zh_tw;

    companion object {
        fun fromString(name: String): FieldLanguageMode {
            // return enum value, name can either be the actual name or the serialized name
            return entries.find { it.name == name || it.name == name.replace("-", "_").uppercase() } ?: throw IllegalArgumentException("Invalid serialized name")
        }
    }
}