    //回复评论
    function postComment() {
        var comments = $("#comments");
        var questionId = $("#question_id").val();
        var commentContent = $("#commentContent").val();
        var $commentContent = $("#commentContent");
        if(commentContent ==null||commentContent == ""){
            alert("评论内容不能为空");
            return ;
        }
        //通过ajax的方式向后端发送请求
        $.ajax({
            type : "POST",
            url : "/comment",
            data : JSON.stringify(
                {parentId : questionId ,content : commentContent , type: 0}),
            contentType: "application/json",
            dataType : "json",
            success:function (result) {
                //判断后台返回的数据的状态码是否为200，不是则提交数据出现问题
                if(result.code!=200){
                    alert(result.message);
                }else{
                    //清空回复框中的内容
                    $commentContent.val("");
                    //    将后台返回的数据加载到相应位置
                    //    构建元素
                    var parentDiv = $("<div></div>").addClass("col-lg-12 col-md-12 col-sm-12 col-xs-12 comments");
                    var mediaDiv = $("<div></div>").addClass("media");
                    var mediaLeftDiv = $("<div></div>").addClass("media-left").append($("<a></a>").attr("href","#")
                        .append($("<img>").addClass("media-object img-rounded").attr("src",result.data.user.avatar)));
                    var mediaBody = $("<div></div>").addClass("media-body");
                    var mediaHeading = $("<h5></h5>").addClass("media-heading").append($("<span></span>").append(result.data.user.name));
                    var commentContent = $("<div></div>").append(result.data.content);
                    var menuDiv = $("<div></div>").addClass("menu");
                    var like = $("<span></span>").addClass("glyphicon glyphicon-thumbs-up icon").attr("data-id",result.data.id);
                    like.click(function () {
                        toLike(like);
                    });
                    var likeSpan = $("<span></span>").addClass("comment-icon").append(like).append(" ").append($("<span></span>").attr("id","likeSpan-"+result.data.id).attr("data",result.data.likeCount).append(result.data.likeCount));
                    var comment = $("<span></span>").addClass("glyphicon glyphicon-comment").attr("data-id",result.data.id);
                    comment.click(function () {
                        showComments(comment)
                    });
                    var commentSpan = $("<span></span>").addClass("comment-icon").append(comment).append("&nbsp; ").append($("<span></span>").attr("id","span-"+result.data.id).attr("data",result.data.commentCount).append(result.data.commentCount));
                    var dateSpan = $("<span></span>").addClass("pull-right").append(moment(result.data.gmt_create).format("YYYY-MM-DD"));
                    //构建二级评论
                    var showDiv = $("<div></div>").addClass("col-lg-12 col-md-12 col-sm-12 col-xs-12 sub-comments collapse").attr("id","comment-"+result.data.id).attr("isShow","true");
                    var button = $("<button></button>").addClass("btn btn-success pull-right").attr("type","button").attr("data-id",result.data.id).append("评论");
                    button.click(function () {
                        comment2Comment(button);
                    });
                    var controlDiv = $("<div></div>").addClass("col-lg-12 col-md-12 col-sm-12 col-xs-12").append($("<input>").addClass("form-control").attr("type","text").attr("placeholder","评论一下").attr("id","input-"+result.data.id)).append(button);
                    showDiv.append(controlDiv);
                    mediaDiv.append(mediaLeftDiv)
                        .append(mediaBody.append(mediaHeading).append(commentContent).append(menuDiv.append(likeSpan).append("&nbsp; &nbsp;").append(commentSpan).append(dateSpan)))
                        .append(showDiv);
                    parentDiv.append(mediaDiv);
                    comments.append(parentDiv);
                }
            }
        });
    }

