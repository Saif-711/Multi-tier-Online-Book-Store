INSERT INTO books (title, topic, quantity, price)
SELECT 'How to get a good grade in DOS in 40 minutes a day.', 'distributed systems', 10, 40
WHERE NOT EXISTS (SELECT 1 FROM books WHERE title = 'How to get a good grade in DOS in 40 minutes a day.');

INSERT INTO books (title, topic, quantity, price)
SELECT 'RPCs for Noobs.', 'distributed systems', 5, 50
WHERE NOT EXISTS (SELECT 1 FROM books WHERE title = 'RPCs for Noobs.');

INSERT INTO books (title, topic, quantity, price)
SELECT 'Xen and the Art of Surviving Undergraduate School.', 'undergraduate school', 7, 45
WHERE NOT EXISTS (SELECT 1 FROM books WHERE title = 'Xen and the Art of Surviving Undergraduate School.');

INSERT INTO books (title, topic, quantity, price)
SELECT 'Cooking for the Impatient Undergrad.', 'undergraduate school', 8, 35
WHERE NOT EXISTS (SELECT 1 FROM books WHERE title = 'Cooking for the Impatient Undergrad.');
