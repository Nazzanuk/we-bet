var userDao = require("./userDao");
var gameDao = require("./gameDao");

module.exports = {

    people: function (req, res) {
        console.log(req.params);
        var id = req.params.id + "!";
        res.send(id);
    },

    createGame: function (req, res) {
        //var id = req.params.id;
        //var user = userDao.getUserById(id);
        //var gameName = req.params.gameName;
        //var game = mongo.saveGame();

        //res.send(game);
    }
};

