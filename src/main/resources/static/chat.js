var ws = null;
var url = "ws://localhost:8080/";

function connect()
{
  ws = new WebSocket(url);
  ws.onopen = function() {
    log('Info: Connection Established.');
  };

  ws.onmessage = function(event) {
    log(event.data);
  };

  ws.onclose = function(event) {
    log('Info: Closing Connection.');
  };
}

function disconnect()
{
  if (ws != null) {
    ws.close();
    ws = null;
  }
}

function echo(message)
{
  if (ws != null)
  {
    log('Sent to server :: ' + message);
    ws.send(message);
  } else {
    alert('connection not established, please connect.');
  }
}

function log(message)
{
  console.log(message);
}
connect()