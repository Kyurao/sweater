<#import "parts/common.ftlh" as c>
<#import "parts/login-register.ftlh" as l>

<@c.page>
<div class="text-info text-center font-weight-bold mb-3">
${message!}
</div>
<div class="mb-1 text-info">Create ACCOUNT</div>
    <@l.login "/registration" "Create" true />
</@c.page>