package com.example.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.data.model.Project
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ExportHelper {

    fun copyToClipboard(context: Context, label: String, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "ကလစ်ဘုတ်သို့ ကူးယူပြီးပါပြီ (Copied to clipboard)", Toast.LENGTH_SHORT).show()
    }

    fun shareText(context: Context, title: String, content: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, content)
            putExtra(Intent.EXTRA_TITLE, title)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, title)
        context.startActivity(shareIntent)
    }

    /**
     * Generates a beautifully formatted Screenplay TXT according to standard screenplay conventions
     */
    fun formatScreenplayTxt(project: Project): String = buildString {
        appendLine("================================================================================")
        appendLine("                             ${project.title.uppercase()}")
        appendLine("                               SCREENPLAY DRAFT")
        appendLine("Genre: ${project.genre} | Language: ${project.dialogueLanguage}")
        appendLine("Visual Style: ${project.visualStyle}")
        appendLine("Date: ${SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())}")
        appendLine("================================================================================")
        appendLine()

        if (project.storyDevelopment.logline.isNotBlank()) {
            appendLine("LOGLINE:")
            appendLine(project.storyDevelopment.logline)
            appendLine()
        }

        appendLine("--- CAST OF CHARACTERS ---")
        project.characters.forEach { char ->
            appendLine("• ${char.name.uppercase()} (${char.age}): ${char.personality.ifBlank { char.genderPresentation }}")
        }
        appendLine()
        appendLine("================================================================================")
        appendLine()

        if (project.screenplay.isEmpty()) {
            appendLine("[ဇာတ်ညွှန်း အခန်းများ ထည့်သွင်းထားခြင်း မရှိသေးပါ]")
        } else {
            project.screenplay.forEach { item ->
                append(item.toScreenplayFormattedText())
            }
        }
    }

    /**
     * Generates Scene Breakdown TXT
     */
    fun formatSceneBreakdownTxt(project: Project): String = buildString {
        appendLine("================================================================================")
        appendLine("                   SCENE BREAKDOWN — ${project.title.uppercase()}")
        appendLine("Target Duration: ${project.targetDurationSec}s | Total Planned Scenes: ${project.scenes.size}")
        val totalActual = project.scenes.sumOf { it.durationSec }
        appendLine("Estimated Scenes Duration: ${totalActual}s")
        appendLine("Aspect Ratio: ${project.aspectRatio} | Platform: ${project.platform}")
        appendLine("================================================================================")
        appendLine()

        project.scenes.sortedBy { it.sceneNumber }.forEach { scene ->
            appendLine("SCENE ${scene.sceneNumber}: ${scene.title.uppercase()} [${scene.durationSec}s]")
            appendLine("Location: ${scene.locationName.ifBlank { "Not Specified" }}")
            appendLine("Characters: ${scene.characterNames.joinToString(", ").ifBlank { "None" }}")
            if (scene.openingAction.isNotBlank()) appendLine("Opening Action: ${scene.openingAction}")
            if (scene.mainAction.isNotBlank()) appendLine("Main Action: ${scene.mainAction}")
            if (scene.characterEmotions.isNotBlank()) appendLine("Emotions: ${scene.characterEmotions}")
            if (scene.dialogue.isNotBlank()) appendLine("Dialogue: \"${scene.dialogue}\"")
            if (scene.cameraDirection.isNotBlank()) appendLine("Camera: ${scene.cameraDirection}")
            if (scene.soundEffects.isNotBlank()) appendLine("Audio/SFX: ${scene.soundEffects}")
            if (scene.endingAction.isNotBlank()) appendLine("Ending Action: ${scene.endingAction}")
            if (scene.continuityNotes.isNotBlank()) appendLine("Continuity: ${scene.continuityNotes}")
            appendLine("--------------------------------------------------------------------------------")
        }
    }

    /**
     * Generates Complete Production Package TXT
     */
    fun formatProductionPackageTxt(project: Project): String = buildString {
        appendLine("********************************************************************************")
        appendLine("                AI VIDEO STUDIO — COMPLETE PRODUCTION PACKAGE")
        appendLine("Project: ${project.title}")
        appendLine("Genre: ${project.genre} | Style: ${project.visualStyle}")
        appendLine("Platform: ${project.platform} | Ratio: ${project.aspectRatio}")
        appendLine("Generated: ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())}")
        appendLine("********************************************************************************")
        appendLine()

        appendLine("== 1. STORY DEVELOPMENT ==")
        appendLine("Story Idea: ${project.storyDevelopment.storyIdea.ifBlank { project.storyIdea }}")
        appendLine("Logline: ${project.storyDevelopment.logline}")
        appendLine("Main Conflict: ${project.storyDevelopment.mainConflict}")
        appendLine("Theme: ${project.storyDevelopment.storyTheme}")
        appendLine("Climax: ${project.storyDevelopment.climax}")
        appendLine("Ending: ${project.storyDevelopment.ending}")
        appendLine()

        appendLine("== 2. APPROVED CHARACTERS ==")
        project.characters.forEachIndexed { idx, c ->
            appendLine("[${idx + 1}] ${c.name} (${c.age} yrs, ${c.genderPresentation})")
            appendLine("Appearance: ${c.buildConsistentDescription()}")
            appendLine("Prompt: ${c.characterReferencePrompt}")
            appendLine()
        }

        appendLine("== 3. PRODUCTION LOCATIONS ==")
        project.locations.forEachIndexed { idx, l ->
            appendLine("[${idx + 1}] ${l.name}")
            appendLine("Setting: ${l.buildConsistentDescription()}")
            appendLine("Prompt: ${l.locationPrompt}")
            appendLine()
        }

        appendLine("== 4. SCENE BREAKDOWNS ==")
        project.scenes.forEach { s ->
            appendLine("Scene ${s.sceneNumber} (${s.durationSec}s): ${s.title}")
            appendLine("Action: ${s.mainAction}")
            appendLine("Camera: ${s.cameraDirection}")
            appendLine()
        }

        appendLine("== 5. SAVED VIDEO PROMPT VERSIONS ==")
        project.promptVersions.forEachIndexed { idx, pv ->
            appendLine("--- Version ${idx + 1}: ${pv.versionTitle} ---")
            appendLine(pv.englishPrompt)
            appendLine()
        }
    }

    /**
     * Converts Project to clean JSON string
     */
    fun formatProjectJson(project: Project): String {
        val root = JSONObject().apply {
            put("id", project.id)
            put("title", project.title)
            put("genre", project.genre)
            put("storyIdea", project.storyIdea)
            put("targetAudience", project.targetAudience)
            put("targetDurationSec", project.targetDurationSec)
            put("visualStyle", project.visualStyle)
            put("customStyleNotes", project.customStyleNotes)
            put("platform", project.platform)
            put("aspectRatio", project.aspectRatio)
            put("dialogueLanguage", project.dialogueLanguage)
            put("notes", project.notes)
            put("createdAt", project.createdAt)
            put("updatedAt", project.updatedAt)

            // Story dev
            put("storyDevelopment", JSONObject().apply {
                put("storyIdea", project.storyDevelopment.storyIdea)
                put("logline", project.storyDevelopment.logline)
                put("mainCharactersSummary", project.storyDevelopment.mainCharactersSummary)
                put("mainConflict", project.storyDevelopment.mainConflict)
                put("storyTheme", project.storyDevelopment.storyTheme)
                put("climax", project.storyDevelopment.climax)
                put("ending", project.storyDevelopment.ending)
            })

            // Characters
            val charArray = JSONArray()
            project.characters.forEach { c ->
                charArray.put(JSONObject().apply {
                    put("id", c.id)
                    put("name", c.name)
                    put("age", c.age)
                    put("genderPresentation", c.genderPresentation)
                    put("clothing", c.clothing)
                    put("personality", c.personality)
                    put("consistentDescription", c.buildConsistentDescription())
                    put("characterReferencePrompt", c.characterReferencePrompt)
                })
            }
            put("characters", charArray)

            // Locations
            val locArray = JSONArray()
            project.locations.forEach { l ->
                locArray.put(JSONObject().apply {
                    put("id", l.id)
                    put("name", l.name)
                    put("environmentDescription", l.environmentDescription)
                    put("lighting", l.lighting)
                    put("consistentDescription", l.buildConsistentDescription())
                    put("locationPrompt", l.locationPrompt)
                })
            }
            put("locations", locArray)

            // Scenes
            val sceneArray = JSONArray()
            project.scenes.forEach { s ->
                val cArr = JSONArray()
                s.characterNames.forEach { cArr.put(it) }
                sceneArray.put(JSONObject().apply {
                    put("sceneNumber", s.sceneNumber)
                    put("title", s.title)
                    put("durationSec", s.durationSec)
                    put("locationName", s.locationName)
                    put("characters", cArr)
                    put("mainAction", s.mainAction)
                    put("cameraDirection", s.cameraDirection)
                    put("soundEffects", s.soundEffects)
                })
            }
            put("scenes", sceneArray)

            // Prompts
            val promptArray = JSONArray()
            project.promptVersions.forEach { p ->
                promptArray.put(JSONObject().apply {
                    put("versionTitle", p.versionTitle)
                    put("englishPrompt", p.englishPrompt)
                    put("burmeseExplanation", p.burmeseExplanation)
                })
            }
            put("promptVersions", promptArray)
        }
        return root.toString(2)
    }
}
