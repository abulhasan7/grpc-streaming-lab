/*
 *
 * Copyright 2015 gRPC authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

let PROTO_PATH = __dirname + "/./file.proto";

let parseArgs = require("minimist");
let grpc = require("@grpc/grpc-js");
let protoLoader = require("@grpc/proto-loader");
let fs = require("fs");

let packageDefinition = protoLoader.loadSync(PROTO_PATH, {
  keepCase: true,
  longs: String,
  enums: String,
  defaults: true,
  oneofs: true,
});

let hello_proto = grpc.loadPackageDefinition(packageDefinition).helloworld;

function callServer(target) {
  // client.sayHello({name: user}, function(err, response) {
  //   console.log('Greeting:', response.message);
  // });

  // client.sayHelloAgain({name: user}, function(err, response) {
  //   console.log('Greeting:', response.message);
  // });

  // let file = fs.readFileSync("C:\\Users\\AbulHasan\\Pictures\\Q.png");
  // let file = fs.readFileSync("C:\\Users\\AbulHasan\\Documents\\Bandicam\\bandicam 2022-03-10 13-51-01-064.mp4");
  // let file = fs.readFileSync("C:\\Users\\AbulHasan\\Documents\\Softwares\\Windows-Drivers\\Windows10-Drivers\\WLAN.exe");
  // let file = fs.readFileSync("C:\\Users\\AbulHasan\\Documents\\Softwares\\Windows-Drivers\\Windows10-Drivers\\MSStoreSmartDisplays.msixbundle")
  // let file = fs.readFileSync("C:\\Users\\AbulHasan\\Documents\\Development\\repos\\backend\\grpc\\examples\\node\\package-lock.json");
  // let file = fs.readFileSync("C:\\Users\\AbulHasan\\Videos\\MOVIES\\Hollywood\\10,000 B.C. (2008) [1080p]\\10,000.B.C.2008.1080p.BrRip.x264.YIFY.mp4");
  // let file = fs.readFileSync("C:\\Users\\AbulHasan\\Videos\\MOVIES\\Hollywood\\2 Guns (2013)\\2.Guns.2013.720p.BluRay.x264.YIFY.mp4");
  // let file = fs.readFileSync("C:\\Users\\AbulHasan\\Videos\\MOVIES\\Hollywood\\Gone.Girl.720p.BluRay.x264.[Alldownloads4u.Com].mp4");
  // let file = fs.readFileSync("C:\\Users\\AbulHasan\\Videos\\Captures\\Navbar.js - etsy-clone - Visual Studio Code 2022-03-04 19-56-00.mp4");
  // let file = fs.readFileSync("C:\\Users\\AbulHasan\\Videos\\MOVIES\\Hollywood\\Orphan.mp4");

  let filename =
    "C:\\Users\\AbulHasan\\Downloads\\archive\\crime-data-from-2010-to-present.csv";
  let filenamearr = filename.split("\\");
  let filename1 = filenamearr[filenamearr.length - 1];
  // console.log(filename1)
  let file = fs.readFileSync(filename);
  // console.log(file.length,file.byteLength,file.BYTES_PER_ELEMENT);
  let byteSize = 50000;
  let byteStart = 0;
  let count = 0;
  // let client = new hello_proto.Greeter(
  //   target,
  //   grpc.credentials.createInsecure()
  // );
  while (byteStart < file.length) {

    // let user;
    // if (argv._.length > 0) {
    //   user = argv._[0];
    // } else {
    //   user = "world";
    // }

    count += 1;

    console.log(count, byteStart, byteStart + byteSize);
    let tempbuffer = file.slice(
      byteStart,
      byteStart + byteSize <= file.length ? byteStart + byteSize : file.length
    );
    // console.log(tempbuffer.toString());
    byteStart += byteSize;
    // fs.appendFileSync(__dirname+'/../A.png',tempbuffer);
    // console.log("Greeting:", tempbuffer,tempbuffer.toString());
  let client = new hello_proto.Greeter(
    target,
    grpc.credentials.createInsecure()
  );
    client.gotFile({ index: count, payload: tempbuffer }, (err, response) => {
      //   // console.log("Greeting:", response.message);
      client.close();
    });
  }
  let client = new hello_proto.Greeter(
    target,
    grpc.credentials.createInsecure()
  );

  client.gotFile(
    { index: -1, totalsize: count, filename: filename1 },
    (err, response) => {
      // console.log("Greeting:", response.message);
    }
  );
}

function main() {
  let argv = parseArgs(process.argv.slice(2), {
    string: "target",
  });
  let target;
  if (argv.target) {
    target = argv.target;
  } else {
    target = "10.0.0.123:50051";
    // target = "localhost:50052"
  }
  callServer(target);
}

main();
