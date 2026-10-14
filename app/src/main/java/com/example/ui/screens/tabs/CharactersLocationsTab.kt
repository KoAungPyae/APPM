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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.CharacterProfile
import com.example.data.model.LocationProfile
import com.example.data.model.Project
import com.example.ui.StudioViewModel
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
import com.example.util.ExportHelper

@Composable
fun CharactersLocationsTab(
    project: Project,
    viewModel: StudioViewModel,
    modifier: Modifier = Modifier
) {
    var selectedSubTab by remember { mutableIntStateOf(0) } // 0: Characters, 1: Locations
    var editingCharacter by remember { mutableStateOf<CharacterProfile?>(null) }
    var isNewCharacter by remember { mutableStateOf(false) }

    var editingLocation by remember { mutableStateOf<LocationProfile?>(null) }
    var isNewLocation by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        // Sub Tabs
        TabRow(
            selectedTabIndex = selectedSubTab,
            containerColor = StudioSurface,
            contentColor = StudioPurpleLight,
            indicator = { tabPositions ->
                SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedSubTab]),
                    color = StudioPurpleLight
                )
            }
        ) {
            Tab(
                selected = selectedSubTab == 0,
                onClick = { selectedSubTab = 0 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Person, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("ဇာတ်ကောင်များ (${project.characters.size})", fontWeight = FontWeight.Bold)
                    }
                }
            )
            Tab(
                selected = selectedSubTab == 1,
                onClick = { selectedSubTab = 1 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("နေရာ / စတူဒီယို (${project.locations.size})", fontWeight = FontWeight.Bold)
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (selectedSubTab == 0) {
            // Characters List
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "အတည်ပြုပြီး ဇာတ်ကောင် ရုပ်သွင်များ (Approved Profiles)",
                    style = MaterialTheme.typography.bodySmall.copy(color = StudioTextSecondary)
                )
                Button(
                    onClick = {
                        editingCharacter = CharacterProfile(projectId = project.id)
                        isNewCharacter = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = StudioPurple),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("add_character_button")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("ဇာတ်ကောင်သစ်", fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (project.characters.isEmpty()) {
                EmptyPlaceholder(
                    icon = Icons.Default.Person,
                    title = "ဇာတ်ကောင် မထည့်သွင်းရသေးပါ",
                    desc = "ဇာတ်ကောင်သစ် ထည့်သွင်းပြီး အဝတ်အစား၊ ဆံပင်၊ မျက်နှာသွင်ပြင်နှင့် ရည်ညွှန်းပုံကို သတ်မှတ်ပါ"
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(project.characters, key = { it.id }) { char ->
                        CharacterCardItem(
                            character = char,
                            onEdit = {
                                editingCharacter = char
                                isNewCharacter = false
                            },
                            onDelete = { viewModel.deleteCharacter(project.id, char.id) }
                        )
                    }
                }
            }
        } else {
            // Locations List
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ဇာတ်ဝင်ခန်း နေရာများနှင့် အလင်းအမှောင် (Location Library)",
                    style = MaterialTheme.typography.bodySmall.copy(color = StudioTextSecondary)
                )
                Button(
                    onClick = {
                        editingLocation = LocationProfile(
                            projectId = project.id,
                            visualStyle = project.visualStyle
                        )
                        isNewLocation = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = StudioBlue),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("add_location_button")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("နေရာသစ် ထည့်ရန်", fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (project.locations.isEmpty()) {
                EmptyPlaceholder(
                    icon = Icons.Default.LocationOn,
                    title = "နေရာ / ပတ်ဝန်းကျင် မရှိသေးပါ",
                    desc = "ဇာတ်လမ်း ဖြစ်ပွားရာ နေရာ၊ နောက်ခံ ပစ္စည်းများ၊ အလင်းနှင့် ရာသီဥတုကို သတ်မှတ်ပါ"
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(project.locations, key = { it.id }) { loc ->
                        LocationCardItem(
                            location = loc,
                            onEdit = {
                                editingLocation = loc
                                isNewLocation = false
                            },
                            onDelete = { viewModel.deleteLocation(project.id, loc.id) }
                        )
                    }
                }
            }
        }
    }

    // Character Dialog Editor
    if (editingCharacter != null) {
        CharacterEditorDialog(
            character = editingCharacter!!,
            isNew = isNewCharacter,
            onDismiss = { editingCharacter = null },
            onSave = { updatedChar ->
                viewModel.saveCharacter(project.id, updatedChar)
                editingCharacter = null
            }
        )
    }

    // Location Dialog Editor
    if (editingLocation != null) {
        LocationEditorDialog(
            location = editingLocation!!,
            isNew = isNewLocation,
            onDismiss = { editingLocation = null },
            onSave = { updatedLoc ->
                viewModel.saveLocation(project.id, updatedLoc)
                editingLocation = null
            }
        )
    }
}

@Composable
fun EmptyPlaceholder(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, desc: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = StudioSurface),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = StudioTextMuted, modifier = Modifier.size(40.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = StudioTextPrimary))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = desc, style = MaterialTheme.typography.bodySmall.copy(color = StudioTextSecondary))
        }
    }
}

