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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CharacterProfile
import com.example.ui.ScreenDestination
import com.example.ui.StudioViewModel
import com.example.ui.components.StudioTopBar
import com.example.ui.screens.tabs.CharacterCardItem
import com.example.ui.screens.tabs.CharacterEditorDialog
import com.example.ui.theme.StudioBackground
import com.example.ui.theme.StudioBorder
import com.example.ui.theme.StudioCyan
import com.example.ui.theme.StudioPurple
import com.example.ui.theme.StudioPurpleLight
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.StudioSurfaceElevated
import com.example.ui.theme.StudioTextMuted
import com.example.ui.theme.StudioTextPrimary
import com.example.ui.theme.StudioTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersGlobalScreen(
    viewModel: StudioViewModel,
    modifier: Modifier = Modifier
) {
    val projects by viewModel.rawProjects.collectAsState()
    val allCharacters = projects.flatMap { p -> p.characters.map { c -> Pair(p, c) } }

    var editingCharacterWithProject by remember { mutableStateOf<Pair<String, CharacterProfile>?>(null) }
    var isNew by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            StudioTopBar(
                title = "ဇာတ်ကောင် စာကြည့်တိုက် (Character Library)",
                subtitle = "ပရောဂျက်အားလုံးရှိ အတည်ပြုပြီး ဇာတ်ကောင်များ (${allCharacters.size})",
                showBackButton = false
            )
        },
        floatingActionButton = {
            if (projects.isNotEmpty()) {
                FloatingActionButton(
                    onClick = {
                        val firstProject = projects.first()
                        editingCharacterWithProject = Pair(firstProject.id, CharacterProfile(projectId = firstProject.id))
                        isNew = true
                    },
                    containerColor = StudioPurple,
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier.testTag("fab_add_global_character")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "ဇာတ်ကောင်သစ်")
                }
            }
        },
        containerColor = StudioBackground,
        modifier = modifier
    ) { innerPadding ->
        if (allCharacters.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = StudioTextMuted,
                        modifier = Modifier.size(54.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "ဇာတ်ကောင် ပရိုဖိုင် မရှိသေးပါ",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                    )
                    Text(
                        text = "ပရောဂျက်များတွင် ဇာတ်ကောင် အချက်အလက်များ ထည့်သွင်းထားပါက ဤနေရာတွင် စုစည်းပြသပေးမည် ဖြစ်ပါသည်",
                        style = MaterialTheme.typography.bodySmall.copy(color = StudioTextSecondary)
                    )
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                items(allCharacters, key = { it.second.id }) { (project, char) ->
                    Column {
                        Text(
                            text = "ပရောဂျက်: ${project.title}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = StudioCyan,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(bottom = 4.dp, start = 4.dp)
                        )
                        CharacterCardItem(
                            character = char,
                            onEdit = {
                                editingCharacterWithProject = Pair(project.id, char)
                                isNew = false
                            },
                            onDelete = { viewModel.deleteCharacter(project.id, char.id) }
                        )
                    }
                }
            }
        }
    }

    if (editingCharacterWithProject != null) {
        val (projId, char) = editingCharacterWithProject!!
        CharacterEditorDialog(
            character = char,
            isNew = isNew,
            onDismiss = { editingCharacterWithProject = null },
            onSave = { updated ->
                viewModel.saveCharacter(projId, updated)
                editingCharacterWithProject = null
            }
        )
    }
}
