app.controller('ScreenCtrl', ($element, $timeout, State, $state) => {

    var init = () => {
        $timeout(() => $element.find('[screen]').addClass('active'), 50);
        if (!State.isLoggedIn()) $state.go('splash');
        else $state.go('home');
    };

    init();
});
