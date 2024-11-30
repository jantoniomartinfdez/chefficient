CREATE TABLE IF NOT EXISTS recipes (
    id SERIAL PRIMARY KEY,
    title VARCHAR(50) NOT NULL,
    steps TEXT NOT NULL,
    description TEXT NOT NULL,
    recommendation TEXT NOT NULL,

    UNIQUE (title)
);

CREATE TABLE IF NOT EXISTS ingredients (
    id SERIAL PRIMARY KEY,
    recipe_id INTEGER,
    name VARCHAR(50) NOT NULL,

    UNIQUE (recipe_id, name)
)