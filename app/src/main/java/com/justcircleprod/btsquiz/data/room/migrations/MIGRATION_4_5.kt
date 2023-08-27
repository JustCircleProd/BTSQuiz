package com.justcircleprod.btsquiz.data.room.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.justcircleprod.btsquiz.data.models.questions.AudioQuestion
import com.justcircleprod.btsquiz.data.models.questions.TextQuestion
import com.justcircleprod.btsquiz.data.models.questions.VideoQuestion


val MIGRATION_4_5: Migration = object : Migration(4, 5) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"Какой хештег, связанный с Jin, стал вирусным после Billboard Music Awards в 2017 году?\" WHERE id = 96"
        )


        for (question in textQuestions) {
            database.execSQL(
                """INSERT OR IGNORE INTO text_questions
                | (id, question, first_option, second_option, 
                | third_option, fourth_option, answer_num, points) 
                | VALUES (${question.id}, '${question.question}',
                | "${question.firstOption}", "${question.secondOption}",
                | "${question.thirdOption}", "${question.fourthOption}",
                | ${question.answerNum}, ${question.points})""".trimMargin()
            )
        }

        for (question in audioQuestions) {
            database.execSQL(
                """INSERT OR IGNORE INTO audio_questions
                | (id, audio_entry_name, first_option, second_option, 
                | third_option, fourth_option, answer_num, points) 
                | VALUES (${question.id}, "${question.audio_entry_name}",
                | "${question.firstOption}", "${question.secondOption}",
                | "${question.thirdOption}", "${question.fourthOption}",
                | ${question.answerNum}, ${question.points})""".trimMargin()
            )
        }

        for (question in videoQuestions) {
            database.execSQL(
                """INSERT OR IGNORE INTO video_questions    
                | (id, video_entry_name, first_option, second_option, 
                | third_option, fourth_option, answer_num, points) 
                | VALUES (${question.id}, "${question.video_entry_name}",
                | "${question.firstOption}", "${question.secondOption}",
                | "${question.thirdOption}", "${question.fourthOption}",
                | ${question.answerNum}, ${question.points})""".trimMargin()
            )
        }
    }

    val textQuestions = listOf(
        TextQuestion(
            154,
            "Какого цвета волосы у V в клипе \"Boy With Luv\"?",
            "Фиолетовые", "Розовые",
            "Серебряные", "Синие",
            4, 600,
        ),
        TextQuestion(
            155,
            "Кто первым присоединился к BTS?",
            "RM", "J-Hope",
            "Suga", "Jungkook",
            1, 500,
        ),
        TextQuestion(
            156,
            "Кто вторым присоединился к BTS?",
            "J-Hope", "Suga",
            "Jin", "V",
            2, 550,
        ),
        TextQuestion(
            157,
            "Кто третьим присоединился к BTS?",
            "RM", "Jimin",
            "J-Hope", "Suga",
            3, 550,
        ),
        TextQuestion(
            158,
            "Кто четвёртым присоединился к BTS?",
            "V", "J-Hope",
            "Jimin", "Jin",
            4, 550,
        ),
        TextQuestion(
            159,
            "Кто пятым присоединился к BTS?",
            "Jungkook", "Suga",
            "J-Hope", "V",
            1, 550,
        ),
        TextQuestion(
            160,
            "Кто шестым присоединился к BTS?",
            "Jimin", "V",
            "RM", "Jin",
            2, 550,
        ),
        TextQuestion(
            161,
            "Кто последним присоединился к BTS?",
            "Jungkook", "Jin",
            "Jimin", "J-Hope",
            3, 500,
        ),
        TextQuestion(
            162,
            "Одним из прозвищ Jin является",
            "Hobi", "Mochi",
            "Golden Maknae", "Worldwide Handsome",
            4, 500,
        ),
        TextQuestion(
            163,
            "Одним из прозвищ Jungkook является",
            "Golden Maknae", "Worldwide Handsome",
            "Taetae", "Mochi",
            1, 500,
        ),
        TextQuestion(
            164,
            "Одним из прозвищ V является",
            "Minstradamus", "Taetae",
            "Mochi", "Worldwide Handsome",
            2, 500,
        ),
        TextQuestion(
            165,
            "Одним из прозвищ Suga является",
            "Golden Maknae", "God of Destruction",
            "Minstradamus", "Taetae",
            3, 500,
        ),
        TextQuestion(
            166,
            "Одним из прозвищ J-Hope является",
            "Minstradamus", "Taetae",
            "Golden Maknae", "Hobi",
            4, 500,
        ),
        TextQuestion(
            167,
            "Одним из прозвищ RM является",
            "God of Destruction", "Hobi",
            "Minstradamus", "Hobi",
            1, 500,
        ),
        TextQuestion(
            168,
            "Одним из прозвищ Jimin является",
            "Worldwide Handsome", "God of Destruction",
            "Mochi", "Taetae",
            3, 500,
        ),
    )

    val audioQuestions = listOf(
        AudioQuestion(
            112,
            "jimin_set_me_free",
            "Jimin 'Like Crazy'", "Filter",
            "Jimin 'Set Me Free Pt.2'", "TAEYANG x Jimin - 'VIBE'",
            3, 500,
        ),
        AudioQuestion(
            113,
            "i_like_it",
            "Blue & Grey", "Film out",
            "134340 (Pluto)", "I Like It (좋아요)",
            4, 600,
        ),
        AudioQuestion(
            114,
            "if_i_ruled_the_world",
            "If I Ruled the World", "Life Goes On",
            "Outro: Wings", "N.O (방탄소년단)",
            1, 400,
        ),
        AudioQuestion(
            115,
            "no",
            "HOME", "N.O (방탄소년단)",
            "Mama", "DDAENG",
            2, 400,
        ),
        AudioQuestion(
            116,
            "outro_luv_in_skool",
            "Boy With Luv", "My Time",
            "Outro: Luv in Skool", "Trivia: Seesaw",
            3, 500,
        ),
        AudioQuestion(
            117,
            "paldogangsan",
            "Epiphany", "We On",
            "Friends", "Paldogangsan (Satoori Rap)",
            4, 550,
        ),
        AudioQuestion(
            118,
            "path",
            "Path (Road)", "Take Two",
            "Spring Day", "Whalien 52",
            1, 600,
        ),
        AudioQuestion(
            119,
            "seven",
            "Trivia: Love", "Jung Kook 'Seven (feat. Latto)'",
            "The Planet", "TAEYANG x Jimin - 'VIBE'",
            2, 350,
        ),
        AudioQuestion(
            120,
            "we_on",
            "Arson", "Attack on Bangtan",
            "We On", "Cypher Pt.1",
            3, 600,
        ),
    )

    val videoQuestions = listOf(
        VideoQuestion(
            35,
            "no_video",
            "PERSONA", "N.O (방탄소년단)",
            "Dynamite", "Black Swan",
            2, 600,
        ),
        VideoQuestion(
            36,
            "seven_video",
            "Bad Decisions", "Make It Right",
            "Jung Kook 'Seven (feat. Latto)'", "FAKE LOVE",
            3, 550,
        ),
    )
}