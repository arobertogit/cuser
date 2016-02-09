<%@page import="fii.odiunu.facebook.FBConnection"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
	FBConnection fbConnection = new FBConnection();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Register to CuSEr</title>
<link rel="stylesheet" type="text/css" href="../css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="../css/auth.css">
<link rel="stylesheet" type="text/css" href="../css/font-awesome.min.css">

<script type="text/javascript" src="../js/jquery-1.11.3.js"></script>
<script type="text/javascript" src="../js/bootstrap.min.js"></script>
<script type="text/javascript" src="../js/facebook.js"></script>

</head>
<body>

	<ul class="nav nav-tabs">
		<li class="active"><a href="#login" data-toggle="tab"
			aria-expanded="false">Login</a></li>
		<li class=""><a href="#register" data-toggle="tab"
			aria-expanded="true">Register</a></li>

	</ul>
	<div id="myTabContent" class="tab-content">
		<div class="tab-pane fade active in" id="login">
			<div class="jumbotron own-jumbotron">
				<h1 class="text-center">Login</h1>
				<form class="form-horizontal own-form" method="post" action="Auth">
					<input type="hidden" value="login" name="typeOfAuth">
					<fieldset class="own-fieldset">
						<div class="form-group">
							<label for="inputEmail" class="col-lg-2 control-label">Name</label>
							<div class="col-lg-10">
								<input type="text" class="form-control" id="inputEmail"
									placeholder="Name">
							</div>
						</div>
						<div class="form-group">
							<label for="inputPassword" class="col-lg-2 control-label">Password</label>
							<div class="col-lg-10">
								<input type="password" class="form-control" id="inputPassword"
									placeholder="Password">
							</div>
						</div>
						<div class="space3">
							<div class="form-group">
								<div class="col-lg-10 col-lg-offset-2">
									<button type="submit" class="btn btn-primary">Submit</button>
								</div>
							</div>
						</div>
					</fieldset>
				</form>
				<h5 class="text-center">or</h5>
				<div class="well own-well">
					<div align="center">
						<div id="status">
							<a href="<%=fbConnection.getFBAuthUrl()%>"> <i
								class="fa fa-facebook-square icon-size5" onclick="Login()"></i>
							</a>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="tab-pane fade" id="register">
			<div class="jumbotron own-jumbotron">
				<h1 class="text-center">Register</h1>
				<form class="form-horizontal own-form" method="post" action="Auth">
					<fieldset class="own-fieldset">
						<input type="hidden" value="register" name="typeOfAuth">
						<div class="form-group">
							<label for="inputEmail" class="col-lg-2 control-label">Name</label>
							<div class="col-lg-10">
								<input type="text" class="form-control" id="inputEmail"
									placeholder="Name">
							</div>
						</div>
						<div class="form-group">
							<label for="inputEmail" class="col-lg-2 control-label">Email</label>
							<div class="col-lg-10">
								<input type="text" class="form-control" id="inputEmail"
									placeholder="Email">
							</div>
						</div>
						<div class="form-group">
							<label for="inputPassword" class="col-lg-2 control-label">Password</label>
							<div class="col-lg-10">
								<input type="password" class="form-control" id="inputPassword"
									placeholder="Password">
							</div>
						</div>
						<div class="space3">
							<div class="form-group">
								<div class="col-lg-10 col-lg-offset-2">
									<button type="submit" class="btn btn-primary">Submit</button>
								</div>
							</div>
						</div>
					</fieldset>
				</form>
			</div>
		</div>

	</div>
</body>
</html>