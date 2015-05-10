SELECT *
FROM thread
WHERE
<#if user_id??>user_id = ${user_id}<#else>forum_id = ${forum_id}</#if>
<#if since??>AND date > '${since}'</#if>
ORDER BY date ${order!"DESC"}
<#if limit??>LIMIT ${limit}</#if>
;