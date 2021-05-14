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
     app.post('/book-cab', (request, response)=>{
      var bodyData = request.body;
      var sourceLatitude = `${bodyData.lat}`;
       var sourceLongitude = `${bodyData.long}`;
       var destination = bodyData.destination;
       console.log(sourceLongitude+", "+sourceLatitude+" = "+destination);
       var db = client.db('rides');
       db.collection('cabs').find({loc: {$geoWithin:{$centerSphere:[[parseFloat(sourceLongitude),parseFloat(sourceLatitude)], 5/6378.1]}}, 'isBooked': false}).toArray(function(err,res1){
        if(!err){
          console.log(res1);
          var data = res1[0];
          console.log(data);
          var id = `${data._id}`;
          console.log(id);
          var body = {
        "user":{
          "lat": parseFloat(sourceLatitude),
          "long": parseFloat(sourceLongitude)
        },
        "destination": destination,
        "driverId": id,
        "status": "Your Ride is Arriving",
        };
        
        db.collection('cabs').updateOne(
          {
            '_id': mongodb.ObjectId(id)
          },
          {
            $set:{
              'isBooked':  true
            }
          },
          function(err,res2) {
            if(!err){
              db.collection('bookings').find({'driverId': id}).count(function(err,number){
                if(number!=0) {
                  response.json({"msg":"Booking Already exists!"});
                  console.log("Unidentified Record");
                } else {
                  db.collection('bookings').insertOne(body, function(err,res3){
                    response.json(res3);
                    console.log(JSON.stringify(res3));
                    console.log("Booking Added!");
                  })
                }
              });
            }else{
              response.json({"msg":"No Cab Found !"});
              console.log(err);
            }
          })
        }else{
          console.log(err);
        }
      })  
    });
    
    //DELETE BOOKING
    app.delete('/delete-booking',(request, response)=>{
      var q = request.query;
      var id = q.driverId;
      var db = client.db('rides');
      db.collection('bookings').find({'driverId': id}).count(function(err,number){
        if(number!=0) {
          db.collection('cabs').updateOne(
            {
              '_id': mongodb.ObjectId(id)
            },
            {
              $set:{
                'isBooked':  false
              }
            },
            function(err,res2) {
          db.collection('bookings').deleteOne({'driverId':id}, function(err, res){
            if(err)
            response.json({"codse":res.statusCode,"msg":"Not Deleted"});
            else
            response.json({"code": res.statusCode,"msg":"Booking Deleted"});
            console.log("Booking deleted" + res);
          });
        })
        } else {
          response.json({"msg":"No data found"})
          console.log("Unidentified Record");
        }
        });
    });
    
    //UPDATE BOOKING
    app.patch('/update-status',(request, response)=>{
      let data = request.query;
      var id = `${data.driverId}`;
      var status = `${data.status}`;
       var db = client.db('rides');
      console.log(id)
      db.collection('bookings').find({'driverId': id}).count(function(err,number){
      if(number!=0) {
        db.collection('bookings').updateOne(
          {
            'driverId': id
          },
          {
            $set: {
              'status': status
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
    app.get('/get-status', (request, response)=>{
      var query = request.query;
      var id = query.driverId;
      var db = client.db('rides');
          db.collection('bookings').findOne({'driverId':id}, function(err,res){
            response.json(res);
            console.log(JSON.stringify(res));
            console.log('Success in fetching status');
          })
    });

    // *************************** CABS DB **************************************************************
    //GET CAB DETAILS
    app.get('/get-cabs', (request, response)=>{
      var query = request.query;
      var id = query._id;
      var db = client.db('rides');
          db.collection('cabs').findOne({'_id': mongodb.ObjectId(id)}, function(err,res){
            if(!err){
              response.json(res);
            console.log(JSON.stringify(res));
            console.log('Success in fetching cabs');
            }
          })
      });
    
    //START WEB SERVER
    var port = 2332;
    Promise.resolve(app.listen(port)).then(()=>{
      console.log("Connected to mongodb server, web server is running on port 2332");
    });
  }
});
