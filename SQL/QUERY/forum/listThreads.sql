SELECT *
FROM thread
WHERE
forum_id = (SELECT id FROM forum WHERE short_name = '${forum}')
<#if since??>AND date > '${since}'</#if>
ORDER BY date ${order!"DESC"}
<#if limit??>LIMIT ${limit}</#if>
;