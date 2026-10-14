package com.example.data.local

import androidx.room.TypeConverter
import com.example.data.model.CharacterProfile
import com.example.data.model.LocationProfile
import com.example.data.model.SceneItem
import com.example.data.model.ScreenplayItem
import com.example.data.model.StoryDevelopment
import com.example.data.model.VideoPromptVersion
import org.json.JSONArray
import org.json.JSONObject

class ProjectConverters {

    // StoryDevelopment
    @TypeConverter
    fun fromStoryDevelopment(dev: StoryDevelopment?): String {
        if (dev == null) return ""
        val json = JSONObject().apply {
            put("storyIdea", dev.storyIdea)
            put("logline", dev.logline)
            put("mainCharactersSummary", dev.mainCharactersSummary)
            put("mainConflict", dev.mainConflict)
            put("storyTheme", dev.storyTheme)
            put("climax", dev.climax)
            put("ending", dev.ending)
        }
        return json.toString()
    }

    @TypeConverter
    fun toStoryDevelopment(value: String?): StoryDevelopment {
        if (value.isNullOrBlank()) return StoryDevelopment()
        return try {
            val json = JSONObject(value)
            StoryDevelopment(
                storyIdea = json.optString("storyIdea", ""),
                logline = json.optString("logline", ""),
                mainCharactersSummary = json.optString("mainCharactersSummary", ""),
                mainConflict = json.optString("mainConflict", ""),
                storyTheme = json.optString("storyTheme", ""),
                climax = json.optString("climax", ""),
                ending = json.optString("ending", "")
            )
        } catch (e: Exception) {
            StoryDevelopment()
        }
    }

    // Screenplay List
    @TypeConverter
    fun fromScreenplayList(list: List<ScreenplayItem>?): String {
        if (list.isNullOrEmpty()) return "[]"
        val arr = JSONArray()
        list.forEach { item ->
            val obj = JSONObject().apply {
                put("id", item.id)
                put("sceneHeading", item.sceneHeading)
                put("actionDescription", item.actionDescription)
                put("characterName", item.characterName)
                put("dialogue", item.dialogue)
                put("emotion", item.emotion)
                put("soundEffects", item.soundEffects)
                put("transition", item.transition)
            }
            arr.put(obj)
        }
        return arr.toString()
    }

