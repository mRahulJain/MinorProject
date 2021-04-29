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
var url = "mongodb+srv://cluster0.ahwt8.mongodb.net/myFirstDatabase";//"mongodb+srv://cluster0.ahwt8.mongodb.net/myFirstDatabase";

mongoClient.connect(url, {useNewUrlParser: true}, function(err,client) {
  if(err) {
    console.log("Unable to connect to the mongodb server. Error: ", err);
  } else {

    //--------------------------------------------------------------------------------------------------------//
    //POST PRODUCT
    app.post('/add-product', (request, response)=>{
      var postData = request.body;

      var productBarcode = `${postData.productBarcode}`;
      var productName = `${postData.productName}`;

      var insertJson = {
        productBarcode: productBarcode,
        productName: productName
      };

      var db = client.db('barcodes');

      db.collection('barcode').find({'productBarcode': productBarcode}).count(function(err,number){
        if(number!=0) {
          db.collection('barcode').updateOne(
            {
              'productBarcode': productBarcode
            },
            {
              $set: {
                'productName': productName
              }
            },
            function(err,res) {
              response.json('Product Updated');
              console.log('Product Updated');
            }
          )
        } else {
          db.collection('barcode').insertOne(insertJson, function(err,res){
            response.json('Product added');
            console.log('Product added');
          })
        }
      })
    });
    //--------------------------------------------------------------------------------------------------------//


    //--------------------------------------------------------------------------------------------------------//
    //GET PRODUCT
    app.get('/get-product', (request, response)=>{
      var queryData = request.query;
      var productBarcode = `${queryData.productBarcode}`;

      var db = client.db('barcodes');
      db.collection('barcode').find({'productBarcode': productBarcode}).count(function(err,number){
        if(number==0) {
          response.json({'productBarcode': "-1"});
          console.log('No such product exists');
        } else {
       console.log(number);
          db.collection('barcode').findOne({'productBarcode': productBarcode}, function(err,res){

            response.json(res);
            console.log(err+", "+res);
            console.log('Success in getting product');
          })
        }
      })
    });
    //--------------------------------------------------------------------------------------------------------//


    //START WEB SERVER
    var port =  process.env.PORT || 4090;
    Promise.resolve(app.listen(port)).then(()=>{
      console.log("Connected to mongodb server, web server is running on port 4090");
    });
  }
});
