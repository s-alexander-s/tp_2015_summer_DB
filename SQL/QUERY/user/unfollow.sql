DELETE following
FROM following JOIN user as u1 JOIN user as u2 ON (follower_id = u1.id AND followee_id = u2.id)
WHERE u1.email = '${follower}' AND u2.email = '${followee}';