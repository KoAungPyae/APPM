package com.example.data.model

import java.util.UUID

data class CharacterProfile(
    val id: String = UUID.randomUUID().toString(),
    val projectId: String = "",
    val name: String = "",
    val age: String = "",
    val genderPresentation: String = "",
    val height: String = "",
    val bodyType: String = "",
    val faceDescription: String = "",
    val eyeDescription: String = "",
    val hairstyle: String = "",
    val hairColor: String = "",
    val skinTone: String = "",
    val clothing: String = "",
    val shoes: String = "",
    val accessories: String = "",
    val personality: String = "",
    val voiceDescription: String = "",
    val distinctiveFeatures: String = "",
    val referenceImageUri: String? = null,
    val characterReferencePrompt: String = ""
) {
    /**
     * Auto-generates a standardized English character appearance prompt
     * for AI video / image generation consistency.
     */
    fun buildConsistentDescription(): String {
        val details = mutableListOf<String>()
        if (name.isNotBlank()) details.add("Character: $name")
        if (age.isNotBlank()) details.add("$age years old")
        if (genderPresentation.isNotBlank()) details.add(genderPresentation)
        if (height.isNotBlank() || bodyType.isNotBlank()) {
            val build = listOfNotNull(height.takeIf { it.isNotBlank() }, bodyType.takeIf { it.isNotBlank() }).joinToString(", ")
            details.add("Physique: $build")
        }
        if (faceDescription.isNotBlank()) details.add("Facial features: $faceDescription")
        if (eyeDescription.isNotBlank()) details.add("Eyes: $eyeDescription")
        if (hairstyle.isNotBlank() || hairColor.isNotBlank()) {
            val hair = listOfNotNull(hairColor.takeIf { it.isNotBlank() }, hairstyle.takeIf { it.isNotBlank() }).joinToString(" ")
            details.add("Hair: $hair")
        }
        if (skinTone.isNotBlank()) details.add("Skin tone: $skinTone")
        if (clothing.isNotBlank()) details.add("Outfit: $clothing")
        if (shoes.isNotBlank()) details.add("Footwear: $shoes")
        if (accessories.isNotBlank()) details.add("Accessories: $accessories")
        if (distinctiveFeatures.isNotBlank()) details.add("Distinctive traits: $distinctiveFeatures")
        return details.joinToString("; ")
    }
}

data class LocationProfile(
    val id: String = UUID.randomUUID().toString(),
    val projectId: String = "",
    val name: String = "",
    val environmentDescription: String = "",
    val backgroundElements: String = "",
    val props: String = "",
    val lighting: String = "",
    val colorPalette: String = "",
    val weather: String = "",
    val timeOfDay: String = "",
    val visualStyle: String = "",
    val referenceImageUri: String? = null,
    val locationPrompt: String = ""
) {
    /**
     * Auto-generates a standardized English location description prompt
     */
    fun buildConsistentDescription(): String {
        val details = mutableListOf<String>()
        if (name.isNotBlank()) details.add("Setting: $name")
        if (environmentDescription.isNotBlank()) details.add(environmentDescription)
        if (timeOfDay.isNotBlank() || weather.isNotBlank()) {
            val tw = listOfNotNull(timeOfDay.takeIf { it.isNotBlank() }, weather.takeIf { it.isNotBlank() }).joinToString(", ")
            details.add("Atmosphere: $tw")
        }
        if (backgroundElements.isNotBlank()) details.add("Background: $backgroundElements")
        if (props.isNotBlank()) details.add("Props: $props")
        if (lighting.isNotBlank()) details.add("Lighting: $lighting")
        if (colorPalette.isNotBlank()) details.add("Palette: $colorPalette")
        return details.joinToString("; ")
    }
}
