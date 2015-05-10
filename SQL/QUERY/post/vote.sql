UPDATE post
SET points = points + ${vote}
<#if vote == 1>, likes = likes + 1</#if>
<#if vote == -1>, dislikes = dislikes + 1</#if>
WHERE id = ${post}
;