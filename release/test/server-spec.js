//set up test syntax
'use strict';

var assert = require("assert"),
    chai = require('chai'),
    expect = chai.expect,
    should = chai.should(),
    request = require('supertest');

//tests
describe('loading express', function () {
    var server;

    beforeEach(function () {
        process.env['PORT'] = 3001; //we test express at port 3001 so we can run it locally at the same time
        server = require(process.cwd() + '/release/server/server');
    });
    afterEach(function () {
        delete process.env['PORT'];
        server.close();
    });

    it('/api/', function (done) {
        return request(server).get('/api/').expect(200, function (something, response) {
            response.res.text.should.equal('Hello');
            done();
        });
    });

    it('/api/login?username=nathan&password=nathan', function (done) {
        request(server).get('/api/login/?username=nathan&password=nathan').expect(200, function (something, response) {
            response.res.text.should.equal("true");
            done();
        });
    });

    it('/foo/bar (404)', function (done) {
        return request(server).get('/foo/bar').expect(404, done);
    });
    it('/ (404)', function (done) {
        return request(server).get('/').expect(404, done);
    });
});