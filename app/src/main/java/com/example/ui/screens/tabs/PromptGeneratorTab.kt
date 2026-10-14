package com.example.ui.screens.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ai.PromptBuilder
import com.example.ai.PromptGenerationResult
import com.example.data.model.Project
import com.example.data.model.SceneItem
import com.example.data.model.VideoPromptVersion
import com.example.ui.StudioViewModel
import com.example.ui.components.StudioBadge
import com.example.ui.theme.StudioAmber
import com.example.ui.theme.StudioBlue
import com.example.ui.theme.StudioBorder
import com.example.ui.theme.StudioCyan
import com.example.ui.theme.StudioPurple
import com.example.ui.theme.StudioPurpleLight
import com.example.ui.theme.StudioRose
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.StudioSurfaceElevated
import com.example.ui.theme.StudioTextMuted
import com.example.ui.theme.StudioTextPrimary
import com.example.ui.theme.StudioTextSecondary
import com.example.util.ExportHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

@Composable
fun PromptGeneratorTab(
    project: Project,
    initialSceneId: String?,
    viewModel: StudioViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedTargetId by remember(initialSceneId) {
        mutableStateOf(initialSceneId ?: "MASTER")
    }

    // Active working prompt
    var activeEnglishPrompt by remember { mutableStateOf("") }
    var activeBurmeseExplanation by remember { mutableStateOf("") }
    var activeVersionTitle by remember { mutableStateOf("Version 1.0 (Google Flow Ready)") }
    var isEditingPrompt by remember { mutableStateOf(false) }

    var saveSuccessDialog by remember { mutableStateOf(false) }

    // Initialize with first saved prompt or generate on first launch
    remember(project.id) {
        if (project.promptVersions.isNotEmpty() && activeEnglishPrompt.isBlank()) {
            val first = project.promptVersions.first()
            activeEnglishPrompt = first.englishPrompt
            activeBurmeseExplanation = first.burmeseExplanation
            activeVersionTitle = first.versionTitle
        }
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxSize().padding(16.dp)
    ) {
        // Target Selector (Master vs Scenes)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = StudioSurfaceElevated),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "ပရော့မ် ထုတ်လုပ်မည့် အပိုင်း ရွေးချယ်ပါ (Target Scope):",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = StudioTextSecondary
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Master option
                        val isMasterSelected = selectedTargetId == "MASTER"
                        Surface(
                            color = if (isMasterSelected) StudioPurple else StudioSurface,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isMasterSelected) StudioPurpleLight else StudioBorder
                            ),
                            modifier = Modifier
                                .clickable { selectedTargetId = "MASTER" }
                                .testTag("target_scope_master")
                        ) {
                            Text(
                                text = "🌟 Master Project Package",
                                color = if (isMasterSelected) Color.White else StudioTextPrimary,
                                fontWeight = if (isMasterSelected) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                fontSize = 12.sp
                            )
                        }

                        // Scenes options
                        project.scenes.sortedBy { it.sceneNumber }.forEach { scene ->
                            val isSceneSelected = selectedTargetId == scene.id
                            Surface(
                                color = if (isSceneSelected) StudioPurple else StudioSurface,
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSceneSelected) StudioPurpleLight else StudioBorder
                                ),
                                modifier = Modifier
                                    .clickable { selectedTargetId = scene.id }
                                    .testTag("target_scope_scene_${scene.sceneNumber}")
                            ) {
                                Text(
                                    text = "🎬 Scene ${scene.sceneNumber}: ${scene.title.take(15)}",
                                    color = if (isSceneSelected) Color.White else StudioTextPrimary,
                                    fontWeight = if (isSceneSelected) FontWeight.Bold else FontWeight.Normal,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Generate Button
                    Button(
                        onClick = {
                            val result = if (selectedTargetId == "MASTER") {
                                PromptBuilder.buildMasterProjectPrompt(
                                    project = project,
                                    characters = project.characters,
                                    locations = project.locations,
                                    scenes = project.scenes
                                )
                            } else {
                                val matchedScene = project.scenes.firstOrNull { it.id == selectedTargetId }
                                    ?: project.scenes.firstOrNull()
                                if (matchedScene != null) {
                                    PromptBuilder.buildScenePrompt(
                                        project = project,
                                        scene = matchedScene,
                                        projectCharacters = project.characters,
                                        projectLocations = project.locations
                                    )
                                } else {
                                    PromptBuilder.buildMasterProjectPrompt(
                                        project = project,
                                        characters = project.characters,
                                        locations = project.locations,
                                        scenes = project.scenes
                                    )
                                }
                            }
                            activeEnglishPrompt = result.englishPrompt
                            activeBurmeseExplanation = result.burmeseExplanation
                            activeVersionTitle = if (selectedTargetId == "MASTER") {
                                "Master Video Package (${SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())})"
                            } else {
                                val sceneNum = project.scenes.firstOrNull { it.id == selectedTargetId }?.sceneNumber ?: 1
                                "Scene $sceneNum 14-Part Prompt (${SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())})"
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = StudioPurple),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().testTag("assemble_prompt_button")
                    ) {
                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "၁၄ ပိုင်းပါ စနစ်တကျ ဗီဒီယို ပရော့မ် တည်ဆောက်ရန် (Assemble 14-Part Prompt)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        // Active Prompt Display Card
        if (activeEnglishPrompt.isNotBlank()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = StudioSurface),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioPurpleLight.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = activeVersionTitle,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = StudioPurpleLight
                                    )
                                )
                                Text(
                                    text = "Ready for Google Flow / Sora / Runway Gen-3 / Kling",
                                    style = MaterialTheme.typography.labelSmall.copy(color = StudioCyan)
                                )
                            }

                            Row {
                                IconButton(
                                    onClick = { isEditingPrompt = !isEditingPrompt }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "ပြင်ဆင်ရန်",
                                        tint = if (isEditingPrompt) StudioCyan else StudioTextSecondary
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        ExportHelper.copyToClipboard(context, "AI Video Prompt", activeEnglishPrompt)
                                    },
                                    modifier = Modifier.testTag("copy_active_prompt_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "ကူးယူရန်",
                                        tint = StudioTextPrimary
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        ExportHelper.shareText(context, activeVersionTitle, activeEnglishPrompt)
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = "မျှဝေရန်",
                                        tint = StudioTextPrimary
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // English Prompt box or editor
                        if (isEditingPrompt) {
                            OutlinedTextField(
                                value = activeEnglishPrompt,
                                onValueChange = { activeEnglishPrompt = it },
                                label = { Text("English Video Prompt တည်းဖြတ်ရန်") },
                                minLines = 8,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = StudioCyan,
                                    unfocusedBorderColor = StudioBorder,
                                    focusedContainerColor = StudioSurfaceElevated,
                                    unfocusedContainerColor = StudioSurfaceElevated,
                                    focusedTextColor = StudioTextPrimary,
                                    unfocusedTextColor = StudioTextPrimary
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth().testTag("edit_active_prompt_input")
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(StudioSurfaceElevated, RoundedCornerShape(10.dp))
                                    .border(1.dp, StudioBorder, RoundedCornerShape(10.dp))
                                    .padding(14.dp)
                            ) {
                                Text(
                                    text = activeEnglishPrompt,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontFamily = FontFamily.Monospace,
                                        color = StudioTextPrimary,
                                        lineHeight = 18.sp
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Burmese Explanation Box
                        if (activeBurmeseExplanation.isNotBlank()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(StudioPurple.copy(alpha = 0.1f), RoundedCornerShape(10.dp))
                                    .border(1.dp, StudioPurple.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = activeBurmeseExplanation,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = StudioTextSecondary,
                                        lineHeight = 20.sp
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Save as Version Button
                        Button(
                            onClick = {
                                val newVersion = VideoPromptVersion(
                                    projectId = project.id,
                                    sceneId = selectedTargetId.takeIf { it != "MASTER" },
                                    sceneTitle = if (selectedTargetId == "MASTER") "Master Package" else "Scene",
                                    versionTitle = activeVersionTitle,
                                    englishPrompt = activeEnglishPrompt,
                                    burmeseExplanation = activeBurmeseExplanation,
                                    createdAt = System.currentTimeMillis()
                                )
                                viewModel.savePromptVersion(project.id, newVersion)
                                saveSuccessDialog = true
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = StudioCyan),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth().testTag("save_prompt_version_button")
                        ) {
                            Icon(imageVector = Icons.Default.BookmarkBorder, contentDescription = null, tint = Color.Black)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("ပရော့မ် ဗားရှင်းအဖြစ် သိမ်းဆည်းမည် (Save Version)", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Saved Prompt Versions History
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.History, contentDescription = null, tint = StudioAmber, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "သိမ်းဆည်းထားသော ပရော့မ် ဗားရှင်းများ (${project.promptVersions.size})",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                    )
                }
            }
        }

        if (project.promptVersions.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = StudioSurface),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "သိမ်းဆည်းထားသော ပရော့မ် ဗားရှင်း မရှိသေးပါ",
                            style = MaterialTheme.typography.bodySmall.copy(color = StudioTextSecondary)
                        )
                    }
                }
            }
        } else {
            items(project.promptVersions, key = { it.id }) { version ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = StudioSurface),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
                    modifier = Modifier.fillMaxWidth().testTag("saved_prompt_${version.id}")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = version.versionTitle,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = StudioTextPrimary
                                    )
                                )
                                Text(
                                    text = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(version.createdAt)),
                                    style = MaterialTheme.typography.labelSmall.copy(color = StudioTextMuted)
                                )
                            }

                            Row {
                                OutlinedButton(
                                    onClick = {
                                        activeEnglishPrompt = version.englishPrompt
                                        activeBurmeseExplanation = version.burmeseExplanation
                                        activeVersionTitle = version.versionTitle
                                    },
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text("ဖွင့်မည် (Load)", fontSize = 11.sp)
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                IconButton(
                                    onClick = {
                                        ExportHelper.copyToClipboard(context, "Prompt Version", version.englishPrompt)
                                    },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "ကူးရန်", tint = StudioTextSecondary, modifier = Modifier.size(16.dp))
                                }
                                IconButton(
                                    onClick = {
                                        viewModel.deletePromptVersion(project.id, version.id)
                                    },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.DeleteOutline, contentDescription = "ဖျက်ရန်", tint = StudioRose, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (saveSuccessDialog) {
        AlertDialog(
            onDismissRequest = { saveSuccessDialog = false },
            title = { Text("ပရော့မ် ဗားရှင်း သိမ်းဆည်းပြီးပါပြီ", color = StudioTextPrimary, fontWeight = FontWeight.Bold) },
            text = { Text("ဗားရှင်းအသစ်ကို ပရောဂျက်ဒေတာဘေ့စ်တွင် အောင်မြင်စွာ သိမ်းဆည်းထားပြီး ဖြစ်ပါသည်။", color = StudioTextSecondary) },
            confirmButton = {
                Button(
                    onClick = { saveSuccessDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = StudioPurple)
                ) {
                    Text("ကောင်းပါပြီ")
                }
            },
            containerColor = StudioSurfaceElevated
        )
    }
}
