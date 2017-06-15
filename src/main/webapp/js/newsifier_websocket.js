var socket;

function openWebSocket() {
	socket = new WebSocket('ws://localhost:9080/newsifier/websocket');

	socket.addEventListener('open', function() {
		console.log("Connection established, handle with event");
	});
	
	socket.onopen = function() {
		console.log("Connection is open");
	};
	
	socket.onmessage = function(event) {
		console.log("[SRV]: " + event.data);
	};
}

function sendMessage(message){
	socket.send(message);
}

function closeWebSocket(){
	socket.close();;
}

function startExecution(){
	var newsLimitParam = document.getElementById("newslimitP").value;
	var kwLimitParam = document.getElementById("kwlimitP").value;
	var catthresholdParam = document.getElementById("catthresholdP").value;
	var kwthresholdParam = document.getElementById("kwthresholdP").value;
	var trainingtestpercentageParam = document.getElementById("trainingtestpercentageP").value;
	var feedsParam = document.getElementById("feedsP").value;
	
	$.post( "feed", { newslimit: newsLimitParam, kwlimit: kwLimitParam, catthreshold: catthresholdParam, kwthreshold: kwthresholdParam, trainingtestpercentage: trainingtestpercentageParam, feeds: feedsParam } );
}