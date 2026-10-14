package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ai.GeminiStudioService
import com.example.ui.StudioViewModel
import com.example.ui.components.StudioBadge
import com.example.ui.components.StudioTopBar
import com.example.ui.theme.StudioAmber
import com.example.ui.theme.StudioBackground
import com.example.ui.theme.StudioBlue
import com.example.ui.theme.StudioBorder
import com.example.ui.theme.StudioCyan
import com.example.ui.theme.StudioEmerald
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
fun SettingsScreen(
    viewModel: StudioViewModel,
    modifier: Modifier = Modifier
) {
    val projects by viewModel.rawProjects.collectAsState()
    val isGeminiReady = GeminiStudioService.isConfigured()

    Scaffold(
        topBar = {
            StudioTopBar(
                title = "ဆက်တင် (Settings & Architecture)",
                subtitle = "စတူဒီယို သတ်မှတ်ချက်များနှင့် AI ချိတ်ဆက်မှု",
                showBackButton = false
            )
        },
        containerColor = StudioBackground,
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // App Identity & Info
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = StudioSurface),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(StudioPurple.copy(alpha = 0.2f), CircleShape)
                                    .border(1.dp, StudioPurpleLight, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(imageVector = Icons.Default.Movie, contentDescription = null, tint = StudioPurpleLight, modifier = Modifier.size(24.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "AI Video Studio — Story to Video",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = StudioTextPrimary
                                    )
                                )
                                Text(
                                    text = "Native Android Kotlin + Jetpack Compose",
                                    style = MaterialTheme.typography.labelSmall.copy(color = StudioCyan)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "ဤအက်ပ်သည် AI Video Generator များ (Google Flow, Sora, Runway, Kling) အတွက် Story Idea မှသည် တိကျသော ၁၄ ပိုင်းပါ Production Video Prompts များအထိ စနစ်တကျ ပြင်ဆင်ဖန်တီးပေးသော Pre-Production Tool ဖြစ်ပါသည်။",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = StudioTextSecondary,
                                lineHeight = 18.sp
                            )
                        )
                    }
                }
            }

            // AI Architecture & Secrets Panel Guidance
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = StudioSurface),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isGeminiReady) StudioEmerald.copy(alpha = 0.5f) else StudioAmber.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("ai_architecture_card")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (isGeminiReady) Icons.Default.CheckCircle else Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = if (isGeminiReady) StudioEmerald else StudioAmber,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Google Gemini AI ချိတ်ဆက်မှု",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = StudioTextPrimary
                                    )
                                )
                            }

                            StudioBadge(
                                text = if (isGeminiReady) "ချိတ်ဆက်ပြီး (Ready)" else "စောင့်ဆိုင်းဆဲ (Manual Mode)",
                                accentColor = if (isGeminiReady) StudioEmerald else StudioAmber
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        if (isGeminiReady) {
                            Text(
                                text = "✅ Gemini API Key ကို အောင်မြင်စွာ ရရှိထားပြီး ဖြစ်ပါသည်။ ဇာတ်လမ်းချဲ့ထွင်ခြင်းနှင့် ကင်မရာညွှန်ကြားချက်များကို AI ဖြင့် အချိန်နှင့်တစ်ပြေးညီ ပေါင်းစပ်ဖန်တီးနိုင်ပါသည်။",
                                style = MaterialTheme.typography.bodySmall.copy(color = StudioTextSecondary, lineHeight = 18.sp)
                            )
                        } else {
                            Text(
                                text = "လက်ရှိတွင် Manual Template & Rule-based Prompt Engine ဖြင့် အပြည့်အဝ အသုံးပြုနိုင်ပါသည်။\n\nAI အလိုအလျောက် ချဲ့ထွင်မှုကို အသုံးပြုလိုပါက AI Studio ရှိ Secrets Panel တွင် GEMINI_API_KEY ကို ထည့်သွင်းပေးနိုင်ပါသည်။ (Fake AI Response များကို လုံးဝ ပြသမထားပါ)",
                                style = MaterialTheme.typography.bodySmall.copy(color = StudioTextSecondary, lineHeight = 18.sp)
                            )
                        }
                    }
                }
            }

            // Database & Storage
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = StudioSurface),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Storage, contentDescription = null, tint = StudioBlue, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "ဒေသတွင်း ဒေတာ သိမ်းဆည်းမှု (Local Room Database)",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = StudioTextPrimary
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        val totalChars = projects.sumOf { it.characters.size }
                        val totalScenes = projects.sumOf { it.scenes.size }
                        val totalPrompts = projects.sumOf { it.promptVersions.size }

                        Text(
                            text = "• စုစုပေါင်း ပရောဂျက်များ: ${projects.size} ခု\n• ဇာတ်ကောင် ပရိုဖိုင်များ: $totalChars ဦး\n• ဇာတ်ဝင်ခန်းများ: $totalScenes ခန်း\n• သိမ်းဆည်းထားသော ပရော့မ်များ: $totalPrompts ခု",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = StudioTextSecondary,
                                lineHeight = 22.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "အက်ပ်ကို ပိတ်ပြီး ပြန်ဖွင့်သော်လည်း ဒေတာများ လုံးဝ ပျောက်ဆုံးခြင်းမရှိဘဲ ဆက်လက်တည်ရှိနေပါမည်။",
                            style = MaterialTheme.typography.labelSmall.copy(color = StudioCyan)
                        )
                    }
                }
            }

            // Language & Standards
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = StudioSurface),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Language, contentDescription = null, tint = StudioPurpleLight, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "ဘာသာစကား သတ်မှတ်ချက် (Language Architecture)",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = StudioTextPrimary
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "• အသုံးပြုသူ မျက်နှာပြင် (User Interface): မြန်မာယူနီကုဒ် (Burmese Unicode)\n• နည်းပညာ ဗီဒီယို ပရော့မ်များ (Technical Prompts): English Language (AI Video Models အတွက် အထူးသင့်လျော်သော အင်္ဂလိပ်စာလုံးများ)\n• ပရော့မ် အပိုင်းတိုင်းအတွက် မြန်မာဘာသာ ရှင်းလင်းချက် အပြည့်အစုံ ပါဝင်ပါသည်။",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = StudioTextSecondary,
                                lineHeight = 20.sp
                            )
                        )
                    }
                }
            }
        }
    }
}
