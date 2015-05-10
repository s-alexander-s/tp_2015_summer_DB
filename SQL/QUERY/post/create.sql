SET @mpath=(SELECT m_path FROM post WHERE id = ${parent});
INSERT INTO post
(m_path, user_id, message, thread_id, date, isApproved, isHighlighted, isEdited, isSpam, isDeleted, forum, parent)
VALUES (
  <#if parent??>CONCAT(IFNULL(@mpath,""), '.${parent}')<#else>NULL</#if>,
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