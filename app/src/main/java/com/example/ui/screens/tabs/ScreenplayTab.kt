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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
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
import com.example.data.model.Project
import com.example.data.model.ScreenplayItem
import com.example.ui.StudioViewModel
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

@Composable
fun ScreenplayTab(
    project: Project,
    viewModel: StudioViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var editingItem by remember { mutableStateOf<ScreenplayItem?>(null) }
    var isNewItem by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        // Top Action Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "ဇာတ်ညွှန်း တည်းဖြတ်မှု (Screenplay Editor)",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = StudioTextPrimary
                )
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = {
                        val formatted = ExportHelper.formatScreenplayTxt(project)
                        ExportHelper.copyToClipboard(context, "Screenplay", formatted)
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("copy_screenplay_button")
                ) {
                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("ကူးယူမည်", fontSize = 11.sp)
                }

                Button(
                    onClick = {
                        editingItem = ScreenplayItem(
                            sceneHeading = "INT. LOCATION - DAY",
                            transition = "CUT TO:"
                        )
                        isNewItem = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = StudioPurple),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("add_screenplay_item_button")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("အခန်းသစ် ထည့်ရန်", fontSize = 11.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (project.screenplay.isEmpty()) {
            Card(
                colors = CardDefaults.cardColors(containerColor = StudioSurface),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = null,
                        tint = StudioTextMuted,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "ဇာတ်ညွှန်း မရေးသားရသေးပါ",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                    )
                    Text(
                        text = "အခန်းခေါင်းစဉ်၊ ဇာတ်ကောင် စကားပြောနှင့် အသံအထူးပြုလုပ်ချက်များကို စံသတ်မှတ်ချက်အတိုင်း ထည့်သွင်းနိုင်ပါသည်",
                        style = MaterialTheme.typography.bodySmall.copy(color = StudioTextSecondary)
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                itemsIndexed(project.screenplay, key = { _, item -> item.id }) { index, item ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = StudioSurface),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = item.sceneHeading.ifBlank { "SCENE HEADING #${index + 1}" },
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace,
                                        color = StudioCyan
                                    )
                                )

                                Row {
                                    IconButton(
                                        onClick = {
                                            editingItem = item
                                            isNewItem = false
                                        },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "ပြင်ဆင်ရန်",
                                            tint = StudioPurpleLight,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    IconButton(
                                        onClick = {
                                            val updated = project.screenplay.filterNot { it.id == item.id }
                                            viewModel.saveScreenplay(project.id, updated)
                                        },
                                        modifier = Modifier.size(32.dp)
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

                            if (item.actionDescription.isNotBlank()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = item.actionDescription,
                                    style = MaterialTheme.typography.bodySmall.copy(color = StudioTextPrimary)
                                )
                            }

                            if (item.characterName.isNotBlank()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(StudioSurfaceElevated, RoundedCornerShape(8.dp))
                                        .padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = item.characterName.uppercase(),
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = StudioAmber
                                        )
                                    )
                                    if (item.emotion.isNotBlank()) {
                                        Text(
                                            text = "(${item.emotion})",
                                            style = MaterialTheme.typography.labelSmall.copy(color = StudioTextSecondary)
                                        )
                                    }
                                    if (item.dialogue.isNotBlank()) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "\"${item.dialogue}\"",
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                color = StudioTextPrimary,
                                                fontWeight = FontWeight.Medium
                                            )
                                        )
                                    }
                                }
                            }

                            if (item.soundEffects.isNotBlank()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "[SFX: ${item.soundEffects}]",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = StudioPurpleLight,
                                        fontFamily = FontFamily.Monospace
                                    )
                                )
                            }

                            if (item.transition.isNotBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = item.transition.uppercase(),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = StudioTextMuted,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace
                                    ),
                                    modifier = Modifier.align(Alignment.End)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Edit/Add Screenplay Item Dialog
    if (editingItem != null) {
        val current = editingItem!!
        var heading by remember { mutableStateOf(current.sceneHeading) }
        var action by remember { mutableStateOf(current.actionDescription) }
        var charName by remember { mutableStateOf(current.characterName) }
        var dialogue by remember { mutableStateOf(current.dialogue) }
        var emotion by remember { mutableStateOf(current.emotion) }
        var sfx by remember { mutableStateOf(current.soundEffects) }
        var transition by remember { mutableStateOf(current.transition) }

        AlertDialog(
            onDismissRequest = { editingItem = null },
            title = {
                Text(
                    text = if (isNewItem) "ဇာတ်ညွှန်း အခန်းသစ် ထည့်သွင်းရန်" else "ဇာတ်ညွှန်း ပြင်ဆင်ရန်",
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
                            value = heading,
                            onValueChange = { heading = it },
                            label = { Text("Scene Heading (e.g., INT. COFFEE SHOP - DAY)") },
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("screenplay_heading_input")
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = action,
                            onValueChange = { action = it },
                            label = { Text("Action Description (လှုပ်ရှားမှု ဖော်ပြချက်)") },
                            shape = RoundedCornerShape(8.dp),
                            minLines = 2,
                            modifier = Modifier.fillMaxWidth().testTag("screenplay_action_input")
                        )
                    }
                    item {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = charName,
                                onValueChange = { charName = it },
                                label = { Text("Character Name") },
                                shape = RoundedCornerShape(8.dp),
                                singleLine = true,
                                modifier = Modifier.weight(1f).testTag("screenplay_char_name_input")
                            )
                            OutlinedTextField(
                                value = emotion,
                                onValueChange = { emotion = it },
                                label = { Text("Emotion (ခံစားချက်)") },
                                shape = RoundedCornerShape(8.dp),
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    item {
                        OutlinedTextField(
                            value = dialogue,
                            onValueChange = { dialogue = it },
                            label = { Text("Dialogue (ပြောစကား)") },
                            shape = RoundedCornerShape(8.dp),
                            minLines = 2,
                            modifier = Modifier.fillMaxWidth().testTag("screenplay_dialogue_input")
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = sfx,
                            onValueChange = { sfx = it },
                            label = { Text("Sound Effects (SFX)") },
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = transition,
                            onValueChange = { transition = it },
                            label = { Text("Transition (CUT TO:, FADE OUT:)") },
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val saved = current.copy(
                            sceneHeading = heading.trim(),
                            actionDescription = action.trim(),
                            characterName = charName.trim(),
                            dialogue = dialogue.trim(),
                            emotion = emotion.trim(),
                            soundEffects = sfx.trim(),
                            transition = transition.trim()
                        )
                        val updatedList = if (isNewItem) {
                            project.screenplay + saved
                        } else {
                            project.screenplay.map { if (it.id == saved.id) saved else it }
                        }
                        viewModel.saveScreenplay(project.id, updatedList)
                        editingItem = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = StudioPurple),
                    modifier = Modifier.testTag("save_screenplay_dialog_button")
                ) {
                    Text("သိမ်းဆည်းမည်")
                }
            },
            dismissButton = {
                TextButton(onClick = { editingItem = null }) {
                    Text("မလုပ်တော့ပါ", color = StudioTextSecondary)
                }
            },
            containerColor = StudioSurfaceElevated
        )
    }
}
