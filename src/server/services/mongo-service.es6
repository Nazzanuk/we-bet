'use strict';

var shortid = require('shortid'),
    assert = require('assert'),
    MongoClient = require('mongodb').MongoClient;

//console.log(shortid.generate());
////PPBqWA9

var db, url = 'mongodb://we-bet:we-bet@ds031407.mongolab.com:31407/we-bet';

MongoClient.connect(url, (err, database) => {
    db = database;
    assert.equal(null, err);
    console.log("Connected correctly to server");
});

module.exports = {
    find(collection, data, fields, callback) {
        var collection = db.collection(collection);

        collection.find(data, fields).toArray((err, docs) => {
            assert.equal(err, null);
            callback(docs);
        });
    }
};