app.controller('GroupCtrl', ($element, $timeout, API, $scope, State, $stateParams) => {

    var group = {};

    var getGroup = () => {
        return group;
    };

    var loadGroup = () => {
        API.getGroupById($stateParams._id).then((response) => {
            group = response[0];
            $element.find('[screen]').addClass('active');
        });
    };

    var init = () => {
        console.log($stateParams);
        loadGroup();
    };

    init();

    $scope.getGroup = getGroup;
});



