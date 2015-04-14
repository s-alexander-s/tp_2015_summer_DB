UPDATE user
SET about = '${about}', name = '${name}', isAnonymous = ${(isAnonymous?c)!"isAnonymous"}