SELECT email
FROM user
WHERE
EXISTS (SELECT * FROM post WHERE forum = '${forum}' AND user_id = user.id)
<#if since_id??>AND id > ${since_id}</#if>
ORDER BY name ${order!"DESC"}
<#if limit??>LIMIT ${limit}</#if>
;