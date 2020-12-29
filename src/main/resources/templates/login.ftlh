<#import "parts/common.ftlh" as c>
<#import "parts/login-register.ftlh" as l>

<@c.page>
<#if Session?? && SPRING_SECURITY_LAST_EXCEPTION??>
    <div class="alert alert-danger" role="alert">
        ${SPRING_SECURITY_LAST_EXCEPTION.message}
    </div>
</#if>
<div class="text-info text-center font-weight-bold mb-3">
${message!}
</div>
<@l.login "/login" "Sign in" false />
</@c.page>
