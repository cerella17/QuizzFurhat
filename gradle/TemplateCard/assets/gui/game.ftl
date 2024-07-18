<!DOCTYPE html>
<html>
  <head>
    <title>Timeline</title>
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, minimal-ui">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-title" content="Timeline">

    <link rel="stylesheet" href="/css/font-awesome.min.css">
<style type="text/css">
body {
	margin: 0px;
  overflow: hidden;
  background: url(img/bg.png) no-repeat center center fixed;
  background-size: cover;
  width: 100vmax;
  height: 100vmax;
}
::-webkit-scrollbar-thumb {
	box-shadow: inset 0 0 10px rgb(12,70,88);
	-moz-box-shadow: inset 0 0 10px rgb(12,70,88);
	-webkit-box-shadow: inset 0 0 10px rgb(12,70,88);
	background: rgb(14,77,96);
}
::-webkit-scrollbar-track {
	box-shadow: inset 0 0 6px 20px rgb(208, 208, 208);
	-moz-box-shadow: inset 0 0 6px 20px rgb(208, 208, 208);
  -webkit-box-shadow: inset 0 0 6px 20px rgb(208, 208, 208);
}
::-webkit-scrollbar {
	width: 18px;
}

#top_menu{
  margin-top: .5em;
  margin-right: .5em;
  border-radius: 4px;
  width: 100%;
  max-width: 520px;
  position: absolute;
  right:0px;
  top:0px;
}
.btn{
  padding: .5em 1em;
  border: none;
  -webkit-border: none;
  -moz-border: none;
  outline: none;
  -webkit-outline: none;
  -ms-outline: none;
  cursor: pointer;
  background: #E5603F;
  color: #fff;
  font-size: 12px;
  box-shadow: 0 1px 0 #343438;
  -webkit-box-shadow: 0 1px 0 #343438;
  -ms-box-shadow: 0 1px 0 #343438;
  -moz-shadow: 0 1px 0 #343438;
  border-radius: 2px;
  width: 100%;
  max-width: 100px;

}
.btn.orange{
   background-image: linear-gradient(#12586d,#04303e);
}
.btn.orange:hover{
  background-image: linear-gradient(#094860,#0b506b);
  border: 1px solid #696A6E;
  transition: all .2s ease-in-out;
}
.btn.orange:active{
  background-image: radial-gradient(#6b6c73,#37393d);
  border: 1px solid #37393d;
  color: #151419;
}

select{
  height: 53px;
  padding-bottom: 1em !important;
}
select option{
  background: #57585D;
  border: none;
}
#taskbutton{
  display:none;
  font-size:4vmin;
  color: #fff;
  padding:20px;
  box-shadow: 2px 4px 10px #204e6a;
  cursor: pointer;
  background-image: linear-gradient(#004660,#08303f);
  border: none;
outline: none;
z-index: 999;
}
#taskbutton:hover {
    background: #0d4b5e;
}
#plar_icons {
	position: absolute;
	width: 100vw;
  margin: .5em;
}
#plar_icons img {
	max-width: 100px;
}
.card {
	position:absolute;
	width: 15%;
	height: 40%;
	border-radius: 10px;
	background:white;
	display:none;
	box-shadow: 18px 15px 21px rgba(24, 78, 127, 0.59);
	z-index: 1;
}
.deckset {
  position: absolute;
  top: 15%;
  left: 7%;
  width: 88%;
  text-align: center;
  height: 84%;
  overflow-y: auto;
}
.categoryset {
	position:absolute;
	top:15%;
	left:7%;
	width:88%;
  display:none;
	text-align: center;
}
.deck {
	width: 15vw;
	height: 40vh;
	box-shadow: 18px 15px 21px rgba(24, 78, 127, 0.59);
	border-radius: 10px;
	background:white;
	display:none;
	vertical-align: top;
	margin-right:2%;
	margin-bottom:2%;
}
.category {
	width: 15vw;
	height: 40vh;
	box-shadow: 18px 15px 21px rgba(24, 78, 127, 0.59);
	border-radius: 10px;
	background:white;
	display:inline-block;
	vertical-align: top;
	margin-right:2%;
	margin-bottom:2%;
}
.decklabel {
	font-family:arial;
	text-align:center;
	width: 100%;
	font-size: 2vmin;
	white-space: normal
}
.picture {
	margin:5%;
	background-image:url(img/blank.png);
	background-size: cover;
	background-repeat: no-repeat;
	background-position: center;
	width: 90%;
	height: 80%;
  box-shadow: 0 5px 13px rgba(0, 0, 0, 0.07);
}
.label {
	font-family:arial;
	text-align:center;
	width: 100%;
	font-size: 2vmin;
}
.smiley {
	width:60%;
	position:absolute;
    left:0; right:0;
    top:0; bottom:0;
    margin:auto;
    display:none;
}
.truthlabel {
  width:60%;
  height:4vh;
  border:1px solid black;
  font-size: 2vmin;
  font-family:helvetica;
  background:white;
  position:absolute;
  left:0; right:0;
  top:20vh; bottom:0;
  text-align:center;
  vertical-align: middle;
  line-height: 4vh;
  margin:auto;
  display:none;
}
.header0{
  position:absolute;
  left:0px;
  top:5vh;
  width:100%;
  font-size:4vmin;
  font-weight:bold;
  font-family:helvetica;
  color:white;
  text-align:center;
}
.header1{
  position:absolute;
  left:0px;
  top:5vh;
  width:100%;
  font-size:4vmin;
  font-weight:bold;
  font-family:helvetica;
  color:white;
  text-align:center;
}
.header2 {
	position:absolute;
	left:0px;
	top:15vh;
	width:100%;
	font-size:4vmin;
	font-family:helvetica;
	color:white;
	text-align:center;
}
</style>

