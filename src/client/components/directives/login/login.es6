app.directive('login', ($sce, $timeout) => {
    return {
        templateUrl: 'login.html',
        scope: {
        },
        link(scope, element, attrs) {
            console.log('login')

            var init = () => {

            };

            init();
        }
    }
});
