SELECT user.email
FROM following JOIN user ON (following.follower_id = user.id)
WHERE following.followee_id = ${id}
<#if since_id??>AND user.id >= ${since_id}</#if>
<#if order??>ORDER BY name ${order}</#if>
<#if limit??>LIMIT ${limit}</#if>
;
