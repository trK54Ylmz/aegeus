<#macro layout>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>${title!"Error"} - Aegeus</title>
    <link rel="stylesheet" href="/assets/css/bootstrap.css"/>
    <link rel="stylesheet" href="/assets/css/style.css"/>
</head>
<body>
<div class="container">
    <#nested>
</div>
</body>
<script src="/assets/js/jquery.min.js"></script>
<script src="/assets/js/bootstrap.min.js"></script>
</html>
</#macro>