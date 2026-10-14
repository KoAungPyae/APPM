package com.example.ui.screens.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Project
import com.example.data.model.StoryDevelopment
import com.example.ui.StudioViewModel
import com.example.ui.theme.StudioAmber
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
fun StoryWorkspaceTab(
    project: Project,
    viewModel: StudioViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isAiLoading by viewModel.isAiLoading.collectAsState()
    val aiMessage by viewModel.aiMessage.collectAsState()

    var storyIdea by remember(project.id) { mutableStateOf(project.storyDevelopment.storyIdea.ifBlank { project.storyIdea }) }
    var logline by remember(project.id) { mutableStateOf(project.storyDevelopment.logline) }
    var mainCharacters by remember(project.id) { mutableStateOf(project.storyDevelopment.mainCharactersSummary) }
    var mainConflict by remember(project.id) { mutableStateOf(project.storyDevelopment.mainConflict) }
    var storyTheme by remember(project.id) { mutableStateOf(project.storyDevelopment.storyTheme) }
    var climax by remember(project.id) { mutableStateOf(project.storyDevelopment.climax) }
    var ending by remember(project.id) { mutableStateOf(project.storyDevelopment.ending) }

    var saveSuccess by remember { mutableStateOf(false) }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxSize().padding(16.dp)
    ) {
        // AI Assistant Bar
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = StudioSurfaceElevated),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioPurple.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = StudioPurpleLight,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "AI ဇာတ်လမ်းချဲ့ထွင်မှု (AI Story Assistant)",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = StudioTextPrimary
                                )
                            )
                        }

                        if (isAiLoading) {
                            CircularProgressIndicator(
                                color = StudioPurpleLight,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(20.dp)
                            )
                        } else {
                            OutlinedButton(
                                onClick = {
                                    viewModel.expandStoryWithAi(
                                        premise = storyIdea.ifBlank { project.title },
                                        genre = project.genre,
                                        visualStyle = project.visualStyle
                                    ) { expandedText ->
                                        if (logline.isBlank()) {
                                            logline = "Premise: $storyIdea"
                                        }
                                        if (storyTheme.isBlank()) {
                                            storyTheme = "AI Story Expanded Blueprint"
                                        }
                                    }
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = StudioPurpleLight),
                                modifier = Modifier.testTag("ai_expand_story_button")
                            ) {
                                Text("AI ဖြင့် ချဲ့ထွင်ရန်", fontSize = 12.sp)
                            }
                        }
                    }

                    if (aiMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = aiMessage ?: "",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = if (aiMessage!!.contains("မထည့်ရသေး")) StudioAmber else StudioCyan
                            )
                        )
                    }
                }
            }
        }

        // Section A: Story Idea
        item {
            StorySectionCard(
                letter = "A",
                titleBurmese = "ဇာတ်လမ်း စိတ်ကူး (Story Idea)",
                value = storyIdea,
                onValueChange = { storyIdea = it },
                placeholder = "ဇာတ်လမ်း အခြေခံ စိတ်ကူးနှင့် အယူအဆ..."
            )
        }

        // Section B: Logline
        item {
            StorySectionCard(
                letter = "B",
                titleBurmese = "လော့ဂ်လိုင်း / အကျဉ်းချုပ် (Logline)",
                value = logline,
                onValueChange = { logline = it },
                placeholder = "ဇာတ်လမ်းကို စာတစ်ကြောင်းတည်းဖြင့် စိတ်ဝင်စားဖွယ် ဖမ်းစားနိုင်သော အကျဉ်းချုပ်..."
            )
        }

        // Section C: Main Characters Summary
        item {
            StorySectionCard(
                letter = "C",
                titleBurmese = "အဓိက ဇာတ်ကောင်များ (Main Characters)",
                value = mainCharacters,
                onValueChange = { mainCharacters = it },
                placeholder = "အဓိက ဇာတ်ဆောင်များနှင့် သူတို့၏ ရည်မှန်းချက်..."
            )
        }

        // Section D: Main Conflict
        item {
            StorySectionCard(
                letter = "D",
                titleBurmese = "အဓိက ပဋိပက္ခ (Main Conflict)",
                value = mainConflict,
                onValueChange = { mainConflict = it },
                placeholder = "ဇာတ်လမ်း၏ အဓိက အတားအဆီး သို့မဟုတ် ရင်ဆိုင်ရမည့် ရန်သူ/အခက်အခဲ..."
            )
        }

        // Section E: Story Theme
        item {
            StorySectionCard(
                letter = "E",
                titleBurmese = "ဇာတ်လမ်း ဆောင်ပုဒ် (Story Theme)",
                value = storyTheme,
                onValueChange = { storyTheme = it },
                placeholder = "ဇာတ်လမ်းက ပေးလိုသော အဓိပ္ပာယ် သို့မဟုတ် သတင်းစကား..."
            )
        }

        // Section F: Climax
        item {
            StorySectionCard(
                letter = "F",
                titleBurmese = "အထွတ်အထိပ် (Climax)",
                value = climax,
                onValueChange = { climax = it },
                placeholder = "ဇာတ်လမ်း၏ စိတ်လှုပ်ရှားဖွယ်ရာ အထွတ်အထိပ် အခိုက်အတန့်..."
            )
        }

        // Section G: Ending
        item {
            StorySectionCard(
                letter = "G",
                titleBurmese = "ဇာတ်သိမ်း (Ending)",
                value = ending,
                onValueChange = { ending = it },
                placeholder = "ဇာတ်လမ်း ပြီးဆုံးသွားပုံနှင့် နောက်ဆက်တွဲ ရလဒ်..."
            )
        }

        // Save & Export Actions Bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        val updatedDev = StoryDevelopment(
                            storyIdea = storyIdea.trim(),
                            logline = logline.trim(),
                            mainCharactersSummary = mainCharacters.trim(),
                            mainConflict = mainConflict.trim(),
                            storyTheme = storyTheme.trim(),
                            climax = climax.trim(),
                            ending = ending.trim()
                        )
                        viewModel.updateStoryDev(project.id, updatedDev)
                        saveSuccess = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = StudioPurple),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f).testTag("save_story_dev_button")
                ) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (saveSuccess) "သိမ်းဆည်းပြီး (Saved)" else "သိမ်းဆည်းမည် (Save)")
                }

                OutlinedButton(
                    onClick = {
                        val fullStoryText = buildString {
                            appendLine("=== STORY DEVELOPMENT: ${project.title} ===")
                            appendLine("A. Story Idea: $storyIdea")
                            appendLine("B. Logline: $logline")
                            appendLine("C. Main Characters: $mainCharacters")
                            appendLine("D. Conflict: $mainConflict")
                            appendLine("E. Theme: $storyTheme")
                            appendLine("F. Climax: $climax")
                            appendLine("G. Ending: $ending")
                        }
                        ExportHelper.copyToClipboard(context, "Story Development", fullStoryText)
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f).testTag("copy_story_dev_button")
                ) {
                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("ကူးယူမည် (Copy)")
                }

                OutlinedButton(
                    onClick = {
                        val fullStoryText = buildString {
                            appendLine("=== STORY DEVELOPMENT: ${project.title} ===")
                            appendLine("A. Story Idea: $storyIdea")
                            appendLine("B. Logline: $logline")
                            appendLine("C. Main Characters: $mainCharacters")
                            appendLine("D. Conflict: $mainConflict")
                            appendLine("E. Theme: $storyTheme")
                            appendLine("F. Climax: $climax")
                            appendLine("G. Ending: $ending")
                        }
                        ExportHelper.shareText(context, "${project.title} - Story Dev", fullStoryText)
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("export_story_dev_button")
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

@Composable
fun StorySectionCard(
    letter: String,
    titleBurmese: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = StudioSurface),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .background(StudioPurple.copy(alpha = 0.2f), RoundedCornerShape(6.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = letter,
                        color = StudioPurpleLight,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = titleBurmese,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = StudioTextPrimary
                    )
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = { Text(placeholder, color = StudioTextMuted, fontSize = 13.sp) },
                minLines = 2,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = StudioPurple,
                    unfocusedBorderColor = StudioBorder,
                    focusedContainerColor = StudioSurfaceElevated,
                    unfocusedContainerColor = StudioSurfaceElevated,
                    focusedTextColor = StudioTextPrimary,
                    unfocusedTextColor = StudioTextPrimary
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth().testTag("story_section_$letter")
            )
        }
    }
}
