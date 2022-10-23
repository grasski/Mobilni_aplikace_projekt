package com.example.words.request_result.data
import android.graphics.Color as c
import androidx.compose.ui.graphics.Color


enum class EnumCardColors{
    RED{
        override fun color(): Color {
            return Color(c.parseColor("#ad313e"))
        }
    },
    ORANGE{
        override fun color(): Color {
            return Color(c.parseColor("#ac3620"))
        }
    },
    BROWN{
        override fun color(): Color {
            return Color(c.parseColor("#6f5e00"))
        }
    },
    GREEN{
        override fun color(): Color {
            return Color(c.parseColor("#3a6b00"))
        }
    },
    TURQUOISE{
        override fun color(): Color {
            return Color(c.parseColor("#006b62"))
        }
    },
    BLUE{
        override fun color(): Color {
            return Color(c.parseColor("#00668b"))
        }
    },
    PURPLE{
        override fun color(): Color {
            return Color(c.parseColor("#594acd"))
        }
    },
    PINK{
        override fun color(): Color {
            return Color(c.parseColor("#a62f74"))
        }
    };

    abstract fun color(): Color
}