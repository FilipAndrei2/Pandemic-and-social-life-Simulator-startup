  PRAGMA foreign_keys = ON;
    CREATE TABLE IF NOT EXISTS simulations (
        sim_id INTEGER PRIMARY KEY AUTOINCREMENT,
        name TEXT
    );

    CREATE TABLE IF NOT EXISTS persons (
        sim_id INTEGER NOT NULL,
        id INTEGER NOT NULL,
        maxHp INTEGER NOT NULL CHECK (maxHp <= 255),
        hp INTEGER NOT NULL CHECK (hp <= maxHp),
        x REAL NOT NULL,
        y REAL NOT NULL,
        age TEXT CHECK (age in ('DEAD', 'INFANT', 'CHILD', 'TEEN', 'ADULT', 'OLD')) DEFAULT 'ADULT' NOT NULL,
        infectionState TEXT CHECK (infectionState in ('DEAD', 'HEALTHY', 'INFECTED', 'IMMUNIZED', 'IMMUNE')) NOT NULL,
        familyId INTEGER NOT NULL,
        firstName TEXT NOT NULL,
        FOREIGN KEY(sim_id) REFERENCES simulations(sim_id) ON DELETE CASCADE,
        PRIMARY KEY(sim_id, id)
    );

    CREATE TABLE IF NOT EXISTS buildings (
        sim_id INTEGER NOT NULL,
        id INTEGER NOT NULL,
        x INTEGER,
        y INTEGER,
        w INTEGER,
        h INTEGER,
        type TEXT NOT NULL CHECK(type in('HOUSE', 'HOSPITAL', 'SCHOOL', 'MARKET', 'WORKPLACE')),
        FOREIGN KEY(sim_id) REFERENCES simulations(sim_id) ON DELETE CASCADE,
        PRIMARY KEY(sim_id, id)
    );

    CREATE TABLE IF NOT EXISTS families (
        sim_id INTEGER NOT NULL,
        id INTEGER NOT NULL,
        lastName TEXT NOT NULL,
        homeId INTEGER NOT NULL,

        FOREIGN KEY(sim_id) REFERENCES simulations(sim_id) ON DELETE CASCADE,
        FOREIGN KEY(homeId) REFERENCES buildings(id) ON DELETE SET NULL,
        PRIMARY KEY(sim_id, id)
    );

    CREATE TABLE IF NOT EXISTS family_members (
        sim_id INTEGER NOT NULL,
        member_id INTEGER NOT NULL,
        family_id INTEGER NOT NULL,
        FOREIGN KEY(member_id) REFERENCES persons(id) ON DELETE CASCADE,
        FOREIGN KEY(family_id) REFERENCES families(id) ON DELETE CASCADE,
        PRIMARY KEY(sim_id, member_id)
    );
