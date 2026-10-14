package com.example.ai

import com.example.data.model.CharacterProfile
import com.example.data.model.LocationProfile
import com.example.data.model.Project
import com.example.data.model.SceneItem

data class PromptGenerationResult(
    val englishPrompt: String,
    val burmeseExplanation: String,
    val sectionItems: List<PromptSectionDetail>
)

data class PromptSectionDetail(
    val partNumber: Int,
    val englishTitle: String,
    val burmeseTitle: String,
    val content: String,
    val burmesePurpose: String
)

object PromptBuilder {

    /**
     * Builds the professional 14-part AI video prompt from scene, project,
     * character profiles, and location data. Omits empty fields strictly.
     */
    fun buildScenePrompt(
        project: Project,
        scene: SceneItem,
        projectCharacters: List<CharacterProfile>,
        projectLocations: List<LocationProfile>
    ): PromptGenerationResult {
        val sections = mutableListOf<PromptSectionDetail>()

        // 1. Clip Duration
        val durationStr = "${scene.durationSec} seconds (Aspect Ratio: ${project.aspectRatio})"
        sections.add(
            PromptSectionDetail(
                partNumber = 1,
                englishTitle = "Clip Duration & Format",
                burmeseTitle = "အပိုင်းကြာချိန် နှင့် အချိုးအစား",
                content = durationStr,
                burmesePurpose = "ဗီဒီယို မော်ဒယ်အတွက် သတ်မှတ်ထားသော ကြာချိန် (${scene.durationSec} စက္ကန့်) နှင့် ပုံစံ (${project.aspectRatio}) ဖြစ်ပါသည်။"
            )
        )

        // 2. Visual Style
        val visualStyleStr = buildString {
            append(project.visualStyle)
            if (project.customStyleNotes.isNotBlank()) {
                append(" - Notes: ${project.customStyleNotes}")
            }
        }.trim()
        if (visualStyleStr.isNotBlank()) {
            sections.add(
                PromptSectionDetail(
                    partNumber = 2,
                    englishTitle = "Visual Style & Aesthetics",
                    burmeseTitle = "ရုပ်ထွက် စတိုင်နှင့် အလှအပ",
                    content = "$visualStyleStr, ultra-high resolution, cinematic color grading",
                    burmesePurpose = "ဗီဒီယို၏ အနုပညာစတိုင် (${project.visualStyle}) ကို တသမတ်တည်း ထိန်းသိမ်းစေပါသည်။"
                )
            )
        }

        // 3. Character Appearance
        // Pull approved character descriptions for characters assigned to this scene
        val relevantCharacters = projectCharacters.filter { char ->
            scene.characterNames.any { it.equals(char.name, ignoreCase = true) }
        }
        val characterDescStr = if (relevantCharacters.isNotEmpty()) {
            relevantCharacters.joinToString("\n") { it.buildConsistentDescription() }
        } else if (scene.characterNames.isNotEmpty()) {
            "Characters in scene: " + scene.characterNames.joinToString(", ")
        } else ""

        if (characterDescStr.isNotBlank()) {
            sections.add(
                PromptSectionDetail(
                    partNumber = 3,
                    englishTitle = "Character Appearance",
                    burmeseTitle = "ဇာတ်ကောင် ရုပ်ပိုင်းဆိုင်ရာ ဖော်ပြချက်",
                    content = characterDescStr,
                    burmesePurpose = "ဇာတ်ကောင်များ၏ အဝတ်အစား၊ မျက်နှာသွင်ပြင်နှင့် ဆံပင်ပုံစံ အစစ်အမှန်အတိုင်း ပေါ်ပေါက်စေပါသည်။"
                )
            )
        }

        // 4. Location
        val matchedLocation = projectLocations.firstOrNull {
            it.name.equals(scene.locationName, ignoreCase = true)
        }
        val locationDescStr = when {
            matchedLocation != null -> matchedLocation.buildConsistentDescription()
            scene.locationName.isNotBlank() -> "Environment: ${scene.locationName}"
            else -> ""
        }
        if (locationDescStr.isNotBlank()) {
            sections.add(
                PromptSectionDetail(
                    partNumber = 4,
                    englishTitle = "Location & Environment",
                    burmeseTitle = "နေရာနှင့် ပတ်ဝန်းကျင်",
                    content = locationDescStr,
                    burmesePurpose = "ဇာတ်ဝင်ခန်း ဖြစ်ပွားရာ နောက်ခံ၊ ပစ္စည်းများနှင့် ပတ်ဝန်းကျင် အနေအထားကို သတ်မှတ်သည်။"
                )
            )
        }

        // 5. Opening Frame
        val openingStr = listOfNotNull(
            scene.openingAction.takeIf { it.isNotBlank() },
            scene.openingFrame.takeIf { it.isNotBlank() },
            scene.characterPositions.takeIf { it.isNotBlank() }?.let { "Positions: $it" }
        ).joinToString("; ")
        if (openingStr.isNotBlank()) {
            sections.add(
                PromptSectionDetail(
                    partNumber = 5,
                    englishTitle = "Opening Frame",
                    burmeseTitle = "စတင်ချိန် ပထမဖရိန် မြင်ကွင်း",
                    content = openingStr,
                    burmesePurpose = "ဗီဒီယို ကလစ် စတင်ချိန် (0:00) တွင် မြင်တွေ့ရမည့် အစပြု မြင်ကွင်းဖြစ်ပါသည်။"
                )
            )
        }

        // 6. Main Actions
        val mainActionStr = scene.mainAction.trim()
        if (mainActionStr.isNotBlank()) {
            sections.add(
                PromptSectionDetail(
                    partNumber = 6,
                    englishTitle = "Main Actions",
                    burmeseTitle = "အဓိက လှုပ်ရှားဆောင်ရွက်မှု",
                    content = mainActionStr,
                    burmesePurpose = "ဇာတ်ဝင်ခန်းတွင် ဖြစ်ပျက်မည့် ပင်မ အဓိက လှုပ်ရှားမှုကို ဖော်ပြသည်။"
                )
            )
        }

        // 7. Facial Expressions
        val expressionStr = listOfNotNull(
            scene.characterEmotions.takeIf { it.isNotBlank() },
            scene.characterExpressions.takeIf { it.isNotBlank() }
        ).joinToString("; ")
        if (expressionStr.isNotBlank()) {
            sections.add(
                PromptSectionDetail(
                    partNumber = 7,
                    englishTitle = "Facial Expressions & Emotion",
                    burmeseTitle = "မျက်နှာအမူအရာနှင့် ခံစားချက်",
                    content = expressionStr,
                    burmesePurpose = "ဇာတ်ကောင်များ၏ စိတ်ခံစားမှုနှင့် မျက်နှာ အမူအရာ တိကျစေရန် ညွှန်ကြားသည်။"
                )
            )
        }

        // 8. Body Movement
        val bodyMovementStr = listOfNotNull(
            scene.mainMovement.takeIf { it.isNotBlank() }
        ).joinToString("; ")
        if (bodyMovementStr.isNotBlank()) {
            sections.add(
                PromptSectionDetail(
                    partNumber = 8,
                    englishTitle = "Body Movement & Pacing",
                    burmeseTitle = "ခန္ဓာကိုယ် လှုပ်ရှားပုံနှင့် အရှိန်",
                    content = bodyMovementStr,
                    burmesePurpose = "ရုပ်လုံးကြွ လှုပ်ရှားမှုနှင့် ကာတွန်း/လူ လမ်းလျှောက် အနေအထားကို သတ်မှတ်သည်။"
                )
            )
        }

        // 9. Camera Direction
        val cameraStr = listOfNotNull(
            scene.cameraDirection.takeIf { it.isNotBlank() },
            scene.cameraAngle.takeIf { it.isNotBlank() }?.let { "Angle: $it" },
            scene.cameraMovement.takeIf { it.isNotBlank() }?.let { "Movement: $it" }
        ).joinToString("; ")
        if (cameraStr.isNotBlank()) {
            sections.add(
                PromptSectionDetail(
                    partNumber = 9,
                    englishTitle = "Camera Direction",
                    burmeseTitle = "ကင်မရာ ရိုက်ချက်နှင့် ထောင့်ချိုး",
                    content = cameraStr,
                    burmesePurpose = "ရုပ်ရှင်ဆန်သော ကင်မရာ ရွေ့လျားမှု (Pan, Tilt, Dolly, Zoom) ကို တိကျစေပါသည်။"
                )
            )
        }

        // 10. Lighting
        val lightingStr = listOfNotNull(
            matchedLocation?.lighting?.takeIf { it.isNotBlank() },
            scene.lightingDetail.takeIf { it.isNotBlank() }
        ).distinct().joinToString("; ")
        if (lightingStr.isNotBlank()) {
            sections.add(
                PromptSectionDetail(
                    partNumber = 10,
                    englishTitle = "Lighting & Atmosphere",
                    burmeseTitle = "အလင်းအမှောင်နှင့် လေထု",
                    content = lightingStr,
                    burmesePurpose = "ဗီဒီယို၏ အလင်းထင်ဟပ်မှု၊ အရိပ်နှင့် အငွေ့အသက်ကို ဖန်တီးပေးသည်။"
                )
            )
        }

        // 11. Dialogue
        val dialogueStr = scene.dialogue.trim()
        if (dialogueStr.isNotBlank()) {
            sections.add(
                PromptSectionDetail(
                    partNumber = 11,
                    englishTitle = "Dialogue & Lip Sync",
                    burmeseTitle = "ပြောစကားနှင့် နှုတ်ခမ်းလှုပ်ရှားမှု",
                    content = "Spoken dialogue: \"$dialogueStr\" (${project.dialogueLanguage})",
                    burmesePurpose = "ဗီဒီယိုတွင် ပါဝင်မည့် နှုတ်ထွက်စကားနှင့် အသံ ထပ်တူကျစေရန် ဖြစ်သည်။"
                )
            )
        }

        // 12. Environmental Sound
        val soundStr = scene.soundEffects.trim()
        if (soundStr.isNotBlank()) {
            sections.add(
                PromptSectionDetail(
                    partNumber = 12,
                    englishTitle = "Environmental Sound & Audio",
                    burmeseTitle = "ပတ်ဝန်းကျင် အသံနှင့် SFX",
                    content = "Audio atmosphere: $soundStr",
                    burmesePurpose = "နောက်ခံ ပတ်ဝန်းကျင်အသံများနှင့် အထူးပြုလုပ်ချက် အသံများကို ညွှန်ကြားသည်။"
                )
            )
        }

        // 13. Ending Frame
        val endingStr = listOfNotNull(
            scene.endingAction.takeIf { it.isNotBlank() },
            scene.finalFrame.takeIf { it.isNotBlank() },
            scene.importantVisualEvent.takeIf { it.isNotBlank() }?.let { "Key Event: $it" }
        ).joinToString("; ")
        if (endingStr.isNotBlank()) {
            sections.add(
                PromptSectionDetail(
                    partNumber = 13,
                    englishTitle = "Ending Frame",
                    burmeseTitle = "ပြီးဆုံးချိန် နောက်ဆုံးဖရိန်",
                    content = endingStr,
                    burmesePurpose = "ကလစ် အဆုံးသတ်ချိန် နောက်ဆုံး မြင်ကွင်းအနေအထားကို ပုံဖော်ပေးသည်။"
                )
            )
        }

        // 14. Continuity Instructions
        val continuityStr = listOfNotNull(
            scene.continuityNotes.takeIf { it.isNotBlank() },
            scene.nextSceneTransition.takeIf { it.isNotBlank() }?.let { "Transition: $it" }
        ).joinToString("; ")
        if (continuityStr.isNotBlank()) {
            sections.add(
                PromptSectionDetail(
                    partNumber = 14,
                    englishTitle = "Continuity Instructions",
                    burmeseTitle = "ဇာတ်ဆက်စပ်မှု ညွှန်ကြားချက်",
                    content = "$continuityStr, seamless scene flow",
                    burmesePurpose = "နောက်ဆက်တွဲ ဇာတ်ဝင်ခန်းများနှင့် ပုံစံ၊ နေရာ၊ အဝတ်အစား မလွဲစေရန် ဆက်စပ်ပေးသည်။"
                )
            )
        }

        // Assemble Full Structured English Prompt
        val fullEnglish = buildString {
            appendLine("=== AI VIDEO PRODUCTION PROMPT (SCENE ${scene.sceneNumber}: ${scene.title.ifBlank { "Untitled" }}) ===")
            sections.forEach { sec ->
                appendLine("[${sec.partNumber}. ${sec.englishTitle.uppercase()}]")
                appendLine(sec.content)
                appendLine()
            }
        }.trim()

        // Assemble Burmese Explanation
        val burmeseExpl = buildString {
            appendLine("🎬 ဇာတ်ဝင်ခန်း ${scene.sceneNumber} အတွက် AI ဗီဒီယို ပရော့မ် ရှင်းလင်းချက်:")
            sections.forEach { sec ->
                appendLine("• ${sec.partNumber}. ${sec.burmeseTitle}: ${sec.burmesePurpose}")
            }
        }.trim()

        return PromptGenerationResult(
            englishPrompt = fullEnglish,
            burmeseExplanation = burmeseExpl,
            sectionItems = sections
        )
    }

