app.controller('GroupCtrl', ($element, $timeout, API, $scope, State, $stateParams) => {

    var group = {};

    var getGroup = () => {
        return group;
    };

    var loadGroup = () => {
        API.getGroupById($stateParams._id).then((response) => {
            group = response[0];
            $timeout(() => $element.find('[screen]').addClass('active'), 50);
        });
    };

    var init = () => {
        console.log($stateParams);
        loadGroup();
    };

    init();

    $scope.getGroup = getGroup;
});



