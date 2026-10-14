package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.ai.PromptBuilder
import com.example.data.local.ProjectConverters
import com.example.data.model.CharacterProfile
import com.example.data.model.DefaultTemplates
import com.example.data.model.LocationProfile
import com.example.data.model.Project
import com.example.data.model.SceneItem
import com.example.data.model.ScreenplayItem
import com.example.data.model.StoryDevelopment
import com.example.util.ExportHelper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context matches AI Video Studio`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("AI Video Studio", appName)
    }

    @Test
    fun `promptBuilder generates 14-part prompt with Burmese explanation`() {
        val sampleProject = DefaultTemplates.createSampleProject()
        val scene = sampleProject.scenes.first()
        val result = PromptBuilder.buildScenePrompt(
            project = sampleProject,
            scene = scene,
            projectCharacters = sampleProject.characters,
            projectLocations = sampleProject.locations
        )

        assertNotNull(result.englishPrompt)
        assertTrue(result.englishPrompt.contains("AI VIDEO PRODUCTION PROMPT"))
        assertTrue(result.englishPrompt.contains("CLIP DURATION & FORMAT"))
        assertTrue(result.englishPrompt.contains("VISUAL STYLE"))
        assertTrue(result.burmeseExplanation.contains("ရှင်းလင်းချက်"))
    }

    @Test
    fun `screenplay formatted text generates proper layout`() {
        val item = ScreenplayItem(
            sceneHeading = "INT. CYBERPUNK LAB - NIGHT",
            actionDescription = "Thurein activates his scanner.",
            characterName = "THUREIN",
            dialogue = "We have found the archive.",
            emotion = "whispering",
            soundEffects = "humming electronic core",
            transition = "CUT TO:"
        )
        val formatted = item.toScreenplayFormattedText()
        assertTrue(formatted.contains("INT. CYBERPUNK LAB - NIGHT"))
        assertTrue(formatted.contains("THUREIN"))
        assertTrue(formatted.contains("(whispering)"))
        assertTrue(formatted.contains("\"We have found the archive.\""))
        assertTrue(formatted.contains("[SFX: humming electronic core]"))
        assertTrue(formatted.contains("CUT TO:"))
    }

    @Test
    fun `project converters serialize and deserialize correctly`() {
        val converters = ProjectConverters()
        val storyDev = StoryDevelopment(
            storyIdea = "Test Idea",
            logline = "Test Logline",
            climax = "Test Climax"
        )
        val json = converters.fromStoryDevelopment(storyDev)
        val restored = converters.toStoryDevelopment(json)
        assertEquals("Test Idea", restored.storyIdea)
        assertEquals("Test Logline", restored.logline)
        assertEquals("Test Climax", restored.climax)
    }

    @Test
    fun `export helper generates valid project json`() {
        val project = DefaultTemplates.createSampleProject()
        val json = ExportHelper.formatProjectJson(project)
        assertTrue(json.contains("\"title\":"))
        assertTrue(json.contains("\"storyDevelopment\":"))
        assertTrue(json.contains("\"characters\":"))
        assertTrue(json.contains("\"scenes\":"))
    }
}
