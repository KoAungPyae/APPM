package com.example.data.model

import java.util.UUID

data class SceneItem(
    val id: String = UUID.randomUUID().toString(),
    val projectId: String = "",
    val sceneNumber: Int = 1,
    val title: String = "",
    val durationSec: Int = 10, // default 10s planning duration
    val locationName: String = "",
    val characterNames: List<String> = emptyList(),
    val openingAction: String = "",
    val mainAction: String = "",
    val characterEmotions: String = "",
    val dialogue: String = "",
    val cameraDirection: String = "",
    val soundEffects: String = "",
    val endingAction: String = "",
    val continuityNotes: String = "",

    // Storyboard detailed parameters
    val openingFrame: String = "",
    val characterPositions: String = "",
    val characterExpressions: String = "",
    val mainMovement: String = "",
    val cameraAngle: String = "",
    val cameraMovement: String = "",
    val backgroundDetails: String = "",
    val lightingDetail: String = "",
    val importantVisualEvent: String = "",
    val finalFrame: String = "",
    val nextSceneTransition: String = "",
    val storyboardImageUri: String? = null
)

data class VideoPromptVersion(
    val id: String = UUID.randomUUID().toString(),
    val projectId: String = "",
    val sceneId: String? = null,
    val sceneTitle: String = "All Scenes / Project Master",
    val versionTitle: String = "Version 1.0 (Master Prompt)",
    val englishPrompt: String = "",
    val burmeseExplanation: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
