$(function () {
    var noticeSpan = $("#noticeSpan");
    $.ajax({
        type: "POST",
        url: "/findCurrentUser" ,
        contentType: "application/json",
        dataType: "json",
        success: function (result) {
            if(result.code == 200){
               webSocketConnect(result);
            }

        }
    });

    noticeSpan.text( $.session.get("noticeCount"));

});

function webSocketConnect(result){
    var noticeSpan = $("#noticeSpan");
    var stompClient = null;
    var socket = new SockJS("/noticeServer");
    stompClient = Stomp.over(socket);
    stompClient.connect({},function (data) {
        stompClient.subscribe("/user/"+result.data.accountId+"/message",function (response) {
            var noticeCount = $.session.get("noticeCount");
            if(!isNaN(noticeCount)){
                noticeCount = parseInt(noticeCount)+parseInt(response.body);
                $.session.set("noticeCount",noticeCount);
            }else{
                noticeCount = parseInt(response.body);
                $.session.set("noticeCount",parseInt(response.body));
            }
            noticeSpan.text(noticeCount);

        })
    });
}

function toNotice() {
    $.session.clear("noticeCount");
    window.location.href = "/profile/notice";
}