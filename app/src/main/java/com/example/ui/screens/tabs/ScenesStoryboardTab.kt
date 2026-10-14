package com.example.ui.screens.tabs

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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Photo
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.Project
import com.example.data.model.SceneItem
import com.example.ui.ScreenDestination
import com.example.ui.StudioViewModel
import com.example.ui.components.DurationWarningBadge
import com.example.ui.components.ImagePickerBox
import com.example.ui.components.StudioBadge
import com.example.ui.theme.StudioAmber
import com.example.ui.theme.StudioBackground
import com.example.ui.theme.StudioBlue
import com.example.ui.theme.StudioBorder
import com.example.ui.theme.StudioCyan
import com.example.ui.theme.StudioPurple
import com.example.ui.theme.StudioPurpleLight
import com.example.ui.theme.StudioRose
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.StudioSurfaceElevated
import com.example.ui.theme.StudioSurfaceVariant
import com.example.ui.theme.StudioTextMuted
import com.example.ui.theme.StudioTextPrimary
import com.example.ui.theme.StudioTextSecondary

@Composable
fun ScenesStoryboardTab(
    project: Project,
    viewModel: StudioViewModel,
    onNavigateToPromptForScene: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val estimatedTotalSec = project.scenes.sumOf { it.durationSec }

    var editingSceneBreakdown by remember { mutableStateOf<SceneItem?>(null) }
    var isNewScene by remember { mutableStateOf(false) }

    var editingStoryboard by remember { mutableStateOf<SceneItem?>(null) }

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        // Duration Calculation and Target Warning Header
        Card(
            colors = CardDefaults.cardColors(containerColor = StudioSurfaceElevated),
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "ခန့်မှန်းခြေ ကြာချိန် (Total Estimated): ${estimatedTotalSec} စက္ကန့်",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = StudioTextPrimary
                            )
                        )
                        Text(
                            text = "ပရောဂျက် ပစ်မှတ်: ${project.targetDurationSec} စက္ကန့် (${project.scenes.size} ဇာတ်ဝင်ခန်း)",
                            style = MaterialTheme.typography.labelSmall.copy(color = StudioTextSecondary)
                        )
                    }

                    DurationWarningBadge(
                        plannedSec = estimatedTotalSec,
                        targetSec = project.targetDurationSec
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Actions Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "ဇာတ်ဝင်ခန်းများ (Chronological Scenes)",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = StudioTextPrimary
                )
            )

            Button(
                onClick = {
                    editingSceneBreakdown = SceneItem(
                        projectId = project.id,
                        sceneNumber = project.scenes.size + 1,
                        durationSec = 10,
                        title = "ဇာတ်ဝင်ခန်း ${project.scenes.size + 1}"
                    )
                    isNewScene = true
                },
                colors = ButtonDefaults.buttonColors(containerColor = StudioPurple),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("add_blank_scene_button")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("ဇာတ်ဝင်ခန်းသစ်", fontSize = 11.sp)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (project.scenes.isEmpty()) {
            EmptyPlaceholder(
                icon = Icons.Default.Movie,
                title = "ဇာတ်ဝင်ခန်း မရှိသေးပါ",
                desc = "ဇာတ်ဝင်ခန်းအသစ် စတင်ထည့်သွင်းပြီး အချိန်ကာလ (Duration)၊ လှုပ်ရှားမှု၊ ကင်မရာနှင့် စတိုရီဘုတ်များကို သတ်မှတ်ပါ"
            )
        } else {
            val sortedScenes = project.scenes.sortedBy { it.sceneNumber }
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                itemsIndexed(sortedScenes, key = { _, s -> s.id }) { index, scene ->
                    SceneBreakdownCard(
                        scene = scene,
                        index = index,
                        totalScenes = sortedScenes.size,
                        onMoveUp = { viewModel.moveSceneUp(project.id, index, sortedScenes) },
                        onMoveDown = { viewModel.moveSceneDown(project.id, index, sortedScenes) },
                        onEditBreakdown = {
                            editingSceneBreakdown = scene
                            isNewScene = false
                        },
                        onEditStoryboard = { editingStoryboard = scene },
                        onDuplicate = { viewModel.duplicateScene(project.id, scene.id) },
                        onDelete = { viewModel.deleteScene(project.id, scene.id) },
                        onGeneratePrompt = { onNavigateToPromptForScene(scene.id) }
                    )
                }
            }
        }
    }

    // Scene Breakdown Editor Dialog
    if (editingSceneBreakdown != null) {
        SceneBreakdownEditorDialog(
            scene = editingSceneBreakdown!!,
            project = project,
            isNew = isNewScene,
            onDismiss = { editingSceneBreakdown = null },
            onSave = { updated ->
                viewModel.saveScene(project.id, updated)
                editingSceneBreakdown = null
            }
        )
    }

    // Storyboard Editor Dialog
    if (editingStoryboard != null) {
        StoryboardEditorDialog(
            scene = editingStoryboard!!,
            onDismiss = { editingStoryboard = null },
            onSave = { updated ->
                viewModel.saveScene(project.id, updated)
                editingStoryboard = null
            }
        )
    }
}

