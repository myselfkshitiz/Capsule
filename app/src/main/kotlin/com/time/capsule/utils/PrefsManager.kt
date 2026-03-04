package com.kshitiz.capsule.utils

import android.content.Context
import android.content.SharedPreferences

class PrefsManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("bubble_prefs", Context.MODE_PRIVATE)

    var xOffset: Int
        get() = prefs.getInt("x_offset", 25)
        set(value) = prefs.edit().putInt("x_offset", value).apply()

    var yOffset: Int
        get() = prefs.getInt("y_offset", 30)
        set(value) = prefs.edit().putInt("y_offset", value).apply()

    var pillWidth: Int
        get() = prefs.getInt("pill_width", 90)
        set(value) = prefs.edit().putInt("pill_width", value).apply()

    var pillHeight: Int
        get() = prefs.getInt("pill_height", 35)
        set(value) = prefs.edit().putInt("pill_height", value).apply()

    var textSize: Int
        get() = prefs.getInt("text_size", 14)
        set(value) = prefs.edit().putInt("text_size", value).apply()

    var keepRatio: Boolean
        get() = prefs.getBoolean("keep_ratio", true)
        set(value) = prefs.edit().putBoolean("keep_ratio", value).apply()

    var is24HourFormat: Boolean
        get() = prefs.getBoolean("is_24_hour", true)
        set(value) = prefs.edit().putBoolean("is_24_hour", value).apply()

    var isAmPmEnabled: Boolean
        get() = prefs.getBoolean("is_am_pm", false)
        set(value) = prefs.edit().putBoolean("is_am_pm", value).apply()

    var showSeconds: Boolean
        get() = prefs.getBoolean("show_seconds", false)
        set(value) = prefs.edit().putBoolean("show_seconds", value).apply()

    var bgMode: String
        get() = prefs.getString("bg_mode", "SOLID") ?: "SOLID"
        set(value) = prefs.edit().putString("bg_mode", value).apply()

    var colorType: String
        get() = prefs.getString("color_type", "TOKEN") ?: "TOKEN"
        set(value) = prefs.edit().putString("color_type", value).apply()

    var tokenSolid: String
        get() = prefs.getString("token_solid", "Primary") ?: "Primary"
        set(value) = prefs.edit().putString("token_solid", value).apply()

    var tokenGradStart: String
        get() = prefs.getString("token_grad_start", "Primary") ?: "Primary"
        set(value) = prefs.edit().putString("token_grad_start", value).apply()

    var tokenGradEnd: String
        get() = prefs.getString("token_grad_end", "Tertiary") ?: "Tertiary"
        set(value) = prefs.edit().putString("token_grad_end", value).apply()

    var hexSolidLight: String
        get() = prefs.getString("hex_solid_light", "#FF000000") ?: "#FF000000"
        set(value) = prefs.edit().putString("hex_solid_light", value).apply()

    var hexSolidDark: String
        get() = prefs.getString("hex_solid_dark", "#FFFFFFFF") ?: "#FFFFFFFF"
        set(value) = prefs.edit().putString("hex_solid_dark", value).apply()

    var hexGradStartLight: String
        get() = prefs.getString("hex_grad_start_light", "#FF000000") ?: "#FF000000"
        set(value) = prefs.edit().putString("hex_grad_start_light", value).apply()

    var hexGradStartDark: String
        get() = prefs.getString("hex_grad_start_dark", "#FFFFFFFF") ?: "#FFFFFFFF"
        set(value) = prefs.edit().putString("hex_grad_start_dark", value).apply()

    var hexGradEndLight: String
        get() = prefs.getString("hex_grad_end_light", "#FF555555") ?: "#FF555555"
        set(value) = prefs.edit().putString("hex_grad_end_light", value).apply()

    var hexGradEndDark: String
        get() = prefs.getString("hex_grad_end_dark", "#FFAAAAAA") ?: "#FFAAAAAA"
        set(value) = prefs.edit().putString("hex_grad_end_dark", value).apply()

    var textColorType: String
        get() = prefs.getString("text_color_type", "TOKEN") ?: "TOKEN"
        set(value) = prefs.edit().putString("text_color_type", value).apply()

    var textToken: String
        get() = prefs.getString("text_token", "OnPrimary") ?: "OnPrimary"
        set(value) = prefs.edit().putString("text_token", value).apply()

    var textHexLight: String
        get() = prefs.getString("text_hex_light", "#FFFFFFFF") ?: "#FFFFFFFF"
        set(value) = prefs.edit().putString("text_hex_light", value).apply()

    var textHexDark: String
        get() = prefs.getString("text_hex_dark", "#FF000000") ?: "#FF000000"
        set(value) = prefs.edit().putString("text_hex_dark", value).apply()

    var textWeight: String
        get() = prefs.getString("text_weight", "BOLD") ?: "BOLD"
        set(value) = prefs.edit().putString("text_weight", value).apply()

    var textShadow: Boolean
        get() = prefs.getBoolean("text_shadow", false)
        set(value) = prefs.edit().putBoolean("text_shadow", value).apply()

    var cornerTL: Int
        get() = prefs.getInt("corner_tl", 50)
        set(value) = prefs.edit().putInt("corner_tl", value).apply()

    var cornerTR: Int
        get() = prefs.getInt("corner_tr", 50)
        set(value) = prefs.edit().putInt("corner_tr", value).apply()

    var cornerBL: Int
        get() = prefs.getInt("corner_bl", 50)
        set(value) = prefs.edit().putInt("corner_bl", value).apply()

    var cornerBR: Int
        get() = prefs.getInt("corner_br", 50)
        set(value) = prefs.edit().putInt("corner_br", value).apply()

    var fillMode: String
        get() = prefs.getString("bg_fill_mode", "FILL") ?: "FILL"
        set(value) = prefs.edit().putString("bg_fill_mode", value).apply()

    var strokeWidth: Int
        get() = prefs.getInt("bg_stroke_width", 2)
        set(value) = prefs.edit().putInt("bg_stroke_width", value).apply()

    fun registerListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        prefs.registerOnSharedPreferenceChangeListener(listener)
    }

    fun unregisterListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        prefs.unregisterOnSharedPreferenceChangeListener(listener)
    }
}
