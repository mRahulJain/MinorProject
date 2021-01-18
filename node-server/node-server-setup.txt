
NodeJS (download link)
	-> https://nodejs.org/en/download/
	-> set up environment variable.... (System variables) . Add <Path to Node JS. eg: "C:\Program Files\nodejs\">

Mongo DB (download link)
	-> https://www.mongodb.com/try/download/community
	-> set up environment variable.... (User variables) . Add <Path to Node JS. eg: "C:\Program Files\MongoDB\Server\4.2\bin">
	
Product Recognition  Feature (Backend setup)
	(command prompt as admin)
	-> net start MongoDb
	-> mongod --port 4090
	(command prompt)
	-> mongo --port 4090
	Run your server.js file.	

		Misc
	-> show dbs			
	-> use barcodes			//barcodes is the database name. barcode is the collection name	
	-> db.barcode.find().pretty()	// lists all the entries in database

	to import the entries....
	-> open a new command prompt
	-> cd <path where you want to your results.json file>
	-> mongoimport  --host 127.0.0.1:4090 --port 4090 --db barcodes --collection barcode --file results.json --jsonArray
	
	// for storing your entries in database
	-> mongoexport -h 127.0.0.1 --port 4090 -d barcodes -c barcode -o results.json --jsonArray