<script src="js/jquery-2.1.4.min.js"></script>

<script type="text/javascript">

var width = 0;
var height = 0;
var ncards = 5;
var touchTimer;
var sendTimer;
var updateStarted = false;
var ongoing = new Array();
var touches = new Array();
var withdrawn = true;
var positions = new Array();
var table = document.getElementById("card-table");
var heighestZIndex = 5;
var z = 1;

function touchUpdate() {
	if (updateStarted) return;
	updateStarted = true;
	for (i = 0; i < touches.length; i++) {
		var touch = touches[i];
		var px = touch.pageX;
		var py = touch.pageY;
		var o = ongoing[touch.identifier];
		if (o != null) {
			var card = $("#" + o.card);
			var pos = card.offset();
			var topCalc = pos.top + (py - o.y);
			var leftCalc = pos.left + (px - o.x);

      //Check Card Top Left offset
			if(topCalc > screen.height - 420){
				topCalc = screen.height - 420;
			}
			else if(topCalc < 0){
				topCalc = 0;

			}
      else if(leftCalc > screen.width - 285){
				leftCalc = screen.width - 285;

			}
      else if(leftCalc < 0){
				leftCalc = 0;

			}
			card.offset({top: topCalc, left: leftCalc});
			o.x = px;
			o.y = py;
		}
	}
	updateStarted = false;
}

function touchstart(ts) {
	for (i = 0; i < ts.length; i++) {
		var touch = ts[i];
		var px = touch.pageX;
		var py = touch.pageY;
		var current_cards = [];
		ongoing[touch.identifier] = null;
		for (c = 0; c < ncards; c++) {
			var card = $("#card" + c);
			var pos = card.offset();
			if (px > pos.left && px < (pos.left + card.width()) && py > pos.top && py < (pos.top + card.height())) {
				current_cards.push(card);
			}
		}
		var cardZIndex = 0, b;
	    for (var f = 0; f < current_cards.length; f++) {
	      var ca = current_cards[f];
        //Set card z-index when touched
	      if (ca.css('z-index') > cardZIndex){
	        cardZIndex = ca.css('z-index');
	        b = ca;
	        o = new Object();
	        ongoing[touch.identifier] = o;
	        o.card = b.attr("id");
	        o.x = px;
	        o.y = py;
	      }
	    }
	    if (b != undefined){
	      b.css('z-index', heighestZIndex++);
	    }
	}
}
function touchend(ts) {
	if (ts.length == 0) {
		touches = [];
	}
}
function setupCards() {
	for (c = 0; c < ncards; c++) {
		var card = $("#card" + c);
		card.offset({top: -card.height()-30, left: (width / 2) - (card.width() / 2)});
		card.show();
	}
	withdrawn = true;
}
function withdrawCards() {
	if (!withdrawn) {
		for (i = 0; i < ncards; i++) {
			$(".card").removeAttr("item-id");
		}
		for (c = 0; c < ncards; c++) {
			var card = $("#card" + c);
			card.animate({top: -card.height()-30, left: (width / 2) - (card.width() / 2)}, 2000);
		}
		withdrawn = true;
		return 2000;
	}
	return 0;
}
function dealCards(delay, time) {
	for (c = 0; c < ncards; c++) {
		var card = $("#card" + c);
		card.animate({top: (height / 2) - (card.height() / 2), left: (c + 0.5) * (width / ncards) - (card.width() / 2)}, 2000);
	}
	withdrawn = false;
}
function hideSmileys() {
	for (c = 0; c < ncards; c++) {
		$("#cardsmi" + c).hide();
		$("#cardtruth" + c).hide();
	}
}
function showDashboard() {
	$("#dashboard").attr("src", "/dashboard/panel");
	$("#dashboard").show();
}
function hideDashboard() {
	$("#dashboard").attr("src", "/empty.html");
	$("#dashboard").hide();
}
function toggleDashboard() {
	if ($("#dashboard").is(":visible")) {
		hideDashboard();
	}
  else {
		showDashboard();
	}
}
function resetGame() {
	system.sendEvent("sense.game.reset", {});
}
function refreshGame() {
	location.reload();
	system.sendEvent("sense.game.refresh", {});
}
function pauseGame() {
	system.sendEvent("sense.game.pause", {});
}
function showInfo(left, right) {
	if(left == ""){
			left = "img/pInactive.png";
	}
	if(right == ""){
			right = "img/pInactive.png";
	}
	$("#leftInfo").attr("src", left);
	$("#leftInfo").show();
	$("#rightInfo").attr("src", right);
	$("#rightInfo").show();
}
function noInfo() {
	$("#leftInfo").attr("src", "img/pInactive.png");
	$("#rightInfo").attr("src", "img/pInactive.png");
}
function senseButton() {
	system.sendEvent("sense.game.button", {});
}
function chooseCategory(category) {
	$("body").css('overflow', 'hidden');
  system.sendEvent("sense.game.choosecategory", {category:category});
}
function chooseTask(task) {
	$("body").css('overflow', 'hidden');
	system.sendEvent("sense.game.choosetask",
		  {task:task}
		);
}
function resize() {
	for (c = 0; c < ncards; c++) {
		var card = $("#card" + c);
		var offset = card.offset();
		card.offset({top: offset.top * window.innerHeight / height, left: offset.left * window.innerWidth / width})
	}
	width = window.innerWidth;
	height = window.innerHeight;
}
function load() {
	width = window.innerWidth;
	height = window.innerHeight;
	touchTimer = setInterval(touchUpdate, 40);
	document.addEventListener('touchend', function() {
		touchend(event.touches);
	});
	document.addEventListener('touchmove', function(event) {
	  event.preventDefault();
	  touches = event.touches;
	});
	document.addEventListener('touchstart', function(event) {
	  touchstart(event.touches);
	});
	setupCards();
	system.sendEvent("monitor.game.connect", {});
};

