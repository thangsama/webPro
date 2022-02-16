<%@ page language = "java" contentType = "text/html;charset =UTF-8" pageEncoding="UTF-8"%>
<div style = "background: #EOEOEO; height: 55px; padding: 5px;">
<div style="float: left">
<h1>My site</h1>
</div>

<div style = "float: right; padding: 10px; text-align: right;">

<!-- User store in session with attribute: loginedUser -->
hello <b>${loginedUser.userName}</b>
<br/>
Search<input name= "search">
</div>

</div>