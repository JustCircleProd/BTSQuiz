package com.justcircleprod.btsquiz.data.room.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.justcircleprod.btsquiz.data.models.questions.TextQuestion
import com.justcircleprod.btsquiz.data.models.questions.AudioQuestion
import com.justcircleprod.btsquiz.data.models.questions.VideoQuestion


val MIGRATION_9_10: Migration = object : Migration(9, 10) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "UPDATE OR IGNORE audio_questions SET third_option = \"RM 'Yun ft. Erykah Badu'\" WHERE id = 129"
        )

        for (question in textQuestions) {
            db.execSQL(
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
            db.execSQL(
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
            db.execSQL(
                """INSERT OR IGNORE INTO video_questions    
                | (id, video_entry_name, first_option, second_option, 
                | third_option, fourth_option, answer_num, points) 
                | VALUES (${question.id}, "${question.video_entry_name}",
                | "${question.firstOption}", "${question.secondOption}",
                | "${question.thirdOption}", "${question.fourthOption}",
                | ${question.answerNum}, ${question.points})""".trimMargin()
            )
        }


        db.execSQL(
            "UPDATE OR IGNORE levels_progress SET questions_quantity = 48 WHERE id = 1"
        )

        db.execSQL(
            "UPDATE OR IGNORE levels_progress SET questions_quantity = 56 WHERE id = 2"
        )

        db.execSQL(
            "UPDATE OR IGNORE levels_progress SET questions_quantity = 92 WHERE id = 3"
        )

        db.execSQL(
            "UPDATE OR IGNORE levels_progress SET questions_quantity = 99 WHERE id = 4"
        )

        db.execSQL(
            "UPDATE OR IGNORE levels_progress SET questions_quantity = 107 WHERE id = 5"
        )

        db.execSQL(
            "UPDATE OR IGNORE levels_progress SET questions_quantity = 107 WHERE id = 6"
        )

        db.execSQL(
            "UPDATE OR IGNORE levels_progress SET questions_quantity = 166 WHERE id = 7"
        )


        db.execSQL(
            "UPDATE OR IGNORE locked_levels SET price = 1250 WHERE id = 2"
        )

        db.execSQL(
            "UPDATE OR IGNORE locked_levels SET price = 2050 WHERE id = 3"
        )

        db.execSQL(
            "UPDATE OR IGNORE locked_levels SET price = 4500 WHERE id = 4"
        )

        db.execSQL(
            "UPDATE OR IGNORE locked_levels SET price = 5750 WHERE id = 5"
        )

        db.execSQL(
            "UPDATE OR IGNORE locked_levels SET price = 7250 WHERE id = 6"
        )

        db.execSQL(
            "UPDATE OR IGNORE locked_levels SET price = 8750 WHERE id = 7"
        )
    }

    val textQuestions = listOf(
        TextQuestion(
            169,
            "Какой рост у Jungkook?",
            "177 см", "181 см",
            "182 см", "179 см",
            4, 450,
        ),
        TextQuestion(
            170,
            "Какой рост у Jimin?",
            "174 см", "172 см",
            "176 см", "170 см",
            1, 450,
        ),
        TextQuestion(
            171,
            "Какой рост у Jin?",
            "172 см", "179 см",
            "177 см", "181 см",
            2, 450,
        ),
        TextQuestion(
            172,
            "Какой рост у Suga?",
            "179 см", "172 см",
            "174 см", "176 см",
            3, 450,
        ),
        TextQuestion(
            173,
            "Какой рост у J-Hope?",
            "175 см", "179 см",
            "182 см", "177 см",
            4, 450,
        ),
        TextQuestion(
            174,
            "Какой рост у V?",
            "179 см", "181 см",
            "177 см", "174 см",
            1, 450,
        ),
        TextQuestion(
            175,
            "Кто первым из участников выпустил сольный микстейп?",
            "Suga", "RM",
            "Jungkook", "V",
            2, 550,
        ),
        TextQuestion(
            176,
            "В каком году BTS получили первую награду \"социальный артист\" на Billboard Music Awards?",
            "2016",
            "2018",
            "2017",
            "2015",
            3,
            550,
        ),
        TextQuestion(
            177,
            "Какая песня BTS впервые возглавила чарт Billboard Hot 100?",
            "Permission to Dance", "Butter",
            "My Universe", "Dynamite",
            4, 600,
        ),
        TextQuestion(
            178,
            "Кто из участников BTS старше остальных?",
            "Jin", "J-Hope",
            "Suga", "RM",
            1, 400,
        ),
        TextQuestion(
            179,
            "Кто спродюсировал дебютный сингл BTS \"No More Dream\"?",
            "Bang Si-hyuk", "Pdogg",
            "Adora", "Supreme Boi",
            2, 550,
        ),
        TextQuestion(
            180,
            "Сколько наград BTS получили на Billboard Music Awards 2022?",
            "1", "0",
            "3", "2",
            3, 600,
        ),
        TextQuestion(
            181,
            "Сколько участников в BTS?",
            "9", "6",
            "8", "7",
            4, 300,
        ),
        TextQuestion(
            182,
            "Какой клип BTS был занесён в книгу рекордов Гиннесса как cамое просматриваемое музыкальное видео на YouTube за 24 часа?",
            "Butter", "Boy With Luv",
            "Dynamite", "My Universe",
            1, 500,
        ),
        TextQuestion(
            183,
            "В каком году родился Jin?",
            "1993", "1992",
            "1991", "1994",
            2, 450,
        ),
        TextQuestion(
            184,
            "В каком году родился Jimin?",
            "1992", "1994",
            "1995", "1993",
            3, 450,
        ),
        TextQuestion(
            185,
            "В каком году родился Jungkook?",
            "1995", "1996",
            "1998", "1997",
            4, 450,
        ),
        TextQuestion(
            186,
            "В каком году родился J-Hope?",
            "1994", "1995",
            "1993", "1992",
            1, 450,
        ),
        TextQuestion(
            187,
            "В каком году родился Suga?",
            "1991", "1993",
            "1995", "1992",
            2, 450,
        ),
        TextQuestion(
            188,
            "В каком году родился V?",
            "1996", "1994",
            "1995", "1993",
            3, 450,
        ),
        TextQuestion(
            189,
            "В каком году родился RM?",
            "1992", "1993",
            "1995", "1994",
            4, 450,
        ),
        TextQuestion(
            190,
            "Какой скрытый смысл вложен в песню 134340 (Pluto)?",
            "Боль быть забытым", "Критика чрезмерного давления на молодое поколение",
            "Преодоление трудностей", "Подростковая зависть и жадность",
            1, 500,
        ),
        TextQuestion(
            191,
            "Какой скрытый смысл вложен в песню Sea?",
            "Боль быть забытым",
            "Преодоление трудностей",
            "Критика чрезмерного давления на молодое поколение",
            "Признание своих страхов как артистов",
            2,
            600,
        ),
        TextQuestion(
            192,
            "Какой скрытый смысл вложен в песню Whalien 52?",
            "Прославление простоты любви",
            "Призыв перестать закрывать глаза на коррупцию и пытаться бороться за перемены",
            "Одиночество и тревога перед неизведанным",
            "Призыв девушек быть самими собой",
            3,
            500,
        ),
        TextQuestion(
            193,
            "Какой скрытый смысл вложен в песню Trivia: Love?",
            "Призыв девушек быть самими собой", "Тёмная сторона любви",
            "Боль быть забытым", "Прославление простоты любви",
            4, 500,
        ),
        TextQuestion(
            194,
            "Какой скрытый смысл вложен в песню Dis-ease?",
            "Преодоление тревоги и страха", "Прославление простоты любви",
            "Подростковая зависть и жадность", "Критика потребительства",
            1, 550,
        ),
        TextQuestion(
            195,
            "Какой скрытый смысл вложен в песню Spine Breaker?",
            "Одиночество и тревога перед неизведанным", "Подростковая зависть и жадность",
            "Вечная благодарность своим фанатам", "Признание своих страхов как артистов",
            2, 500,
        ),
        TextQuestion(
            196,
            "Какой скрытый смысл вложен в песню Paradise?",
            "Преодоление трудностей", "Стремление вдохновлять своей музыкой",
            "Жить настоящим моментом — нормально", "Одиночество и тревога перед неизведанным",
            3, 600,
        ),
        TextQuestion(
            197,
            "Какой скрытый смысл вложен в песню Black Swan?",
            "Призыв девушек быть самими собой",
            "Боль, которую приходится пережить, столкнувшись с утратой",
            "Прославление простоты любви",
            "Страх потерять страсть к музыке",
            4,
            600,
        ),
        TextQuestion(
            198,
            "Какой скрытый смысл вложен в песню Silver Spoon?",
            "Критика чрезмерного давления на молодое поколение", "Тёмная сторона любви",
            "Критика потребительства", "Вечная благодарность своим фанатам",
            1, 600,
        ),
        TextQuestion(
            199,
            "Какой скрытый смысл вложен в песню The Truth Untold?",
            "Вечная благодарность своим фанатам",
            "Страх перед неуверенностью, которая отталкивает близких",
            "Жить настоящим моментом — нормально",
            "Тёмная сторона любви",
            2,
            600,
        ),
        TextQuestion(
            200,
            "Какой скрытый смысл вложен в песню Young Forever?",
            "Призыв перестать закрывать глаза на коррупцию и пытаться бороться за перемены",
            "Подростковая зависть и жадность",
            "Признание своих страхов как артистов",
            "Критика чрезмерного давления на молодое поколение",
            3,
            600,
        ),
        TextQuestion(
            201,
            "Какой скрытый смысл вложен в песню Magic Shop?",
            "Тёмная сторона любви",
            "Одиночество и тревога перед неизведанным",
            "Боль, которую приходится пережить, столкнувшись с утратой",
            "Вечная благодарность своим фанатам",
            4,
            550,
        ),
        TextQuestion(
            202,
            "Какой скрытый смысл вложен в песню Am I Wrong?",
            "Призыв перестать закрывать глаза на коррупцию и пытаться бороться за перемены",
            "Боль быть забытым",
            "Критика потребительства",
            "Прославление простоты любви",
            1,
            550,
        ),
        TextQuestion(
            203,
            "Какой скрытый смысл вложен в песню Go Go?",
            "Жить настоящим моментом — нормально", "Критика потребительства",
            "Призыв девушек быть самими собой", "Критика чрезмерного давления на молодое поколение",
            2, 600,
        ),
        TextQuestion(
            204,
            "Какой скрытый смысл вложен в песню FAKE LOVE?",
            "Прославление простоты любви", "Подростковая зависть и жадность",
            "Тёмная сторона любви", "Страх потерять страсть к музыке",
            3, 450,
        ),
        TextQuestion(
            205,
            "Какой скрытый смысл вложен в песню 21st Century Girls?",
            "Подростковая зависть и жадность",
            "Стремление вдохновлять своей музыкой",
            "Призыв перестать закрывать глаза на коррупцию и пытаться бороться за перемены",
            "Призыв девушек быть самими собой",
            4,
            450,
        ),
        TextQuestion(
            206,
            "Какой скрытый смысл вложен в песню Anpanman?",
            "Стремление вдохновлять своей музыкой",
            "Боль, которую приходится пережить, столкнувшись с утратой",
            "Жить настоящим моментом — нормально",
            "Страх перед неуверенностью, которая отталкивает близких",
            1,
            600,
        ),
        TextQuestion(
            207,
            "Какой скрытый смысл вложен в песню Spring Day?",
            "Жить настоящим моментом — нормально",
            "Боль, которую приходится пережить, столкнувшись с утратой",
            "Страх потерять страсть к музыке",
            "Подростковая зависть и жадность",
            2,
            600,
        ),
    )

    val audioQuestions = listOf(
        AudioQuestion(
            152,
            "ma_city",
            "I NEED U", "Intro: Never Mind",
            "Ma City", "Magic Shop",
            3, 300,
        ),
        AudioQuestion(
            153,
            "intro_never_mind",
            "Lost", "JUMP",
            "Stay Gold", "Intro: Never Mind",
            4, 300,
        ),
        AudioQuestion(
            154,
            "moving_on",
            "Moving On", "2nd Grade",
            "Intro: Boy Meets Evil", "Coffee",
            1, 600,
        ),
        AudioQuestion(
            155,
            "intro_boy_meets_evil",
            "Epiphany", "Intro: Boy Meets Evil",
            "The Truth Untold", "Moon",
            2, 550,
        ),
        AudioQuestion(
            156,
            "first_love",
            "FAKE LOVE", "Dionysus",
            "First Love", "Moving On",
            3, 600,
        ),
        AudioQuestion(
            157,
            "stigma",
            "War of Hormone", "I Like It (좋아요)",
            "Inner Child", "Stigma",
            4, 600,
        ),
        AudioQuestion(
            158,
            "lost",
            "Lost", "Am I Wrong",
            "Outro: Her", "Love Maze",
            1, 300,
        ),
        AudioQuestion(
            159,
            "awake",
            "Jin 'The Astronaut'", "Awake",
            "24/7=Heaven", "Spine Breaker",
            2, 550,
        ),
        AudioQuestion(
            160,
            "reflection",
            "Awake", "Rain",
            "Reflection", "Dis-ease",
            3, 600,
        ),
        AudioQuestion(
            161,
            "magic_shop",
            "Bad Decisions", "V 'Love Me Again'",
            "N.O (방탄소년단)", "Magic Shop",
            4, 400,
        ),
        AudioQuestion(
            162,
            "two_three",
            "2! 3! (Still Wishing For More Good Days)", "Look Here",
            "Stigma", "134340 (Pluto)",
            1, 600,
        ),
        AudioQuestion(
            163,
            "love_is_not_over",
            "Take Two", "Love Is Not Over",
            "First Love", "2! 3! (Still Wishing For More Good Days)",
            2, 600,
        ),
        AudioQuestion(
            164,
            "jimin_closer_than_this",
            "Jimin 'Like Crazy'", "Jimin 'Set Me Free Pt.2'",
            "Jimin 'Closer Than This'", "Jimin 'Filter'",
            3, 500,
        ),
        AudioQuestion(
            165,
            "jungkook_hate_you",
            "Jung Kook 'Somebody'", "Jung Kook feat. Major Lazer 'Closer to You'",
            "Jung Kook 'Seven (feat. Latto)'", "Jung Kook 'Hate You'",
            4, 300,
        ),
        AudioQuestion(
            166,
            "jungkook_yes_or_no",
            "Jung Kook 'Yes or No'", "Jung Kook 'Hate You'",
            "Jung Kook 'Please Don't Change' ", "Jung Kook '3D (feat. Jack Harlow)' ",
            1, 300,
        ),
        AudioQuestion(
            167,
            "jungkook_closer_to_you",
            "Jung Kook 'Standing Next to You'", "Jung Kook feat. Major Lazer 'Closer to You'",
            "Jung Kook 'Seven (feat. Latto)'", "Jung Kook 'Somebody'",
            2, 350,
        ),
        AudioQuestion(
            168,
            "jungkook_please_dont_change",
            "Jung Kook 'Hate You'", "'Dreamers' Jung Kook x Fahad Al Kubaisi",
            "Jung Kook 'Please Don't Change' ", "Jung Kook '3D (feat. Jack Harlow)' ",
            3, 300,
        ),
        AudioQuestion(
            169,
            "jungkook_somebody",
            "Jung Kook 'Standing Next to You'", "Jung Kook 'Yes or No'",
            "'Dreamers' Jung Kook x Fahad Al Kubaisi", "Jung Kook 'Somebody'",
            4, 300,
        ),
        AudioQuestion(
            170,
            "a_supplementary_story_you_never_walk_alone",
            "A Supplementary Story: You Never Walk Alone", "HOME",
            "Outro: Ego", "If I Ruled the World",
            1, 350,
        ),
        AudioQuestion(
            171,
            "outro_her",
            "Magic Shop", "Outro: Her",
            "00:00 (Zero O’Clock)", "Outro: Ego",
            2, 350,
        ),
        AudioQuestion(
            172,
            "let_go",
            "Save ME", "Dynamite",
            "Let Go", "Begin",
            3, 350,
        ),
        AudioQuestion(
            173,
            "outro_tear",
            "Outro: Ego", "Let Me Know",
            "Waste It On Me", "Outro: Tear",
            4, 400,
        ),
        AudioQuestion(
            174,
            "answer_love_myself",
            "Answer: Love Myself", "I'm Fine",
            "First Love", "V 'For Us'",
            1, 300,
        ),
        AudioQuestion(
            175,
            "inner_child",
            "Stigma", "Inner Child",
            "어디에서 왔는지 (Where Did You Come From)", "Jung Kook 'Please Don't Change' ",
            2, 550,
        ),
        AudioQuestion(
            176,
            "moon",
            "Path (Road)", "Jin 'The Astronaut'",
            "Moon", "2! 3! (Still Wishing For More Good Days)",
            3, 600,
        ),
        AudioQuestion(
            177,
            "respect",
            "Lost", "Stay",
            "Tomorrow", "Respect",
            4, 300,
        ),
        AudioQuestion(
            178,
            "outro_ego",
            "Outro: Ego", "Airplane Pt.2",
            "Outro: Her", "Mama",
            1, 400,
        ),
    )

    val videoQuestions = listOf(
        VideoQuestion(
            43,
            "intro_boy_meets_evil_video",
            "Dynamite", "Intro: Boy Meets Evil",
            "Just One Day", "J-Hope '방화 (Arson)'",
            2, 550,
        ),
        VideoQuestion(
            44,
            "jimin_closer_than_this_video",
            "Jimin 'Like Crazy'", "Serendipity",
            "Jimin 'Closer Than This'", "Singularity",
            3, 600,
        ),
        VideoQuestion(
            45,
            "jungkook_hate_you_video",
            "Jung Kook 'Seven (feat. Latto)'", "Euphoria",
            "Life Goes On", "Jung Kook 'Hate You'",
            4, 600,
        ),
        VideoQuestion(
            46,
            "outro_ego_video",
            "Outro: Ego", "J-Hope 'Daydream (백일몽)'",
            "Boy With Luv", "J-Hope 'on the street (with J. Cole)'",
            1, 500,
        ),
    )
}