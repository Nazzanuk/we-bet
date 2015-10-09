//set up test syntax
var assert = require("assert"),
    chai = require('chai'),
    expect = chai.expect,
    should = chai.should(),
    request = require('supertest');

//tests
describe('loading express', () => {
    var server;

    beforeEach(() => {
        process.env['PORT'] = 3001; //we test express at port 3001 so we can run it locally at the same time
        server = require(process.cwd() + '/release/server/server')
    });
    afterEach(() => {
        delete process.env['PORT'];
        server.close()
    });

    it('responds to /api/', done => request(server).get('/api/').expect(200, done));
    it('404 everything else (/foo/bar)', done => request(server).get('/foo/bar').expect(404, done));
    it('404 everything else (/)', done => request(server).get('/').expect(404, done));
});