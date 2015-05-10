UPDATE thread
SET isDeleted = ${isDeleted?c}
WHERE id = ${thread}
;