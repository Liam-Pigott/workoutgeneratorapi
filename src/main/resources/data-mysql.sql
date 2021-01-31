
/*
    Equipment
*/
INSERT INTO Equipment (id, name) VALUES (1, 'Dumbbell');
INSERT INTO Equipment (id, name) VALUES (2, 'Barbell');
INSERT INTO Equipment (id, name) VALUES (3, 'Medicine ball');
INSERT INTO Equipment (id, name) VALUES (4, 'Kettlebell');
INSERT INTO Equipment (id, name) VALUES (5, 'Pull up bar');
INSERT INTO Equipment (id, name) VALUES (6, 'Rowing machine');
INSERT INTO Equipment (id, name) VALUES (7, 'Bicycle');
INSERT INTO Equipment (id, name) VALUES (8, 'Treadmill');
INSERT INTO Equipment (id, name) VALUES (9, 'Cross trainer');
INSERT INTO Equipment (id, name) VALUES (10, 'Jump rope');
INSERT INTO Equipment (id, name) VALUES (11, 'Bench');


/*
    Starter exercises
*/
INSERT INTO Exercise (id, name, description, type, muscle_group, equipment_id) VALUES (1, 'Outdoor run', 'running', 'CARDIO', 'CORE', null);
INSERT INTO Exercise (id, name, description, type, muscle_group, equipment_id) VALUES (2, 'Dumbell Chest Press', 'Push weights away from chest', 'STRENGTH', 'CHEST', 1);
INSERT INTO Exercise (id, name, description, type, muscle_group, equipment_id) VALUES (3, 'Barbell Chest Press', 'Push bar away from chest', 'STRENGTH', 'CHEST', 2);
INSERT INTO Exercise (id, name, description, type, muscle_group, equipment_id) VALUES (4, 'Tricep Dip', 'dip until your triceps hurt', 'STRENGTH', 'TRICEPS', 11);
INSERT INTO Exercise (id, name, description, type, muscle_group, equipment_id) VALUES (5, 'Hammer Curl', 'Curl dumbell towards chest', 'STRENGTH', 'BICEPS', 1);
INSERT INTO Exercise (id, name, description, type, muscle_group, equipment_id) VALUES (6, 'Bodyweight squat', 'Squat without additional weight', 'STRENGTH', 'QUADS', null);