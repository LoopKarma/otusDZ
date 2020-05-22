let stompClient = null;

const isConnected = (connected) => {
    if (connected) {
        requestAllUsers();
    }
};

const connect = () => {
    stompClient = Stomp.over(new SockJS('/websocket'));
    stompClient.connect({}, frame => {
        isConnected(true);
        stompClient.subscribe('/topic/users', users => {
            let message = JSON.parse(users.body);
            let data = JSON.parse(message.payload);
            if (!Array.isArray(data)) {
                data = [data];
            }
            data.map(function(n) {
                printUser(n);
            })

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
    $.get("/user");
};

$(() => {
    connect();
    $("form").on('submit', event => event.preventDefault());
    $("#createUser").on('submit', event => createUser(event));
});
