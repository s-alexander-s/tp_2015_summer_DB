INSERT INTO thread
(title, slug, date, user_id, forum_id, isClosed, message, isDeleted)
VALUES ('${title}', '${slug}', '${date}', ${user_id}, ${forum_id}, ${(isClosed!false)?c}, '${message}', ${(isDeleted!false)?c});