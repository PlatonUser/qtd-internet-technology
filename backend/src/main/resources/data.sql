INSERT INTO category (name, slug, description, icon, color, active) VALUES
('Friends',   'friends',   'Questions to bond with friends',     '👥', 'general', TRUE),
('Dating',    'dating',    'Questions for meaningful dates',     '❤️', 'general', TRUE),
('Deep Talk', 'deep-talk', 'Questions for deep conversations',   '🧠', 'general', TRUE),
('Fun Topics','fun-topics','Questions for lighthearted fun',     '😊', 'general', TRUE);

INSERT INTO question (text, category_id, active) VALUES
('What is your favourite memory with your closest friend?', 1, TRUE),
('If you could travel anywhere with friends right now, where would it be?', 1, TRUE),
('What is the most romantic gesture you have ever experienced?', 2, TRUE),
('What three qualities do you value most in a partner?', 2, TRUE),
('What belief did you hold strongly five years ago that you no longer believe?', 3, TRUE),
('When in your life did you feel most alive?', 3, TRUE),
('If you could have any superpower for one day, what would you choose?', 4, TRUE),
('What is your most controversial food opinion?', 4, TRUE);

INSERT INTO session (category_id, started_at, completed) VALUES
(1, '2026-04-08 14:30:00', TRUE),
(1, '2026-04-10 19:15:00', TRUE);

INSERT INTO session_answer (session_id, question_id, player_name, answer_text) VALUES
(1, 1, 'Alice', 'Building forts in my best friend''s basement.'),
(1, 1, 'Bob', 'Our trip to the coast when we were 12.'),
(1, 2, 'Alice', 'Tokyo with the whole crew.'),
(1, 2, 'Bob', 'A road trip through New Zealand.'),
(2, 1, 'Charlie', 'Sleepovers playing board games until 3am.'),
(2, 2, 'Diana', 'Iceland in the winter.');