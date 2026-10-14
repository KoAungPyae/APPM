package com.example.data.model

import java.util.UUID

object DefaultTemplates {

    fun createSampleProject(): Project {
        val projId = UUID.randomUUID().toString()

        val charThurein = CharacterProfile(
            id = UUID.randomUUID().toString(),
            projectId = projId,
            name = "သူရိန် (Thurein)",
            age = "28",
            genderPresentation = "Male, athletic hero build",
            height = "180 cm",
            bodyType = "Lean muscular build",
            faceDescription = "Sharp jawline, determined brow, faint scar on left cheek",
            eyeDescription = "Intense dark brown eyes with cyber-scanner reflection",
            hairstyle = "Short textured undercut fade",
            hairColor = "Raven black",
            skinTone = "Warm golden olive tan",
            clothing = "Futuristic dark leather bomber jacket with neon purple glowing trim, black tactical cargo pants",
            shoes = "Reinforced combat boots with blue LED sole accents",
            accessories = "Holographic comms earpiece on right ear, silver signet ring",
            personality = "Brave, protective, observant",
            voiceDescription = "Deep resonant confident voice with calm authority",
            distinctiveFeatures = "Bioluminescent tattoo on right forearm",
            characterReferencePrompt = "28-year-old Burmese male cyberpunk investigator, short textured undercut, sharp features, glowing purple collar jacket, ultra-realistic character render"
        )

        val charMay = CharacterProfile(
            id = UUID.randomUUID().toString(),
            projectId = projId,
            name = "မေနဒီ (May Nadi)",
            age = "26",
            genderPresentation = "Female, graceful tech specialist",
            height = "168 cm",
            bodyType = "Slender agile posture",
            faceDescription = "Gentle oval face, intelligent gaze, slight smile",
            eyeDescription = "Expressive almond amber eyes",
            hairstyle = "Sleek high ponytail with loose side bangs",
            hairColor = "Midnight blue sheen over black",
            skinTone = "Fair porcelain",
            clothing = "Sleek carbon-weave kimono-cut coat with illuminated cyan circuitry patterns",
            shoes = "Lightweight high-top magnetic sneakers",
            accessories = "Digital gauntlet projector on left wrist",
            personality = "Genius hacker, witty, quick-thinking",
            voiceDescription = "Clear melodic cadence with sharp intellect",
            distinctiveFeatures = "Cyan neural optic interface tattoo near temple",
            characterReferencePrompt = "26-year-old Burmese woman cyberpunk hacker, midnight blue ponytailed hair, amber eyes, glowing cyan patterned coat, futuristic film lighting"
        )

        val locMandalayNight = LocationProfile(
            id = UUID.randomUUID().toString(),
            projectId = projId,
            name = "မန္တလေးမြို့ဟောင်း နီယွန်ည (Neo-Mandalay Ancient Moat)",
            environmentDescription = "Ancient brick fortress walls meeting sprawling cyberpunk neon high-rises across a misty water moat",
            backgroundElements = "Illuminated pagoda spires in the far sky, floating lantern drones, shimmering reflections in moat water",
            props = "Hover-rickshaws, holographic teak wood market stalls, digital food cart signs",
            lighting = "Dual-tone volumetric lighting: Warm golden candlelight mixed with vibrant purple and cyan neon glows",
            colorPalette = "Deep midnight blue, neon violet (#A855F7), electric cyan (#38BDF8), and sacred pagoda gold (#FBBF24)",
            weather = "Gentle night mist with subtle rain droplets reflecting street neon",
            timeOfDay = "Midnight (11:45 PM)",
            visualStyle = VisualStyles.CINEMATIC_REALISTIC,
            locationPrompt = "Cyberpunk Neo-Mandalay fortress moat at midnight, ancient pagodas with holographic halos, purple neon mist, hyper-detailed Unreal Engine 5 aesthetic"
        )

        val locLab = LocationProfile(
            id = UUID.randomUUID().toString(),
            projectId = projId,
            name = "လျှို့ဝှက် နည်းပညာ သုတေသနခန်း (Shadow Archive Lab)",
            environmentDescription = "Subterranean high-tech laboratory hidden beneath historic colonial arches",
            backgroundElements = "Holographic timeline displays, quantum server pillars, bronze ancient manuscripts preserved in glass chambers",
            props = "Floating glass data slates, crystalline power core",
            lighting = "Atmospheric rim lighting, deep shadows with soft pulsing blue ambient glow",
            colorPalette = "Dark obsidian, cobalt blue, bronze accents",
            weather = "Indoor climate-controlled dry chill",
            timeOfDay = "Night",
            visualStyle = VisualStyles.CINEMATIC_REALISTIC,
            locationPrompt = "High-tech subterranean archive lab, holographic consoles displaying ancient Burmese scripts, cinematic dark mood, anamorphic lens flare"
        )

        val scenes = listOf(
            SceneItem(
                id = UUID.randomUUID().toString(),
                projectId = projId,
                sceneNumber = 1,
                title = "နီယွန်ကျုံးဘေးမှ တွေ့ဆုံမှု (Rendezvous by the Moat)",
                durationSec = 10,
                locationName = locMandalayNight.name,
                characterNames = listOf(charThurein.name, charMay.name),
                openingAction = "Thurein stands on the ancient stone bridge, mist curling around his combat boots as neon reflections ripple below",
                mainAction = "May steps out from the shadows of a floating drone stall, handing a glowing memory crystal to Thurein as alarms suddenly echo",
                characterEmotions = "Tense anticipation shifting into sudden vigilance",
                dialogue = "သူတို့ နောက်က လိုက်လာပြီ သူရိန်... (They're right behind us, Thurein...)",
                cameraDirection = "Low-angle tracking dolly shot pushing forward, tilting up to frame the ancient fortress wall against neon skyscrapers",
                soundEffects = "Distant drone hum, water ripples, sudden electronic siren pulse",
                endingAction = "Thurein activates his forearm scanner, glancing back over his shoulder into the camera",
                continuityNotes = "Keep rain mist density consistent into Scene 2",
                openingFrame = "Wide establishing shot of the illuminated moat water reflecting purple neon spires",
                characterPositions = "Thurein at frame-center-left; May approaches from right background shadow",
                characterExpressions = "Serious, alert, watchful eyes scanning the perimeter",
                mainMovement = "May walks swiftly forward; Thurein turns slightly to receive the crystal",
                cameraAngle = "Low-angle medium shot, 35mm lens equivalent",
                cameraMovement = "Smooth steadycam slow dolly push-in",
                backgroundDetails = "Rain mist droplets drifting slowly through neon light cones",
                lightingDetail = "Strong cyan rim light on Thurein's leather jacket; warm gold key light on May's face",
                importantVisualEvent = "The crystal pulses with a brilliant violet flare when touched",
                finalFrame = "Close-up on Thurein's face with neon reflection in his eyes",
                nextSceneTransition = "QUICK CUT TO interior lab security alert"
            ),
            SceneItem(
                id = UUID.randomUUID().toString(),
                projectId = projId,
                sceneNumber = 2,
                title = "သုတေသနခန်းအတွင်း ဝင်ရောက်ခြင်း (Infiltration to Archive)",
                durationSec = 10,
                locationName = locLab.name,
                characterNames = listOf(charThurein.name, charMay.name),
                openingAction = "Heavy blast doors slide open with a pressurized hiss, revealing the shadowy subterranean archive",
                mainAction = "May rushes to the central holographic console and connects her digital gauntlet, decrypting the ancient file",
                characterEmotions = "Focused urgency, breakthrough excitement",
                dialogue = "ဒီမှာပဲ... မန္တလေးမြို့ရဲ့ ပျောက်ဆုံးနေတဲ့ ဒေတာကုဒ်တွေ အားလုံး ရှိနေတယ်! (Here it is... all the lost data codes of the city!)",
                cameraDirection = "Orbital 360-degree crane shot spiraling down around the circular holographic dais",
                soundEffects = "Hydraulic door hiss, rapid keyboard clicks, resonant harmonic chime",
                endingAction = "A 3D holographic map of the entire golden city illuminates above them",
                continuityNotes = "May's coat circuitry pulses cyan in sync with the console light",
                openingFrame = "Door frame silhouette backlit by deep blue corridor light",
                characterPositions = "May at the central console; Thurein guarding the doorway entrance",
                characterExpressions = "May's eyes wide with wonder; Thurein watchful and protective",
                mainMovement = "Rapid hand gestures manipulating 3D floating interface",
                cameraAngle = "Eye-level medium-wide transitioning into high-angle view",
                cameraMovement = "Dynamic arc around the glowing console table",
                backgroundDetails = "Glass storage cylinders pulsing with blue quantum energy",
                lightingDetail = "Volumetric blue projector beams cutting through dust motes",
                importantVisualEvent = "Hologram blossoms into a giant shimmering golden lotus map",
                finalFrame = "Over-the-shoulder shot looking past Thurein at the glowing map",
                nextSceneTransition = "FADE TO dramatic finale cliffhanger"
            )
        )

        val screenplay = listOf(
            ScreenplayItem(
                sceneHeading = "EXT. NEO-MANDALAY MOAT - NIGHT",
                actionDescription = "A dense violet fog creeps across the ancient stone embankment. Raindrops strike the water, scattering the neon reflections of towering sky-spires. THUREIN (28) stands sentinel under the stone arch.",
                characterName = "THUREIN",
                dialogue = "နောက်ကျနေပြီ၊ မေနဒီ... (You're late, May Nadi...)",
                emotion = "without looking back",
                soundEffects = "Wind howling through ancient parapets, low frequency drone motor",
                transition = "CUT TO:"
            ),
            ScreenplayItem(
                sceneHeading = "EXT. STONE BRIDGE - CONTINUOUS",
                actionDescription = "MAY NADI (26) emerges from the mist, her carbon-weave coat glowing with gentle cyan circuitry lines. She pulls a gleaming crystalline drive from her sleeve.",
                characterName = "MAY NADI",
                dialogue = "သူတို့ နောက်က လိုက်လာပြီ သူရိန်။ အခုချက်ချင်း ထွက်မှ ဖြစ်မယ်! (They're right behind us, Thurein. We must move now!)",
                emotion = "breathless but firm",
                soundEffects = "Footsteps splashing on wet stone, sudden drone alarm screeching in distance",
                transition = "CUT TO:"
            )
        )

        return Project(
            id = projId,
            title = "မန္တလေးညရဲ့ လျှို့ဝှက်ချက် (Mystery of Mandalay Night)",
            genre = "Sci-Fi (သိပ္ပံစိတ်ကူးယဉ်)",
            storyIdea = "အနာဂတ် ဆိုက်ဘာပန့်ခ် မန္တလေးမြို့တော်ကြီးတွင် ရှေးဟောင်းအမွေအနှစ်များနှင့် ခေတ်မီနည်းပညာ ရောယှက်နေစဉ်၊ မြို့တော်ကြီးကို ကာကွယ်ရန် ပျောက်ဆုံးနေသော ဒေတာကုဒ်များကို စုံထောက်သူရိန်နှင့် နည်းပညာရှင် မေနဒီတို့ ရှာဖွေဖော်ထုတ်သည့် ဇာတ်လမ်း။",
            targetAudience = "Sci-Fi & Action Lovers, AI Video Community",
            targetDurationSec = 60,
            visualStyle = VisualStyles.CINEMATIC_REALISTIC,
            customStyleNotes = "Blade Runner meets ancient Burmese architecture, anamorphic lens flares, rich neon reflections, Unreal Engine 5 cinematography",
            platform = Platforms.TIKTOK,
            aspectRatio = AspectRatios.RATIO_9_16,
            dialogueLanguage = "Burmese (မြန်မာစကားပြော/စာတန်းထိုး)",
            notes = "Google Flow နှင့် Sora ဗီဒီယို မော်ဒယ်များတွင် အသုံးပြုရန် ပြင်ဆင်ထားသော ပရော့မ်များ ပါဝင်သည်။",
            storyDevelopment = StoryDevelopment(
                storyIdea = "အနာဂတ် မန္တလေးမြို့တွင် မြို့တော်စွမ်းအင်ဗဟိုချက်ကို ထိန်းချုပ်မည့် ရှေးဟောင်းဒေတာကို ပြန်လည်ရယူရန် စွန့်စားရသည့် ဇာတ်လမ်း။",
                logline = "အန္တရာယ်ကြီးမားသော ဆိုက်ဘာမာဖီးယားများလက်မှ မြို့တော်၏ နှလုံးသားကို ကယ်တင်ရန် စုံထောက်တစ်ဦးနှင့် ဟက်ကာမလေးတို့ ညတွင်းချင်း လျှို့ဝှက်စစ်ဆင်ရေး ဆင်နွှဲရပုံ။",
                mainCharactersSummary = "သူရိန် (သတ္တိရှိသော စုံထောက်) နှင့် မေနဒီ (ဉာဏ်ကြီးရှင် နည်းပညာရှင်)",
                mainConflict = "ဆိုက်ဘာမာဖီးယားဂိုဏ်းက မြို့တော်၏ ဒေတာဗဟိုကို ဖျက်ဆီးရန် ကြိုးပမ်းခြင်း",
                storyTheme = "သမိုင်းအမွေအနှစ်နှင့် အနာဂတ်နည်းပညာ သဟဇာတဖြစ်မှု၊ သစ္စာရှိမှု",
                climax = "လျှို့ဝှက်သုတေသနခန်းအတွင်း ရန်သူ့ဒရုန်းများ ဝိုင်းရံထားစဉ် ဒေတာကုဒ်ကို အောင်မြင်စွာ decrypt ပြုလုပ်ခြင်း",
                ending = "မန္တလေးမြို့တော်ကြီး ရွှေရောင်အလင်းတန်းများဖြင့် ပြန်လည်လင်းလက်လာပြီး ငြိမ်းချမ်းသွားခြင်း"
            ),
            screenplay = screenplay,
            characters = listOf(charThurein, charMay),
            locations = listOf(locMandalayNight, locLab),
            scenes = scenes,
            promptVersions = listOf(
                VideoPromptVersion(
                    id = UUID.randomUUID().toString(),
                    projectId = projId,
                    sceneTitle = "Scene 1: နီယွန်ကျုံးဘေးမှ တွေ့ဆုံမှု",
                    versionTitle = "Cinematic 4K Google Flow Prompt (v1.0)",
                    englishPrompt = """
[1. CLIP DURATION & FORMAT]
10 seconds (Aspect Ratio: 9:16 vertical)

[2. VISUAL STYLE & AESTHETICS]
Realistic Cinematic Video - Notes: Blade Runner meets ancient Burmese architecture, anamorphic lens flares, rich neon reflections, ultra-high resolution, cinematic color grading

[3. CHARACTER APPEARANCE]
Character: သူရိန် (Thurein); 28 years old; Male, athletic hero build; Physique: 180 cm, Lean muscular build; Facial features: Sharp jawline, determined brow, faint scar on left cheek; Eyes: Intense dark brown eyes with cyber-scanner reflection; Hair: Raven black Short textured undercut fade; Skin tone: Warm golden olive tan; Outfit: Futuristic dark leather bomber jacket with neon purple glowing trim, black tactical cargo pants; Footwear: Reinforced combat boots with blue LED sole accents; Distinctive traits: Bioluminescent tattoo on right forearm

[4. LOCATION & ENVIRONMENT]
Setting: မန္တလေးမြို့ဟောင်း နီယွန်ည (Neo-Mandalay Ancient Moat); Ancient brick fortress walls meeting sprawling cyberpunk neon high-rises across a misty water moat; Atmosphere: Midnight (11:45 PM), Gentle night mist with subtle rain droplets reflecting street neon; Background: Illuminated pagoda spires in the far sky, floating lantern drones; Lighting: Dual-tone volumetric lighting: Warm golden candlelight mixed with vibrant purple and cyan neon glows

[5. OPENING FRAME]
Thurein stands on the ancient stone bridge, mist curling around his combat boots; Positions: Thurein at frame-center-left

[6. MAIN ACTIONS]
May steps out from the shadows of a floating drone stall, handing a glowing memory crystal to Thurein as alarms suddenly echo

[7. FACIAL EXPRESSIONS & EMOTION]
Tense anticipation shifting into sudden vigilance; Serious, alert, watchful eyes

[8. BODY MOVEMENT & PACING]
May walks swiftly forward; Thurein turns slightly to receive the crystal

[9. CAMERA DIRECTION]
Low-angle tracking dolly shot pushing forward, tilting up to frame the ancient fortress wall against neon skyscrapers; Angle: Low-angle medium shot, 35mm lens equivalent; Movement: Smooth steadycam slow dolly push-in

[10. LIGHTING & ATMOSPHERE]
Dual-tone volumetric lighting; Strong cyan rim light on leather jacket, warm gold key light

[11. DIALOGUE & LIP SYNC]
Spoken dialogue: "သူတို့ နောက်က လိုက်လာပြီ သူရိန်... (They're right behind us, Thurein...)" (Burmese)

[12. ENVIRONMENTAL SOUND & AUDIO]
Audio atmosphere: Distant drone hum, water ripples, sudden electronic siren pulse

[13. ENDING FRAME]
Thurein activates his forearm scanner, glancing back over his shoulder into the camera; Final frame close-up

[14. CONTINUITY INSTRUCTIONS]
Keep rain mist density consistent into Scene 2, seamless scene flow
                    """.trimIndent(),
                    burmeseExplanation = "🎬 ဇာတ်ဝင်ခန်း ၁ အတွက် AI ဗီဒီယို ပရော့မ် ရှင်းလင်းချက်:\n• ကလစ်ကြာချိန် ၁၀ စက္ကန့်နှင့် ၉:၁၆ ဒေါင်လိုက် ဖော်မတ်\n• မန္တလေးမြို့ဟောင်း ကျုံးဘေး နီယွန်ညအလှနှင့် ဇာတ်ကောင် သူရိန်၏ ရုပ်သွင်ပြင်\n• ကင်မရာ ရိုက်ချက်၊ အလင်းအမှောင်နှင့် အသံအထူးပြုလုပ်ချက်များကို စနစ်တကျ ပေါင်းစပ်ထားသည်။",
                    createdAt = System.currentTimeMillis()
                )
            )
        )
    }
}
