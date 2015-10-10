app.controller('ScreenCtrl', ($element, $timeout, State, $state) => {

    var init = () => {
        $timeout(() => $element.find('[screen]').addClass('active'), 50);
    };

    init();
});