function toggleFullScreen() {
	if ((document.fullScreenElement && document.fullScreenElement !== null) || (!document.mozFullScreen && !document.webkitIsFullScreen)) {
		if (document.documentElement.requestFullScreen) {
	      document.documentElement.requestFullScreen();
	    }
      else if (document.documentElement.mozRequestFullScreen) {
	      document.documentElement.mozRequestFullScreen();
	    }
      else if (document.documentElement.webkitRequestFullScreen) {
	      document.documentElement.webkitRequestFullScreen(Element.ALLOW_KEYBOARD_INPUT);
	    }
	}
  else {
		if (document.cancelFullScreen) {
	      document.cancelFullScreen();
	    }
      else if (document.mozCancelFullScreen) {
	      document.mozCancelFullScreen();
	    }
      else if (document.webkitCancelFullScreen) {
	      document.webkitCancelFullScreen();
	    }
	}
}

var system = parent.system;

system.onEvent = function (name, params) {
	if (name == "action.game.new") {
		hideSmileys();
		delay = withdrawCards();
		setTimeout(function() {
			for (c = 0; c < ncards; c++) {
				var card = params.cards[c];
				$("#cardpic" + c).css("background-image","url(" + card.img.replace(/ /g, "%20") + ")");
				$("#cardtruth" + c).html(card.truth);
				$("#cardlbl" + c).html(card.label);
				$("#card" + c).attr("item-id", card.id);
			}
			$("#header2").html(params.taskdesc);
			dealCards();
		}, delay );
	}
  else if (name == "action.game.showbutton") {
		$("#taskbutton").attr("value", params.text);
		$("#taskbutton").show();
	}
  else if (name == "action.game.hidebutton") {
		$("#taskbutton").hide();
	}
  else if (name == "action.game.header1") {
		if (params.text == "") {
			$("#header1").hide();
		}
    else {
			$("#header1").html(params.text);
			$("#header1").show();
		}
	}
  else if (name == "action.game.info") {
		showInfo(params.left, params.right);
	}
  else if (name == "action.game.noinfo") {
		noInfo();
	}
  else if (name == "action.game.showdashboard") {
		showDashboard();
	}
  else if (name == "action.game.hidedashboard") {
		hideDashboard();
	}
  else if(name=="action.game.showcategories"){
    $("#header0").html(params.instruction);
    $("#header0").show();
    for(var i=0;i<$(".category").length;i++){
      $("."+$(".category")[i].className.split(" ")[1]+":not(:first)").remove();
    }
    $("#categoryset").show();
    $("#deckset").hide();
    $(".deck").css("display","none");
    $("#header1").hide();
    if($(".category").length == 1){
      $(".category")[0].click();
    }
  }
  else if (name == "action.game.showtasks") {
    $("#header0").hide();
    $("#categoryset").hide();

		$("#header1").html(params.instruction);
    $("#header1").show();

    $("#deckset").show();
    $(".deck").css("display","none");
    $("."+params.categoryName).css("display","inline-block");

		//Check when more than 10 Task in deckset
		var decksetLength = $("#deckset .deck").length;
		if(decksetLength > 10){
      //Set body overflow-x
			//$("body").css('overflow-y', 'auto');
			console.log(decksetLength);
		}
  }
  else if (name == "action.game.hidetasks") {
    	$("#header1").hide();
		  $("#deckset").hide();
	}
  else if (name == "action.game.reveal") {
		for (c = 0; c < ncards; c++) {
			var pic = "sad.png";
			if (params.solution[c]) {
				pic = "happy.png";
			}
			$("#cardsmi" + c).attr("src", "img/" + pic);
			$("#cardsmi" + c).show();
			$("#cardtruth" + c).show();
		}
	}
  else if (name == "action.game.solve") {
		for (c = 0; c < ncards; c++) {
			var card = $("#card" + c);
			card.animate({top: (height / 2) - (card.height() / 2), left: (params.solution[c] + 0.5) * (width / ncards) - (card.width() / 2)}, 2000);
		}
	}
  else if (name == "action.game.clear") {
		$("#taskbutton").hide();
		$("#header2").html("");
		hideSmileys();
		withdrawCards();
	}
}

