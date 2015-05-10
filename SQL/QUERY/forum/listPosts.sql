SELECT *
FROM post
WHERE
forum = '${forum}'
<#if since??>AND date > '${since}'</#if>
ORDER BY date ${order!"DESC"}
<#if limit??>LIMIT ${limit}</#if>
;