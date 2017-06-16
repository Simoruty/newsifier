var socket;

function openWebSocket() {
    var windowLocation = (window.location.href).replace("https", "ws").replace("http", "ws") + "websocket";
    socket = new WebSocket(windowLocation);

    socket.addEventListener('open', function () {
        console.log("Connection established, handle with event");
    });

    socket.onopen = function () {
        console.log("Connection is open");
    };

    socket.onmessage = function (event) {
//		console.log("[SRV]: " + event.data);
        document.getElementById("feedsP").value = document.getElementById("feedsP").value + event.data + "\n";
        document.getElementById("feedsP").scrollTop = document.getElementById("feedsP").scrollHeight
    };
}

function sendMessage(message) {
    socket.send(message);
}

function closeWebSocket(){
	socket.close();
}

function startExecution() {
    var newsLimitParam = document.getElementById("newslimitP").value;
    var kwLimitParam = document.getElementById("kwlimitP").value;
    var catthresholdParam = document.getElementById("catthresholdP").value;
    var kwthresholdParam = document.getElementById("kwthresholdP").value;
    var trainingtestpercentageParam = document.getElementById("trainingtestpercentageP").value;
    var feedsParam = document.getElementById("feedsP").value;

    $.post("feed", {
        newslimit: newsLimitParam,
        kwlimit: kwLimitParam,
        catthreshold: catthresholdParam,
        kwthreshold: kwthresholdParam,
        trainingtestpercentage: trainingtestpercentageParam,
        feeds: feedsParam
    });
}

function reset() {
    var answer = confirm("Are you sure to erase all text?")
    if (answer) {
        document.getElementById("newslimitP").value = "";
        document.getElementById("kwlimitP").value = "";
        document.getElementById("catthresholdP").value = "";
        document.getElementById("kwthresholdP").value = "";
        document.getElementById("trainingtestpercentageP").value = "";
        document.getElementById("feedsP").value = "";
    }
}

function eraseAll() {
    var answer = confirm("Are you sure to erase all data?")
    if (answer) {
        $.get("feed");
    }

}