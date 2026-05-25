INSERT INTO category (name, slug, description, icon, color, active) VALUES
('Friends',   'friends',   'Questions to bond with friends',     '👥', 'general', TRUE),
('Dating',    'dating',    'Questions for meaningful dates',     '❤️', 'general', TRUE),
('Deep Talk', 'deep-talk', 'Questions for deep conversations',   '🧠', 'general', TRUE),
('Fun Topics','fun-topics','Questions for lighthearted fun',     '😊', 'general', TRUE);

INSERT INTO question (text, category_id, active) VALUES
-- Friends (1)
('What is your favourite memory with your closest friend?', 1, TRUE),
('If you could travel anywhere with friends right now, where would it be?', 1, TRUE),
('What is one thing your best friend has taught you about yourself?', 1, TRUE),
('Which friendship in your life has changed you the most, and how?', 1, TRUE),
('What is the funniest moment you have ever shared with friends?', 1, TRUE),
('If your friends had to describe you in three words, which would they pick?', 1, TRUE),
('What activity always brings you closer to the people you care about?', 1, TRUE),

-- Dating (2)
('What is the most romantic gesture you have ever experienced?', 2, TRUE),
('What three qualities do you value most in a partner?', 2, TRUE),
('What is your idea of a perfect first date?', 2, TRUE),
('What is the biggest lesson a past relationship taught you?', 2, TRUE),
('How do you know when you can fully trust someone?', 2, TRUE),
('What does feeling truly loved look like to you?', 2, TRUE),
('What is a deal-breaker you would never compromise on?', 2, TRUE),

-- Deep Talk (3)
('What belief did you hold strongly five years ago that you no longer believe?', 3, TRUE),
('When in your life did you feel most alive?', 3, TRUE),
('What is something you are still trying to forgive yourself for?', 3, TRUE),
('If you could relive one day exactly as it was, which would you choose?', 3, TRUE),
('What does success mean to you, separate from what others expect?', 3, TRUE),
('What is one fear that has quietly shaped most of your decisions?', 3, TRUE),
('When was the last time you changed your mind about something important?', 3, TRUE),

-- Fun Topics (4)
('If you could have any superpower for one day, what would you choose?', 4, TRUE),
('What is your most controversial food opinion?', 4, TRUE),
('If you had to live inside any movie for a month, which one would you pick?', 4, TRUE),
('What is the weirdest thing you genuinely enjoy?', 4, TRUE),
('Which fictional character do you secretly relate to the most?', 4, TRUE),
('If animals could talk, which species would be the most annoying?', 4, TRUE),
('What is the most useless skill you are weirdly proud of?', 4, TRUE);

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