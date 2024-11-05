package com.justcircleprod.btsquiz.core.data.room.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.justcircleprod.btsquiz.core.data.models.questions.AudioQuestion
import com.justcircleprod.btsquiz.core.data.models.questions.TextQuestion
import com.justcircleprod.btsquiz.core.data.models.questions.VideoQuestion


val MIGRATION_13_14: Migration = object : Migration(13, 14) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = \"В июле 2017 года BTS объявили, что их название будет иметь ещё одну расшифровку. Что это за расшифровка?\" WHERE id = 3"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET points = 400 WHERE id = 3"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = 'Какое название получил дебютный сингл альбом в карьере BTS?' WHERE id = 7"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET first_option = \"Wings\" WHERE id = 7"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET second_option = \"2 Cool 4 Skool\" WHERE id = 7"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET third_option = \"Dark & Wild\" WHERE id = 7"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET fourth_option = \"Love Yourself: Tear\" WHERE id = 7"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET answer_num = 2 WHERE id = 7"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET points = 400 WHERE id = 7"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = 'Какую страну НЕ посетили BTS во время своего первого концертного тура?' WHERE id = 11"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = 'Какое любимое число у Jimin?' WHERE id = 102"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET points = 550 WHERE id = 142"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET question = 'Кем в детстве хотел стать V?' WHERE id = 50"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET first_option = \"Робототехником\" WHERE id = 50"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET second_option = \"Музыкантом\" WHERE id = 50"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET third_option = \"Журналистом\" WHERE id = 50"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET fourth_option = \"Танцором\" WHERE id = 50"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET answer_num = 2 WHERE id = 50"
        )
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET points = 550 WHERE id = 50"
        )

        db.execSQL(
            "UPDATE OR IGNORE text_questions SET second_option = \"Map of the Soul: Persona\" WHERE id = 30"
        )


        for (question in textQuestions) {
            db.execSQL(
                """INSERT OR IGNORE INTO text_questions
                | (id, question, first_option, second_option, 
                | third_option, fourth_option, answer_num, points) 
                | VALUES (${question.id}, '${question.question}',
                | '${question.firstOption}', '${question.secondOption}',
                | '${question.thirdOption}', '${question.fourthOption}',
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
            "UPDATE OR IGNORE levels_progress SET questions_quantity = 64 WHERE id = 1"
        )

        db.execSQL(
            "UPDATE OR IGNORE levels_progress SET questions_quantity = 69 WHERE id = 2"
        )

        db.execSQL(
            "UPDATE OR IGNORE levels_progress SET questions_quantity = 104 WHERE id = 3"
        )

        db.execSQL(
            "UPDATE OR IGNORE levels_progress SET questions_quantity = 106 WHERE id = 4"
        )

        db.execSQL(
            "UPDATE OR IGNORE levels_progress SET questions_quantity = 125 WHERE id = 5"
        )

        db.execSQL(
            "UPDATE OR IGNORE levels_progress SET questions_quantity = 133 WHERE id = 6"
        )

        db.execSQL(
            "UPDATE OR IGNORE levels_progress SET questions_quantity = 199 WHERE id = 7"
        )


        db.execSQL(
            "UPDATE OR IGNORE locked_levels SET price = 1450 WHERE id = 2"
        )

        db.execSQL(
            "UPDATE OR IGNORE locked_levels SET price = 2400 WHERE id = 3"
        )

        db.execSQL(
            "UPDATE OR IGNORE locked_levels SET price = 5150 WHERE id = 4"
        )

        db.execSQL(
            "UPDATE OR IGNORE locked_levels SET price = 6200 WHERE id = 5"
        )

        db.execSQL(
            "UPDATE OR IGNORE locked_levels SET price = 8650 WHERE id = 6"
        )

        db.execSQL(
            "UPDATE OR IGNORE locked_levels SET price = 11000 WHERE id = 7"
        )
    }

    val textQuestions = listOf(
        TextQuestion(
            191,
            "Как дословно переводится первоначальная расшифровка \"BTS\"?",
            "Братья творческого союза",
            "Пуленепробиваемые бойскауты",
            "Будущие творцы стиля",
            "Талантливые парни",
            2,
            350,
        ),
        TextQuestion(
            192,
            "Что отражает первоначальная расшифровка \"BTS\"?",
            "Стремление создать новое музыкальное направление в k-pop индустрии",
            "Противостояние традиционным ценностям корейского общества",
            "Желание группы \"блокировать стереотипы, критику и ожидания, которые нацелены на подростков, как пули\"",
            "Защиту творческой свободы молодых артистов",
            3,
            400,
        ),
        TextQuestion(
            193,
            "Какой смысл добавила новая расшифровка группы \"BTS\", объявленная в июле 2017 года?",
            "Преодоление границ между разными культурами через музыку",
            "Путь от начинающих артистов к мировым звездам",
            "Создание нового направления в современной музыке",
            "Идея роста \"от мальчика до взрослого, который открывает двери, обращённые вперёд\"",
            4,
            450,
        ),
        TextQuestion(
            194,
            "Какой отличительной особенностью обладали BTS при создании группы по сравнении с другими идол-группами?",
            "Участники были индивидуальностями, а не ансамблем, и могли свободно выражать себя",
            "Все участники должны были писать собственную музыку",
            "У них не было заранее определённых ролей в группе",
            "Группа изначально создавалась как рок-группа с элементами k-pop",
            1,
            400,
        ),
        TextQuestion(
            195,
            "Когда BTS выпустили свой дебютный сингл альбом?",
            "В марте 2010 года",
            "В июне 2013 года",
            "В июле 2014 года",
            "В декабре 2013 года",
            2,
            500,
        ),
        TextQuestion(
            196,
            "Когда BTS дебютировали на сцене со своим синглом \"No More Dream\"?",
            "В апреле 2010 года",
            "В январе 2014 года",
            "В июне 2013 года",
            "В августе 2013 года",
            3,
            500,
        ),
        TextQuestion(
            197,
            "Какой смысл вложен в сингл \"No More Dream\"?",
            "Дружба и поддержка между участниками группы",
            "Мечты о мировой славе",
            "Критика современной системы развлечений",
            "Беспокойство молодых людей перед лицом высоких родительских ожиданий",
            4,
            550,
        ),
        TextQuestion(
            198,
            "Какое звучание использовалось в сингл альбоме \"2 Cool 4 Skool\"?",
            "Олдскульное хип-хоп-звучание 1990-х годов",
            "Традиционное k-pop звучание с элементами R&B",
            "Смесь рока и k-pop",
            "Экспериментальный джаз-фьюжн",
            1,
            600,
        ),
        TextQuestion(
            199,
            "Какой смысл вложен во многие ранние работы BTS, такие как \"NO\" и \"No More Dreams\"?",
            "Проблемы взаимоотношений между поколениями",
            "Выражение бунта против истеблишмента, который затронул разочарование корейских подростков в образовательной системе страны",
            "Желание изменить k-pop индустрию",
            "Стремление к мировому признанию",
            2,
            550,
        ),
        TextQuestion(
            200,
            "Какое название получил первый мини-альбом в карьере BTS?",
            "Dark & Wild",
            "Be",
            "O!RUL8,2?",
            "The Most Beautiful Moment in Life, Pt. 1",
            3,
            400,
        ),
        TextQuestion(
            201,
            "Какое название получил второй мини-альбом в карьере BTS?",
            "2 Cool 4 Skool",
            "Wings",
            "Map of the Soul: Persona",
            "Skool Luv Affair",
            4,
            450,
        ),
        TextQuestion(
            202,
            "Какой альбом BTS первым появился в чарте альбомов Billboard World?",
            "Skool Luv Affair",
            "The Most Beautiful Moment in Life, Pt. 2",
            "Wings: You Never Walk Alone",
            "2 Cool 4 Skool",
            1,
            550,
        ),
        TextQuestion(
            203,
            "Как называются синглы, вошедшие в альбом \"Skool Luv Affair\"?",
            "\"First Love\" и \"Yet To Come\"",
            "\"Boy in Luv\" и \"Just One Day\"",
            "\"Butter\" и \"Blood Sweat&Tears\"",
            "\"Danger\" и \"War of Hormone\"",
            2,
            500,
        ),
        TextQuestion(
            204,
            "После релиза какого альбома BTS провели первую фан-встречу в Сеуле, на которой выбрали название для их фан-клуба?",
            "The Most Beautiful Moment in Life, Pt. 1",
            "Dark & Wild",
            "Skool Luv Affair",
            "2 Cool 4 Skool",
            3,
            450,
        ),
        TextQuestion(
            205,
            "В каком году начался первый концертный тур BTS?",
            "В 2010 году",
            "В 2015 году",
            "В 2013 году",
            "В 2014 году",
            4,
            450,
        ),
        TextQuestion(
            206,
            "Как называются синглы, вошедшие в альбом \"Dark & Wild\"?",
            "\"Danger\" and \"War of Hormone\"",
            "\"FIRE\" и \"24/7=Heaven\"",
            "\"Boy in Luv\" и \"Just One Day\"",
            "\"Look Here\" и \"Stay Gold\"",
            1,
            550,
        ),
        TextQuestion(
            207,
            "В каком году BTS провели свой первый концерт в США?",
            "В 2013 году",
            "В 2014 году",
            "В 2012 году",
            "В 2015 году",
            2,
            550,
        ),
        TextQuestion(
            208,
            "Сколько стран посетили BTS во время своего первого концертного тура?",
            "2",
            "5",
            "13",
            "9",
            3,
            550,
        ),
        TextQuestion(
            209,
            "Сколько суммарно зрителей удалось собрать BTS во время своего первого концертного тура?",
            "30 000 зрителей",
            "220 000 зрителей",
            "135 000 зрителей",
            "80 000 зрителей",
            4,
            450,
        ),
        TextQuestion(
            210,
            "Что хотели выразить BTS своим альбомом \"The Most Beautiful Moment in Life, Pt.1\"?",
            "Красоту и тревожность молодости",
            "Влияние славы на личность",
            "Важность сохранения традиций",
            "Конфликт между мечтами и реальностью",
            1,
            600,
        ),
        TextQuestion(
            211,
            "Какой сингл принёс BTS победу на SBS MTV The Show?",
            "Let Me Know",
            "I Need U",
            "Save Me",
            "Danger",
            2,
            600,
        ),
        TextQuestion(
            212,
            "Благодаря какому альбому BTS впервые попали в чарт Billboard 200?",
            "O!RUL8,2?",
            "The Most Beautiful Moment in Life, Pt. 2",
            "Love Yourself: Her",
            "Dark & Wild",
            2,
            600,
        ),
        TextQuestion(
            213,
            "Какое название получил первый студийный альбом в карьере BTS?",
            "2 Cool 4 Skool",
            "Map of the Soul: Persona",
            "Dark & Wild",
            "Love Yourself: Tear",
            3,
            400,
        ),
        TextQuestion(
            214,
            "Какое любимое число у J-Hope?",
            "10",
            "5",
            "8",
            "3",
            4,
            600,
        ),
        TextQuestion(
            215,
            "Какое любимое число у RM?",
            "7",
            "1",
            "9",
            "2",
            1,
            600,
        ),
        TextQuestion(
            216,
            "Какое любимое число у V?",
            "3",
            "1",
            "4",
            "9",
            2,
            600,
        ),
        TextQuestion(
            217,
            "Какое любимое число у Jin?",
            "6",
            "5",
            "10",
            "8",
            3,
            600,
        ),
        TextQuestion(
            218,
            "Какое любимое число у Suga?",
            "7",
            "4",
            "9",
            "3",
            4,
            600,
        ),
        TextQuestion(
            219,
            "Какое любимое число у Jungkook?",
            "1",
            "11",
            "5",
            "6",
            1,
            600,
        ),
        TextQuestion(
            220,
            "Как называется совместный проект LINE FRIENDS CREATORS и BTS, благодаря которому появились мультяшные персонажи, олицетворяющие BTS?",
            "Mini BTS",
            "BT21",
            "BT7",
            "Mult BTS",
            2,
            400,
        ),
        TextQuestion(
            221,
            "Кого олицетворяет Tata из BT21?",
            "Jimin",
            "Jungkook",
            "V",
            "Jin",
            3,
            600,
        ),
        TextQuestion(
            222,
            "Кого олицетворяет RJ из BT21?",
            "Suga",
            "V",
            "ARMY",
            "Jin",
            4,
            600,
        ),
        TextQuestion(
            223,
            "Кого олицетворяет Chimmy из BT21?",
            "Jimin",
            "Suga",
            "Jungkook",
            "J-Hope",
            1,
            600,
        ),
        TextQuestion(
            224,
            "Кого олицетворяет Cooky из BT21?",
            "RM",
            "Jungkook",
            "Jin",
            "Suga",
            2,
            600,
        ),
        TextQuestion(
            225,
            "Кого олицетворяет Mang из BT21?",
            "RM",
            "Jungkook",
            "J-Hope",
            "Jimin",
            3,
            600,
        ),
        TextQuestion(
            226,
            "Кого олицетворяет Shooky из BT21?",
            "Jin",
            "ARMY",
            "V",
            "Suga",
            4,
            600,
        ),
        TextQuestion(
            227,
            "Кого олицетворяет Koya из BT21?",
            "RM",
            "Jin",
            "Jimin",
            "J-Hope",
            1,
            600,
        ),
        TextQuestion(
            228,
            "Кого олицетворяет Van из BT21?",
            "J-Hope",
            "ARMY",
            "V",
            "RM",
            2,
            600,
        ),
        TextQuestion(
            229,
            "Сколько персонажей входит в состав BT21?",
            "1",
            "3",
            "8",
            "7",
            3,
            450,
        ),
        TextQuestion(
            230,
            "Какое участие BTS принимали в создании персонажей BT21?",
            "Вообще не принимали участия",
            "Только утверждали готовый результат",
            "Только рисовали эскизы",
            "Участвовали во всём процессе от рисования эскизов до проработки",
            4,
            450,
        ),
        TextQuestion(
            231,
            "Кем в детстве хотел стать RM?",
            "Журналистом",
            "Программистом",
            "Предпринимателем",
            "Полицейским",
            1,
            550,
        ),
        TextQuestion(
            232,
            "Кем в детстве хотел стать Suga?",
            "Танцором",
            "Журналистом",
            "Юристом",
            "Космонавтом",
            2,
            550,
        ),
        TextQuestion(
            233,
            "Кем в детстве хотел стать Jin?",
            "Иллюстратором ",
            "Танцором",
            "Предпринимателем",
            "Поваром",
            3,
            550,
        ),
        TextQuestion(
            234,
            "Кем в детстве хотел стать J-Hope?",
            "Юристом",
            "Психологом",
            "Музыкантом",
            "Танцором",
            4,
            550,
        ),
        TextQuestion(
            235,
            "Кем в детстве хотел стать Jimin?",
            "Полицейским",
            "Робототехником",
            "Танцором",
            "Учителем",
            1,
            550,
        ),
        TextQuestion(
            236,
            "Кем в детстве хотел стать Jungkook?",
            "Предпринимателем",
            "Иллюстратором ",
            "Инженером",
            "Учителем",
            2,
            550,
        ),
        TextQuestion(
            237,
            "Какое название получил первый японский и второй в общем студийный альбом в карьере BTS?",
            "Youth",
            "Skool Luv Affair",
            "Wake Up",
            "Face Yourself",
            3,
            450,
        ),
        TextQuestion(
            238,
            "Какое название получил второй японский и третий в общем студийный альбом в карьере BTS?",
            "Map of the Soul: Persona",
            "Wake Up",
            "Be",
            "Youth",
            4,
            500,
        ),
        TextQuestion(
            239,
            "Какое название получил второй корейский и четвёртый в общем студийный альбом в карьере BTS?",
            "Wings",
            "O!RUL8,2?",
            "Map of the Soul: 7 – The Journey",
            "The Most Beautiful Moment in Life, Pt. 1",
            1,
            500,
        ),
        TextQuestion(
            240,
            "Какое название получил третий японский и пятый в общем студийный альбом в карьере BTS?",
            "The Most Beautiful Moment in Life, Pt. 2",
            "Face Yourself",
            "Love Yourself: Answer",
            "Map of the Soul: 7 – The Journey",
            2,
            500,
        ),
        TextQuestion(
            241,
            "Какое название получил третий корейский и шестой в общем студийный альбом в карьере BTS?",
            "Love Yourself: Her",
            "Face Yourself",
            "Love Yourself: Tear",
            "Map of the Soul: 7",
            3,
            500,
        ),
        TextQuestion(
            242,
            "Какое название получил четвёртый корейский и седьмой в общем студийный альбом в карьере BTS?",
            "Wake Up",
            "Be",
            "Youth",
            "Map of the Soul: 7",
            4,
            500,
        ),
        TextQuestion(
            243,
            "Какое название получил четвёртый японский и восьмой в общем студийный альбом в карьере BTS?",
            "Map of the Soul: 7 – The Journey",
            "The Most Beautiful Moment in Life, Pt. 1",
            "Face Yourself",
            "Love Yourself: Tear",
            1,
            500,
        ),
        TextQuestion(
            244,
            "Какое название получил пятый корейский и девятый в общем студийный альбом в карьере BTS?",
            "2 Cool 4 Skool",
            "Map of the Soul: Persona",
            "Be",
            "Wake Up",
            2,
            500,
        ),
        TextQuestion(
            245,
            "Какое название получил третий мини-альбом в карьере BTS?",
            "Love Yourself: Answer",
            "Map of the Soul: Persona",
            "The Most Beautiful Moment in Life, Pt. 1",
            "Skool Luv Affair",
            3,
            500,
        ),
        TextQuestion(
            246,
            "Какое название получил четвёртый мини-альбом в карьере BTS?",
            "Wake Up",
            "O!RUL8,2?",
            "Youth",
            "The Most Beautiful Moment in Life, Pt. 2",
            4,
            500,
        ),
        TextQuestion(
            247,
            "Какое название получил пятый мини-альбом в карьере BTS?",
            "Love Yourself: Her",
            "Love Yourself: Tear",
            "The Most Beautiful Moment in Life, Pt. 2",
            "Dark & Wild",
            1,
            500,
        ),
        TextQuestion(
            248,
            "Какое название получил шестой мини-альбом в карьере BTS?",
            "The Most Beautiful Moment in Life, Pt. 1",
            "Map of the Soul: Persona",
            "Wings",
            "Love Yourself: Her",
            2,
            500,
        )
    )

    val audioQuestions = listOf(
        AudioQuestion(
            240,
            "jin_i_ll_be_there",
            "Jin 'The Astronaut'", "Jin '슈퍼 참치 (Super Tuna)'",
            "Jin 'I'll Be There'", "I'm Fine",
            3, 300,
        )
    )

    val videoQuestions = listOf(
        VideoQuestion(
            68,
            "jin_i_ll_be_there_video",
            "Jin 'The Astronaut'", "Jin '슈퍼 참치 (Super Tuna)'",
            "Jin 'I'll Be There'", "I Need U",
            3, 550,
        )
    )
}