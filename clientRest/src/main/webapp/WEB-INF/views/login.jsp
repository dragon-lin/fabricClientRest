<%--
  Created by IntelliJ IDEA.
  User: linwf
  Date: 2018/11/18
  Time: 16:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>登录</title>
    <link type="text/css" rel="stylesheet" href="./lib/bootstrap/css/bootstrap.min.css">
    <link type="text/css" rel="stylesheet" href="./css/common.css">
    <link type="text/css" rel="stylesheet" href="./css/login.css">
</head>
<body>
<div class="login-container">
    <h1>HyperLedger Fabric SDK配置</h1>
    <div class="form-container">
        <form class="form-horizontal" name="form" accept-charset="utf-8"  action="/logon" method="post">
            <div class="form-group account-box">
                <img class="icon account-icon" src="./images/account-icon.png"/>
                <input type="text" class="form-control" id="account" name="account" placeholder="用户名" autocomplete="off"/>
            </div>
            <div class="form-group password-box">
                <img class="icon password-icon" src="./images/password-icon.png"/>
                <input type="password" class="form-control" id="password"  name="password" placeholder="密码" autocomplete="off"/>
            </div>
            <div class="form-group">
                <div class="submit-box">
                    <button type="submit" class="submit-btn">登&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;录</button>
                </div>
            </div>
        </form>
    </div>
    <div style="color:red" th:if="${param.error}" th:text="用户或密码错误，请重新输入！"></div>
</div>
</body>
</html>
