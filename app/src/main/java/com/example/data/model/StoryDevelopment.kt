package com.example.data.model

import java.util.UUID

data class StoryDevelopment(
    val storyIdea: String = "",
    val logline: String = "",
    val mainCharactersSummary: String = "",
    val mainConflict: String = "",
    val storyTheme: String = "",
    val climax: String = "",
    val ending: String = ""
)

data class ScreenplayItem(
    val id: String = UUID.randomUUID().toString(),
    val sceneHeading: String = "", // e.g., "INT. FUTURISTIC LAB - NIGHT"
    val actionDescription: String = "",
    val characterName: String = "",
    val dialogue: String = "",
    val emotion: String = "",
    val soundEffects: String = "",
    val transition: String = "CUT TO:" // "CUT TO:", "DISSOLVE TO:", "FADE OUT", etc.
) {
    fun toScreenplayFormattedText(): String {
        val sb = StringBuilder()
        if (sceneHeading.isNotBlank()) {
            sb.appendLine(sceneHeading.uppercase())
            sb.appendLine()
        }
        if (actionDescription.isNotBlank()) {
            sb.appendLine(actionDescription)
            sb.appendLine()
        }
        if (characterName.isNotBlank()) {
            sb.appendLine("                    ${characterName.uppercase()}")
            if (emotion.isNotBlank()) {
                sb.appendLine("                ($emotion)")
            }
            if (dialogue.isNotBlank()) {
                sb.appendLine("          \"$dialogue\"")
            }
            sb.appendLine()
        }
        if (soundEffects.isNotBlank()) {
            sb.appendLine("[SFX: $soundEffects]")
            sb.appendLine()
        }
        if (transition.isNotBlank()) {
            sb.appendLine("                                                ${transition.uppercase()}")
            sb.appendLine()
        }
        return sb.toString()
    }
}
