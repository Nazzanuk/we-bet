var gulp = require('gulp'),
    rename = require('gulp-rename'),
    del = require('del'),
    mocha = require('gulp-mocha'),
    traceur = require('gulp-traceur');

gulp.task("default", function () {
    gulp.start([
        'test'
    ]);
});

gulp.task('auto-test', ['default'], function () {
    gulp.watch([
        'test/**/*'
    ], ['default']);
});

gulp.task('es6', function () {
    return gulp.src('test/**/*.es6')
        .pipe(traceur())
        .pipe(rename({extname: ".js"}))
        .pipe(gulp.dest("./gen"));
});

gulp.task('test', ['es6'], function () {
    return gulp.src('gen/**/*.js', {read: false})
        .pipe(mocha());
}, ['clean']);

//gulp.task('clean', ['test'], function () {
//    return del('gen');
//});