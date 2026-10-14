package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.CharacterProfile
import com.example.data.model.LocationProfile
import com.example.data.model.Project
import com.example.data.model.SceneItem
import com.example.data.model.ScreenplayItem
import com.example.data.model.StoryDevelopment
import com.example.data.model.VideoPromptVersion

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey val id: String,
    val title: String,
    val genre: String,
    val storyIdea: String,
    val targetAudience: String,
    val targetDurationSec: Int,
    val visualStyle: String,
    val customStyleNotes: String,
    val platform: String,
    val aspectRatio: String,
    val dialogueLanguage: String,
    val notes: String,
    val createdAt: Long,
    val updatedAt: Long,
    val storyDevelopment: StoryDevelopment,
    val screenplay: List<ScreenplayItem>,
    val characters: List<CharacterProfile>,
    val locations: List<LocationProfile>,
    val scenes: List<SceneItem>,
    val promptVersions: List<VideoPromptVersion>
) {
    fun toDomain(): Project = Project(
        id = id,
        title = title,
        genre = genre,
        storyIdea = storyIdea,
        targetAudience = targetAudience,
        targetDurationSec = targetDurationSec,
        visualStyle = visualStyle,
        customStyleNotes = customStyleNotes,
        platform = platform,
        aspectRatio = aspectRatio,
        dialogueLanguage = dialogueLanguage,
        notes = notes,
        createdAt = createdAt,
        updatedAt = updatedAt,
        storyDevelopment = storyDevelopment,
        screenplay = screenplay,
        characters = characters,
        locations = locations,
        scenes = scenes,
        promptVersions = promptVersions
    )

    companion object {
        fun fromDomain(p: Project): ProjectEntity = ProjectEntity(
            id = p.id,
            title = p.title,
            genre = p.genre,
            storyIdea = p.storyIdea,
            targetAudience = p.targetAudience,
            targetDurationSec = p.targetDurationSec,
            visualStyle = p.visualStyle,
            customStyleNotes = p.customStyleNotes,
            platform = p.platform,
            aspectRatio = p.aspectRatio,
            dialogueLanguage = p.dialogueLanguage,
            notes = p.notes,
            createdAt = p.createdAt,
            updatedAt = p.updatedAt,
            storyDevelopment = p.storyDevelopment,
            screenplay = p.screenplay,
            characters = p.characters,
            locations = p.locations,
            scenes = p.scenes,
            promptVersions = p.promptVersions
        )
    }
}
