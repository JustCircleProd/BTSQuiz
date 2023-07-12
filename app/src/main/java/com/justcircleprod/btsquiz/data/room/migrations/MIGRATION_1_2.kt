package com.justcircleprod.btsquiz.data.room.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.justcircleprod.btsquiz.data.models.AudioQuestion
import com.justcircleprod.btsquiz.data.models.ImageQuestion
import com.justcircleprod.btsquiz.data.models.TextQuestion
import com.justcircleprod.btsquiz.data.models.VideoQuestion


val MIGRATION_1_2: Migration = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        for (question in textQuestions) {
            database.execSQL(
                """INSERT OR IGNORE INTO text_questions
                | (id, question, first_option, second_option, 
                | third_option, fourth_option, answer_num, points) 
                | VALUES (${question.id}, "${question.question}",
                | "${question.firstOption}", "${question.secondOption}",
                | "${question.thirdOption}", "${question.fourthOption}",
                | ${question.answerNum}, ${question.points})""".trimMargin()
            )
        }

        for (question in imageQuestions) {
            database.execSQL(
                """INSERT OR IGNORE INTO image_questions
                | (id, image_entry_name, first_option, second_option, 
                | third_option, fourth_option, answer_num, points) 
                | VALUES (${question.id}, "${question.image_entry_name}",
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
            148,
            "Сколько различных наград выиграли BTS к середине 2023 года?",
            "483", "128",
            "255", "600",
            1, 600,
        ),
        TextQuestion(
            149,
            "Кто из участников хорошо играет на фортепиано?",
            "Jimin", "Suga",
            "RM", "Jungkook",
            2, 550,
        ),
        TextQuestion(
            150,
            "Сколько участников BTS родились в Сеуле?",
            "1", "3",
            "0", "2",
            3, 450,
        ),
        TextQuestion(
            151,
            "Кто из участников стал амбассадором баскетбольной лиги NBA в 2023 году?",
            "Jimin", "Jungkook",
            "V", "Suga",
            4, 550,
        ),
        TextQuestion(
            152,
            "Кто из участников любит коллекционировать вещи, связанные с Марио?",
            "Jin", "RM",
            "J-Hope", "Suga",
            1, 550,
        ),
        TextQuestion(
            153,
            "Кто из участников хорошо рисует?",
            "V и J-Hope", "Jimin и Jungkook",
            "Jungkook и RM", "Jin и Jimin",
            2, 600,
        )
    )

    val imageQuestions = listOf(
        ImageQuestion(
            210,
            "jeon_jung_kook_26",
            "RM", "V",
            "Jungkook", "Suga",
            3, 550,
        ),
        ImageQuestion(
            211,
            "jimin_jhope_1",
            "Jimin, Jin", "J-Hope, Jimin",
            "RM, J-Hope", "Jimin, J-Hope",
            4, 600,
        ),
        ImageQuestion(
            212,
            "jimin_jhope_2",
            "Jimin, J-Hope", "Jimin, Jin",
            "J-Hope, Jimin", "RM, V",
            1, 600,
        ),
        ImageQuestion(
            213,
            "jung_ho_seok_26",
            "Jimin", "J-Hope",
            "RM", "Jin",
            2, 400,
        ),
        ImageQuestion(
            214,
            "jung_ho_seok_27",
            "V", "Jin",
            "J-Hope", "Suga",
            3, 550,
        ),
        ImageQuestion(
            215,
            "kim_nam_joon_25",
            "Suga", "J-Hope",
            "Jungkook", "RM",
            4, 600,
        ),
        ImageQuestion(
            216,
            "kim_seok_jin_28",
            "Jin", "J-Hope",
            "V", "Suga",
            1, 400,
        ),
        ImageQuestion(
            217,
            "kim_tae_hyung_29",
            "Jin", "V",
            "Jimin", "J-Hope",
            2, 500,
        ),
        ImageQuestion(
            218,
            "min_yoon_gi_28",
            "RM", "Jungkook",
            "Suga", "Jimin",
            3, 450,
        ),
        ImageQuestion(
            219,
            "park_ji_min_27",
            "Suga", "J-Hope",
            "Jin", "Jimin",
            4, 450,
        )
    )

    val audioQuestions = listOf(
        AudioQuestion(
            95,
            "agust_d_amygdala",
            "Agust D '대취타' (Daechwita)", "Agust D 'AMYGDALA'",
            "Agust D 'Agust D'", "Agust D '해금' (Haegeum)",
            2, 400,
        ),
        AudioQuestion(
            96,
            "agust_d_haegeum",
            "Agust D '대취타' (Daechwita)", "Agust D 'give it to me'",
            "Agust D '해금' (Haegeum)", "Agust D 'AMYGDALA'",
            3, 500,
        ),
        AudioQuestion(
            97,
            "grade_2nd",
            "Life Goes On", "Coffee",
            "BEGIN", "2nd Grade",
            4, 600,
        ),
        AudioQuestion(
            98,
            "jhope_on_the_street",
            "J-Hope 'on the street (with J. Cole)'", "J-Hope '방화 (Arson)'",
            "J-Hope 'MORE'", "J-Hope 'Daydream (백일몽)'",
            1, 600,
        ),
        AudioQuestion(
            99,
            "jimin_like_crazy",
            "Jimin 'Set Me Free Pt.2'", "Jimin 'Like Crazy'",
            "TAEYANG x Jimin - 'VIBE'", "Serendipity",
            2, 600,
        ),
        AudioQuestion(
            100,
            "jimin_set_me_free",
            "Jimin 'Like Crazy'", "Filter",
            "Jimin 'Set Me Free Pt.2'", "TAEYANG x Jimin- 'VIBE'",
            3, 500,
        )
    )

    val videoQuestions = listOf(
        VideoQuestion(
            26,
            "agust_d_amygdala_video",
            "Agust D 'AMYGDALA'", "Run",
            "Agust D '해금' (Haegeum)", "Agust D 'give it to me'",
            1, 600,
        ),
        VideoQuestion(
            27,
            "agust_d_haegeum_video",
            "Black Swan", "Agust D '해금' (Haegeum)",
            "Jimin 'Like Crazy'", "Agust D 'AMYGDALA'",
            2, 600,
        ),
        VideoQuestion(
            28,
            "arson_video",
            "J-Hope 'MORE'", "J-Hope 'on the street (with J. Cole)'",
            "J-Hope '방화 (Arson)'", "Just One Day",
            3, 600,
        ),
        VideoQuestion(
            29,
            "jhope_on_the_street_video",
            "J-Hope '방화 (Arson)'", "Epilogue: Young Forever",
            "J-Hope 'Daydream (백일몽)'", "J-Hope 'on the street (with J. Cole)'",
            4, 600,
        ),
        VideoQuestion(
            30,
            "jimin_like_crazy_video",
            "Jimin 'Like Crazy'", "TAEYANG x Jimin - 'VIBE'",
            "Stay Gold", "Serendipity",
            1, 600,
        ),
        VideoQuestion(
            31,
            "stay_gold_video",
            "DNA", "Stay Gold",
            "Serendipity", "Jimin 'Like Crazy'",
            2, 600,
        ),
    )
}