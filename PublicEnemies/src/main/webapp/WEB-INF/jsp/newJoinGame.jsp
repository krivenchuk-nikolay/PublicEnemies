<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Game</title>	
<script type="text/javascript" src="js/jquery-1.7.2.js"></script>
<style type="text/css">
body {
	background-image: url('img/bg_city.jpg');
	color: white;
	font-family: 'impact';
	font-size: 14pt;
}

table {
	background-color: black
}

A:link {
	text-decoration: underline;
	color: white
}

A:visited {
	text-decoration: none;
	color: white;
}

A:hover {
	text-decoration: underline;
	color: red;
}

table#main{
	background-color: #444D48;
	opacity: .6;
	filter: alpha(opacity = 60);
	-moz-opacity: .6;
	border-width: 3px;
	border-spacing: 5px;
	border-style: outset;
	border-color: white;
	border-collapse: separate;
}
</style>
<script type="text/javascript">
$("document").ready(function(){
	$("div#pvp").slideUp(100);
	$("div#join").slideUp(100);
	$("div#bot").slideUp(100);
});

</script>
</head>
<body>
	<table align="center" width="100%" style="background-color: transparent">
		<tr>
			<td style="background-color: black; width: 100%;">
				<!--  up bar with stats, gamelogo, avatar -->
				<table width="100%">
					<tr>
						<td width="100" align="left">
							<img src="img/gamelogo_small.jpg" width="100px"></img>
						</td>
						<td width="40" valign="middle" align="right">
							<img src="img/coin.png" width="40px" title="Money"></img>
						</td>
						<td width="20" valign="middle" align="left">
							904
						</td>
						<td width="80" valign="middle" align="right">
							<img src="img/hp.png" width="40px" title="Hit Points"></img>
						</td>
						<td width="20" valign="middle" align="left">
							80
						</td>
						<td width="80" valign="middle" align="right">
							<img src="img/strength.png" width="40px" title="Strength"></img>
						</td>
						<td width="20" valign="middle" align="left">
							21
						</td>
						<td width="80" valign="middle" align="right">
							<img src="img/agility.png" width="40px" title="Agility"></img>
						</td>
						<td width="20" valign="middle" align="left">
							34
						</td>
						<td width="80" valign="middle" align="right">
							<img src="img/inteligence.png" width="40px" title="Inteligence"></img>
						</td>
						<td width="20" valign="middle" align="left">
							8
						</td>
						<td width="100" align="right">
							<img src="img/avatars/gangster.gif" title="${user_email}'s profile" border="1" width="40px"></img>
						</td>
						<td width="30" align="center">
							<a href="logout.html">Logout</a> <br />
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table id="main" align="center" width="500" height="500" style="margin-top: 50px">
					<tr>
						<td>
							<form name="radio">
								<div align="left"><br />
									<input type="radio" name="group1" value="0"> Create new game vs player<br>
									<!-- PvP mode -->
										<div id="pvp" align="center">
											<form action="newGame.html" method="post">
												<spring:bind path="gameToCreate.name">
												<p>Enter the name of the game that you want to create:</p>
												<input type="text" name="name" value="${status.value}"><br />
												<font color="red">${status.errorMessage}</font>
												<br />
												</spring:bind>
												<button type="submit" id="button">Create</button><br />
											</form>
										</div>
									<input type="radio" name="group1" value="1">Join an existing game<br />
									<!-- Join game mode  -->
										<div id="join" align="center">
											<form action="joinGame.html" method="post">
												<spring:bind path="gameToJoin.name">
												<p>Enter the name of the game that you want to join:</p>
												<input type="text" name="name" value="${status.value}">
												<font color="red">${status.errorMessage}</font>
												<br />
												</spring:bind>
												<button type="submit" id="button">Join</button>
											</form>
										</div>
									<input type="radio" name="group1" value="2">Create new game vs bot
									<!-- Player vs Bot mode -->
									<div id="bot" align="center">
										<h1>Not implemented yet, sorry :)</h1>
									</div>
								</div>
							</form>
						</td>
					</tr>
					<tr>
						<td valign="bottom" align="center">
							<a href="userStartPage.html">Back</a>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
<script type="text/javascript">
	$("input[name='group1']").change(function(){
		if($("input[name='group1']:checked").val() == 0){
			$("div#pvp").slideDown("slow");
			$("div#join").slideUp("slow");
			$("div#bot").slideUp("slow");
		}else if ($("input[name='group1']:checked").val() == 1){
			$("div#pvp").slideUp("slow");
			$("div#join").slideDown("slow");
			$("div#bot").slideUp("slow");
		}else if ($("input[name='group1']:checked").val() == 2){
			$("div#pvp").slideUp("slow");
			$("div#join").slideUp("slow");
			$("div#bot").slideDown("slow");
		}
	});
</script>		
		
</body>
</html>