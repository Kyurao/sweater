<#assign
    <#--if session exists = true -->
    known = Session.SPRING_SECURITY_CONTEXT??
>

<#if known>
    <#assign
    <#-- get current user from Security principal -->
        user = Session.SPRING_SECURITY_CONTEXT.authentication.principal
    <#-- we can use users methods -->
        name = user.getUsername()
        isAdmin = true
    >
<#else>
    <#assign
        name = "Guest"
        isAdmin = false
        currentUserId = -1
    >
</#if>
