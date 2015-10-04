
var fsMock = {
    readFile: function (path, encoding, cb) {
        expect(path).to.equal("/somewhere/on/the/disk");
        cb(null, "Success!");
    }
};
myModule.__set__("fs", fsMock);

myModule.readSomethingFromFileSystem(function (err, data) {
    console.log(data); // = Success!
});