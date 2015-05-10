DELETE FROM subscribing
WHERE thread_id = ${thread} AND user_id = (SELECT id FROM user WHERE email = '${user}')
;
