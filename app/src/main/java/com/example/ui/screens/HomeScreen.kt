package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Project
import com.example.ui.ScreenDestination
import com.example.ui.StudioViewModel
import com.example.ui.components.StudioBadge
import com.example.ui.components.StudioTopBar
import com.example.ui.theme.StudioAmber
import com.example.ui.theme.StudioBackground
import com.example.ui.theme.StudioBlue
import com.example.ui.theme.StudioBorder
import com.example.ui.theme.StudioCyan
import com.example.ui.theme.StudioPurple
import com.example.ui.theme.StudioPurpleDark
import com.example.ui.theme.StudioPurpleLight
import com.example.ui.theme.StudioRose
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.StudioSurfaceElevated
import com.example.ui.theme.StudioSurfaceVariant
import com.example.ui.theme.StudioTextMuted
import com.example.ui.theme.StudioTextPrimary
import com.example.ui.theme.StudioTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: StudioViewModel,
    modifier: Modifier = Modifier
) {
    val projects by viewModel.rawProjects.collectAsState()
    var projectToDelete by remember { mutableStateOf<Project?>(null) }

    val totalProjects = projects.size
    val totalCharacters = projects.sumOf { it.characters.size }
    val totalScenes = projects.sumOf { it.scenes.size }
    val totalPrompts = projects.sumOf { it.promptVersions.size }

    Scaffold(
        topBar = {
            StudioTopBar(
                title = "AI Video Studio",
                subtitle = "ဇာတ်လမ်းမှ ဗီဒီယိုသို့ — Story to Video",
                showBackButton = false,
                actions = {
                    IconButton(
                        onClick = { viewModel.navigateTo(ScreenDestination.ProjectForm()) },
                        modifier = Modifier.testTag("home_create_project_icon")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "ပရောဂျက်အသစ် ဖန်တီးရန်",
                            tint = StudioPurpleLight
                        )
                    }
                }
            )
        },
        containerColor = StudioBackground,
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = innerPadding.calculateTopPadding() + 8.dp,
                bottom = innerPadding.calculateBottomPadding() + 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // Hero Studio Banner
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = StudioSurfaceVariant),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioPurple.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        StudioPurpleDark.copy(alpha = 0.6f),
                                        StudioSurfaceElevated,
                                        StudioSurface
                                    )
                                )
                            )
                            .padding(20.dp)
                    ) {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .background(StudioPurple.copy(alpha = 0.25f), CircleShape)
                                        .border(1.dp, StudioPurpleLight, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Movie,
                                        contentDescription = null,
                                        tint = StudioPurpleLight,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "AI ဗီဒီယို စတူဒီယို",
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = StudioTextPrimary
                                        )
                                    )
                                    Text(
                                        text = "Professional AI Video Pre-Production",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = StudioCyan
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = "ဇာတ်လမ်းစိတ်ကူးမှသည် Google Flow နှင့် ဗီဒီယို မော်ဒယ်များအတွက် အသင့်သုံး ၁၄ ပိုင်းပါ ပရော့မ်များအထိ စနစ်တကျ ဖန်တီးစီမံပါ။",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = StudioTextSecondary,
                                    lineHeight = 20.sp
                                )
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { viewModel.navigateTo(ScreenDestination.ProjectForm()) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = StudioPurple,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("create_new_project_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "ပရောဂျက် အသစ်ဖန်တီးရန် (New Project)",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Stats Row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        count = totalProjects.toString(),
                        label = "ပရောဂျက်များ",
                        sub = "Projects",
                        accentColor = StudioPurpleLight,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        count = totalCharacters.toString(),
                        label = "ဇာတ်ကောင်များ",
                        sub = "Characters",
                        accentColor = StudioBlue,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        count = totalScenes.toString(),
                        label = "ဇာတ်ဝင်ခန်း",
                        sub = "Scenes",
                        accentColor = StudioCyan,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        count = totalPrompts.toString(),
                        label = "ပရော့မ်များ",
                        sub = "Prompts",
                        accentColor = StudioAmber,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Quick Studio Hub
            item {
                Text(
                    text = "လုပ်ငန်းသုံး ကဏ္ဍများ (Studio Hub)",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = StudioTextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickActionCard(
                        title = "ဇာတ်ကောင် စာကြည့်တိုက်",
                        sub = "Character Library",
                        icon = Icons.Default.Person,
                        accentColor = StudioBlue,
                        onClick = { viewModel.navigateTo(ScreenDestination.CharactersList) },
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionCard(
                        title = "ဗီဒီယို ပရော့မ်များ",
                        sub = "Prompt Generator",
                        icon = Icons.Default.AutoAwesome,
                        accentColor = StudioPurpleLight,
                        onClick = { viewModel.navigateTo(ScreenDestination.PromptsList) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Recent Projects Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "လက်တလော ပရောဂျက်များ (Recent Projects)",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                    )
                    TextButton(
                        onClick = { viewModel.switchBottomTab(1) }
                    ) {
                        Text(
                            text = "အားလုံးကြည့်မည် (${projects.size})",
                            color = StudioPurpleLight,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }

            // Recent Projects List
            if (projects.isEmpty()) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = StudioSurface),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.VideoLibrary,
                                contentDescription = null,
                                tint = StudioTextMuted,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "ပရောဂျက် မရှိသေးပါ",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = StudioTextPrimary
                                )
                            )
                            Text(
                                text = "ဗီဒီယို စိတ်ကူးသစ်တစ်ခု စတင်ဖန်တီးလိုက်ပါ",
                                style = MaterialTheme.typography.bodySmall.copy(color = StudioTextSecondary)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { viewModel.navigateTo(ScreenDestination.ProjectForm()) },
                                colors = ButtonDefaults.buttonColors(containerColor = StudioPurple)
                            ) {
                                Text("စတင်ဖန်တီးရန်")
                            }
                        }
                    }
                }
            } else {
                items(projects.take(5), key = { it.id }) { project ->
                    ProjectCardItem(
                        project = project,
                        onOpen = {
                            viewModel.selectActiveProject(project)
                            viewModel.navigateTo(ScreenDestination.ProjectDetail(project.id))
                        },
                        onDuplicate = { viewModel.duplicateProject(project) },
                        onDelete = { projectToDelete = project }
                    )
                }
            }
        }
    }

    // Delete Confirmation Dialog
    if (projectToDelete != null) {
        val p = projectToDelete!!
        AlertDialog(
            onDismissRequest = { projectToDelete = null },
            title = {
                Text(
                    text = "ပရောဂျက် ဖျက်မည်လား?",
                    color = StudioTextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "\"${p.title}\" နှင့် ၎င်းတွင် ပါဝင်သော ဇာတ်ကောင်၊ ဇာတ်ဝင်ခန်း၊ ပရော့မ် အားလုံးကို အပြီးတိုင် ဖျက်ပစ်ပါမည်။",
                    color = StudioTextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteProject(p.id)
                        projectToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = StudioRose)
                ) {
                    Text("ဖျက်မည်")
                }
            },
            dismissButton = {
                TextButton(onClick = { projectToDelete = null }) {
                    Text("မဖျက်တော့ပါ", color = StudioTextSecondary)
                }
            },
            containerColor = StudioSurfaceElevated
        )
    }
}

