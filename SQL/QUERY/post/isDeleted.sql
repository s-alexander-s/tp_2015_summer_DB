UPDATE post
SET isDeleted = ${isDeleted?c}
WHERE id = ${post}
;