

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
    console.log("hejhej");
	system.sendEvent("SenseReset", {});
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

var system = {};

document.ready(function() {
    Furhat(function(furhat) {
          furhat.subscribe('furhatos.event.responses.ResponseSkillGUIName', function(data) {
              if(data.port == window.location.port) {
                  setPageTitle(data.name)
              }
          })

          furhat.subscribe('furhatos.event.actions.ActionSkillGUIClear', function(data) {
              if(data.port == window.location.port) {
                  clearScreen()
              }
          })

          furhat.subscribe('furhatos.event.actions.ActionSkillGUIWrite', function(data) {
              if(data.port == window.location.port) {
                  clearScreen()
                  appendText(data.text)
              }
          })

          furhat.subscribe('furhatos.event.actions.ActionSkillGUIAppend', function(data) {
              if(data.port == window.location.port) {
                  appendText(data.text)
              }
          })

          furhat.send({
              event_name: 'furhatos.event.requests.RequestSkillGUIName',
              port: window.location.port,
          })

          system.sendEvent = (event, params) => {
              furhat.send({
                  event_name: 'LALALALALA '
              })
          }
      })

    system.sendEvent("hejhej", {"hej": "då"})
    system.sendEvent("hejhej", {"hej": "då"})
    system.sendEvent("hejhej", {"hej": "då"})
    system.sendEvent("hejhej", {"hej": "då"})
})

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