    @TypeConverter
    fun toScreenplayList(value: String?): List<ScreenplayItem> {
        if (value.isNullOrBlank()) return emptyList()
        return try {
            val arr = JSONArray(value)
            val list = mutableListOf<ScreenplayItem>()
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                list.add(
                    ScreenplayItem(
                        id = obj.optString("id"),
                        sceneHeading = obj.optString("sceneHeading"),
                        actionDescription = obj.optString("actionDescription"),
                        characterName = obj.optString("characterName"),
                        dialogue = obj.optString("dialogue"),
                        emotion = obj.optString("emotion"),
                        soundEffects = obj.optString("soundEffects"),
                        transition = obj.optString("transition", "CUT TO:")
                    )
                )
            }
            list
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Character Profile List
    @TypeConverter
    fun fromCharacterList(list: List<CharacterProfile>?): String {
        if (list.isNullOrEmpty()) return "[]"
        val arr = JSONArray()
        list.forEach { char ->
            val obj = JSONObject().apply {
                put("id", char.id)
                put("projectId", char.projectId)
                put("name", char.name)
                put("age", char.age)
                put("genderPresentation", char.genderPresentation)
                put("height", char.height)
                put("bodyType", char.bodyType)
                put("faceDescription", char.faceDescription)
                put("eyeDescription", char.eyeDescription)
                put("hairstyle", char.hairstyle)
                put("hairColor", char.hairColor)
                put("skinTone", char.skinTone)
                put("clothing", char.clothing)
                put("shoes", char.shoes)
                put("accessories", char.accessories)
                put("personality", char.personality)
                put("voiceDescription", char.voiceDescription)
                put("distinctiveFeatures", char.distinctiveFeatures)
                put("referenceImageUri", char.referenceImageUri ?: "")
                put("characterReferencePrompt", char.characterReferencePrompt)
            }
            arr.put(obj)
        }
        return arr.toString()
    }

    @TypeConverter
    fun toCharacterList(value: String?): List<CharacterProfile> {
        if (value.isNullOrBlank()) return emptyList()
        return try {
            val arr = JSONArray(value)
            val list = mutableListOf<CharacterProfile>()
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                list.add(
                    CharacterProfile(
                        id = obj.optString("id"),
                        projectId = obj.optString("projectId"),
                        name = obj.optString("name"),
                        age = obj.optString("age"),
                        genderPresentation = obj.optString("genderPresentation"),
                        height = obj.optString("height"),
                        bodyType = obj.optString("bodyType"),
                        faceDescription = obj.optString("faceDescription"),
                        eyeDescription = obj.optString("eyeDescription"),
                        hairstyle = obj.optString("hairstyle"),
                        hairColor = obj.optString("hairColor"),
                        skinTone = obj.optString("skinTone"),
                        clothing = obj.optString("clothing"),
                        shoes = obj.optString("shoes"),
                        accessories = obj.optString("accessories"),
                        personality = obj.optString("personality"),
                        voiceDescription = obj.optString("voiceDescription"),
                        distinctiveFeatures = obj.optString("distinctiveFeatures"),
                        referenceImageUri = obj.optString("referenceImageUri").takeIf { it.isNotBlank() },
                        characterReferencePrompt = obj.optString("characterReferencePrompt")
                    )
                )
            }
            list
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Location Profile List
    @TypeConverter
    fun fromLocationList(list: List<LocationProfile>?): String {
        if (list.isNullOrEmpty()) return "[]"
        val arr = JSONArray()
        list.forEach { loc ->
            val obj = JSONObject().apply {
                put("id", loc.id)
                put("projectId", loc.projectId)
                put("name", loc.name)
                put("environmentDescription", loc.environmentDescription)
                put("backgroundElements", loc.backgroundElements)
                put("props", loc.props)
                put("lighting", loc.lighting)
                put("colorPalette", loc.colorPalette)
                put("weather", loc.weather)
                put("timeOfDay", loc.timeOfDay)
                put("visualStyle", loc.visualStyle)
                put("referenceImageUri", loc.referenceImageUri ?: "")
                put("locationPrompt", loc.locationPrompt)
            }
            arr.put(obj)
        }
        return arr.toString()
    }

    @TypeConverter
    fun toLocationList(value: String?): List<LocationProfile> {
        if (value.isNullOrBlank()) return emptyList()
        return try {
            val arr = JSONArray(value)
            val list = mutableListOf<LocationProfile>()
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                list.add(
                    LocationProfile(
                        id = obj.optString("id"),
                        projectId = obj.optString("projectId"),
                        name = obj.optString("name"),
                        environmentDescription = obj.optString("environmentDescription"),
                        backgroundElements = obj.optString("backgroundElements"),
                        props = obj.optString("props"),
                        lighting = obj.optString("lighting"),
                        colorPalette = obj.optString("colorPalette"),
                        weather = obj.optString("weather"),
                        timeOfDay = obj.optString("timeOfDay"),
                        visualStyle = obj.optString("visualStyle"),
                        referenceImageUri = obj.optString("referenceImageUri").takeIf { it.isNotBlank() },
                        locationPrompt = obj.optString("locationPrompt")
                    )
                )
            }
            list
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Scene Items List
    @TypeConverter
    fun fromSceneList(list: List<SceneItem>?): String {
        if (list.isNullOrEmpty()) return "[]"
        val arr = JSONArray()
        list.forEach { s ->
            val charNamesArr = JSONArray()
            s.characterNames.forEach { charNamesArr.put(it) }

            val obj = JSONObject().apply {
                put("id", s.id)
                put("projectId", s.projectId)
                put("sceneNumber", s.sceneNumber)
                put("title", s.title)
                put("durationSec", s.durationSec)
                put("locationName", s.locationName)
                put("characterNames", charNamesArr)
                put("openingAction", s.openingAction)
                put("mainAction", s.mainAction)
                put("characterEmotions", s.characterEmotions)
                put("dialogue", s.dialogue)
                put("cameraDirection", s.cameraDirection)
                put("soundEffects", s.soundEffects)
                put("endingAction", s.endingAction)
                put("continuityNotes", s.continuityNotes)
                // Storyboard fields
                put("openingFrame", s.openingFrame)
                put("characterPositions", s.characterPositions)
                put("characterExpressions", s.characterExpressions)
                put("mainMovement", s.mainMovement)
                put("cameraAngle", s.cameraAngle)
                put("cameraMovement", s.cameraMovement)
                put("backgroundDetails", s.backgroundDetails)
                put("lightingDetail", s.lightingDetail)
                put("importantVisualEvent", s.importantVisualEvent)
                put("finalFrame", s.finalFrame)
                put("nextSceneTransition", s.nextSceneTransition)
                put("storyboardImageUri", s.storyboardImageUri ?: "")
            }
            arr.put(obj)
        }
        return arr.toString()
    }

    @TypeConverter
    fun toSceneList(value: String?): List<SceneItem> {
        if (value.isNullOrBlank()) return emptyList()
        return try {
            val arr = JSONArray(value)
            val list = mutableListOf<SceneItem>()
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                val charArr = obj.optJSONArray("characterNames")
                val charNames = mutableListOf<String>()
                if (charArr != null) {
                    for (c in 0 until charArr.length()) {
                        charNames.add(charArr.getString(c))
                    }
                }
                list.add(
                    SceneItem(
                        id = obj.optString("id"),
                        projectId = obj.optString("projectId"),
                        sceneNumber = obj.optInt("sceneNumber", 1),
                        title = obj.optString("title"),
                        durationSec = obj.optInt("durationSec", 10),
                        locationName = obj.optString("locationName"),
                        characterNames = charNames,
                        openingAction = obj.optString("openingAction"),
                        mainAction = obj.optString("mainAction"),
                        characterEmotions = obj.optString("characterEmotions"),
                        dialogue = obj.optString("dialogue"),
                        cameraDirection = obj.optString("cameraDirection"),
                        soundEffects = obj.optString("soundEffects"),
                        endingAction = obj.optString("endingAction"),
                        continuityNotes = obj.optString("continuityNotes"),
                        openingFrame = obj.optString("openingFrame"),
                        characterPositions = obj.optString("characterPositions"),
                        characterExpressions = obj.optString("characterExpressions"),
                        mainMovement = obj.optString("mainMovement"),
                        cameraAngle = obj.optString("cameraAngle"),
                        cameraMovement = obj.optString("cameraMovement"),
                        backgroundDetails = obj.optString("backgroundDetails"),
                        lightingDetail = obj.optString("lightingDetail"),
                        importantVisualEvent = obj.optString("importantVisualEvent"),
                        finalFrame = obj.optString("finalFrame"),
                        nextSceneTransition = obj.optString("nextSceneTransition"),
                        storyboardImageUri = obj.optString("storyboardImageUri").takeIf { it.isNotBlank() }
                    )
                )
            }
            list
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Prompt Versions List
    @TypeConverter
    fun fromPromptVersionList(list: List<VideoPromptVersion>?): String {
        if (list.isNullOrEmpty()) return "[]"
        val arr = JSONArray()
        list.forEach { p ->
            val obj = JSONObject().apply {
                put("id", p.id)
                put("projectId", p.projectId)
                put("sceneId", p.sceneId ?: "")
                put("sceneTitle", p.sceneTitle)
                put("versionTitle", p.versionTitle)
                put("englishPrompt", p.englishPrompt)
                put("burmeseExplanation", p.burmeseExplanation)
                put("createdAt", p.createdAt)
            }
            arr.put(obj)
        }
        return arr.toString()
    }

    @TypeConverter
    fun toPromptVersionList(value: String?): List<VideoPromptVersion> {
        if (value.isNullOrBlank()) return emptyList()
        return try {
            val arr = JSONArray(value)
            val list = mutableListOf<VideoPromptVersion>()
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                list.add(
                    VideoPromptVersion(
                        id = obj.optString("id"),
                        projectId = obj.optString("projectId"),
                        sceneId = obj.optString("sceneId").takeIf { it.isNotBlank() },
                        sceneTitle = obj.optString("sceneTitle", "All Scenes"),
                        versionTitle = obj.optString("versionTitle", "Version 1.0"),
                        englishPrompt = obj.optString("englishPrompt"),
                        burmeseExplanation = obj.optString("burmeseExplanation"),
                        createdAt = obj.optLong("createdAt", System.currentTimeMillis())
                    )
                )
            }
            list
        } catch (e: Exception) {
            emptyList()
        }
    }
}
