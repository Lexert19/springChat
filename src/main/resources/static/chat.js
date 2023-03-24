var ws = null;
var url = "ws://localhost:8080/chat";

var token;

function loginAndStart(name){
fetch("http://localhost:8080/auth/register?name="+name+"&email="+name+"&password=1234")
.then(() =>{
    fetch("http://localhost:8080/auth/login?name="+name+"&password=1234")
    .then(response=>(
         response.text()
    ))
    .then(text =>{
        console.log(text);
        token = text;

        connect()
    });
});


}
var blob;
var count = 0;
function connect()
{
  ws = new WebSocket(url, [token]);
  ws.onopen = function() {
    log('Info: Connection Established.');
    echo("l0000000001"+token)
    echo("c0000000001");
    //echo("a00000000012")
    echo("j0000000001");
    for(let i=0; i<1; i++){
    echo("s0000000001Siema 123")
    }

    echo("p0000000001")
    //echo("a00000000012")

    echo("a00000000012")
    echo("d0000000001")
    record();
  };

  ws.onmessage = function(event) {
    count++;

    if(typeof event.data === "object"){
        event.data.arrayBuffer().then(e=>{
            //console.log((new TextDecoder().decode(new Uint8Array(e))));
            //console.log(new Uint8Array(e));
        });
        //blob = event.data.slice(0, event.data.size, "audio/ogg; codecs=opus")
        const audioUrl = URL.createObjectURL(event.data);
              // Create an Audio element and set its source to the URL
         const audioElement = new Audio(audioUrl);
              // Play the audio element
         audioElement.play();
    }else{
        console.log(event.data);
    }


//    if(count > 95000){
//        console.log(event.data)
//    }
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
    ws.send(message);
  } else {
    alert('connection not established, please connect.');
  }
}

function log(message)
{
  console.log(message);
}


const audioContext = new AudioContext();

// Get access to the user's microphone
var mediaRecorder;
var chunks = [];
var audioBlob;


navigator.mediaDevices.getUserMedia({
audio: true,
noiseSuppression: false
})
  .then(stream => {
    // Create a MediaRecorder object
    mediaRecorder = new MediaRecorder(stream);

    mediaRecorder.addEventListener('dataavailable', event => {
        chunks = [];
        chunks.push(event.data);
    });

    // Listen for the stop event, which occurs when the user stops recording
    mediaRecorder.addEventListener('stop', () => {
        chunks[0].arrayBuffer().then(e=>{
            const dataBlob = new Blob(["v0000000001",chunks[0]],{type: 'audio/ogg; codecs=opus'} );
            echo(dataBlob);
        });
    });
  })
  .catch(error => {
    console.error(error);
  });


function record(){
    mediaRecorder.start()
     setTimeout(() => {
        mediaRecorder.stop();
        record();
     },500);
}