</script>

</head>
<body onload="load()" onresize="resize()" oncontextmenu="return false;">

<img src="/img/logo_small.png" width="100px" style="position:absolute; right: 15px; bottom: 15px"/>

<div class="table">

<div id="header0" class="header0"></div>

<div id="header1" class="header1"></div>

<div id="header2" class="header2"></div>

<#list 0..4 as i>
<div class="card" id="card${i}">
	<div id="cardpic${i}" class="picture">
		<img id="cardsmi${i}" src="img/happy.png" class="smiley"/>
		<div id="cardtruth${i}" class="truthlabel">3000</div>
	</div>
	<div id="cardlbl${i}" class="label"></div>
</div>
</#list>

<div class="categoryset" id="categoryset">
	<#list tasks as task>
	<div class="category cat${task.category}" id="category${task.category}" onclick="chooseCategory('${task.category}')">
		<div class="picture" style="background-image:url(image?task=${task.id}&name=${task.cards.card[0].image})"></div>
		<div class="categorylabel">${task.category}</div>
	</div>
	</#list>
</div>

<div class="deckset" id="deckset">
	<#list tasks as task>
	<div class="deck ${task.category}" id="deck${task.id}" onclick="chooseTask('${task.id}')">
		<div class="picture" style="background-image:url(image?task=${task.id}&name=${task.cards.card[0].image})"></div>
		<div class="decklabel">${task.title}</div>
	</div>
	</#list>
</div>

<div style="position:absolute;width:0;height:0;left:25vw;top:50vh;visibility:hidden" item-id="game_lowest"></div>
<div style="position:absolute;width:0;height:0;left:75vw;top:50vh;visibility:hidden" item-id="game_highest"></div>

<div id="plar_icons">
	<img src="img/pActive.png" id="leftInfo" style="display:none"/>
	<img src="img/pInactive.png" id="rightInfo" style="display:none"/>
</div>

<div style="position:absolute;left:0px;top:80vh;width:100%;text-align:center; z-index: 9999;">
	<input id="taskbutton" type="button" value="Visa lösningen" onclick="senseButton()" item-id="game_button"/>
</div>

<iframe style="display:none;position:absolute;left:20vw;top:1vh;width:60vw;height:55vh" id="dashboard" src="/empty.html" frameBorder="0">
</iframe>

</div>


<div id="top_menu">

  <button type="button" class="btn orange" onclick="toggleFullScreen()">
    <i class="fa fa-arrows-alt" aria-hidden="true" style="display: block;
    font-size: 24px;
    margin-bottom: 5px;"></i> Fullscreen
  </button>

  <button type="button" class="btn orange" onclick="toggleDashboard()">
    <i class="fa fa-wrench" aria-hidden="true" style="display: block;
    font-size: 24px;
    margin-bottom: 5px;"></i> Dashboard
  </button>

  <button type="button" class="btn orange" onclick="resetGame()">
    <i class="fa fa-refresh" aria-hidden="true" style="display: block;
    font-size: 24px;
    margin-bottom: 5px;"></i> Reset
  </button>

  <button type="button" class="btn orange" onclick="pauseGame()">
    <i class="fa fa-pause" aria-hidden="true" style="display: block;
    font-size: 24px;
    margin-bottom: 5px;"></i> Pause
  </button>

</div>


</body>
</html>
