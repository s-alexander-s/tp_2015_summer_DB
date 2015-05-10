INSERT INTO subscribing
(thread_id, user_id)
VALUES (
  ${thread},
  (SELECT id FROM user WHERE email = '${user}')
)
;