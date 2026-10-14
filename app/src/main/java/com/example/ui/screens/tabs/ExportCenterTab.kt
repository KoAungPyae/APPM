package com.example.ui.screens.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FolderZip
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Project
import com.example.ui.StudioViewModel
import com.example.ui.theme.StudioAmber
import com.example.ui.theme.StudioBlue
import com.example.ui.theme.StudioBorder
import com.example.ui.theme.StudioCyan
import com.example.ui.theme.StudioPurple
import com.example.ui.theme.StudioPurpleLight
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.StudioSurfaceElevated
import com.example.ui.theme.StudioTextMuted
import com.example.ui.theme.StudioTextPrimary
import com.example.ui.theme.StudioTextSecondary
import com.example.util.ExportHelper

@Composable
fun ExportCenterTab(
    project: Project,
    viewModel: StudioViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var previewTitle by remember { mutableStateOf<String?>(null) }
    var previewContent by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = modifier.fillMaxSize().padding(16.dp)
    ) {
        item {
            Text(
                text = "ထုတ်ယူမှု စင်တာ (Production Export Center)",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = StudioTextPrimary
                )
            )
            Text(
                text = "ပရောဂျက်ကို JSON ဒေတာ၊ စံသတ်မှတ်ချက် ဇာတ်ညွှန်း သို့မဟုတ် ထုတ်လုပ်မှု ပက်ကေ့ချ်အဖြစ် အပြည့်အစုံ ထုတ်ယူပါ",
                style = MaterialTheme.typography.bodySmall.copy(color = StudioTextSecondary)
            )
        }

        // 1. Complete Project as JSON
        item {
            ExportOptionCard(
                title = "ပရောဂျက် အပြည့်အစုံ JSON ဖိုင် (Complete Project JSON)",
                desc = "ဇာတ်ကောင်၊ နေရာ၊ ဇာတ်ဝင်ခန်းနှင့် ပရော့မ်များ အားလုံးပါဝင်သော ဒစ်ဂျစ်တယ် အချက်အလက် JSON",
                icon = Icons.Default.Code,
                accentColor = StudioCyan,
                onPreview = {
                    val content = ExportHelper.formatProjectJson(project)
                    previewTitle = "${project.title}.json"
                    previewContent = content
                },
                onCopy = {
                    val content = ExportHelper.formatProjectJson(project)
                    ExportHelper.copyToClipboard(context, "Project JSON", content)
                },
                onShare = {
                    val content = ExportHelper.formatProjectJson(project)
                    ExportHelper.shareText(context, "${project.title}.json", content)
                }
            )
        }

        // 2. Screenplay as TXT
        item {
            ExportOptionCard(
                title = "စံသတ်မှတ်ချက် ဇာတ်ညွှန်း စာသားဖိုင် (Screenplay TXT)",
                desc = "ရုပ်ရှင်လောက စံချိန်စံညွှန်းအတိုင်း ဖော်မတ်ချထားသော အခန်းခေါင်းစဉ်၊ ဇာတ်ကောင် စကားပြောနှင့် အသံများ",
                icon = Icons.Default.Description,
                accentColor = StudioPurpleLight,
                onPreview = {
                    val content = ExportHelper.formatScreenplayTxt(project)
                    previewTitle = "${project.title} - Screenplay.txt"
                    previewContent = content
                },
                onCopy = {
                    val content = ExportHelper.formatScreenplayTxt(project)
                    ExportHelper.copyToClipboard(context, "Screenplay", content)
                },
                onShare = {
                    val content = ExportHelper.formatScreenplayTxt(project)
                    ExportHelper.shareText(context, "${project.title} - Screenplay.txt", content)
                }
            )
        }

        // 3. Scene Breakdown as TXT
        item {
            ExportOptionCard(
                title = "ဇာတ်ဝင်ခန်း ခွဲခြမ်းစိတ်ဖြာမှု စာသားဖိုင် (Scene Breakdown TXT)",
                desc = "ဇာတ်ဝင်ခန်း အသီးသီး၏ ကြာချိန်၊ နေရာ၊ လှုပ်ရှားမှု၊ ကင်မရာနှင့် ဆက်စပ်မှု မှတ်စုများ",
                icon = Icons.Default.Movie,
                accentColor = StudioBlue,
                onPreview = {
                    val content = ExportHelper.formatSceneBreakdownTxt(project)
                    previewTitle = "${project.title} - Scenes.txt"
                    previewContent = content
                },
                onCopy = {
                    val content = ExportHelper.formatSceneBreakdownTxt(project)
                    ExportHelper.copyToClipboard(context, "Scene Breakdown", content)
                },
                onShare = {
                    val content = ExportHelper.formatSceneBreakdownTxt(project)
                    ExportHelper.shareText(context, "${project.title} - Scenes.txt", content)
                }
            )
        }

        // 4. Complete Production Package TXT
        item {
            ExportOptionCard(
                title = "ထုတ်လုပ်မှု အလုံးစုံ ပက်ကေ့ချ် (Production Master Package TXT)",
                desc = "ဇာတ်လမ်းစိတ်ကူး၊ ဇာတ်ကောင် ရုပ်သွင်ဖော်ပြချက်၊ နေရာများနှင့် ဗီဒီယို ပရော့မ် ဗားရှင်းများ အားလုံး စုစည်းမှု",
                icon = Icons.Default.FolderZip,
                accentColor = StudioAmber,
                onPreview = {
                    val content = ExportHelper.formatProductionPackageTxt(project)
                    previewTitle = "${project.title} - Production Master.txt"
                    previewContent = content
                },
                onCopy = {
                    val content = ExportHelper.formatProductionPackageTxt(project)
                    ExportHelper.copyToClipboard(context, "Production Package", content)
                },
                onShare = {
                    val content = ExportHelper.formatProductionPackageTxt(project)
                    ExportHelper.shareText(context, "${project.title} - Production Master.txt", content)
                }
            )
        }
    }

    // Preview Dialog
    if (previewContent != null && previewTitle != null) {
        AlertDialog(
            onDismissRequest = {
                previewTitle = null
                previewContent = null
            },
            title = {
                Text(
                    text = previewTitle!!,
                    color = StudioTextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            },
            text = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(360.dp)
                        .background(StudioSurface, RoundedCornerShape(8.dp))
                        .border(1.dp, StudioBorder, RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    LazyColumn {
                        item {
                            Text(
                                text = previewContent!!,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontFamily = FontFamily.Monospace,
                                    color = StudioTextPrimary,
                                    fontSize = 11.sp,
                                    lineHeight = 16.sp
                                )
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        ExportHelper.copyToClipboard(context, previewTitle!!, previewContent!!)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = StudioPurple)
                ) {
                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("ကူးယူမည် (Copy)")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    previewTitle = null
                    previewContent = null
                }) {
                    Text("ပိတ်မည်", color = StudioTextSecondary)
                }
            },
            containerColor = StudioSurfaceElevated
        )
    }
}

@Composable
fun ExportOptionCard(
    title: String,
    desc: String,
    icon: ImageVector,
    accentColor: Color,
    onPreview: () -> Unit,
    onCopy: () -> Unit,
    onShare: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = StudioSurface),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
        modifier = Modifier.fillMaxWidth().testTag("export_card_${title.take(10)}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(accentColor.copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                    )
                    Text(
                        text = desc,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = StudioTextSecondary,
                            fontSize = 11.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onPreview,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("ကြည့်မည်", fontSize = 11.sp)
                }

                OutlinedButton(
                    onClick = onCopy,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("ကူးယူမည်", fontSize = 11.sp)
                }

                Button(
                    onClick = onShare,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                    modifier = Modifier.weight(1.2f)
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = null, tint = Color.Black, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("မျှဝေ / ထုတ်ယူ", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
        }
    }
}
