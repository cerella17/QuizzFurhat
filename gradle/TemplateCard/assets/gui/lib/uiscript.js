var currentTask = "";
var fileName = "";
var cardId = "";
var imgLoadedName = "";
var currentCardId = "";
var changedImage = false;
var checkNewImg = false;
var cardNameChanged = false;

function createCardId() {
    var id = document.getElementById('CardnameN').value;
    var random = Math.floor((Math.random() * 1000) + 1);
    var ran = random.toString();
    id = id + ran;
    return id;
}

function getId() {
    cardId = createCardId();
    document.getElementById('cardIdN').value = cardId;
    document.getElementById('taskIdN').value = currentTask;
    document.getElementById('imgName').value = fileName;
}

function getCurrentTask(taskName) {
    currentTask = taskName;
}

function showTask(id) {
    var x = document.getElementsByClassName("tasks");
    for (var i = 0; i < x.length; i++) {
        x[i].style.display = 'none';
    }
    document.getElementById("Task").style.display = 'none';
    document.getElementById('cardS').style.display = 'none';
    resetCardForm();
    document.getElementById(id).style.display = 'block';
}

function addCard() {
    Dropzone.forElement("#my-awesome-dropzone").removeAllFiles(true);
    reset();
    currentCardId = "";
    $('#cardS').hide();
    $('#card').show();
    fileName = "";
    $('html,body').animate({
        scrollTop: $("#card").offset().top
    }, 'slow');
}

function handleTask(e) {
    showTask(e);
    if (e != "home") {
        getCurrentTask(e);
    }
}

function newCardCheck() {
    if (currentCardId == "") {
        return false;
    } else {
        return true;
    }
}

Dropzone.options.myAwesomeDropzone = {
    paramName: "file", // The name that will be used to transfer the file
    maxFilesize: 10, // MB
    maxFiles: 1,
    addRemoveLinks: true,
    uploadMultiple: false,
    //  createImageThumbnails: false,
    acceptedFiles: 'image/*',

    init: function() {
        this.on("sending", function(file) {
            fileName = file.name;
            document.getElementById('cardId1').value = fileName;
            document.getElementById('cardId2').value = fileName;
        });

        this.on("maxfilesexceeded", function(file) {
            this.removeAllFiles();
            this.addFile(file);
        });

        this.on("removedfile", function(file) {
            $.post("deleteCardImage", {
                image: fileName
            }, function(data, status) {});

        });

        this.on("complete", function(file) {});

        this.on("error", function(file) {});

    }
}

function reset() {
    document.getElementById("CardForm").reset();
    $('div.dz-success').hide();
}

function resetCardForm() {
    document.getElementById('editCardForm').reset();
    document.getElementById('deleteCardForm').reset();
    document.getElementById('cardname').innerHTML = "";
    document.getElementById('imgS').src = "";
}

