package com.justcircleprod.btsquiz.core.data.room.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.justcircleprod.btsquiz.core.data.models.questions.AudioQuestion
import com.justcircleprod.btsquiz.core.data.models.questions.ImageQuestion
import com.justcircleprod.btsquiz.core.data.models.questions.VideoQuestion


val MIGRATION_8_9: Migration = object : Migration(8, 9) {
    override fun migrate(db: SupportSQLiteDatabase) {
        for (question in imageQuestions) {
            db.execSQL(
                """INSERT OR IGNORE INTO image_questions    
                | (id, image_entry_name, first_option, second_option, 
                | third_option, fourth_option, answer_num, points) 
                | VALUES (${question.id}, "${question.imageEntryName}",
                | "${question.firstOption}", "${question.secondOption}",
                | "${question.thirdOption}", "${question.fourthOption}",
                | ${question.answerNum}, ${question.points})""".trimMargin()
            )
        }

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
            "UPDATE OR IGNORE audio_questions SET fourth_option = \"J-Hope '방화 (Arson)'\" WHERE id = 73"
        )

        db.execSQL(
            "UPDATE OR IGNORE audio_questions SET first_option = \"RM '들꽃놀이 (Wild Flower) (with 조유진)'\" WHERE id = 86"
        )

        db.execSQL(
            "UPDATE OR IGNORE audio_questions SET fourth_option = \"J-Hope '방화 (Arson)'\" WHERE id = 94"
        )

        db.execSQL(
            "UPDATE OR IGNORE audio_questions SET second_option = \"J-Hope '방화 (Arson)'\" WHERE id = 98"
        )

        db.execSQL(
            "UPDATE OR IGNORE audio_questions SET first_option = \"J-Hope '방화 (Arson)'\" WHERE id = 120"
        )


        db.execSQL(
            "UPDATE OR IGNORE levels_progress SET questions_quantity = 38 WHERE id = 1"
        )

        db.execSQL(
            "UPDATE OR IGNORE levels_progress SET questions_quantity = 52 WHERE id = 2"
        )

        db.execSQL(
            "UPDATE OR IGNORE levels_progress SET questions_quantity = 88 WHERE id = 3"
        )

        db.execSQL(
            "UPDATE OR IGNORE levels_progress SET questions_quantity = 84 WHERE id = 4"
        )

        db.execSQL(
            "UPDATE OR IGNORE levels_progress SET questions_quantity = 100 WHERE id = 5"
        )

        db.execSQL(
            "UPDATE OR IGNORE levels_progress SET questions_quantity = 97 WHERE id = 6"
        )

        db.execSQL(
            "UPDATE OR IGNORE levels_progress SET questions_quantity = 146 WHERE id = 7"
        )


        db.execSQL(
            "UPDATE OR IGNORE locked_levels SET price = 950 WHERE id = 2"
        )

        db.execSQL(
            "UPDATE OR IGNORE locked_levels SET price = 1900 WHERE id = 3"
        )

        db.execSQL(
            "UPDATE OR IGNORE locked_levels SET price = 4300 WHERE id = 4"
        )

        db.execSQL(
            "UPDATE OR IGNORE locked_levels SET price = 4850 WHERE id = 5"
        )

        db.execSQL(
            "UPDATE OR IGNORE locked_levels SET price = 6800 WHERE id = 6"
        )

        db.execSQL(
            "UPDATE OR IGNORE locked_levels SET price = 8000 WHERE id = 7"
        )
    }

    val imageQuestions = listOf(
        ImageQuestion(
            220,
            "jeon_jung_kook_27",
            "Jungkook", "RM",
            "V", "Jimin",
            1, 450,
        ),
        ImageQuestion(
            221,
            "jeon_jung_kook_28",
            "J-Hope", "Jungkook",
            "Suga", "Jin",
            2, 350,
        ),
        ImageQuestion(
            222,
            "jeon_jung_kook_29",
            "Suga", "Jin",
            "Jungkook", "V",
            3, 400,
        ),
        ImageQuestion(
            223,
            "jeon_jung_kook_30",
            "Jimin", "V",
            "RM", "Jungkook",
            4, 450,
        ),
        ImageQuestion(
            224,
            "jung_ho_seok_28",
            "J-Hope", "Jin",
            "Jungkook", "Suga",
            1, 500,
        ),
        ImageQuestion(
            225,
            "jung_ho_seok_29",
            "Jin", "J-Hope",
            "RM", "V",
            2, 400,
        ),
        ImageQuestion(
            226,
            "jung_ho_seok_30",
            "Jungkook", "Jimin",
            "J-Hope", "Suga",
            3, 500,
        ),
        ImageQuestion(
            227,
            "jungkook_v_1",
            "V, J-Hope", "V, Jungkook",
            "Jungkook, Suga", "Jungkook, V",
            4, 600,
        ),
        ImageQuestion(
            228,
            "kim_nam_joon_26",
            "RM", "Jimin",
            "J-Hope", "V",
            1, 500,
        ),
        ImageQuestion(
            229,
            "kim_nam_joon_27",
            "Suga", "RM",
            "Jin", "Jimin",
            2, 350,
        ),
        ImageQuestion(
            230,
            "kim_nam_joon_28",
            "Jin", "J-Hope",
            "RM", "Jimin",
            3, 550,
        ),
        ImageQuestion(
            231,
            "kim_nam_joon_29",
            "J-Hope", "Suga",
            "Jungkook", "RM",
            4, 600,
        ),
        ImageQuestion(
            232,
            "kim_nam_joon_30",
            "RM", "Jimin",
            "V", "J-Hope",
            1, 600,
        ),
        ImageQuestion(
            233,
            "kim_seok_jin_29",
            "J-Hope", "Jin",
            "RM", "Jungkook",
            2, 350,
        ),
        ImageQuestion(
            234,
            "kim_seok_jin_30",
            "Suga", "V",
            "Jin", "Jimin",
            3, 400,
        ),
        ImageQuestion(
            235,
            "kim_tae_hyung_30",
            "RM", "J-Hope",
            "Suga", "V",
            4, 550,
        ),
        ImageQuestion(
            236,
            "min_yoon_gi_29",
            "Suga", "V",
            "Jungkook", "Jin",
            1, 450,
        ),
        ImageQuestion(
            237,
            "min_yoon_gi_30",
            "Jungkook", "Suga",
            "Jimin", "RM",
            2, 550,
        ),
        ImageQuestion(
            238,
            "park_ji_min_28",
            "J-Hope", "Jin",
            "Jimin", "V",
            3, 450,
        ),
        ImageQuestion(
            239,
            "park_ji_min_29",
            "RM", "Jungkook",
            "Suga", "Jimin",
            4, 550,
        ),
        ImageQuestion(
            240,
            "park_ji_min_30",
            "Jimin", "V",
            "Jungkook", "J-Hope",
            1, 550,
        ),
        ImageQuestion(
            241,
            "rm_suga_1",
            "Suga, Jin", "RM, Suga",
            "Jungkook, Jimin", "RM, V",
            2, 600,
        ),
        ImageQuestion(
            242,
            "rm_suga_2",
            "RM, J-Hope", "J-Hope, Jimin",
            "RM, Suga", "Suga, V",
            3, 600,
        ),
        ImageQuestion(
            243,
            "v_jin_1",
            "V, Suga", "Jin, Jungkook",
            "J-Hope, V", "V, Jin",
            4, 600,
        ),
        ImageQuestion(
            244,
            "v_jungkook_1",
            "V, Jungkook", "J-Hope, Jin",
            "Jungkook, Jimin", "V, Suga",
            1, 600,
        )
    )

    val audioQuestions = listOf(
        AudioQuestion(
            121,
            "boy_in_luv",
            "Path (Road)", "Boy With Luv",
            "21st Century Girls", "Boy In Luv",
            4, 550,
        ),
        AudioQuestion(
            122,
            "jimin_alone",
            "Jimin 'Alone'", "Jimin 'Filter'",
            "Jimin 'Face-off'", "Jimin 'Set Me Free Pt.2'",
            1, 600,
        ),
        AudioQuestion(
            123,
            "jimin_face_off",
            "Jimin 'Like Crazy'", "Jimin 'Face-off'",
            "Jimin 'Filter'", "Jimin 'Alone'",
            2, 600,
        ),
        AudioQuestion(
            124,
            "jungkook_3d",
            "Jung Kook 'Standing Next to You'", "Butterfly",
            "Jung Kook '3D (feat. Jack Harlow)' ", "Jung Kook 'Seven (feat. Latto)'",
            3, 300,
        ),
        AudioQuestion(
            125,
            "rm_all_day",
            "RM '들꽃놀이 (Wild Flower) (with 조유진)'", "RM 'Change pt.2'",
            "J-Hope '방화 (Arson)'", "RM 'All Day ft. Tablo'",
            4, 300,
        ),
        AudioQuestion(
            126,
            "rm_change_pt_2",
            "RM 'Change pt.2'", "RM x Kim Sawo '건망증 (Forg_tful)'",
            "RM 'All Day ft. Tablo'", "RM 'Closer (with Paul Blanco, Mahalia)'",
            1, 350,
        ),
        AudioQuestion(
            127,
            "rm_closer",
            "Stay", "RM 'Closer (with Paul Blanco, Mahalia)'",
            "RM '들꽃놀이 (Wild Flower) (with 조유진)'", "RM 'Change pt.2'",
            2, 450,
        ),
        AudioQuestion(
            128,
            "rm_forg_tful",
            "RM 'Yun ft. Erykah Badu'", "RM 'All Day ft. Tablo'",
            "RM x Kim Sawo '건망증 (Forg_tful)'", "J-Hope '방화 (Arson)'",
            3, 600,
        ),
        AudioQuestion(
            129,
            "rm_hectic",
            "RM 'Change pt.2'", "RM x Kim Sawo '건망증 (Forg_tful)'",
            "RM 'Change pt.2'", "RM 'Hectic ft. Colde'",
            4, 350,
        ),
        AudioQuestion(
            130,
            "rm_lonely",
            "RM 'Lonely'", "RM 'Hectic ft. Colde'",
            "RM 'Still Life'", "RM 'No.2 ft. Park Ji Yoon'",
            1, 400,
        ),
        AudioQuestion(
            131,
            "rm_no_2",
            "RM 'Closer (with Paul Blanco, Mahalia)'", "RM 'No.2 ft. Park Ji Yoon'",
            "RM 'Lonely'", "RM x Kim Sawo '건망증 (Forg_tful)'",
            2, 450,
        ),
        AudioQuestion(
            132,
            "rm_yun",
            "RM 'I wanna be a human'", "RM x Kim Sawo '건망증 (Forg_tful)'",
            "RM 'Yun ft. Erykah Badu'", "RM 'No.2 ft. Park Ji Yoon'",
            3, 600,
        ),
        AudioQuestion(
            133,
            "v_blue",
            "V 'Love Me Again'", "V 'For Us'",
            "V 'Rainy Days'", "V 'Blue'",
            4, 400,
        ),
        AudioQuestion(
            134,
            "v_for_us",
            "V 'For Us'", "V 'Slow Dancing'",
            "V 'Love Me Again'", "V 'Rainy Days'",
            1, 400,
        ),
        AudioQuestion(
            135,
            "v_love_me_again",
            "V 'Rainy Days'", "V 'Love Me Again'",
            "V 'For Us'", "V 'Blue'",
            2, 300,
        ),
        AudioQuestion(
            136,
            "v_rainy_days",
            "V 'For Us'", "V 'Slow Dancing'",
            "V 'Rainy Days'", "V 'Love Me Again'",
            3, 300,
        ),
        AudioQuestion(
            137,
            "v_slow_dancing",
            "V 'Love Me Again'", "V 'For Us'",
            "V 'Blue'", "V 'Slow Dancing'",
            4, 300,
        ),
        AudioQuestion(
            138,
            "where_did_you_come_from",
            "어디에서 왔는지 (Where Did You Come From)", "Blue & Grey",
            "Interlude : Shadow", "Your Eyes Tell",
            1, 600,
        ),
        AudioQuestion(
            139,
            "blanket_kick",
            "Your Eyes Tell", "Blanket Kick",
            "Dionysus", "Take Two",
            2, 600,
        ),
        AudioQuestion(
            140,
            "can_you_turn_off_your_phone",
            "Blanket Kick", "Telepathy",
            "Can You Turn Off Your Phone", "Coffee",
            3, 600,
        ),
        AudioQuestion(
            141,
            "cypher_part_2",
            "Spine Breaker", "Cypher Pt.3: Killer",
            "Hip Hop Phile", "Cypher Pt.1",
            4, 550,
        ),
        AudioQuestion(
            142,
            "cypher_part_3_killer",
            "Cypher Pt.3: Killer", "Paldogangsan (Satoori Rap)",
            "J-Hope '방화 (Arson)'", "Cypher Pt.2",
            1, 500,
        ),
        AudioQuestion(
            143,
            "heaven_24_7",
            "Epiphany", "24/7=Heaven",
            "Mikrokosmos", "I Like It (좋아요)",
            2, 600,
        ),
        AudioQuestion(
            144,
            "jump",
            "Jung Kook '3D (feat. Jack Harlow)' ", "Euphoria",
            "JUMP", "Converse High",
            3, 300,
        ),
        AudioQuestion(
            145,
            "let_me_know",
            "Hold Me Tight", "21st Century Girls",
            "Tomorrow", "Let Me Know",
            4, 350,
        ),
        AudioQuestion(
            146,
            "look_here",
            "Look Here", "Dionysus",
            "Path (Road)", "Interlude : Shadow",
            1, 600,
        ),
        AudioQuestion(
            147,
            "miss_right",
            "My Time", "Miss Right",
            "21st Century Girls", "Make It Right",
            2, 550,
        ),
        AudioQuestion(
            148,
            "outro_propose",
            "Outro: Wings", "Anpanman",
            "Outro: Propose", "Hold Me Tight",
            3, 600,
        ),
        AudioQuestion(
            149,
            "spine_breaker",
            "We On", "Cypher Pt.4",
            "RM 'Hectic ft. Colde'", "Spine Breaker",
            4, 550,
        ),
        AudioQuestion(
            150,
            "war_of_hormone",
            "War of Hormone", "PERSONA",
            "Look Here", "Boy In Luv",
            1, 550,
        ),
        AudioQuestion(
            151,
            "jungkook_standing_next_to_you",
            "Jung Kook 'Seven (feat. Latto)'", "Jung Kook 'Standing Next to You'",
            "'Dreamers' Jung Kook x Fahad Al Kubaisi", "Jung Kook '3D (feat. Jack Harlow)' ",
            2, 300,
        )
    )

    val videoQuestions = listOf(
        VideoQuestion(
            37,
            "jungkook_3d_video",
            "Jung Kook 'Seven (feat. Latto)'", "Jung Kook 'Standing Next to You'",
            "Film out", "Jung Kook '3D (feat. Jack Harlow)'",
            4, 600,
        ),
        VideoQuestion(
            38,
            "v_blue_video",
            "V 'Blue'", "Singularity",
            "V 'Rainy Days'", "Serendipity",
            1, 600,
        ),
        VideoQuestion(
            39,
            "v_rainy_days_video",
            "V 'Slow Dancing'", "V 'Rainy Days'",
            "My Universe", "V 'Blue'",
            2, 550,
        ),
        VideoQuestion(
            40,
            "v_slow_dancing_video",
            "Stay Gold", "Film out",
            "V 'Slow Dancing'", "V 'Rainy Days'",
            3, 550,
        ),
        VideoQuestion(
            41,
            "war_of_hormone_video",
            "FAKE LOVE", "V 'Blue'",
            "I Need U", "War of Hormone",
            4, 550,
        ),
        VideoQuestion(
            42,
            "jungkook_standing_next_to_you_video",
            "Jung Kook 'Standing Next to You'", "My Universe",
            "Jung Kook '3D (feat. Jack Harlow)'", "Epilogue: Young Forever",
            1, 500,
        ),
    )
}