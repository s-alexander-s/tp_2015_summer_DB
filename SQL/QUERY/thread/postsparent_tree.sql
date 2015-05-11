SELECT user.email as user, date, dislikes, forum, post.id, isApproved, isDeleted, isEdited, isHighlighted, isSpam, likes, message, parent, points, post.thread_id as 'thread'
FROM post JOIN user ON (user.id = post.user_id)
WHERE
thread_id = ${thread}
<#if since??>AND date > '${since}'</#if>
ORDER BY date ${order!"DESC"}
<#if limit??>LIMIT ${limit}</#if>
;