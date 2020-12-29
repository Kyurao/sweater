<#include "security.ftlh">

<#macro page>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Sweater</title>
    <script src="/webjars/jquery/3.5.1/jquery.min.js"></script>
    <script src="/webjars/popper.js/1.16.0/umd/popper.min.js"></script>
    <script src="/webjars/bootstrap/4.5.3/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="/webjars/bootstrap/4.5.3/css/bootstrap.min.css"/>
</head>

<body>
    <#include "navbar.ftlh">
    <div class="container-fluid mt-3"> <!-- Bootstrap aligning content -->
        <div class="row">
                <nav class="col-md-2 d-none d-md-block sidebar">
                <#if known>
                <img src="/img/${photo}" class="rounded-circle my-4" style="width:100%">
                    <div class="sidebar-sticky ml-3 mt-4">
                        <ul class="nav flex-column">
                            <li class="nav-item">
                                <a class="nav-link active" href="/user-messages/${currentUserId}">My message</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link active" href="/user/dialogs">My Dialogs</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link active" href="/users">All users</a>
                            </li>
                        </ul>
                    </div>
                </#if>
                </nav>
            <main class="col-md-9 ml-sm-auto col-lg-10 px-4" role="main">
            <#nested>
            </main>
        </div>
    </div>
</body>
</html>
</#macro>