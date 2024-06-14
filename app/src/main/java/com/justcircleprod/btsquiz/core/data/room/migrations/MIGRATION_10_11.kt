package com.justcircleprod.btsquiz.core.data.room.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.justcircleprod.btsquiz.core.data.models.questions.AudioQuestion
import com.justcircleprod.btsquiz.core.data.models.questions.VideoQuestion


val MIGRATION_10_11: Migration = object : Migration(10, 11) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "UPDATE OR IGNORE text_questions " +
                    "SET question = \"Какие два персонажа из мстителей так или иначе упоминались Jimin как любимые?\", " +
                    "first_option = \"Тор, Чёрная вдова\", " +
                    "second_option = \"Железный человек, Халк\", " +
                    "third_option = \"Капитан Америка, Халк\", " +
                    "fourth_option = \"Вижн, Железный человек\" " +
                    "WHERE id = 61"
        )

        for (question in audioQuestions) {
            db.execSQL(
                """INSERT OR IGNORE INTO audio_questions
                | (id, audio_entry_name, first_option, second_option, 
                | third_option, fourth_option, answer_num, points) 
                | VALUES (${question.id}, "${question.audioEntryName}",
                | "${question.firstOption}", "${question.secondOption}",
                | "${question.thirdOption}", "${question.fourthOption}",
                | ${question.answerNum}, ${question.points})""".trimMargin()
            )
        }

        for (question in videoQuestions) {
            db.execSQL(
                """INSERT OR IGNORE INTO video_questions    
                | (id, video_entry_name, first_option, second_option, 
                | third_option, fourth_option, answer_num, points) 
                | VALUES (${question.id}, "${question.videoEntryName}",
                | "${question.firstOption}", "${question.secondOption}",
                | "${question.thirdOption}", "${question.fourthOption}",
                | ${question.answerNum}, ${question.points})""".trimMargin()
            )
        }


        db.execSQL(
            "UPDATE OR IGNORE levels_progress SET questions_quantity = 52 WHERE id = 1"
        )

        db.execSQL(
            "UPDATE OR IGNORE levels_progress SET questions_quantity = 57 WHERE id = 2"
        )

        db.execSQL(
            "UPDATE OR IGNORE levels_progress SET questions_quantity = 99 WHERE id = 3"
        )

        db.execSQL(
            "UPDATE OR IGNORE levels_progress SET questions_quantity = 100 WHERE id = 4"
        )

        db.execSQL(
            "UPDATE OR IGNORE levels_progress SET questions_quantity = 108 WHERE id = 5"
        )

        db.execSQL(
            "UPDATE OR IGNORE levels_progress SET questions_quantity = 111 WHERE id = 6"
        )

        db.execSQL(
            "UPDATE OR IGNORE levels_progress SET questions_quantity = 173 WHERE id = 7"
        )


        db.execSQL(
            "UPDATE OR IGNORE locked_levels SET price = 1350 WHERE id = 2"
        )

        db.execSQL(
            "UPDATE OR IGNORE locked_levels SET price = 2100 WHERE id = 3"
        )

        db.execSQL(
            "UPDATE OR IGNORE locked_levels SET price = 4800 WHERE id = 4"
        )

        db.execSQL(
            "UPDATE OR IGNORE locked_levels SET price = 5800 WHERE id = 5"
        )

        db.execSQL(
            "UPDATE OR IGNORE locked_levels SET price = 7350 WHERE id = 6"
        )

        db.execSQL(
            "UPDATE OR IGNORE locked_levels SET price = 9000 WHERE id = 7"
        )
    }

    val audioQuestions = listOf(
        AudioQuestion(
            179,
            "a_brand_new_day",
            "If I Ruled the World", "A Brand New Day",
            "Airplane Pt.2", "For You",
            2, 400,
        ),
        AudioQuestion(
            180,
            "adult_child",
            "'학교의눈물' (School of Tears)", "Inner Child",
            "어른아이 (Adult Child)", "Jin 'The Astronaut'",
            3, 600,
        ),
        AudioQuestion(
            181,
            "all_night",
            "RM 'All Day ft. Tablo'", "Respect",
            "Stay", "All Night (feat. Juice WRLD)",
            4, 300,
        ),
        AudioQuestion(
            182,
            "born_singer",
            "Born Singer", "N.O (방탄소년단)",
            "Beautiful", "Let Me Know",
            1, 300,
        ),
        AudioQuestion(
            183,
            "for_you",
            "Anpanman", "For You",
            "Lights", "For Youth",
            2, 400,
        ),
        AudioQuestion(
            184,
            "for_youth",
            "어른아이 (Adult Child)", "2! 3! (Still Wishing For More Good Days)",
            "For Youth", "Daechwita",
            3, 600,
        ),
        AudioQuestion(
            185,
            "heartbeat",
            "Take Two", "Your Eyes Tell",
            "Trivia: Love", "Heartbeat",
            4, 500,
        ),
        AudioQuestion(
            186,
            "jhope_neuron",
            "J-Hope 'NEURON (with 개코, 윤미래)'", "Cypher Pt.3: Killer",
            "Paldogangsan (Satoori Rap)", "J-Hope 'MORE'",
            1, 400,
        ),
        AudioQuestion(
            187,
            "jin_super_tuna",
            "Jamais Vu", "Jin '슈퍼 참치 (Super Tuna)'",
            "Blanket Kick", "Jin 'The Astronaut'",
            2, 600,
        ),
        AudioQuestion(
            188,
            "lights",
            "So What", "We Are Bulletproof Pt. 1",
            "Lights", "Let Go",
            3, 400,
        ),
        AudioQuestion(
            189,
            "rm_come_back_to_me",
            "RM x Kim Sawo '건망증 (Forg_tful)'", "RM 'Lonely'",
            "RM 'No.2 ft. Park Ji Yoon'", "RM 'Come back to me'",
            4, 350,
        ),
        AudioQuestion(
            190,
            "school_of_tears",
            "'학교의눈물' (School of Tears)", "Paradise",
            "Blue & Grey", "Spine Breaker",
            1, 600,
        ),
        AudioQuestion(
            191,
            "so_far_away",
            "For You", "So Far Away",
            "Dream Glow", "No More Dream",
            2, 400,
        ),
        AudioQuestion(
            192,
            "v_fri_end_s",
            "Outro: Ego", "V 'For Us'",
            "V 'FRI(END)S'", "V 'Love Me Again'",
            3, 300,
        ),
        AudioQuestion(
            193,
            "we_are_bulletproof_pt_1",
            "We are Bulletproof: the Eternal", "A Typical Trainee’s Christmas",
            "Magic Shop", "We Are Bulletproof Pt. 1",
            4, 400,
        ),
        AudioQuestion(
            194,
            "with_seoul",
            "With Seoul", "Butterfly",
            "Rain", "Airplane Pt.2",
            1, 450,
        ),
        AudioQuestion(
            195,
            "a_typical_trainees_christmas",
            "So Far Away", "A Typical Trainee’s Christmas",
            "Butter", "Crystal Snow",
            2, 400,
        ),
        AudioQuestion(
            196,
            "beautiful",
            "All Night (feat. Juice WRLD)", "Let Go",
            "Beautiful", "Lights",
            3, 300,
        )
    )

    val videoQuestions = listOf(
        VideoQuestion(
            47,
            "adult_child_video",
            "Not Today", "어른아이 (Adult Child)",
            "FAKE LOVE", "Crystal Snow",
            2, 600,
        ),
        VideoQuestion(
            48,
            "jin_super_tuna_video",
            "Jin 'The Astronaut'", "Black Swan",
            "Jin '슈퍼 참치 (Super Tuna)'", "Permission to Dance",
            3, 550,
        ),
        VideoQuestion(
            49,
            "lights_video",
            "V 'FRI(END)S'", "Euphoria",
            "Bad Decisions", "Lights",
            4, 550,
        ),
        VideoQuestion(
            50,
            "rm_come_back_to_me_video",
            "RM 'Come back to me'", "RM 'I wanna be a human'",
            "V 'FRI(END)S'", "RM 'Lonely'",
            1, 600,
        ),
        VideoQuestion(
            51,
            "v_fri_end_s_video",
            "Lights", "V 'FRI(END)S'",
            "V 'Love Me Again'", "Friends",
            2, 600,
        ),
        VideoQuestion(
            52,
            "jin_the_astronaut_video",
            "My Universe", "Jin '슈퍼 참치 (Super Tuna)'",
            "Jin 'The Astronaut'", "Mikrokosmos",
            3, 550,
        ),
        VideoQuestion(
            53,
            "for_you_video",
            "War of Hormone", "Lights",
            "Just One Day", "For You",
            4, 550,
        )
    )
}