const http = require('http');
const static = require('node-static');
const WebSocket = require('ws');

const wss = new WebSocket.Server({ port: 8080 });

wss.on('connection', ws => {
  ws.on('message', message => {
    console.log(`Received message => ${message}`);
  });
  ws.send('ho!');
});

const file = new static.Server('./');

const server = http.createServer((req, res) => {
  req.addListener('end', () => file.serve(req, res)).resume();
});

const port = 3210;

server.listen(port, () => console.log(`Server running at http://localhost:${port}`));
