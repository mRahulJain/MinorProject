var mongodb = require('mongodb');
var objectId = mongodb.ObjectID;
var express = require('express');
var bodyParser = require('body-parser');

//CREATE EXPRESS SERVICE
var app = express();
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));

//CREATE MONGODB CLIENT
var mongoClient = mongodb.MongoClient;

//CONNECTION URL
var url = "mongodb+srv://admin:admin123@cluster0.ahwt8.mongodb.net/rides";

mongoClient.connect(url, {useNewUrlParser: true}, function(err,client) {
  if(err) {
    console.log("Unable to connect to the mongodb server. Error: ", err);
  } else {
    //  ***************************** BOOKED CABS *******************************************************
    //--------------------------------------------------------------------------------------------------------//
     //POST BOOKING 
     app.post('/add-booking', (request, response)=>{
      var data = request.body;
      var userLat = `${data.user.lat}`;
      var userLong = `${data.user.long}`;
      var name = `${data.driverName}`;
      var driverLat = `${data.driver.lat}`;
      var driverLong = `${data.driver.long}`;
      var destination = `${data.destination}`;
      var driverId = `${data.driverId}`;
      var status = `${data.status}`;
      var phone = `${data.driverContact}`;
      var vehicle = data.vehicle;
      var type = data.vehicleType;
      var num = data.vehicleNumber;
      var body = {
        "user":{
          "lat": parseFloat(userLat),
          "long": parseFloat(userLong)
        },
        "driver":{
          "lat": parseFloat(driverLat),
          "long": parseFloat(driverLong)
        },
        "destination": destination,
        "driverId": driverId,
        "driverName": name,
        "status": status,
        "isBooked": "Arriving",
        "driverContact": phone,
        "vehicle": vehicle,
        "vehicleType": type,
        "vehicleNumber": num
      };

      var db = client.db('rides');
      db.collection('bookings').find({'driverId': id}).count(function(err,number){
        if(number!=0) {
          response.json({"msg":"Booking Already exists!"})
          console.log("Unidentified Record");
        } else {
          db.collection('bookings').insertOne(body, function(err,res){
            response.json(res);
            console.log("Booking Added!");
          })
          
        }
        });
    });
    
    //DELETE BOOKING
    app.delete('/delete-booking',(request, response)=>{
      var q = request.query;
      var id = q.driverId;
      var db = client.db('rides');
      db.collection('bookings').find({'driverId': id}).count(function(err,number){
        if(number!=0) {
          db.collection('bookings').deleteOne({'driverId':id}, function(err, res){
            if(err)
            response.json({"codse":res.statusCode,"msg":"Not Deleted"});
            else
            response.json({"code": res.statusCode,"msg":"Booking Deleted"});
            console.log("Booking deleted" + res);
          });
        } else {
          response.json({"msg":"No data found"})
          console.log("Unidentified Record");
        }
        });
    });
    
    //UPDATE BOOKING
    app.patch('/update-booking',(request, response)=>{
      let data = request.body;
      var id = `${data.driverId}`;
      var status = `${data.status}`;
      var bookigStatus = `${data.isBooked}`;
      var db = client.db('rides');
      
      db.collection('bookings').find({'driverId': id}).count(function(err,number){
      if(number!=0) {
        db.collection('bookings').updateOne(
          {
            'driverId': id
          },
          {
            $set: {
              'status': status,
              'isBooked': bookigStatus
            }
          },
          function(err,res) {
            response.json({"msg":"Booking Updated"});
            console.log('Updated Booking'+res);
          }
        )
      } else {
        response.json({"msg":"No data found"})
        console.log("Unidentified Record");
      }
      });
    });
    //GET BOOKING
    app.get('/get-booking', (request, response)=>{
      var query = request.query;
      var id = query.driverId;
      var db = client.db('rides');
          db.collection('bookings').findOne({'driverId': id}, function(err,res){
            response.json(res);
            console.log(JSON.stringify(res));
            console.log('Success in fetching booking');
          })
    });

    // *************************** CABS DB **************************************************************
    //GET CAB DETAILS
    app.get('/get-cabs', (request, response)=>{
        var queryData = request.query;
       var sourceLatitude = queryData.lat;
        var sourceLongitude = queryData.long;
        var db = client.db('rides');
        db.collection('cabs').find({loc: {$geoWithin:{$centerSphere:[[parseFloat(sourceLongitude),parseFloat(sourceLatitude)], 5/6378.1]}}}).toArray(function(err,res){
           response.json(res);
           console.log(JSON.stringify(res));
            console.log('Success in getting cabs');
          })
      });
     
    //START WEB SERVER
    var port = 2332;
    Promise.resolve(app.listen(port)).then(()=>{
      console.log("Connected to mongodb server, web server is running on port 2332");
    });
  }
});
