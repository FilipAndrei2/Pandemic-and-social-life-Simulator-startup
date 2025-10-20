CREATE TABLE IF NOT EXISTS simulations (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT UNIQUE NOT NULL,
    entity_number INTEGER NOT NULL DEFAULT 0 CHECK(entity_number >= 0)
);

CREATE TABLE IF NOT EXISTS vehicles (
    id INTEGER NOT NULL CHECK(id > 0),
    sim_id INTEGER NOT NULL CHECK(sim_id > 0),
    PRIMARY KEY(id, sim_id),
    FOREIGN KEY(sim_id) REFERENCES simulations(id)
);

CREATE TABLE IF NOT EXISTS families (
    id INTEGER NOT NULL CHECK(id > 0),
    sim_id INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS  persons (
    id INTEGER NOT NULL CHECK(id > 0),
    sim_id INTEGER NOT NULL,
    partner_id INTEGER DEFAULT NULL CHECK(partner_id != id),
    house_id INTEGER NOT NULL,
    vehicle_id INTEGER DEFAULT NULL,
    family_id INTEGER,
    name TEXT NOT NULL,
    infectionState TEXT NOT NULL DEFAULT 'HEALTHY' CHECK (infectionState IN ('DEAD', 'HEALTHY', 'INFECTED', 'IMMUNIZED', 'VACCINATED', 'IMMUNE')),
    lifeStage TEXT NOT NULL DEFAULT 'CHILD' CHECK (lifeStage IN ('INFANT', 'CHILD', 'ADULT', 'OLD')),
    profession TEXT NOT NULL DEFAULT 'UNEMPLOYED' CHECK (profession IN ('UNEMPLOYED', 'STUDENT', 'TEACHER', 'WORKER')),
    mood TEXT NOT NULL DEFAULT 'NEUTRAL' CHECK (mood IN ('NEUTRAL', 'HAPPY', 'SAD', 'ANGRY')),
    maxHp INTEGER NOT NULL CHECK (maxHp >= 0),
    hp INTEGER NOT NULL CHECK (hp >= 0 AND hp <= maxHp),
    maxEnergy INTEGER NOT NULL CHECK (maxEnergy >= 0),
    energy INTEGER NOT NULL CHECK (energy >= 0),
    world_x REAL NOT NULL,
    world_y REAL NOT NULL,
    isInsideBuilding INTEGER NOT NULL DEFAULT 0 CHECK (isInsideBuilding in (0, 1) AND (isInsideBuilding=0 OR vehicle_id IS NULL)),
    isTired INTEGER NOT NULL DEFAULT 0 CHECK (isTired in (0, 1)),
    PRIMARY KEY(sim_id, id),
    FOREIGN KEY(sim_id) REFERENCES simulations(id) ON DELETE CASCADE,
    FOREIGN KEY(family_id) REFERENCES families(id) ON DELETE SET NULL,
    FOREIGN KEY(partner_id) REFERENCES persons(id) ON DELETE SET DEFAULT,
    FOREIGN KEY(vehicle_id) REFERENCES vehicles(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS family_members (
    sim_id INTEGER NOT NULL CHECK(sim_id > 0),
    member_id INTEGER NOT NULL CHECK(member_id > 0),
    family_id INTEGER NOT NULL CHECK(member_id > 0),
    FOREIGN KEY(member_id) REFERENCES persons(id) ON DELETE CASCADE,
    FOREIGN KEY(sim_id) REFERENCES simulations(id) ON DELETE CASCADE,
    FOREIGN KEY(family_id) REFERENCES families(id) ON DELETE CASCADE,
    PRIMARY KEY(sim_id, member_id)
);


