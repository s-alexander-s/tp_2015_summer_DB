UPDATE thread
SET isClosed = ${isClosed?c}
WHERE id = ${thread}
;