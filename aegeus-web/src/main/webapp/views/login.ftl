<#import "form_layout.ftl" as form>
<@form.layout>
<div class="row">
    <div class="login-form shadow-border">
        <form action="/login" method="post" accept-charset="utf-8">
            <label for="username" class="non-b">Username</label>
            <input type="text" name="username" id="username" class="form-control"/>
            <label for="password" class="mt non-b">Password</label>
            <input type="password" name="password" id="password" class="form-control"/>
            <button class="btn btn-primary mt-x-l">Log In</button>
        </form>
    </div>
</div>
</@form.layout>