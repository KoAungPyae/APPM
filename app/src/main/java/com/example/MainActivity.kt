package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.ScreenDestination
import com.example.ui.StudioViewModel
import com.example.ui.components.StudioBottomBar
import com.example.ui.screens.CharactersGlobalScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ProjectDetailScreen
import com.example.ui.screens.ProjectFormScreen
import com.example.ui.screens.ProjectsScreen
import com.example.ui.screens.PromptsGlobalScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.StudioBackground
import com.example.ui.theme.VideoStudioTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VideoStudioTheme {
                VideoStudioApp()
            }
        }
    }
}

@Composable
fun VideoStudioApp(viewModel: StudioViewModel = viewModel()) {
    val currentScreen by viewModel.currentScreen.collectAsState()

    val isRootTab = when (currentScreen) {
        is ScreenDestination.Dashboard,
        is ScreenDestination.ProjectsList,
        is ScreenDestination.CharactersList,
        is ScreenDestination.PromptsList,
        is ScreenDestination.Settings -> true
        else -> false
    }

    val selectedTabIndex = when (currentScreen) {
        is ScreenDestination.Dashboard -> 0
        is ScreenDestination.ProjectsList -> 1
        is ScreenDestination.CharactersList -> 2
        is ScreenDestination.PromptsList -> 3
        is ScreenDestination.Settings -> 4
        else -> 0
    }

    BackHandler(enabled = !isRootTab) {
        viewModel.navigateBack()
    }

    Scaffold(
        bottomBar = {
            if (isRootTab) {
                StudioBottomBar(
                    selectedTabIndex = selectedTabIndex,
                    onTabSelected = { index ->
                        viewModel.switchBottomTab(index)
                    }
                )
            }
        },
        containerColor = StudioBackground,
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = if (isRootTab) innerPadding.calculateBottomPadding() else 0.dp)
        ) {
            Crossfade(targetState = currentScreen, label = "ScreenTransition") { screen ->
                when (screen) {
                    is ScreenDestination.Dashboard -> {
                        HomeScreen(viewModel = viewModel)
                    }
                    is ScreenDestination.ProjectsList -> {
                        ProjectsScreen(viewModel = viewModel)
                    }
                    is ScreenDestination.CharactersList -> {
                        CharactersGlobalScreen(viewModel = viewModel)
                    }
                    is ScreenDestination.PromptsList -> {
                        PromptsGlobalScreen(viewModel = viewModel)
                    }
                    is ScreenDestination.Settings -> {
                        SettingsScreen(viewModel = viewModel)
                    }
                    is ScreenDestination.ProjectForm -> {
                        ProjectFormScreen(
                            editProjectId = screen.editProjectId,
                            viewModel = viewModel
                        )
                    }
                    is ScreenDestination.ProjectDetail -> {
                        ProjectDetailScreen(
                            projectId = screen.projectId,
                            initialTab = screen.initialTab,
                            viewModel = viewModel
                        )
                    }
                    else -> {
                        HomeScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }
}