//    查看二级评论
function showComments(e) {
    var comments;
    var commentId;
    //判断传进来的对象子是jquery对象还是dom对象
    if(e instanceof jQuery){
        //jquery对象使用attr()方法获取属性的值
        commentId = e.attr("data-id");
        comments = $("#comment-"+commentId);
    }else{
        //dom对象使用getAttribute()方法获取属性的值
        commentId = e.getAttribute("data-id");
        comments = $("#comment-"+commentId);
    }
    var $comment = $("#comment-"+commentId);
    var flag = $comment.attr("isShow");

    if(flag=="false"){
        //使用bootstrap的插件collapse。
        //通过在元素的class属性上添加collapse使元素隐藏，加上collapse in使元素显示
        //向上折叠至隐藏
        $comment.removeClass("in");
        $comment.attr("isShow","show");
    }
    if(flag=="show"){
        //向下展开至显示
        $comment.addClass("in");
        $comment.attr("isShow","false");
    }
    if(flag=="true"){
        //向下展开至显示
        $comment.addClass("in");
        $comment.attr("isShow","false");
        $.ajax({
            type : "POST",
            url : "/comments",
            data : JSON.stringify({commentId : commentId}),
            contentType: "application/json",
            dataType: "json",
            success : function (results) {
                //   将返回的数据以异步的方式增加的界面上
                $.each(results,function (index,result) {
                    var parentDiv = $("<div></div>").addClass("col-lg-12 col-md-12 col-sm-12 col-xs-12 comments");
                    var mediaDiv = $("<div></div>").addClass("media");
                    var mediaLeftDiv = $("<div></div>").addClass("media-left").append($("<a></a>").attr("href","#")
                        .append($("<img>").addClass("media-object img-rounded").attr("src",result.user.avatar)));
                    var mediaBody = $("<div></div>").addClass("media-body");
                    var mediaHeading = $("<h5></h5>").addClass("media-heading").append($("<span></span>").append(result.user.name));
                    var commentContent = $("<div></div>").append(result.content);
                    var menuDiv = $("<div></div>").addClass("menu");
                    var dateSpan = $("<span></span>").addClass("pull-right").append(moment(result.gmt_create).format("YYYY-MM-DD"));
                    parentDiv.append(mediaDiv.append(mediaLeftDiv)
                        .append(mediaBody.append(mediaHeading).append(commentContent).append(menuDiv.append(dateSpan))));
                    $comment.prepend(parentDiv);
                });

            }
        });

    }

}

//二级评论功能
function comment2Comment(ele) {
    var questionId;
    var comments;
    var commentSpan;
    //判断传进来的对象子是jquery对象还是dom对象
    if(ele instanceof jQuery){
        //jquery对象使用attr()方法获取属性的值
        questionId = ele.attr("data-id");
        comments = $("#comment-"+questionId);
        commentSpan= $("#span-"+questionId);
    }else{
        //dom对象使用getAttribute()方法获取属性的值
        questionId = ele.getAttribute("data-id");
        comments = $("#comment-"+questionId);
        commentSpan= $("#span-"+questionId);
    }
    var content = $("#input-"+questionId).val();
    var $content = $("#input-"+questionId)
    //通过ajax的方式向后端发送请求
    $.ajax({
        type: "POST",
        url: "/comment",
        data: JSON.stringify(
            {parentId: questionId, content: content, type: 1}),
        contentType: "application/json",
        dataType: "json",
        success: function (result) {
            if(result.code!=200){
                alert(result.message);
            }else{
             //清空回复框中的内容
             $content.val("");
            //    将后台返回的数据加载到相应位置
            //    构建元素
            var parentDiv = $("<div></div>").addClass("col-lg-12 col-md-12 col-sm-12 col-xs-12 comments");
            var mediaDiv = $("<div></div>").addClass("media");
            var mediaLeftDiv = $("<div></div>").addClass("media-left").append($("<a></a>").attr("href","#")
                .append($("<img>").addClass("media-object img-rounded").attr("src",result.data.user.avatar)));
            var mediaBody = $("<div></div>").addClass("media-body");
            var mediaHeading = $("<h5></h5>").addClass("media-heading").append($("<span></span>").append(result.data.user.name));
            var commentContent = $("<div></div>").append(result.data.content);
            var menuDiv = $("<div></div>").addClass("menu");
            var dateSpan = $("<span></span>").addClass("pull-right").append(moment(result.data.gmt_create).format("YYYY-MM-DD"));
            parentDiv.append(mediaDiv.append(mediaLeftDiv)
                .append(mediaBody.append(mediaHeading).append(commentContent).append(menuDiv.append(dateSpan))));
                comments.prepend(parentDiv);
            var value = commentSpan.attr("data");
            //将返回的string转换成int
            value = parseInt(value,10)+1;
            commentSpan.attr("data",value);
            commentSpan.text(value);
            }

        }
    });
}

//点赞功能
    function toLike(element) {
        var commentId;
        var likeSpan;
        //判断传进来的对象子是jquery对象还是dom对象
        if(element instanceof jQuery){
            //jquery对象使用attr()方法获取属性的值
            commentId = element.attr("data-id");
            likeSpan = $("#likeSpan-"+commentId);
        }else{
            //dom对象使用getAttribute()方法获取属性的值
            commentId = element.getAttribute("data-id");
            likeSpan = $("#likeSpan-"+commentId);
        }
        $.ajax({
            type: "POST",
            url: "/liked",
            data: JSON.stringify(
                {id: commentId, type: 1}),
            contentType: "application/json",
            dataType: "json",
            success : function (result) {
                if(result.code != 200){
                    alert(result.message)
                }else{
                    likeSpan.text(result.data);
                }

            }
        });
    }