@Composable
fun SceneBreakdownCard(
    scene: SceneItem,
    index: Int,
    totalScenes: Int,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onEditBreakdown: () -> Unit,
    onEditStoryboard: () -> Unit,
    onDuplicate: () -> Unit,
    onDelete: () -> Unit,
    onGeneratePrompt: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = StudioSurface),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
        modifier = Modifier.fillMaxWidth().testTag("scene_card_${scene.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row: Scene # & Duration & Reorder controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .background(StudioPurple.copy(alpha = 0.25f), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${scene.sceneNumber}",
                            color = StudioPurpleLight,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = scene.title.ifBlank { "ဇာတ်ဝင်ခန်း ${scene.sceneNumber}" },
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Reorder & Action buttons
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onMoveUp,
                        enabled = index > 0,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowUpward,
                            contentDescription = "အပေါ်သို့",
                            tint = if (index > 0) StudioTextPrimary else StudioTextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    IconButton(
                        onClick = onMoveDown,
                        enabled = index < totalScenes - 1,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowDownward,
                            contentDescription = "အောက်သို့",
                            tint = if (index < totalScenes - 1) StudioTextPrimary else StudioTextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    IconButton(
                        onClick = onDuplicate,
                        modifier = Modifier.size(28.dp).testTag("duplicate_scene_${scene.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "မိတ္တူကူးရန်",
                            tint = StudioTextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(28.dp).testTag("delete_scene_${scene.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "ဖျက်ရန်",
                            tint = StudioRose,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Badges Row: Duration, Location, Characters
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StudioBadge(text = "${scene.durationSec}s ကြာချိန်", accentColor = StudioAmber)
                if (scene.locationName.isNotBlank()) {
                    StudioBadge(text = scene.locationName.take(18), accentColor = StudioBlue)
                }
                if (scene.characterNames.isNotEmpty()) {
                    StudioBadge(text = scene.characterNames.joinToString(", ").take(20), accentColor = StudioPurpleLight)
                }
            }

            // Main Action Snippet
            if (scene.mainAction.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "အဓိက လှုပ်ရှားမှု: ${scene.mainAction}",
                    style = MaterialTheme.typography.bodySmall.copy(color = StudioTextSecondary),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Camera Direction
            if (scene.cameraDirection.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "ကင်မရာ ရိုက်ချက်: ${scene.cameraDirection}",
                    style = MaterialTheme.typography.labelSmall.copy(color = StudioCyan),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Storyboard Thumbnail preview if image attached
            if (!scene.storyboardImageUri.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                AsyncImage(
                    model = scene.storyboardImageUri,
                    contentDescription = "Storyboard Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, StudioBorder, RoundedCornerShape(8.dp))
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Buttons: Edit Breakdown | Storyboard | Generate Prompt
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onEditBreakdown,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f).testTag("edit_breakdown_${scene.id}")
                ) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Breakdown", fontSize = 11.sp)
                }

                OutlinedButton(
                    onClick = onEditStoryboard,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = StudioCyan),
                    modifier = Modifier.weight(1f).testTag("edit_storyboard_${scene.id}")
                ) {
                    Icon(imageVector = Icons.Default.Image, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("စတိုရီဘုတ်", fontSize = 11.sp)
                }

                Button(
                    onClick = onGeneratePrompt,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = StudioPurple),
                    modifier = Modifier.weight(1.2f).testTag("generate_scene_prompt_${scene.id}")
                ) {
                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("ပရော့မ် ရယူရန်", fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
fun SceneBreakdownEditorDialog(
    scene: SceneItem,
    project: Project,
    isNew: Boolean,
    onDismiss: () -> Unit,
    onSave: (SceneItem) -> Unit
) {
    var title by remember { mutableStateOf(scene.title) }
    var durationSec by remember { mutableFloatStateOf(scene.durationSec.toFloat()) }
    var locationName by remember { mutableStateOf(scene.locationName) }
    var selectedCharacters by remember { mutableStateOf(scene.characterNames) }
    var openingAction by remember { mutableStateOf(scene.openingAction) }
    var mainAction by remember { mutableStateOf(scene.mainAction) }
    var emotions by remember { mutableStateOf(scene.characterEmotions) }
    var dialogue by remember { mutableStateOf(scene.dialogue) }
    var camera by remember { mutableStateOf(scene.cameraDirection) }
    var soundEffects by remember { mutableStateOf(scene.soundEffects) }
    var endingAction by remember { mutableStateOf(scene.endingAction) }
    var continuity by remember { mutableStateOf(scene.continuityNotes) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isNew) "ဇာတ်ဝင်ခန်း အသစ် ထည့်သွင်းရန်" else "ဇာတ်ဝင်ခန်း ပြင်ဆင်ရန် (Scene Breakdown)",
                color = StudioTextPrimary,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("ဇာတ်ဝင်ခန်း အမည် (Scene Title) *") },
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().testTag("scene_title_input")
                    )
                }
                item {
                    Text(
                        text = "ကြာချိန်: ${durationSec.toInt()} စက္ကန့် (Seconds)",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = StudioAmber,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    androidx.compose.material3.Slider(
                        value = durationSec,
                        onValueChange = { durationSec = it },
                        valueRange = 3f..30f,
                        steps = 26
                    )
                }
                item {
                    OutlinedTextField(
                        value = locationName,
                        onValueChange = { locationName = it },
                        label = { Text("နေရာ (Location)") },
                        placeholder = { Text("စာကြည့်တိုက်မှ ရွေးနိုင် သို့မဟုတ် အသစ်ရိုက်ထည့်ပါ") },
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().testTag("scene_location_input")
                    )
                }
                // Quick character selection chips
                if (project.characters.isNotEmpty()) {
                    item {
                        Text("ပါဝင်မည့် ဇာတ်ကောင်များ ရွေးပါ (Select Characters):", style = MaterialTheme.typography.labelSmall.copy(color = StudioTextSecondary))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.padding(top = 4.dp)) {
                            project.characters.forEach { char ->
                                val isSelected = selectedCharacters.contains(char.name)
                                Surface(
                                    color = if (isSelected) StudioPurple else StudioSurface,
                                    shape = RoundedCornerShape(12.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) StudioPurpleLight else StudioBorder),
                                    modifier = Modifier.clickable {
                                        selectedCharacters = if (isSelected) {
                                            selectedCharacters - char.name
                                        } else {
                                            selectedCharacters + char.name
                                        }
                                    }
                                ) {
                                    Text(
                                        text = char.name,
                                        color = if (isSelected) Color.White else StudioTextSecondary,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
                item {
                    OutlinedTextField(
                        value = openingAction,
                        onValueChange = { openingAction = it },
                        label = { Text("Opening Action (အစပြု လှုပ်ရှားမှု)") },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = mainAction,
                        onValueChange = { mainAction = it },
                        label = { Text("Main Action (အဓိက ဖြစ်ရပ်/လှုပ်ရှားမှု) *") },
                        minLines = 2,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().testTag("scene_main_action_input")
                    )
                }
                item {
                    OutlinedTextField(
                        value = emotions,
                        onValueChange = { emotions = it },
                        label = { Text("Character Emotions (ခံစားချက်/မျက်နှာအမူအရာ)") },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = dialogue,
                        onValueChange = { dialogue = it },
                        label = { Text("Dialogue (ပြောစကား)") },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = camera,
                        onValueChange = { camera = it },
                        label = { Text("Camera Direction (ကင်မရာ ရိုက်ချက်/ထောင့်ချိုး)") },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = soundEffects,
                        onValueChange = { soundEffects = it },
                        label = { Text("Sound Effects & Audio (SFX)") },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = endingAction,
                        onValueChange = { endingAction = it },
                        label = { Text("Ending Action (အဆုံးသတ် လှုပ်ရှားမှု)") },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = continuity,
                        onValueChange = { continuity = it },
                        label = { Text("Continuity Notes (ဆက်စပ်မှု သတိပေးချက်)") },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val updated = scene.copy(
                        title = title.trim(),
                        durationSec = durationSec.toInt(),
                        locationName = locationName.trim(),
                        characterNames = selectedCharacters,
                        openingAction = openingAction.trim(),
                        mainAction = mainAction.trim(),
                        characterEmotions = emotions.trim(),
                        dialogue = dialogue.trim(),
                        cameraDirection = camera.trim(),
                        soundEffects = soundEffects.trim(),
                        endingAction = endingAction.trim(),
                        continuityNotes = continuity.trim()
                    )
                    onSave(updated)
                },
                colors = ButtonDefaults.buttonColors(containerColor = StudioPurple),
                modifier = Modifier.testTag("save_scene_dialog_button")
            ) {
                Text("သိမ်းဆည်းမည်")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("မလုပ်တော့ပါ", color = StudioTextSecondary)
            }
        },
        containerColor = StudioSurfaceElevated
    )
}

@Composable
fun StoryboardEditorDialog(
    scene: SceneItem,
    onDismiss: () -> Unit,
    onSave: (SceneItem) -> Unit
) {
    var openingFrame by remember { mutableStateOf(scene.openingFrame) }
    var positions by remember { mutableStateOf(scene.characterPositions) }
    var expressions by remember { mutableStateOf(scene.characterExpressions) }
    var movement by remember { mutableStateOf(scene.mainMovement) }
    var angle by remember { mutableStateOf(scene.cameraAngle) }
    var camMovement by remember { mutableStateOf(scene.cameraMovement) }
    var backgroundDetails by remember { mutableStateOf(scene.backgroundDetails) }
    var lightingDetail by remember { mutableStateOf(scene.lightingDetail) }
    var visualEvent by remember { mutableStateOf(scene.importantVisualEvent) }
    var finalFrame by remember { mutableStateOf(scene.finalFrame) }
    var transition by remember { mutableStateOf(scene.nextSceneTransition) }
    var imageUri by remember { mutableStateOf(scene.storyboardImageUri) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "စတိုရီဘုတ် သတ်မှတ်ချက် (Scene ${scene.sceneNumber} Storyboard)",
                color = StudioTextPrimary,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    ImagePickerBox(
                        imageUri = imageUri,
                        label = "စတိုရီဘုတ် ပုံကြမ်း / ရည်ညွှန်းပုံ ထည့်ရန်",
                        onImageSelected = { imageUri = it }
                    )
                }
                item {
                    OutlinedTextField(
                        value = openingFrame,
                        onValueChange = { openingFrame = it },
                        label = { Text("Opening Frame (စတင်ချိန် ပထမဖရိန်)") },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = positions,
                            onValueChange = { positions = it },
                            label = { Text("Character Positions") },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = expressions,
                            onValueChange = { expressions = it },
                            label = { Text("Character Expressions") },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = angle,
                            onValueChange = { angle = it },
                            label = { Text("Camera Angle (Low/Eye/High)") },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = camMovement,
                            onValueChange = { camMovement = it },
                            label = { Text("Camera Movement (Pan/Zoom)") },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                item {
                    OutlinedTextField(
                        value = movement,
                        onValueChange = { movement = it },
                        label = { Text("Main Movement & Pacing") },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = lightingDetail,
                        onValueChange = { lightingDetail = it },
                        label = { Text("Lighting & Atmosphere Details") },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = visualEvent,
                        onValueChange = { visualEvent = it },
                        label = { Text("Important Visual Event (အဓိက အထူးမြင်ကွင်း)") },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = finalFrame,
                        onValueChange = { finalFrame = it },
                        label = { Text("Final Frame (နောက်ဆုံး ဖရိန်)") },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = transition,
                        onValueChange = { transition = it },
                        label = { Text("Next Scene Transition (ကူးပြောင်းမှု)") },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val updated = scene.copy(
                        openingFrame = openingFrame.trim(),
                        characterPositions = positions.trim(),
                        characterExpressions = expressions.trim(),
                        mainMovement = movement.trim(),
                        cameraAngle = angle.trim(),
                        cameraMovement = camMovement.trim(),
                        backgroundDetails = backgroundDetails.trim(),
                        lightingDetail = lightingDetail.trim(),
                        importantVisualEvent = visualEvent.trim(),
                        finalFrame = finalFrame.trim(),
                        nextSceneTransition = transition.trim(),
                        storyboardImageUri = imageUri
                    )
                    onSave(updated)
                },
                colors = ButtonDefaults.buttonColors(containerColor = StudioCyan),
                modifier = Modifier.testTag("save_storyboard_dialog_button")
            ) {
                Text("စတိုရီဘုတ် သိမ်းဆည်းမည်", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("မလုပ်တော့ပါ", color = StudioTextSecondary)
            }
        },
        containerColor = StudioSurfaceElevated
    )
}
