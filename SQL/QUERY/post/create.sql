INSERT INTO post
(m_path, user_id, message, thread_id, date, isApproved, isHighlighted, isEdited, isSpam, isDeleted, forum, parent)
VALUES (
  CONCAT('${mpath!""}.', (SELECT AUTO_INCREMENT
    FROM information_schema.TABLES
    WHERE TABLE_SCHEMA = "forum_db"
    AND TABLE_NAME = "post")),
  (SELECT id FROM user WHERE email = '${user}'),
  '${message}',
  ${thread},
  '${date}',
  ${(isApproved!false)?c},
  ${(isHighlighted!false)?c},
  ${(idEdited!false)?c},
  ${(isSpam!false)?c},
  ${(isDeleted!false)?c},
  '${forum}',
  ${parent!"NULL"}
)
;