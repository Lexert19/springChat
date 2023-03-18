var ws = null;
var url = "ws://localhost:8080/chat";

var token;

fetch("http://localhost:8080/auth/register?name=nwm0&email=nwm0&password=1234")
.then(() =>{
    fetch("http://localhost:8080/auth/login?name=nwm0&password=1234")
    .then(response=>(
         response.text()
    ))
    .then(text =>{
        console.log(text);
        token = text;

        connect()
    });
});




var count = 0;
function connect()
{
  ws = new WebSocket(url, [token]);
  ws.onopen = function() {
    log('Info: Connection Established.');
    echo("c0000000001");
    echo("j0000000001");
  };

  ws.onmessage = function(event) {
    count++;
    if(count > 30000){
        console.log(event.data)
    }
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
