package com.example.data.local

import android.content.Context
import com.example.data.model.CharacterProfile
import com.example.data.model.DefaultTemplates
import com.example.data.model.LocationProfile
import com.example.data.model.Project
import com.example.data.model.SceneItem
import com.example.data.model.ScreenplayItem
import com.example.data.model.StoryDevelopment
import com.example.data.model.VideoPromptVersion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class ProjectRepository(
    private val projectDao: ProjectDao,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) {

    private val _activeProject = MutableStateFlow<Project?>(null)
    val activeProject: StateFlow<Project?> = _activeProject.asStateFlow()

    val allProjects: Flow<List<Project>> = projectDao.getAllProjectsFlow().map { list ->
        list.map { it.toDomain() }
    }

    init {
        scope.launch {
            seedSampleIfEmpty()
        }
    }

    suspend fun seedSampleIfEmpty() = withContext(Dispatchers.IO) {
        val count = projectDao.getProjectCount()
        if (count == 0) {
            val sample = DefaultTemplates.createSampleProject()
            projectDao.insertProject(ProjectEntity.fromDomain(sample))
            _activeProject.value = sample
        } else {
            // Load the most recently updated project as active project if none selected
            val list = projectDao.getAllProjectsFlow()
            // We'll let UI observe or select
        }
    }

    fun selectActiveProject(project: Project) {
        _activeProject.value = project
    }

    suspend fun getProjectById(id: String): Project? = withContext(Dispatchers.IO) {
        projectDao.getProjectById(id)?.toDomain()
    }

    suspend fun saveProject(project: Project) = withContext(Dispatchers.IO) {
        val updated = project.copy(updatedAt = System.currentTimeMillis())
        projectDao.insertProject(ProjectEntity.fromDomain(updated))
        if (_activeProject.value?.id == project.id) {
            _activeProject.value = updated
        }
    }

    suspend fun duplicateProject(project: Project) = withContext(Dispatchers.IO) {
        val newId = UUID.randomUUID().toString()
        val duplicated = project.copy(
            id = newId,
            title = "${project.title} (မိတ္တူ)",
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            characters = project.characters.map { it.copy(id = UUID.randomUUID().toString(), projectId = newId) },
            locations = project.locations.map { it.copy(id = UUID.randomUUID().toString(), projectId = newId) },
            scenes = project.scenes.map { it.copy(id = UUID.randomUUID().toString(), projectId = newId) },
            screenplay = project.screenplay.map { it.copy(id = UUID.randomUUID().toString()) },
            promptVersions = project.promptVersions.map { it.copy(id = UUID.randomUUID().toString(), projectId = newId) }
        )
        projectDao.insertProject(ProjectEntity.fromDomain(duplicated))
        _activeProject.value = duplicated
    }

    suspend fun deleteProject(projectId: String) = withContext(Dispatchers.IO) {
        projectDao.deleteProjectById(projectId)
        if (_activeProject.value?.id == projectId) {
            _activeProject.value = null
        }
    }

    // Helper updates on active project
    suspend fun updateStoryDevelopment(projectId: String, storyDev: StoryDevelopment) = withContext(Dispatchers.IO) {
        val current = projectDao.getProjectById(projectId)?.toDomain() ?: return@withContext
        val updated = current.copy(storyDevelopment = storyDev, updatedAt = System.currentTimeMillis())
        saveProject(updated)
    }

    suspend fun saveCharacter(projectId: String, character: CharacterProfile) = withContext(Dispatchers.IO) {
        val current = projectDao.getProjectById(projectId)?.toDomain() ?: return@withContext
        val existingIndex = current.characters.indexOfFirst { it.id == character.id }
        val updatedCharacters = if (existingIndex >= 0) {
            current.characters.toMutableList().apply { set(existingIndex, character) }
        } else {
            current.characters + character
        }
        val updated = current.copy(characters = updatedCharacters, updatedAt = System.currentTimeMillis())
        saveProject(updated)
    }

    suspend fun deleteCharacter(projectId: String, characterId: String) = withContext(Dispatchers.IO) {
        val current = projectDao.getProjectById(projectId)?.toDomain() ?: return@withContext
        val updated = current.copy(
            characters = current.characters.filterNot { it.id == characterId },
            updatedAt = System.currentTimeMillis()
        )
        saveProject(updated)
    }

    suspend fun saveLocation(projectId: String, location: LocationProfile) = withContext(Dispatchers.IO) {
        val current = projectDao.getProjectById(projectId)?.toDomain() ?: return@withContext
        val existingIndex = current.locations.indexOfFirst { it.id == location.id }
        val updatedLocations = if (existingIndex >= 0) {
            current.locations.toMutableList().apply { set(existingIndex, location) }
        } else {
            current.locations + location
        }
        val updated = current.copy(locations = updatedLocations, updatedAt = System.currentTimeMillis())
        saveProject(updated)
    }

    suspend fun deleteLocation(projectId: String, locationId: String) = withContext(Dispatchers.IO) {
        val current = projectDao.getProjectById(projectId)?.toDomain() ?: return@withContext
        val updated = current.copy(
            locations = current.locations.filterNot { it.id == locationId },
            updatedAt = System.currentTimeMillis()
        )
        saveProject(updated)
    }

    suspend fun saveScene(projectId: String, scene: SceneItem) = withContext(Dispatchers.IO) {
        val current = projectDao.getProjectById(projectId)?.toDomain() ?: return@withContext
        val existingIndex = current.scenes.indexOfFirst { it.id == scene.id }
        val updatedScenes = if (existingIndex >= 0) {
            current.scenes.toMutableList().apply { set(existingIndex, scene) }
        } else {
            current.scenes + scene.copy(sceneNumber = current.scenes.size + 1)
        }
        val updated = current.copy(scenes = updatedScenes, updatedAt = System.currentTimeMillis())
        saveProject(updated)
    }

    suspend fun deleteScene(projectId: String, sceneId: String) = withContext(Dispatchers.IO) {
        val current = projectDao.getProjectById(projectId)?.toDomain() ?: return@withContext
        val filtered = current.scenes.filterNot { it.id == sceneId }
        val renumbered = filtered.mapIndexed { index, s -> s.copy(sceneNumber = index + 1) }
        val updated = current.copy(scenes = renumbered, updatedAt = System.currentTimeMillis())
        saveProject(updated)
    }

    suspend fun duplicateScene(projectId: String, sceneId: String) = withContext(Dispatchers.IO) {
        val current = projectDao.getProjectById(projectId)?.toDomain() ?: return@withContext
        val target = current.scenes.firstOrNull { it.id == sceneId } ?: return@withContext
        val copyScene = target.copy(
            id = UUID.randomUUID().toString(),
            sceneNumber = current.scenes.size + 1,
            title = "${target.title} (မိတ္တူ)"
        )
        val updated = current.copy(scenes = current.scenes + copyScene, updatedAt = System.currentTimeMillis())
        saveProject(updated)
    }

    suspend fun reorderScenes(projectId: String, scenes: List<SceneItem>) = withContext(Dispatchers.IO) {
        val current = projectDao.getProjectById(projectId)?.toDomain() ?: return@withContext
        val renumbered = scenes.mapIndexed { index, s -> s.copy(sceneNumber = index + 1) }
        val updated = current.copy(scenes = renumbered, updatedAt = System.currentTimeMillis())
        saveProject(updated)
    }

    suspend fun saveScreenplay(projectId: String, screenplay: List<ScreenplayItem>) = withContext(Dispatchers.IO) {
        val current = projectDao.getProjectById(projectId)?.toDomain() ?: return@withContext
        val updated = current.copy(screenplay = screenplay, updatedAt = System.currentTimeMillis())
        saveProject(updated)
    }

    suspend fun savePromptVersion(projectId: String, promptVersion: VideoPromptVersion) = withContext(Dispatchers.IO) {
        val current = projectDao.getProjectById(projectId)?.toDomain() ?: return@withContext
        val updated = current.copy(
            promptVersions = listOf(promptVersion) + current.promptVersions,
            updatedAt = System.currentTimeMillis()
        )
        saveProject(updated)
    }

    suspend fun deletePromptVersion(projectId: String, promptId: String) = withContext(Dispatchers.IO) {
        val current = projectDao.getProjectById(projectId)?.toDomain() ?: return@withContext
        val updated = current.copy(
            promptVersions = current.promptVersions.filterNot { it.id == promptId },
            updatedAt = System.currentTimeMillis()
        )
        saveProject(updated)
    }

    companion object {
        @Volatile
        private var INSTANCE: ProjectRepository? = null

        fun getInstance(context: Context): ProjectRepository {
            return INSTANCE ?: synchronized(this) {
                val db = AppDatabase.getInstance(context)
                val instance = ProjectRepository(db.projectDao())
                INSTANCE = instance
                instance
            }
        }
    }
}
