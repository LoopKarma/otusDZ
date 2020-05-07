let stompClient = null;

const connect = () => {
    stompClient = Stomp.over(new SockJS('/websocket'));
    stompClient.connect({}, frame => {
        // setConnected(true);
        stompClient.subscribe('/topic/users/response', users => {
            let message = JSON.parse(users.body);
            printUser(JSON.parse(message.payload));
        });
    });
};

const createUser = (event) => {
    event.preventDefault();
    let requestData = {};

    $(event.target).serializeArray().map(function (n, i) {
        requestData[n['name']] = n['value'];
    });
    $.post("/user", requestData);
    //clean up the form
    $(event.target).find("input").val("");
};

const printUser = user => {
    $("#users").append(`<tr><td>${user.id}</td><td>${user.login}</td><td>${user.name}</td><td>${user.password}</td></tr>`);
};

const requestAllUsers = () => {
    $.get("/user", function (users) {
        users.map(function (n, i) {
            console.log(n);
            printUser(n);
        });
    });
};

$(() => {
    connect();
    requestAllUsers();
    $("form").on('submit', event => event.preventDefault());
    $("#createUser").on('submit', event => createUser(event));
});
