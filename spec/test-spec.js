var restGet = require('../server/get'),
    assert = require('assert');

//var rewire = require("rewire"),
//    gameDao = rewire("gameDao");
//
//gameDao.__set__("createGame", 'stupid string');

var called, response;
var res = {
    send : function (msg) {
        response = msg;
        called= true;
    }
};

var mongo;

restGet.people({params:{id:"me"}}, res);
assert.equal(response, "me!");
assert.equal(called, true);



//
//when userDao.(1).then return "Aram"
//when gameDao.createGame("Aram","bet"),then retrun {createBy:"Aram",gameName:"bet"}

//restGet.createGame({params:{id:1,gameName:"bet"}}, res);
//assert.deepEqual(response, {createBy:"Aram",gameName:"bet"});