function validateForm() {
    var id = document.getElementById("imgName").value;
    var name = document.getElementById("CardnameN").value;
    var truth = document.getElementById("truthN").value;
    var grammar = document.getElementById("grammerN").value;
    var regNum = /[[0-9]+(\.[0-9]+)?/g;
    var checkValue = regNum.test(truth);
    var regGrammar = /[a-zA-ZöäåÖÄÅ0-9_][a-zA-ZöäåÖÄÅ_0-9\s]+(\s{0,1}\|{1}\s{0,1}[a-zA-ZöäåÖÄÅ][a-zA-ZöäåÖÄÅ_0-9\s]+)*/g;
    var checkGrammar = regGrammar.test(grammar);
    var regName = /[a-zA-ZöäåÖÄÅ][a-zA-ZöäåÖÄÅ0-9_,;!?\.\s]*/g;
    var checkName = regName.test(id);

    if ((id == null) || (id == "")) {
        alert("Det saknas en bild");
        return false;
    } else if ((name == null) || (name == "")) {
        alert("Det saknas text i detta fält");
        document.getElementById("CardnameN").focus();
        return false;
    } else if ((truth == null) || (truth == "")) {
        alert("Det finns inget nummer i detta fält");
        document.getElementById("truthN").focus();
        return false;
    } else if ((grammar == null) || (grammar == "")) {
        alert("Det saknas text i detta fält");
        document.getElementById("grammerN").focus();
        return false;
    } else if (!checkName) {
        alert("Det du fyllt i stämmer inte med fältets syfte");
        document.getElementById("CardnameN").focus();
        return false;
    } else if (!checkValue) {
        alert("Det du fyllt i stämmer inte med fältets syfte");
        document.getElementById("truthN").focus();
        return false;
    } else if (!checkGrammar) {
        alert("Det du fyllt i stämmer inte med fältets syfte");
        document.getElementById("grammerN").focus();
        return false;
    } else {
        return (true);
    }
}

function validateEditForm() {
    var name = document.getElementById("CardnameS").value;
    var truth = document.getElementById("truthS").value;
    var grammar = document.getElementById("grammarS").value;
    var regNum = /[[0-9]+(\.[0-9]+)?/g;
    var checkValue = regNum.test(truth);
    var regGrammar = /[a-zA-ZöäåÖÄÅ0-9_][a-zA-ZöäåÖÄÅ_0-9\s]+(\s{0,1}\|{1}\s{0,1}[a-zA-ZöäåÖÄÅ][a-zA-ZöäåÖÄÅ_0-9\s]+)*/g;
    var checkGrammar = regGrammar.test(grammar);
    var regName = /[a-zA-ZöäåÖÄÅ][a-zA-ZöäåÖÄÅ0-9_,;!?\.\s]*/g;
    var checkName = regName.test(name);

    if (imgLoadedName == "") {
        document.getElementById('cardImgS').value = fileName;
    } else {
        document.getElementById('cardImgS').value = imgLoadedName;
    }
    var image = document.getElementById('cardImgS').value;
    if ((image == "") || (image == null)) {
        alert("load an image");
        return false;
    } else if ((name == null) || (name == "")) {
        alert("Det saknas text i detta fält");
        document.getElementById("CardnameS").focus();
        return false;
    } else if ((truth == null) || (truth == "")) {
        alert("Det finns inget nummer i detta fält");
        document.getElementById("truthS").focus();
        return false;
    } else if ((grammar == null) || (grammar == "")) {
        alert("Det saknas text i detta fält");
        document.getElementById("grammarS").focus();
        return false;
    } else if (!checkName) {
        alert("Det du fyllt i stämmer inte med fältets syfte");
        document.getElementById("CardnameS").focus();
        return false;
    } else if (!checkValue) {
        alert("Det du fyllt i stämmer inte med fältets syfte");
        document.getElementById("truthS").focus();
        return false;
    } else if (!checkGrammar) {
        alert("Det du fyllt i stämmer inte med fältets syfte");
        document.getElementById("grammarS").focus();
        return false;
    } else {
        return (true);
    }
}

function getDeleteCardId() {
    document.getElementById('taskIdDS').value = currentTask;
    document.getElementById('cardIdDS').value = currentCardId;
}

function deleteCard() {
    getDeleteCardId();
    $("#deleteCardForm").submit();
    $('#cardS').hide();
    resetCardForm();
    removeDeletedCard();
    currentCardId = "";
}

function removeDeletedCard() {
    var cartImg = "cart" + currentCardId;
    var cardImg = document.getElementById(cartImg);
    cardImg.parentNode.removeChild(cardImg);
}

function appendNewImage() {
    if (checkNewImg) {
        var data = new Object();
        $.get("getCard", {
            taskIdS: currentTask,
            cardIdS: cardId
        }, function(result) {
            try {
                data = JSON.parse(result.replace(/&quot;/g, '"'));
            } catch (err) {}
            var imgn = data.image;
            var parent = document.getElementById("TaskCards" + currentTask);
            var parentdiv = document.createElement("div");
            parentdiv.setAttribute("class", "img_cart");
            var divid = "cart" + cardId;
            parentdiv.setAttribute("id", divid);
            var newimg = document.createElement("img");
            var path = "file?name=";
            var imgSrc = path + imgn;
            var name = data.name;
            newimg.setAttribute("src", imgSrc);
            newimg.setAttribute("id", cardId);
            newimg.setAttribute("class", "cardImage");
            newimg.setAttribute("width", "140px");
            newimg.setAttribute("height", "140px");
            newimg.onclick = function() {
                var id = $(this).attr('id');
                currentCardId = id;
                $('.cards').hide();
                fillCardS();
                $('#cardS').show();
                $(".infoSpan").text("");
                $('html,body').animate({
                    scrollTop: $("#cardS").offset().top
                }, 'slow');
            };
            var imgnamediv = document.createElement("div");
            imgnamediv.setAttribute("class", "cart_content");
            var par = document.createElement("p");
            par.setAttribute("id", "pic" + cardId);
            var text = document.createTextNode(name);
            par.appendChild(text);
            imgnamediv.appendChild(par);
            parentdiv.appendChild(newimg);
            parentdiv.appendChild(imgnamediv);
            parent.appendChild(parentdiv);
            checkNewImg = false;
            $('#card').hide();
        });
    } else {

    }
}

function decodeHtml(html) {
    var txt = document.createElement("textarea");
    txt.innerHTML = html;
    return txt.value;
}

function fillCardS() {
    document.getElementById('taskIdS').value = currentTask;
    document.getElementById('cardIdS').value = currentCardId;
    document.getElementById('taskIdDS').value = currentTask;
    document.getElementById('cardIdDS').value = currentCardId;
    var data = new Object();
    $.get("getCard", {
        taskIdS: currentTask,
        cardIdS: currentCardId
    }, function(result) {
        try {
            data = JSON.parse(result.replace(/&quot;/g, '"'));
        } catch (err) {}
        if ("name" in data) {
            document.getElementById('CardnameS').value = decodeHtml(data.name);
            document.getElementById('cardname').innerHTML = data.name;
        }
        if ("truth" in data) {
            document.getElementById('truthS').value = decodeHtml(data.truth);
        }
        if ("definite" in data) {
            document.getElementById('definiteS').value = decodeHtml(data.definite);
        }
        if ("plural" in data) {
            document.getElementById('pluralS').value = decodeHtml(data.plural);
        }
        if ("grammar" in data) {
            document.getElementById('grammarS').value = decodeHtml(data.grammar);
        }
        if ("comment" in data) {
            document.getElementById('commentS').value = decodeHtml(data.comment);
        }
        if ("ask" in data) {
            document.getElementById('askS').value = decodeHtml(data.ask);
        }
        if ("answer" in data) {
            document.getElementById('answerS').value = decodeHtml(data.answer);
        }
        if ("image" in data) {
            document.getElementById('imgS').src = "file?name=" + data.image;
            imgLoadedName = data.image;
        }
    });
}

function cancelSavingCard() {
    document.getElementById("CardForm").reset();
    $("#card").hide();
    imgLoadedName = " ";
    cardId = "";
    $.post("deleteCardImage", {
        image: fileName
    }, function(data, status) {});
    Dropzone.forElement("#my-awesome-dropzone").removeAllFiles(true);
    Dropzone.forElement(".dz-error-message").hide();
}

function hiddenLoaded() {
    var data = new Object();
    var currentTaskinfo = "info" + currentTask;
    try {
        data = JSON.parse($("#hidden").contents().text());
    } catch (err) {}
    if ("status" in data) {
        if (data.status == "error") {
            $("#" + currentTaskinfo).text("Error " + data.message);
            $("#" + currentTaskinfo).css("color", "red");
            $("#" + infoCard).text("Error " + data.message);
            $("#" + infoCard).css("color", "red");
        } else {
            $("#" + currentTaskinfo).text(data.status);
            $("#" + currentTaskinfo).css("color", "green");
            $("#infoCard").text(data.status);
            $("#infoCard").css("color", "green");
        }
    }
}

function updteImage() {
    if (changedImage) {
        $('#' + currentCardId).attr("src", "");
        $('#' + currentCardId).hide();
        var data = new Object();
        $.get("getCard", {
            taskIdS: currentTask,
            cardIdS: currentCardId
        }, function(result) {
            try {
                data = JSON.parse(result.replace(/&quot;/g, '"'));
            } catch (err) {}
            var image = data.image;
            var name = data.name;
            var newimage = "file?name=" + image;
            $('#' + currentCardId).show();
            $('#' + currentCardId).attr("src", newimage);
            document.getElementById('pic' + currentCardId).innerHTML = data.name;
            changedImage = false;
            $('#card-image').show();
            document.getElementById('imgS').src = "file?name=" + data.image;
            document.getElementById('cardname').innerHTML = data.name;
        });
    } else if (cardNameChanged) {
        var data = new Object();
        $.get("getCard", {
            taskIdS: currentTask,
            cardIdS: currentCardId
        }, function(result) {
            try {
                data = JSON.parse(result.replace(/&quot;/g, '"'));
            } catch (err) {}
            var name = data.name;
            document.getElementById('pic' + currentCardId).innerHTML = data.name;
            cardNameChanged = false;
        });
    } else {}
}

function deleteImage() {
    $.post("deleteCardImage", {
        image: imgLoadedName
    }, function(data, status) {
        imgLoadedName = "";
    });
    $("#card-image").hide();
    imgLoadedName = "";
    changedImage = true;
}

$(document).ready(function() {
    $("#saveCardBtn").click(function() {
        getId();
        checkNewImg = true;
        var val = validateForm();
        if (val) {
            $("#CardForm").submit();
        }
    });

    $("#CardnameS").on('input', function() {
        cardNameChanged = true;
    });

    $(".cardImage").click(function() {
        $('div.dz-success').remove();
        $('#card').hide();
        var id = $(this).attr('id');
        currentCardId = id;
        fillCardS();
        $("#card-image").show();
        $('#cardS').show();
        $('html,body').animate({
            scrollTop: $("#cardS").offset().top
        }, 'slow');
        $(".infoSpan").text("");
        fileName = "";
    });

    $("#CardEdit").click(function() {
        var formValid = validateEditForm();
        if (formValid) {
            $('#editCardForm').submit();
        }
    });

    $('#hidden').load(function() {
        Dropzone.forElement("#my-awesome-dropzone").removeAllFiles(true);
        updteImage();
        appendNewImage();
    });

});