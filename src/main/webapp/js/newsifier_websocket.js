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