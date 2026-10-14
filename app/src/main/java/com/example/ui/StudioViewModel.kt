package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.GeminiStudioService
import com.example.ai.PromptBuilder
import com.example.ai.PromptGenerationResult
import com.example.data.local.ProjectRepository
import com.example.data.model.CharacterProfile
import com.example.data.model.LocationProfile
import com.example.data.model.Project
import com.example.data.model.SceneItem
import com.example.data.model.ScreenplayItem
import com.example.data.model.StoryDevelopment
import com.example.data.model.VideoPromptVersion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class ScreenDestination {
    object Dashboard : ScreenDestination()
    object ProjectsList : ScreenDestination()
    object CharactersList : ScreenDestination()
    object PromptsList : ScreenDestination()
    object Settings : ScreenDestination()

    // Sub-screens
    data class ProjectForm(val editProjectId: String? = null) : ScreenDestination()
    data class ProjectDetail(val projectId: String, val initialTab: Int = 0) : ScreenDestination()
    data class CharacterForm(val projectId: String, val characterId: String? = null) : ScreenDestination()
    data class LocationForm(val projectId: String, val locationId: String? = null) : ScreenDestination()
    data class SceneForm(val projectId: String, val sceneId: String? = null) : ScreenDestination()
    data class StoryboardForm(val projectId: String, val sceneId: String) : ScreenDestination()
    data class PromptGenerator(val projectId: String, val sceneId: String? = null) : ScreenDestination()
    data class ExportCenter(val projectId: String) : ScreenDestination()
}

enum class ProjectSortOrder {
    DATE_MODIFIED_DESC,
    DATE_CREATED_DESC,
    TITLE_ASC
}

class StudioViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ProjectRepository.getInstance(application)

    // Navigation Stack
    private val _navStack = MutableStateFlow<List<ScreenDestination>>(listOf(ScreenDestination.Dashboard))
    val currentScreen: StateFlow<ScreenDestination> = MutableStateFlow(ScreenDestination.Dashboard)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedGenreFilter = MutableStateFlow("All")
    val selectedGenreFilter: StateFlow<String> = _selectedGenreFilter.asStateFlow()

    private val _sortOrder = MutableStateFlow(ProjectSortOrder.DATE_MODIFIED_DESC)
    val sortOrder: StateFlow<ProjectSortOrder> = _sortOrder.asStateFlow()

    val rawProjects: StateFlow<List<Project>> = repository.allProjects
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredProjects: StateFlow<List<Project>> = combine(
        rawProjects,
        _searchQuery,
        _selectedGenreFilter,
        _sortOrder
    ) { projects, query, genre, sort ->
        var list = projects
        if (query.isNotBlank()) {
            list = list.filter {
                it.title.contains(query, ignoreCase = true) ||
                it.storyIdea.contains(query, ignoreCase = true) ||
                it.genre.contains(query, ignoreCase = true)
            }
        }
        if (genre != "All") {
            list = list.filter { it.genre.startsWith(genre, ignoreCase = true) }
        }
        when (sort) {
            ProjectSortOrder.DATE_MODIFIED_DESC -> list.sortedByDescending { it.updatedAt }
            ProjectSortOrder.DATE_CREATED_DESC -> list.sortedByDescending { it.createdAt }
            ProjectSortOrder.TITLE_ASC -> list.sortedBy { it.title.lowercase() }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeProject: StateFlow<Project?> = repository.activeProject

    // AI State
    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()

    private val _aiMessage = MutableStateFlow<String?>(null)
    val aiMessage: StateFlow<String?> = _aiMessage.asStateFlow()

    // Temporary active generated prompt state
    private val _currentPromptResult = MutableStateFlow<PromptGenerationResult?>(null)
    val currentPromptResult: StateFlow<PromptGenerationResult?> = _currentPromptResult.asStateFlow()

    fun navigateTo(destination: ScreenDestination) {
        val current = _navStack.value
        _navStack.value = current + destination
        (this.currentScreen as MutableStateFlow).value = destination
    }

    fun navigateBack(): Boolean {
        val current = _navStack.value
        if (current.size > 1) {
            val updated = current.dropLast(1)
            _navStack.value = updated
            (this.currentScreen as MutableStateFlow).value = updated.last()
            return true
        }
        return false
    }

    fun switchBottomTab(index: Int) {
        val dest = when (index) {
            0 -> ScreenDestination.Dashboard
            1 -> ScreenDestination.ProjectsList
            2 -> ScreenDestination.CharactersList
            3 -> ScreenDestination.PromptsList
            4 -> ScreenDestination.Settings
            else -> ScreenDestination.Dashboard
        }
        _navStack.value = listOf(dest)
        (this.currentScreen as MutableStateFlow).value = dest
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setGenreFilter(genre: String) {
        _selectedGenreFilter.value = genre
    }

    fun setSortOrder(order: ProjectSortOrder) {
        _sortOrder.value = order
    }

    fun selectActiveProject(project: Project) {
        repository.selectActiveProject(project)
    }

    // Projects CRUD
    fun saveProject(project: Project, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            repository.saveProject(project)
            onComplete()
        }
    }

    fun duplicateProject(project: Project) {
        viewModelScope.launch {
            repository.duplicateProject(project)
        }
    }

    fun deleteProject(projectId: String) {
        viewModelScope.launch {
            repository.deleteProject(projectId)
        }
    }

    // Story Workspace
    fun updateStoryDev(projectId: String, storyDev: StoryDevelopment) {
        viewModelScope.launch {
            repository.updateStoryDevelopment(projectId, storyDev)
        }
    }

    // Characters
    fun saveCharacter(projectId: String, character: CharacterProfile, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            repository.saveCharacter(projectId, character)
            onComplete()
        }
    }

    fun deleteCharacter(projectId: String, characterId: String) {
        viewModelScope.launch {
            repository.deleteCharacter(projectId, characterId)
        }
    }

    // Locations
    fun saveLocation(projectId: String, location: LocationProfile, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            repository.saveLocation(projectId, location)
            onComplete()
        }
    }

    fun deleteLocation(projectId: String, locationId: String) {
        viewModelScope.launch {
            repository.deleteLocation(projectId, locationId)
        }
    }

    // Scenes & Storyboards
    fun saveScene(projectId: String, scene: SceneItem, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            repository.saveScene(projectId, scene)
            onComplete()
        }
    }

    fun deleteScene(projectId: String, sceneId: String) {
        viewModelScope.launch {
            repository.deleteScene(projectId, sceneId)
        }
    }

    fun duplicateScene(projectId: String, sceneId: String) {
        viewModelScope.launch {
            repository.duplicateScene(projectId, sceneId)
        }
    }

    fun moveSceneUp(projectId: String, sceneIndex: Int, scenes: List<SceneItem>) {
        if (sceneIndex <= 0 || sceneIndex >= scenes.size) return
        val mutable = scenes.toMutableList()
        val temp = mutable[sceneIndex]
        mutable[sceneIndex] = mutable[sceneIndex - 1]
        mutable[sceneIndex - 1] = temp
        viewModelScope.launch {
            repository.reorderScenes(projectId, mutable)
        }
    }

    fun moveSceneDown(projectId: String, sceneIndex: Int, scenes: List<SceneItem>) {
        if (sceneIndex < 0 || sceneIndex >= scenes.size - 1) return
        val mutable = scenes.toMutableList()
        val temp = mutable[sceneIndex]
        mutable[sceneIndex] = mutable[sceneIndex + 1]
        mutable[sceneIndex + 1] = temp
        viewModelScope.launch {
            repository.reorderScenes(projectId, mutable)
        }
    }

    // Screenplay
    fun saveScreenplay(projectId: String, screenplay: List<ScreenplayItem>) {
        viewModelScope.launch {
            repository.saveScreenplay(projectId, screenplay)
        }
    }

    // Prompt Generation
    fun generateScenePrompt(project: Project, scene: SceneItem) {
        val result = PromptBuilder.buildScenePrompt(
            project = project,
            scene = scene,
            projectCharacters = project.characters,
            projectLocations = project.locations
        )
        _currentPromptResult.value = result
    }

    fun generateMasterPrompt(project: Project) {
        val result = PromptBuilder.buildMasterProjectPrompt(
            project = project,
            characters = project.characters,
            locations = project.locations,
            scenes = project.scenes
        )
        _currentPromptResult.value = result
    }

    fun savePromptVersion(projectId: String, promptVersion: VideoPromptVersion, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            repository.savePromptVersion(projectId, promptVersion)
            onComplete()
        }
    }

    fun deletePromptVersion(projectId: String, promptId: String) {
        viewModelScope.launch {
            repository.deletePromptVersion(projectId, promptId)
        }
    }

    // AI Assist
    fun expandStoryWithAi(premise: String, genre: String, visualStyle: String, onExpanded: (String) -> Unit) {
        if (!GeminiStudioService.isConfigured()) {
            _aiMessage.value = "Gemini API key မထည့်ရသေးပါ။ Secrets panel တွင် GEMINI_API_KEY ကို ထည့်သွင်းနိုင်ပါသည်။"
            return
        }
        viewModelScope.launch {
            _isAiLoading.value = true
            _aiMessage.value = null
            val result = GeminiStudioService.expandStoryIdeaWithAi(premise, genre, visualStyle)
            _isAiLoading.value = false
            result.onSuccess { text ->
                onExpanded(text)
                _aiMessage.value = "ဇာတ်လမ်းကို AI ဖြင့် ချဲ့ထွင်ပြီးပါပြီ။"
            }.onFailure { err ->
                _aiMessage.value = "AI ချိတ်ဆက်မှု အဆင်မပြေပါ: ${err.message}"
            }
        }
    }

    fun clearAiMessage() {
        _aiMessage.value = null
    }
}
