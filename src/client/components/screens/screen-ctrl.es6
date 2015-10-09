app.controller('ScreenCtrl', ($element, $timeout) => {

    var init = () => {
        $timeout(() => $element.find('[screen]').addClass('active'), 50);
    };

    init();
});
