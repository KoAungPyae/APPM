package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AspectRatios
import com.example.data.model.Genres
import com.example.data.model.Platforms
import com.example.data.model.Project
import com.example.data.model.VisualStyles
import com.example.ui.ScreenDestination
import com.example.ui.StudioViewModel
import com.example.ui.components.StudioTopBar
import com.example.ui.theme.StudioAmber
import com.example.ui.theme.StudioBackground
import com.example.ui.theme.StudioBlue
import com.example.ui.theme.StudioBorder
import com.example.ui.theme.StudioCyan
import com.example.ui.theme.StudioPurple
import com.example.ui.theme.StudioPurpleLight
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.StudioSurfaceElevated
import com.example.ui.theme.StudioSurfaceVariant
import com.example.ui.theme.StudioTextMuted
import com.example.ui.theme.StudioTextPrimary
import com.example.ui.theme.StudioTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectFormScreen(
    editProjectId: String?,
    viewModel: StudioViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler { viewModel.navigateBack() }

    val projects by viewModel.rawProjects.collectAsState()
    val existing = editProjectId?.let { id -> projects.firstOrNull { it.id == id } }

    var title by remember(existing) { mutableStateOf(existing?.title ?: "") }
    var genre by remember(existing) { mutableStateOf(existing?.genre ?: Genres.ALL.first()) }
    var storyIdea by remember(existing) { mutableStateOf(existing?.storyIdea ?: "") }
    var targetAudience by remember(existing) { mutableStateOf(existing?.targetAudience ?: "General Audience & Creators") }
    var durationSec by remember(existing) { mutableFloatStateOf(existing?.targetDurationSec?.toFloat() ?: 60f) }
    var visualStyle by remember(existing) { mutableStateOf(existing?.visualStyle ?: VisualStyles.CINEMATIC_REALISTIC) }
    var customStyleNotes by remember(existing) { mutableStateOf(existing?.customStyleNotes ?: "") }
    var platform by remember(existing) { mutableStateOf(existing?.platform ?: Platforms.TIKTOK) }
    var aspectRatio by remember(existing) { mutableStateOf(existing?.aspectRatio ?: AspectRatios.RATIO_9_16) }
    var dialogueLanguage by remember(existing) { mutableStateOf(existing?.dialogueLanguage ?: "Burmese (မြန်မာ)") }
    var notes by remember(existing) { mutableStateOf(existing?.notes ?: "") }

    var genreMenuExpanded by remember { mutableStateOf(false) }
    var titleError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            StudioTopBar(
                title = if (existing != null) "ပရောဂျက် ပြင်ဆင်ရန်" else "ပရောဂျက် အသစ်ဖန်တီးရန်",
                subtitle = "Project Specifications & Creative Setup",
                showBackButton = true,
                onBackClick = { viewModel.navigateBack() }
            )
        },
        bottomBar = {
            Surface(
                color = StudioSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            if (title.isBlank()) {
                                titleError = true
                                return@Button
                            }
                            titleError = false
                            val projectToSave = existing?.copy(
                                title = title.trim(),
                                genre = genre,
                                storyIdea = storyIdea.trim(),
                                targetAudience = targetAudience.trim(),
                                targetDurationSec = durationSec.toInt(),
                                visualStyle = visualStyle,
                                customStyleNotes = customStyleNotes.trim(),
                                platform = platform,
                                aspectRatio = aspectRatio,
                                dialogueLanguage = dialogueLanguage.trim(),
                                notes = notes.trim(),
                                updatedAt = System.currentTimeMillis()
                            ) ?: Project(
                                title = title.trim(),
                                genre = genre,
                                storyIdea = storyIdea.trim(),
                                targetAudience = targetAudience.trim(),
                                targetDurationSec = durationSec.toInt(),
                                visualStyle = visualStyle,
                                customStyleNotes = customStyleNotes.trim(),
                                platform = platform,
                                aspectRatio = aspectRatio,
                                dialogueLanguage = dialogueLanguage.trim(),
                                notes = notes.trim()
                            )

                            viewModel.saveProject(projectToSave) {
                                viewModel.selectActiveProject(projectToSave)
                                viewModel.navigateBack()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = StudioPurple),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("save_project_submit_button")
                    ) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (existing != null) "ပရောဂျက် သိမ်းဆည်းမည် (Update)" else "ပရောဂျက် စတင်ဖန်တီးမည် (Create Project)",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
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
            // Title & Story Idea
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = StudioSurface),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "အခြေခံ အချက်အလက် (Basic Info)",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = StudioCyan
                            )
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // Title
                        OutlinedTextField(
                            value = title,
                            onValueChange = {
                                title = it
                                if (it.isNotBlank()) titleError = false
                            },
                            label = { Text("ပရောဂျက် အမည် (Project Title) *") },
                            placeholder = { Text("ဥပမာ - မန္တလေးညရဲ့ လျှို့ဝှက်ချက်") },
                            isError = titleError,
                            supportingText = {
                                if (titleError) Text("အမည် ထည့်သွင်းရန် လိုအပ်ပါသည်", color = Color.Red)
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = StudioPurple,
                                unfocusedBorderColor = StudioBorder,
                                focusedContainerColor = StudioSurfaceElevated,
                                unfocusedContainerColor = StudioSurfaceElevated,
                                focusedTextColor = StudioTextPrimary,
                                unfocusedTextColor = StudioTextPrimary
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("project_title_input")
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Genre Dropdown
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = genre,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("ဇာတ်လမ်း အမျိုးအစား (Story Genre)") },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = null,
                                        tint = StudioPurpleLight,
                                        modifier = Modifier.clickable { genreMenuExpanded = true }
                                    )
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = StudioPurple,
                                    unfocusedBorderColor = StudioBorder,
                                    focusedContainerColor = StudioSurfaceElevated,
                                    unfocusedContainerColor = StudioSurfaceElevated,
                                    focusedTextColor = StudioTextPrimary,
                                    unfocusedTextColor = StudioTextPrimary
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { genreMenuExpanded = true }
                            )

                            DropdownMenu(
                                expanded = genreMenuExpanded,
                                onDismissRequest = { genreMenuExpanded = false },
                                modifier = Modifier.background(StudioSurfaceElevated)
                            ) {
                                Genres.ALL.forEach { g ->
                                    DropdownMenuItem(
                                        text = { Text(g, color = StudioTextPrimary) },
                                        onClick = {
                                            genre = g
                                            genreMenuExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Story Idea
                        OutlinedTextField(
                            value = storyIdea,
                            onValueChange = { storyIdea = it },
                            label = { Text("ဇာတ်လမ်း စိတ်ကူး (Story Idea / Concept)") },
                            placeholder = { Text("ဗီဒီယို ပြုလုပ်လိုသည့် အဓိက စိတ်ကူး သို့မဟုတ် အကျဉ်းချုပ်...") },
                            minLines = 3,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = StudioPurple,
                                unfocusedBorderColor = StudioBorder,
                                focusedContainerColor = StudioSurfaceElevated,
                                unfocusedContainerColor = StudioSurfaceElevated,
                                focusedTextColor = StudioTextPrimary,
                                unfocusedTextColor = StudioTextPrimary
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("project_story_idea_input")
                        )
                    }
                }
            }

            // Target Duration & Audience
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = StudioSurface),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "ကြာချိန် နှင့် ပစ်မှတ် (Duration & Audience)",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = StudioAmber
                            )
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "ပစ်မှတ် ဗီဒီယို ကြာချိန် (Target Duration):",
                                style = MaterialTheme.typography.bodyMedium.copy(color = StudioTextSecondary)
                            )
                            Text(
                                text = "${durationSec.toInt()} စက္ကန့် (Sec)",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = StudioAmber
                                )
                            )
                        }

                        Slider(
                            value = durationSec,
                            onValueChange = { durationSec = it },
                            valueRange = 10f..300f,
                            steps = 28, // 10s steps
                            colors = SliderDefaults.colors(
                                thumbColor = StudioAmber,
                                activeTrackColor = StudioAmber,
                                inactiveTrackColor = StudioBorder
                            ),
                            modifier = Modifier.fillMaxWidth().testTag("duration_slider")
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = targetAudience,
                            onValueChange = { targetAudience = it },
                            label = { Text("ပစ်မှတ် ပရိသတ် (Target Audience)") },
                            placeholder = { Text("ဥပမာ - လူငယ်များ၊ ဆန်းသစ်တီထွင်သူများ") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = StudioPurple,
                                unfocusedBorderColor = StudioBorder,
                                focusedContainerColor = StudioSurfaceElevated,
                                unfocusedContainerColor = StudioSurfaceElevated,
                                focusedTextColor = StudioTextPrimary,
                                unfocusedTextColor = StudioTextPrimary
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Visual Style Selection
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = StudioSurface),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "ရုပ်ထွက် စတိုင် (Visual Style)",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = StudioPurpleLight
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        VisualStyles.ALL.forEach { style ->
                            val isSelected = visualStyle == style
                            Surface(
                                color = if (isSelected) StudioPurple.copy(alpha = 0.25f) else StudioSurfaceElevated,
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) StudioPurpleLight else StudioBorder
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable { visualStyle = style }
                                    .testTag("style_option_$style")
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = VisualStyles.getBurmeseLabel(style),
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = if (isSelected) StudioPurpleLight else StudioTextPrimary,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        ),
                                        modifier = Modifier.weight(1f)
                                    )
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = StudioPurpleLight,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }

                        if (visualStyle == VisualStyles.CUSTOM) {
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = customStyleNotes,
                                onValueChange = { customStyleNotes = it },
                                label = { Text("စိတ်ကြိုက် စတိုင် မှတ်စု (Custom Style Notes)") },
                                placeholder = { Text("ဥပမာ - ရောင်စုံဆီဆေး၊ ရေဆေးပန်းချီ၊ မော်ဒန် ဂရက်ဖစ်...") },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = StudioPurple,
                                    unfocusedBorderColor = StudioBorder,
                                    focusedContainerColor = StudioSurfaceElevated,
                                    unfocusedContainerColor = StudioSurfaceElevated,
                                    focusedTextColor = StudioTextPrimary,
                                    unfocusedTextColor = StudioTextPrimary
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            // Platform & Aspect Ratio
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = StudioSurface),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "ထုတ်ဝေမည့် ပလက်ဖောင်း နှင့် အချိုးအစား (Platform & Ratio)",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = StudioBlue
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Text("ပလက်ဖောင်း (Platform):", style = MaterialTheme.typography.labelMedium.copy(color = StudioTextSecondary))
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Platforms.ALL.take(2).forEach { p ->
                                val isSelected = platform == p
                                Surface(
                                    color = if (isSelected) StudioBlue.copy(alpha = 0.25f) else StudioSurfaceElevated,
                                    shape = RoundedCornerShape(10.dp),
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isSelected) StudioBlue else StudioBorder
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { platform = p }
                                ) {
                                    Text(
                                        text = p,
                                        color = if (isSelected) StudioBlue else StudioTextPrimary,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        modifier = Modifier.padding(10.dp),
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Platforms.ALL.drop(2).forEach { p ->
                                val isSelected = platform == p
                                Surface(
                                    color = if (isSelected) StudioBlue.copy(alpha = 0.25f) else StudioSurfaceElevated,
                                    shape = RoundedCornerShape(10.dp),
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isSelected) StudioBlue else StudioBorder
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { platform = p }
                                ) {
                                    Text(
                                        text = p,
                                        color = if (isSelected) StudioBlue else StudioTextPrimary,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        modifier = Modifier.padding(10.dp),
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        Text("ဗီဒီယို အချိုးအစား (Aspect Ratio):", style = MaterialTheme.typography.labelMedium.copy(color = StudioTextSecondary))
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            AspectRatios.ALL.forEach { r ->
                                val isSelected = aspectRatio == r
                                Surface(
                                    color = if (isSelected) StudioCyan.copy(alpha = 0.25f) else StudioSurfaceElevated,
                                    shape = RoundedCornerShape(10.dp),
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isSelected) StudioCyan else StudioBorder
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { aspectRatio = r }
                                ) {
                                    Text(
                                        text = r,
                                        color = if (isSelected) StudioCyan else StudioTextPrimary,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        modifier = Modifier.padding(10.dp),
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Language & Notes
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = StudioSurface),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "ပြောစကား ဘာသာစကား နှင့် အခြားမှတ်စုများ",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = StudioTextPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = dialogueLanguage,
                            onValueChange = { dialogueLanguage = it },
                            label = { Text("ပြောစကား ဘာသာစကား (Dialogue Language)") },
                            placeholder = { Text("Burmese, English, or Silent") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = StudioPurple,
                                unfocusedBorderColor = StudioBorder,
                                focusedContainerColor = StudioSurfaceElevated,
                                unfocusedContainerColor = StudioSurfaceElevated,
                                focusedTextColor = StudioTextPrimary,
                                unfocusedTextColor = StudioTextPrimary
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = notes,
                            onValueChange = { notes = it },
                            label = { Text("ထုတ်လုပ်မှု အထွေထွေ မှတ်စု (Production Notes)") },
                            placeholder = { Text("ဗီဒီယို မော်ဒယ် သတ်မှတ်ချက် သို့မဟုတ် မှတ်ချက်များ...") },
                            minLines = 2,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = StudioPurple,
                                unfocusedBorderColor = StudioBorder,
                                focusedContainerColor = StudioSurfaceElevated,
                                unfocusedContainerColor = StudioSurfaceElevated,
                                focusedTextColor = StudioTextPrimary,
                                unfocusedTextColor = StudioTextPrimary
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