@Composable
fun CharacterCardItem(
    character: CharacterProfile,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = StudioSurface),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
        modifier = Modifier.fillMaxWidth().testTag("character_card_${character.id}")
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.Top) {
            // Character Image or Avatar
            if (!character.referenceImageUri.isNullOrBlank()) {
                AsyncImage(
                    model = character.referenceImageUri,
                    contentDescription = character.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .border(1.dp, StudioPurpleLight, RoundedCornerShape(10.dp))
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(StudioSurfaceElevated, RoundedCornerShape(10.dp))
                        .border(1.dp, StudioBorder, RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = StudioPurpleLight, modifier = Modifier.size(32.dp))
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = character.name.ifBlank { "အမည်မသတ်မှတ်ရသေး" },
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Row {
                        IconButton(onClick = onEdit, modifier = Modifier.size(30.dp)) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "ပြင်ဆင်ရန်", tint = StudioPurpleLight, modifier = Modifier.size(16.dp))
                        }
                        IconButton(onClick = onDelete, modifier = Modifier.size(30.dp)) {
                            Icon(imageVector = Icons.Default.DeleteOutline, contentDescription = "ဖျက်ရန်", tint = StudioRose, modifier = Modifier.size(16.dp))
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    if (character.age.isNotBlank()) StudioBadge(text = "${character.age} နှစ်", accentColor = StudioCyan)
                    if (character.genderPresentation.isNotBlank()) StudioBadge(text = character.genderPresentation.take(15), accentColor = StudioPurpleLight)
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = character.buildConsistentDescription(),
                    style = MaterialTheme.typography.bodySmall.copy(color = StudioTextSecondary, fontSize = 12.sp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun LocationCardItem(
    location: LocationProfile,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = StudioSurface),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
        modifier = Modifier.fillMaxWidth().testTag("location_card_${location.id}")
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.Top) {
            if (!location.referenceImageUri.isNullOrBlank()) {
                AsyncImage(
                    model = location.referenceImageUri,
                    contentDescription = location.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .border(1.dp, StudioBlue, RoundedCornerShape(10.dp))
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(StudioSurfaceElevated, RoundedCornerShape(10.dp))
                        .border(1.dp, StudioBorder, RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = StudioBlue, modifier = Modifier.size(32.dp))
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = location.name.ifBlank { "နေရာ အမည်မသိ" },
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Row {
                        IconButton(onClick = onEdit, modifier = Modifier.size(30.dp)) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "ပြင်ဆင်ရန်", tint = StudioBlue, modifier = Modifier.size(16.dp))
                        }
                        IconButton(onClick = onDelete, modifier = Modifier.size(30.dp)) {
                            Icon(imageVector = Icons.Default.DeleteOutline, contentDescription = "ဖျက်ရန်", tint = StudioRose, modifier = Modifier.size(16.dp))
                        }
                    }
                }

                if (location.timeOfDay.isNotBlank() || location.weather.isNotBlank()) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        if (location.timeOfDay.isNotBlank()) StudioBadge(text = location.timeOfDay, accentColor = StudioAmber)
                        if (location.weather.isNotBlank()) StudioBadge(text = location.weather, accentColor = StudioCyan)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                }

                Text(
                    text = location.buildConsistentDescription(),
                    style = MaterialTheme.typography.bodySmall.copy(color = StudioTextSecondary, fontSize = 12.sp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun CharacterEditorDialog(
    character: CharacterProfile,
    isNew: Boolean,
    onDismiss: () -> Unit,
    onSave: (CharacterProfile) -> Unit
) {
    var name by remember { mutableStateOf(character.name) }
    var age by remember { mutableStateOf(character.age) }
    var gender by remember { mutableStateOf(character.genderPresentation) }
    var height by remember { mutableStateOf(character.height) }
    var bodyType by remember { mutableStateOf(character.bodyType) }
    var face by remember { mutableStateOf(character.faceDescription) }
    var eyes by remember { mutableStateOf(character.eyeDescription) }
    var hairStyle by remember { mutableStateOf(character.hairstyle) }
    var hairColor by remember { mutableStateOf(character.hairColor) }
    var skinTone by remember { mutableStateOf(character.skinTone) }
    var clothing by remember { mutableStateOf(character.clothing) }
    var shoes by remember { mutableStateOf(character.shoes) }
    var accessories by remember { mutableStateOf(character.accessories) }
    var personality by remember { mutableStateOf(character.personality) }
    var voice by remember { mutableStateOf(character.voiceDescription) }
    var distinctive by remember { mutableStateOf(character.distinctiveFeatures) }
    var imageUri by remember { mutableStateOf(character.referenceImageUri) }
    var prompt by remember { mutableStateOf(character.characterReferencePrompt) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isNew) "ဇာတ်ကောင် ပရိုဖိုင် အသစ်" else "ဇာတ်ကောင် ပရိုဖိုင် ပြင်ဆင်ရန်",
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
                        label = "ဇာတ်ကောင် ရည်ညွှန်းပုံ ထည့်သွင်းရန်",
                        onImageSelected = { imageUri = it }
                    )
                }
                item {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("ဇာတ်ကောင် အမည် (Character Name) *") },
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().testTag("char_name_input")
                    )
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = age,
                            onValueChange = { age = it },
                            label = { Text("အသက် (Age)") },
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = gender,
                            onValueChange = { gender = it },
                            label = { Text("ကျား/မ (Gender)") },
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = height,
                            onValueChange = { height = it },
                            label = { Text("အရပ်အမောင်း (Height)") },
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = bodyType,
                            onValueChange = { bodyType = it },
                            label = { Text("ကိုယ်ခန္ဓာ (Body Type)") },
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                item {
                    OutlinedTextField(
                        value = face,
                        onValueChange = { face = it },
                        label = { Text("မျက်နှာသွင်ပြင် (Face Description)") },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = hairStyle,
                            onValueChange = { hairStyle = it },
                            label = { Text("ဆံပင်ပုံစံ (Hairstyle)") },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = hairColor,
                            onValueChange = { hairColor = it },
                            label = { Text("ဆံပင်အရောင် (Color)") },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                item {
                    OutlinedTextField(
                        value = clothing,
                        onValueChange = { clothing = it },
                        label = { Text("အဝတ်အစား (Clothing & Outfit)") },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = distinctive,
                        onValueChange = { distinctive = it },
                        label = { Text("ထင်ရှားသော အမှတ်အသား (Distinctive Features)") },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = prompt,
                        onValueChange = { prompt = it },
                        label = { Text("English AI Reference Prompt") },
                        placeholder = { Text("Consistent prompt used for generation") },
                        minLines = 2,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val updated = character.copy(
                        name = name.trim(),
                        age = age.trim(),
                        genderPresentation = gender.trim(),
                        height = height.trim(),
                        bodyType = bodyType.trim(),
                        faceDescription = face.trim(),
                        eyeDescription = eyes.trim(),
                        hairstyle = hairStyle.trim(),
                        hairColor = hairColor.trim(),
                        skinTone = skinTone.trim(),
                        clothing = clothing.trim(),
                        shoes = shoes.trim(),
                        accessories = accessories.trim(),
                        personality = personality.trim(),
                        voiceDescription = voice.trim(),
                        distinctiveFeatures = distinctive.trim(),
                        referenceImageUri = imageUri,
                        characterReferencePrompt = prompt.trim()
                    )
                    onSave(updated)
                },
                colors = ButtonDefaults.buttonColors(containerColor = StudioPurple),
                modifier = Modifier.testTag("save_character_dialog_button")
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
fun LocationEditorDialog(
    location: LocationProfile,
    isNew: Boolean,
    onDismiss: () -> Unit,
    onSave: (LocationProfile) -> Unit
) {
    var name by remember { mutableStateOf(location.name) }
    var env by remember { mutableStateOf(location.environmentDescription) }
    var bg by remember { mutableStateOf(location.backgroundElements) }
    var props by remember { mutableStateOf(location.props) }
    var lighting by remember { mutableStateOf(location.lighting) }
    var palette by remember { mutableStateOf(location.colorPalette) }
    var weather by remember { mutableStateOf(location.weather) }
    var timeOfDay by remember { mutableStateOf(location.timeOfDay) }
    var imageUri by remember { mutableStateOf(location.referenceImageUri) }
    var prompt by remember { mutableStateOf(location.locationPrompt) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isNew) "နေရာ အသစ် ထည့်သွင်းရန်" else "နေရာ ပြင်ဆင်ရန်",
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
                        label = "နေရာ ရည်ညွှန်းပုံ ထည့်သွင်းရန်",
                        onImageSelected = { imageUri = it }
                    )
                }
                item {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("နေရာ အမည် (Location Name) *") },
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().testTag("loc_name_input")
                    )
                }
                item {
                    OutlinedTextField(
                        value = env,
                        onValueChange = { env = it },
                        label = { Text("ပတ်ဝန်းကျင် ဖော်ပြချက် (Environment Description)") },
                        shape = RoundedCornerShape(8.dp),
                        minLines = 2,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = timeOfDay,
                            onValueChange = { timeOfDay = it },
                            label = { Text("အချိန် (Time of Day)") },
                            placeholder = { Text("Night, Golden Hour...") },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = weather,
                            onValueChange = { weather = it },
                            label = { Text("ရာသီဥတု (Weather)") },
                            placeholder = { Text("Rain, Fog, Clear...") },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                item {
                    OutlinedTextField(
                        value = lighting,
                        onValueChange = { lighting = it },
                        label = { Text("အလင်းအမှောင် (Lighting & Palette)") },
                        placeholder = { Text("Neon violet rim light, dark moody shadows...") },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = prompt,
                        onValueChange = { prompt = it },
                        label = { Text("English AI Location Prompt") },
                        shape = RoundedCornerShape(8.dp),
                        minLines = 2,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val updated = location.copy(
                        name = name.trim(),
                        environmentDescription = env.trim(),
                        backgroundElements = bg.trim(),
                        props = props.trim(),
                        lighting = lighting.trim(),
                        colorPalette = palette.trim(),
                        weather = weather.trim(),
                        timeOfDay = timeOfDay.trim(),
                        referenceImageUri = imageUri,
                        locationPrompt = prompt.trim()
                    )
                    onSave(updated)
                },
                colors = ButtonDefaults.buttonColors(containerColor = StudioBlue),
                modifier = Modifier.testTag("save_location_dialog_button")
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
