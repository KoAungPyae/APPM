package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.ScreenDestination
import com.example.ui.StudioViewModel
import com.example.ui.components.StudioTopBar
import com.example.ui.screens.tabs.CharactersLocationsTab
import com.example.ui.screens.tabs.ExportCenterTab
import com.example.ui.screens.tabs.PromptGeneratorTab
import com.example.ui.screens.tabs.ScenesStoryboardTab
import com.example.ui.screens.tabs.ScreenplayTab
import com.example.ui.screens.tabs.StoryWorkspaceTab
import com.example.ui.theme.StudioBackground
import com.example.ui.theme.StudioPurpleLight
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.StudioTextMuted
import com.example.ui.theme.StudioTextPrimary
import com.example.ui.theme.StudioTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailScreen(
    projectId: String,
    initialTab: Int = 0,
    viewModel: StudioViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler { viewModel.navigateBack() }

    val projects by viewModel.rawProjects.collectAsState()
    val project = projects.firstOrNull { it.id == projectId }

    var selectedTab by remember { mutableIntStateOf(initialTab) }
    var targetPromptSceneId by remember { mutableStateOf<String?>(null) }

    if (project == null) {
        Scaffold(
            topBar = {
                StudioTopBar(
                    title = "ပရောဂျက် မတွေ့ပါ",
                    showBackButton = true,
                    onBackClick = { viewModel.navigateBack() }
                )
            },
            containerColor = StudioBackground
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("ပရောဂျက်ကို ဖျက်လိုက်ပြီး သို့မဟုတ် မရှိတော့ပါ", color = StudioTextSecondary)
            }
        }
        return
    }

    val tabTitles = listOf(
        Triple("ဇာတ်လမ်းစိတ်ကူး", Icons.Default.Lightbulb, "Story Dev"),
        Triple("ဇာတ်ညွှန်း", Icons.Default.Description, "Screenplay"),
        Triple("ဇာတ်ကောင် & နေရာ", Icons.Default.Person, "Characters"),
        Triple("ဇာတ်ဝင်ခန်းများ", Icons.Default.Movie, "Scenes"),
        Triple("ပရော့မ် ဖန်တီးမှု", Icons.Default.AutoAwesome, "Prompts"),
        Triple("ထုတ်ယူမှု စင်တာ", Icons.Default.IosShare, "Export")
    )

    Scaffold(
        topBar = {
            StudioTopBar(
                title = project.title,
                subtitle = "${project.genre.split(" ").first()} • ${project.visualStyle} • ${project.targetDurationSec}s",
                showBackButton = true,
                onBackClick = { viewModel.navigateBack() },
                actions = {
                    IconButton(
                        onClick = { viewModel.navigateTo(ScreenDestination.ProjectForm(project.id)) },
                        modifier = Modifier.testTag("edit_project_specs_icon")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "ပရောဂျက် သတ်မှတ်ချက်များ ပြင်ဆင်ရန်",
                            tint = StudioPurpleLight
                        )
                    }
                }
            )
        },
        containerColor = StudioBackground,
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Scrollable Workspace Tabs
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                containerColor = StudioSurface,
                contentColor = StudioPurpleLight,
                edgePadding = 12.dp,
                indicator = { tabPositions ->
                    SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = StudioPurpleLight
                    )
                }
            ) {
                tabTitles.forEachIndexed { index, (burmese, icon, _) ->
                    val isSelected = selectedTab == index
                    Tab(
                        selected = isSelected,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = burmese,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) StudioPurpleLight else StudioTextMuted
                                )
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = if (isSelected) StudioPurpleLight else StudioTextMuted
                            )
                        },
                        modifier = Modifier.testTag("detail_tab_$index")
                    )
                }
            }

            // Tab Content
            when (selectedTab) {
                0 -> StoryWorkspaceTab(project = project, viewModel = viewModel)
                1 -> ScreenplayTab(project = project, viewModel = viewModel)
                2 -> CharactersLocationsTab(project = project, viewModel = viewModel)
                3 -> ScenesStoryboardTab(
                    project = project,
                    viewModel = viewModel,
                    onNavigateToPromptForScene = { sceneId ->
                        targetPromptSceneId = sceneId
                        selectedTab = 4 // Jump to Prompts Tab
                    }
                )
                4 -> PromptGeneratorTab(
                    project = project,
                    initialSceneId = targetPromptSceneId,
                    viewModel = viewModel
                )
                5 -> ExportCenterTab(project = project, viewModel = viewModel)
            }
        }
    }
}
