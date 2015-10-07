var gulp = require('gulp'),
    rename = require('gulp-rename'),
    del = require('del'),
    mocha = require('gulp-mocha'),
    //traceur = require('gulp-traceur'),
    babel = require("gulp-babel"),
    sourcemaps = require("gulp-sourcemaps");

gulp.task("default", function () {
    gulp.start([
        'test'
    ]);
});

gulp.task('auto-test', ['default'], function () {
    gulp.watch([
        'test/**/*',
        'server-es6/**/*'
    ], ['default']);
});

gulp.task('es6-server', function () {
    return gulp.src('server-es6/**/*.es6')
        .pipe(babel())
        .pipe(rename({extname: ".js"}))
        .pipe(gulp.dest("server"));
});

gulp.task('es6-test', ['es6-server'], function () {
    return gulp.src('test/**/*.es6')
        .pipe(babel())
        .pipe(rename({extname: ".js"}))
        .pipe(sourcemaps.write("."))
        .pipe(gulp.dest("gen"));
});

gulp.task('test', ['es6-test'], function () {
    return gulp.src('gen/**/*.js', {read: false})
        .pipe(mocha());
}, ['clean']);

//gulp.task('clean', ['test'], function () {
//    return del('gen');
//});