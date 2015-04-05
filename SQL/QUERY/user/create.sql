INSERT INTO user
(email, username, name, about, isAnonymous)
<#escape x as x?js_string>
VALUES('${email}', '${username}', '${name}', '${about}', ${isAnonymous?c!"FALSE"})
</#escape>
