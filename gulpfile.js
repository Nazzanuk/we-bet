var gulp = require('gulp'),
    rename = require('gulp-rename'),
    del = require('del'),
    mocha = require('gulp-mocha'),
    concat = require("gulp-concat"),
    babel = require("gulp-babel"),
    order = require("gulp-order"),
    sass = require("gulp-sass"),
    sourcemaps = require("gulp-sourcemaps");

gulp.task("default", function () {
    gulp.start([
        'test',
        'gen-client-html',
        'gen-client-js',
        'gen-client-css',
        'gen-client-lib-js',
        'gen-client-lib-css'
    ]);
});

gulp.task('dev', ['default'], function () {
    gulp.watch([
        'src/**/*'
    ], ['default']);
});

gulp.task('server-es6', ['test-es6'], function () {
    return gulp.src(['src/server/**/*.es6'])
        .pipe(babel())
        .pipe(rename({extname: ".js"}))
        .pipe(gulp.dest("release/server"));
});

gulp.task('test-es6', function () {
    return gulp.src(['src/test/**/*.es6'])
        .pipe(babel())
        .pipe(rename({extname: ".js"}))
        .pipe(gulp.dest("release/test"));
});

gulp.task("gen-client-html", function () {
    return gulp.src([
        "src/client/components/head/head.html",
        "src/client/**/!(footer)*.html",
        "src/client/components/footer/footer.html"
    ])
        .pipe(concat("index.html"))
        .pipe(gulp.dest('release/client'));
});

gulp.task('gen-client-js', function () {
    return gulp.src(['src/client/app.es6', 'src/client/components/**/*.es6'])
        .pipe(concat('app.js'))
        .pipe(babel())
        .pipe(gulp.dest("release/client/public"));
});

gulp.task('gen-client-css', function () {
    return gulp.src(['src/client/app.scss'])
        .pipe(sass())
        .pipe(gulp.dest("release/client/public"));
});

gulp.task('gen-client-lib-js', function () {
    return gulp.src([
        'src/client/bower-components/jquery/dist/jquery.min.js',
        'src/client/bower-components/angular/angular.min.js',
        'src/client/bower-components/angular-ui-router/release/angular-ui-router.min.js',
        'src/client/bower-components/lodash/lodash.min.js'
    ])
        .pipe(concat('lib.js'))
        .pipe(gulp.dest("release/client/public"));
});

gulp.task('gen-client-lib-css', function () {
    return gulp.src([
        'src/client/bower-components/bootstrap/dist/css/bootstrap.min.css'
    ])
        .pipe(concat('lib.css'))
        .pipe(gulp.dest("release/client/public"));
});

gulp.task('test', ['server-es6'], function () {
    return gulp.src('release/test/**/*.js', {read: false})
        .pipe(mocha());
});