@Composable
fun StatCard(
    count: String,
    label: String,
    sub: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = StudioSurface),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = count,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = accentColor
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = StudioTextPrimary,
                    fontSize = 10.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = sub,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = StudioTextMuted,
                    fontSize = 9.sp
                )
            )
        }
    }
}

@Composable
fun QuickActionCard(
    title: String,
    sub: String,
    icon: ImageVector,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = StudioSurfaceElevated),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, accentColor.copy(alpha = 0.3f)),
        modifier = modifier
            .clickable { onClick() }
            .testTag("quick_action_${sub.lowercase().replace(" ", "_")}")
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(accentColor.copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = StudioTextPrimary
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = sub,
                    style = MaterialTheme.typography.labelSmall.copy(color = StudioTextSecondary),
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun ProjectCardItem(
    project: Project,
    onOpen: () -> Unit,
    onDuplicate: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = StudioSurface),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onOpen() }
            .testTag("project_card_${project.id}")
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = project.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = project.storyIdea.ifBlank { "ဇာတ်လမ်းစိတ်ကူး မထည့်ရသေးပါ" },
                        style = MaterialTheme.typography.bodySmall.copy(color = StudioTextSecondary),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row {
                    IconButton(
                        onClick = onDuplicate,
                        modifier = Modifier.size(36.dp).testTag("duplicate_project_${project.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "မိတ္တူကူးရန်",
                            tint = StudioTextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(36.dp).testTag("delete_project_${project.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "ဖျက်ရန်",
                            tint = StudioRose.copy(alpha = 0.8f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Badges row
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StudioBadge(text = project.genre.split(" ").first(), accentColor = StudioPurpleLight)
                StudioBadge(text = project.aspectRatio, accentColor = StudioCyan)
                StudioBadge(text = "${project.targetDurationSec}s", accentColor = StudioAmber)
                StudioBadge(text = "${project.scenes.size} ဇာတ်ဝင်ခန်း", accentColor = StudioBlue)
            }
        }
    }
}
