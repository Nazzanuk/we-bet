var app = angular.module('app', ['ui.router']).
    run(function() {
        FastClick.attach(document.body);
    });;

app.directive('ngEnter', () => {
    return (scope, element, attrs) => {
        element.bind('keydown keypress', (event) => {
            if (event.which !== 13) return;
                scope.$apply(() => scope.$eval(attrs.ngEnter, {$event: event}));
                event.preventDefault();
        });
    };
});