    /**
     * Builds a comprehensive master production prompt for the entire project
     */
    fun buildMasterProjectPrompt(
        project: Project,
        characters: List<CharacterProfile>,
        locations: List<LocationProfile>,
        scenes: List<SceneItem>
    ): PromptGenerationResult {
        val sections = mutableListOf<PromptSectionDetail>()

        // 1. Duration
        val totalEst = scenes.sumOf { it.durationSec }
        sections.add(
            PromptSectionDetail(
                partNumber = 1,
                englishTitle = "Master Project Specs",
                burmeseTitle = "ပရောဂျက် စုစုပေါင်း သတ်မှတ်ချက်များ",
                content = "Target Duration: ${project.targetDurationSec}s (Est. Total Scenes: ${totalEst}s), Aspect Ratio: ${project.aspectRatio}, Platform: ${project.platform}",
                burmesePurpose = "ဗီဒီယိုတစ်ခုလုံး၏ စုစုပေါင်း အလျားနှင့် ဖော်မတ်"
            )
        )

        // 2. Visual Style
        sections.add(
            PromptSectionDetail(
                partNumber = 2,
                englishTitle = "Global Visual Style Guide",
                burmeseTitle = "တစ်ခုလုံးဆိုင်ရာ ရုပ်ထွက်စတိုင် လမ်းညွှန်",
                content = "${project.visualStyle}. ${project.customStyleNotes}. Cinematic lighting, cohesive palette throughout all scenes.",
                burmesePurpose = "ဇာတ်လမ်းတစ်ပုဒ်လုံးတွင် တူညီသော ရုပ်ထွက်စတိုင်ကို ထိန်းသိမ်းရန်"
            )
        )

        // 3. Characters
        val charList = characters.joinToString("\n---\n") { it.buildConsistentDescription() }
        if (charList.isNotBlank()) {
            sections.add(
                PromptSectionDetail(
                    partNumber = 3,
                    englishTitle = "Master Character Profiles",
                    burmeseTitle = "ပင်မ ဇာတ်ကောင် စာရင်းနှင့် ရုပ်သွင်",
                    content = charList,
                    burmesePurpose = "ဗီဒီယို မော်ဒယ်တွင် ဇာတ်ကောင်များ မျက်နှာမပြောင်းသွားစေရန်"
                )
            )
        }

        // 4. Locations
        val locList = locations.joinToString("\n---\n") { it.buildConsistentDescription() }
        if (locList.isNotBlank()) {
            sections.add(
                PromptSectionDetail(
                    partNumber = 4,
                    englishTitle = "Master Locations & Sets",
                    burmeseTitle = "ပင်မ ဇာတ်ဝင်ခန်း နေရာများ",
                    content = locList,
                    burmesePurpose = "နေရာများနှင့် နောက်ခံ အလှအပများ"
                )
            )
        }

        // 5. Scene Breakdown
        val sceneList = scenes.sortedBy { it.sceneNumber }.joinToString("\n") { s ->
            "Scene ${s.sceneNumber} (${s.durationSec}s): ${s.title} | Main Action: ${s.mainAction.ifBlank { "N/A" }} | Cam: ${s.cameraDirection.ifBlank { "Standard" }}"
        }
        if (sceneList.isNotBlank()) {
            sections.add(
                PromptSectionDetail(
                    partNumber = 5,
                    englishTitle = "Sequential Scene Roadmap",
                    burmeseTitle = "ဇာတ်ဝင်ခန်းများ အစီအစဉ် ရိုဒ်မက်",
                    content = sceneList,
                    burmesePurpose = "ဇာတ်လမ်း အစီအစဉ်အတိုင်း ဗီဒီယိုကလစ်များ ထုတ်လုပ်ရန် လမ်းညွှန်"
                )
            )
        }

        val fullEnglish = buildString {
            appendLine("=== MASTER AI VIDEO PRODUCTION PACKAGE: ${project.title.uppercase()} ===")
            appendLine("Genre: ${project.genre} | Style: ${project.visualStyle}")
            appendLine("Logline: ${project.storyDevelopment.logline.ifBlank { project.storyIdea }}")
            appendLine()
            sections.forEach { sec ->
                appendLine("[SECTION: ${sec.englishTitle.uppercase()}]")
                appendLine(sec.content)
                appendLine()
            }
        }.trim()

        val burmeseExpl = buildString {
            appendLine("📋 ပရောဂျက် မာစတာ ဗီဒီယို ပရော့မ် ရှင်းလင်းချက်:")
            sections.forEach { sec ->
                appendLine("• ${sec.burmeseTitle}: ${sec.burmesePurpose}")
            }
        }.trim()

        return PromptGenerationResult(
            englishPrompt = fullEnglish,
            burmeseExplanation = burmeseExpl,
            sectionItems = sections
        )
    }
}
