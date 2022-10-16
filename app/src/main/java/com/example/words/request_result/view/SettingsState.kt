package com.example.words.request_result.view

import android.content.Context
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.example.words.R
import com.example.words.request_result.data.Results


data class SettingsState(
    var show: SnapshotStateMap<String, Boolean> = mutableStateMapOf(
        SettingsEnum.CATEGORY.name to false,
        SettingsEnum.DEFINITION.name to false,
        SettingsEnum.SYNONYMS.name to false,
        SettingsEnum.ANTONYMS.name to false,
        SettingsEnum.EXAMPLES.name to false
    )
)


enum class SettingsEnum{
    CATEGORY {
        override fun getInSettingsText(context: Context): String {
            return context.resources.getStringArray(R.array.categorySettings)[0]
        }

        override fun getResultTitle(context: Context): String {
            return context.resources.getStringArray(R.array.categorySettings)[1]
        }

        override fun getResult(res: Results?): String {
            return res?.typeOf?.joinToString(" | ") ?: ""
        }
    },
    DEFINITION {
        override fun getInSettingsText(context: Context): String {
            return context.resources.getStringArray(R.array.definitionSettings)[0]
        }

        override fun getResultTitle(context: Context): String {
            return context.resources.getStringArray(R.array.definitionSettings)[1]
        }

        override fun getResult(res: Results?): String {
            return res?.definition ?: ""
        }
    },
    SYNONYMS {
        override fun getInSettingsText(context: Context): String {
            return context.resources.getStringArray(R.array.synonymsSettings)[0]
        }

        override fun getResultTitle(context: Context): String {
            return context.resources.getStringArray(R.array.synonymsSettings)[1]
        }

        override fun getResult(res: Results?): String {
            return res?.synonyms?.joinToString(" | ") ?: ""
        }
    },
    ANTONYMS {
        override fun getInSettingsText(context: Context): String {
            return context.resources.getStringArray(R.array.antonymsSettings)[0]
        }

        override fun getResultTitle(context: Context): String {
            return context.resources.getStringArray(R.array.antonymsSettings)[1]
        }

        override fun getResult(res: Results?): String {
            return res?.antonyms?.joinToString(" | ") ?: ""
        }
    },
    EXAMPLES {
        override fun getInSettingsText(context: Context): String {
            return context.resources.getStringArray(R.array.examplesSettings)[0]
        }

        override fun getResultTitle(context: Context): String {
            return context.resources.getStringArray(R.array.examplesSettings)[1]
        }

        override fun getResult(res: Results?): String {
            return res?.examples?.joinToString(" | ") ?: ""
        }
    };


    abstract fun getInSettingsText(context: Context): String
    abstract fun getResultTitle(context: Context): String
    abstract fun getResult(res: Results?): String
}
