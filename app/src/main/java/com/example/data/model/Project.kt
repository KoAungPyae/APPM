package com.example.data.model

import java.util.UUID

data class Project(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val genre: String, // e.g., "Sci-Fi", "Fantasy", "Drama", "Action", "Horror", "Romance", "Mystery", "Animation"
    val storyIdea: String,
    val targetAudience: String = "Young Adults & Creators",
    val targetDurationSec: Int = 60, // in seconds
    val visualStyle: String = VisualStyles.CINEMATIC_REALISTIC,
    val customStyleNotes: String = "",
    val platform: String = Platforms.TIKTOK,
    val aspectRatio: String = AspectRatios.RATIO_9_16,
    val dialogueLanguage: String = "Burmese (မြန်မာ)",
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val storyDevelopment: StoryDevelopment = StoryDevelopment(),
    val screenplay: List<ScreenplayItem> = emptyList(),
    val characters: List<CharacterProfile> = emptyList(),
    val locations: List<LocationProfile> = emptyList(),
    val scenes: List<SceneItem> = emptyList(),
    val promptVersions: List<VideoPromptVersion> = emptyList()
)

object VisualStyles {
    const val STORYBOOK_2D = "2D Storybook Animation"
    const val ANIMATION_3D = "3D Animation"
    const val ANIME = "Anime"
    const val CINEMATIC_REALISTIC = "Realistic Cinematic Video"
    const val FANTASY = "Fantasy"
    const val CUSTOM = "Custom Style"

    val ALL = listOf(
        STORYBOOK_2D,
        ANIMATION_3D,
        ANIME,
        CINEMATIC_REALISTIC,
        FANTASY,
        CUSTOM
    )

    fun getBurmeseLabel(style: String): String = when (style) {
        STORYBOOK_2D -> "၂D ပုံပြင် အန်နီမေးရှင်း (2D Storybook)"
        ANIMATION_3D -> "၃D အန်နီမေးရှင်း (3D Animation)"
        ANIME -> "ဂျပန် အန်နီမေးစတိုင် (Anime)"
        CINEMATIC_REALISTIC -> "ရုပ်ရှင်ဆန်သော ဗီဒီယိုအစစ် (Cinematic Realistic)"
        FANTASY -> "စိတ်ကူးယဉ် အံ့ဖွယ်စတိုင် (Fantasy)"
        CUSTOM -> "စိတ်ကြိုက်စတိုင် (Custom Style)"
        else -> style
    }
}

object Platforms {
    const val TIKTOK = "TikTok"
    const val REELS = "Facebook Reels"
    const val SHORTS = "YouTube Shorts"
    const val YOUTUBE_LONG = "YouTube Long-form"

    val ALL = listOf(TIKTOK, REELS, SHORTS, YOUTUBE_LONG)

    fun getBurmeseLabel(platform: String): String = when (platform) {
        TIKTOK -> "TikTok (၉:၁၆ ဒေါင်လိုက်)"
        REELS -> "Facebook Reels (၉:၁၆)"
        SHORTS -> "YouTube Shorts (၉:၁၆)"
        YOUTUBE_LONG -> "YouTube Long-form (၁၆:၉ အလျားလိုက်)"
        else -> platform
    }
}

object AspectRatios {
    const val RATIO_9_16 = "9:16"
    const val RATIO_16_9 = "16:9"
    const val RATIO_1_1 = "1:1"

    val ALL = listOf(RATIO_9_16, RATIO_16_9, RATIO_1_1)

    fun getBurmeseLabel(ratio: String): String = when (ratio) {
        RATIO_9_16 -> "၉:၁၆ (ဒေါင်လိုက် ဗီဒီယိုတို)"
        RATIO_16_9 -> "၁၆:၉ (ရုပ်ရှင်/YouTube)"
        RATIO_1_1 -> "၁:၁ (စတုရန်းပုံ)"
        else -> ratio
    }
}

object Genres {
    val ALL = listOf(
        "Sci-Fi (သိပ္ပံစိတ်ကူးယဉ်)",
        "Fantasy (စိတ်ကူးယဉ် ဒဏ္ဍာရီ)",
        "Drama (လူမှုဘဝ ဒရာမာ)",
        "Action (အက်ရှင် သရုပ်ဆောင်)",
        "Thriller / Mystery (သည်းထိတ်ရင်ဖို)",
        "Horror (သရဲ/ထိတ်လန့်ဖွယ်)",
        "Romance (အချစ်ဇာတ်လမ်း)",
        "Comedy (ဟာသ)",
        "Historical / Heritage (သမိုင်းကြောင်း)",
        "Documentary / Tech (မှတ်တမ်းနှင့် နည်းပညာ)"
    )
}
