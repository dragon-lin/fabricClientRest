/**
 * Created by linwf on 2018/12/18.
 */

$(function(){
    $(".app-container-fluid").css("minHeight",document.body.clientHeight)
    $(window).resize(function(){
        $(".app-container-fluid").css("minHeight",document.body.clientHeight)
    })
    $(".button-type-group .btn-type").click(function () {
        $(".button-type-group .btn-type").removeClass('active');
        $(this).addClass("active");
    })
    var selectId = "createChannel";

    $("#save").click(function(){
        var jsonParam = getParam();
        $.post("/save",{"jsonParam":jsonParam},function(data){
            var jsonData = eval("(" + data + ")");
            if (jsonData.result > 0){
                alert("保存成功");
            }
        });
    });
    $("#logout").click(function(){
        window.location.href="logout";
    });

    var getParam = function(){
        var strParam = new Object();
        strParam.rowId = $("#rowId").val();
        strParam.leagueId = $("#leagueId").val();
        strParam.orgName = $("#orgName").val();
        strParam.userName = $("#userName").val();
        strParam.cryptoconfigPath = $("#cryptoconfigPath").val();
        strParam.channelartifactsPath = $("#channelartifactsPath").val();
        strParam.orgMspid = $("#orgMspid").val();
        strParam.orgDomain = $("#orgDomain").val();
        strParam.ordererDomain = $("#ordererDomain").val();
        strParam.channelName = $("#channelName").val();
        strParam.chaincodeName = $("#chaincodeName").val();
        strParam.chaincodeSource = $("#chaincodeSource").val();
        strParam.chaincodePath = $("#chaincodePath").val();
        strParam.chaincodePolicy = $("#chaincodePolicy").val();
        strParam.chaincodeVersion = $("#chaincodeVersion").val();
        strParam.proposalWaittime = $("#proposalWaittime").val();
        strParam.invokeWaittime = $("#invokeWaittime").val();
        strParam.isTls = $("input[name='isTls']:checked").val();
        strParam.isCatls = $("input[name='isCatls']:checked").val();
        strParam.ordererList = new Array();
        $.each($("#ordererList tbody tr"), function(i, v){
            strParam.ordererList[i] = new Object();
            strParam.ordererList[i].ordererName = $(v).find("td:eq(0)").text();
            strParam.ordererList[i].ordererLocation = $(v).find("td:eq(1)").text();
        });
        strParam.peerList = new Array();
        $.each($("#peerList tbody tr"), function(i, v){
            strParam.peerList[i] = new Object();
            strParam.peerList[i].configId = $(v).attr("configId");
            strParam.peerList[i].peerName = $(v).find("td:eq(0)").text();
            strParam.peerList[i].peerEventHubName = $(v).find("td:eq(1)").text();
            strParam.peerList[i].peerLocation = $(v).find("td:eq(2)").text();
            strParam.peerList[i].peerEventHubLocation = $(v).find("td:eq(3)").text();
            strParam.peerList[i].isEventListener = ($(v).find("td:eq(4)").attr("value") == "true"?"1":"0");
        });
        return JSON.stringify(strParam);
    };
    $(".navbar li").click(function(){
        var index = $(this).index();
        switch (index){
            case 0:
                $("#paramConfig").show();
                $("#systemOp").hide();
                $("#systemPw").hide();
                break;
            case 1:
                $("#paramConfig").hide();
                $("#systemOp").show();
                $("#systemPw").hide();
                break;
            case 2:
                $("#paramConfig").hide();
                $("#systemOp").hide();
                $("#systemPw").show();
                break;
        }
        $(this).addClass("active");
        $(this).siblings().removeClass("active");
    });
    $("#functionOp button").click(function () {
        var selectIdTmp = $(this).attr("id");
        if (selectId == selectIdTmp){
            return;
        }
        selectId = selectIdTmp;
        switch (selectId){
            case "createChannel":
            case "joinPeer":
            case "install":
                $("#dataRequest").val('');
                $("#regionRequest").hide();
                break;
            case "instantiate":
                $("#dataRequest").val('{"args":[]}');
                $("#regionRequest").show();
                break;
            case "upgrade":
                $("#dataRequest").val('{"args":[]}');
                $("#regionRequest").show();
                break;
            case "invoke":
                $("#dataRequest").val('{"fcn":"invoke","args":["1","2","3","4","5","6"]}');
                $("#regionRequest").show();
                break;
            case "query":
                $("#dataRequest").val('{"fcn":"query","args":["1"]}');
                $("#regionRequest").show();
                break;
            case "queryBlockByTransactionID":
                $("#dataRequest").val('{"txId":"5412bf55447701c58753dc90416e6ddcafc3c280fc5585b7316fbd97e9f5d3fb"}');
                $("#regionRequest").show();
                break;
            case "queryBlockByHash":
                $("#dataRequest").val('{"hash":"b81a92fc5f0427e17cecf461efcf05733b084d691ccf6b56c51f9a94bffa8e27"}');
                $("#regionRequest").show();
                break;
            case "queryBlockByNumber":
                $("#dataRequest").val('{"blockNumber":"1"}');
                $("#regionRequest").show();
                break;
            case "queryCurrentBlockInfo":
                $("#dataRequest").val('');
                $("#regionRequest").hide();
                break;

        }
    });

    $("#run").click(function(){
        $("#dataResponse").val("");
        $("#loading").show();
        $("#run,#systemOp").css("cursor","wait");
        switch (selectId){
            case "createChannel":
                $.post("/chainchannel/createChannel",{},function(jsonData){
                    $("#dataResponse").val(JSON.stringify(jsonData));
                    $("#loading").hide();
                    $("#run,#systemOp").css("cursor","default");
                    if (jsonData.status == 200){
                        alert("加入通道成功");
                    }
                });
                break;
            case "joinPeer":
                $.post("/chainchannel/joinPeer",{},function(jsonData){
                    $("#dataResponse").val(JSON.stringify(jsonData));
                    $("#loading").hide();
                    $("#run,#systemOp").css("cursor","default");
                    if (jsonData.status == 200){
                        alert("加入通道成功");
                    }
                });
                break;
            case "install":
                $.post("/chaincode/install",{},function(jsonData){
                    $("#dataResponse").val(JSON.stringify(jsonData));
                    $("#loading").hide();
                    $("#run,#systemOp").css("cursor","default");
                    if (jsonData.status == 200){
                        alert("安装智能合约成功");
                    }
                });
                break;
            case "instantiate":
                var strParam = $("#dataRequest").val();
                if (strParam == ""){
                    $("#dataResponse").val("参数不能为空");
                    return;
                }
                if (!isJSON(strParam)){
                    $("#dataResponse").val("参数不是json数据");
                    return;
                }
                $.ajax({
                    url: '/chaincode/instantiate',
                    type: 'POST',
                    dataType: 'json',
                    contentType: 'application/json',
                    data: strParam,
                    success: function(jsonData) {
                        $("#dataResponse").val(JSON.stringify(jsonData));
                        $("#loading").hide();
                        $("#run,#systemOp").css("cursor","default");
                        if (jsonData.status == 200){
                            alert("初始化智能合约成功");
                        }
                    }
                });
                break;
            case "upgrade":
                var strParam = $("#dataRequest").val();
                if (strParam == ""){
                    $("#dataResponse").val("参数不能为空");
                    return;
                }
                if (!isJSON(strParam)){
                    $("#dataResponse").val("参数不是json数据");
                    return;
                }
                $.ajax({
                    url: '/chaincode/upgrade',
                    type: 'POST',
                    dataType: 'json',
                    contentType: 'application/json',
                    data: strParam,
                    success: function(jsonData) {
                        $("#dataResponse").val(JSON.stringify(jsonData));
                        $("#loading").hide();
                        $("#run,#systemOp").css("cursor","default");
                        if (jsonData.status == 200){
                            alert("升级智能合约成功");
                        }
                    }
                });
                break;
            case "invoke":
                var strParam = $("#dataRequest").val();
                if (strParam == ""){
                    $("#dataResponse").val("参数不能为空");
                    return;
                }
                if (!isJSON(strParam)){
                    $("#dataResponse").val("参数不是json数据");
                    return;
                }
                $.ajax({
                    url: '/chaincode/invoke',
                    type: 'POST',
                    dataType: 'json',
                    contentType: 'application/json',
                    data: strParam,
                    success: function(jsonData) {
                        $("#dataResponse").val(JSON.stringify(jsonData));
                        $("#loading").hide();
                        $("#run,#systemOp").css("cursor","default");
                        if (jsonData.status == 200){
                            alert("执行智能合约成功");
                        }
                    }
                });
                break;
            case "query":
                var strParam = $("#dataRequest").val();
                if (strParam == ""){
                    $("#dataResponse").val("参数不能为空");
                    return;
                }
                if (!isJSON(strParam)){
                    $("#dataResponse").val("参数不是json数据");
                    return;
                }
                $.ajax({
                    url: '/chaincode/query',
                    type: 'POST',
                    dataType: 'json',
                    contentType: 'application/json',
                    data: strParam,
                    success: function(jsonData) {
                        $("#dataResponse").val(JSON.stringify(jsonData));
                        $("#loading").hide();
                        $("#run,#systemOp").css("cursor","default");
                        if (jsonData.status == 200){
                            alert("查询智能合约成功");
                        }
                    }
                });
                break;
            case "queryBlockByTransactionID":
                var strParam = $("#dataRequest").val();
                if (strParam == ""){
                    $("#dataResponse").val("参数不能为空");
                    return;
                }
                if (!isJSON(strParam)){
                    $("#dataResponse").val("参数不是json数据");
                    return;
                }
                $.ajax({
                    url: '/chainblock/queryBlockByTransactionID',
                    type: 'POST',
                    dataType: 'json',
                    contentType: 'application/json',
                    data: strParam,
                    success: function(jsonData) {
                        $("#dataResponse").val(JSON.stringify(jsonData));
                        $("#loading").hide();
                        $("#run,#systemOp").css("cursor","default");
                        if (jsonData.status == 200){
                            alert("根据交易Id查询区块数据成功");
                        }
                    }
                });
                break;
            case "queryBlockByHash":
                var strParam = $("#dataRequest").val();
                if (strParam == ""){
                    $("#dataResponse").val("参数不能为空");
                    return;
                }
                if (!isJSON(strParam)){
                    $("#dataResponse").val("参数不是json数据");
                    return;
                }
                $.ajax({
                    url: '/chainblock/queryBlockByHash',
                    type: 'POST',
                    dataType: 'json',
                    contentType: 'application/json',
                    data: strParam,
                    success: function(jsonData) {
                        $("#dataResponse").val(JSON.stringify(jsonData));
                        $("#loading").hide();
                        $("#run,#systemOp").css("cursor","default");
                        if (jsonData.status == 200){
                            alert("根据哈希值查询区块数据成功");
                        }
                    }
                });
                break;
            case "queryBlockByNumber":
                var strParam = $("#dataRequest").val();
                if (strParam == ""){
                    $("#dataResponse").val("参数不能为空");
                    return;
                }
                if (!isJSON(strParam)){
                    $("#dataResponse").val("参数不是json数据");
                    return;
                }
                $.ajax({
                    url: '/chainblock/queryBlockByNumber',
                    type: 'POST',
                    dataType: 'json',
                    contentType: 'application/json',
                    data: strParam,
                    success: function(jsonData) {
                        $("#dataResponse").val(JSON.stringify(jsonData));
                        $("#loading").hide();
                        $("#run,#systemOp").css("cursor","default");
                        if (jsonData.status == 200){
                            alert("根据区块高度查询区块数据成功");
                        }
                    }
                });
                break;
            case "queryCurrentBlockInfo":
                $.post("/chainblock/queryCurrentBlockInfo",{},function(jsonData){
                    $("#dataResponse").val(JSON.stringify(jsonData));
                    $("#loading").hide();
                    $("#run,#systemOp").css("cursor","default");
                    if (jsonData.status == 200){
                        alert("查询当前区块信息成功");
                    }
                });
                break;
        }
    });
    var isJSON = function(str) {
        if (typeof str == 'string') {
            try {
                var obj=JSON.parse(str);
                if(typeof obj == 'object' && obj ){
                    return true;
                }else{
                    return false;
                }
            } catch(e) {
                return false;
            }
        }
    }
    var orderId;
    var peerId;
    //新增点击
    $("#add-order-btn").click(function () {
        $("#save-orderer").show();
        $("#modify-orderer").hide();
        clear();
        $("#myOrdererModal").modal("show");
    })
    $("#add-peer-btn").click(function () {
        $("#save-peer").show();
        $("#modify-peer").hide();
        clear();
        $("#myPeerModal").modal("show");
    })
    //清空数据
    function clear() {
        $("#ordererName").val("")
        $("#ordererLocation").val("")

        $("#peerName").val("")
        $("#peerEventHubName").val("")
        $("#peerLocation").val("")
        $("#peerEventHubLocation").val("")
        $("input[name='isCatls']").eq(0).prop('checked', true);
    }
    //保存按钮点击
    $("#save-orderer").click(function(){
        var orderer_name=$("#ordererName").val();
        var orderer_location=$("#ordererLocation").val();
        var str="";
        str+="<tr configid='1'>"
        str+="<td class='orderer_name'>"+orderer_name+"</td>"
        str+="<td class='orderer_location'>"+orderer_location+"</td>"
        str+="<td>"
        str+="<input class='btn btn-primary btn-xs modify-btn' type='button' value='编辑'>"
        str+="<input class='btn btn-danger btn-xs delete-btn' type='button' value='删除'>"
        str+="</td>"
        str+="</tr>"
        $("#ordererList tbody").append(str)
        $("#myOrdererModal").modal("hide");
        clear();

        //编辑按钮点击
        $("#ordererList .modify-btn").unbind();
        $("#ordererList .modify-btn").click(function(){
            orderId=$(this).parents("tr").index()
            $("#save-orderer").hide();
            $("#modify-orderer").show();
            $("#ordererName").val($(this).parents("tr").find(".orderer_name").text())
            $("#ordererLocation").val($(this).parents("tr").find(".orderer_location").text())
            $("#myOrdererModal").modal("show");
        })

        //删除按钮点击
        $("#ordererList .delete-btn").click(function () {
            orderId=$(this).parents("tr").index()
            delete_order_confirm(orderId)
        })
    });
    $("#save-peer").click(function(){
        var peerName=$("#peerName").val();
        var peerEventHubName=$("#peerEventHubName").val();
        var peerLocation=$("#peerLocation").val();
        var peerEventHubLocation=$("#peerEventHubLocation").val();
        var isEventListener=$("input[name='isCatls']:checked").val();
        var str="";
        str+="<tr configid='1'>"
        str+="<td class='peer_name'>"+peerName+"</td>"
        str+="<td class='peer_eventhubname'>"+peerEventHubName+"</td>"
        str+="<td class='peer_location'>"+peerLocation+"</td>"
        str+="<td class='peer_eventhublocation'>"+peerEventHubLocation+"</td>"
        if(isEventListener==1){
            str+="<td class='is_eventlistener' value="+isEventListener+">是</td>"
        }else if(isEventListener==0){
            str+="<td class='is_eventlistener' value="+isEventListener+">否</td>"
        }
        str+="<td>"
        str+="<input class='btn btn-primary btn-xs modify-btn' type='button' value='编辑'>"
        str+="<input class='btn btn-danger btn-xs delete-btn' type='button' value='删除'>"
        str+="</td>"
        str+="</tr>"
        $("#peerList tbody").append(str)
        $("#myPeerModal").modal("hide");
        clear();

        //编辑按钮点击
        $("#peerList .modify-btn").unbind();
        $("#peerList .modify-btn").click(function(){
            peerId=$(this).parents("tr").index()
            $("#save-peer").hide();
            $("#modify-peer").show();
            $("#peerName").val($(this).parents("tr").find(".peer_name").text())
            $("#peerEventHubName").val($(this).parents("tr").find(".peer_eventhubname").text())
            $("#peerLocation").val($(this).parents("tr").find(".peer_location").text())
            $("#peerEventHubLocation").val($(this).parents("tr").find(".peer_eventhublocation").text())
            if($(this).parents("tr").find(".is_eventlistener").attr("value")){
                $("input[name='isCatls']").eq(0).prop('checked', true);
            }else{
                $("input[name='isCatls']").eq(1).prop('checked', true);
            }
            $("#myPeerModal").modal("show");
        })

        //删除按钮点击
        $("#peerList .delete-btn").click(function () {
            peerId=$(this).parents("tr").index()
            delete_peer_confirm(peerId)
        })
    });
    //编辑按钮点击
    $("#ordererList .modify-btn").click(function(){
        orderId=$(this).parents("tr").index()
        $("#save-orderer").hide();
        $("#modify-orderer").show();
        $("#ordererName").val($(this).parents("tr").find(".orderer_name").text())
        $("#ordererLocation").val($(this).parents("tr").find(".orderer_location").text())
        $("#myOrdererModal").modal("show");
    });
    $("#peerList .modify-btn").click(function(){
        peerId=$(this).parents("tr").index()
        $("#save-peer").hide();
        $("#modify-peer").show();
        $("#peerName").val($(this).parents("tr").find(".peer_name").text())
        $("#peerEventHubName").val($(this).parents("tr").find(".peer_eventhubname").text())
        $("#peerLocation").val($(this).parents("tr").find(".peer_location").text())
        $("#peerEventHubLocation").val($(this).parents("tr").find(".peer_eventhublocation").text())
        if($(this).parents("tr").find(".is_eventlistener").attr("value")){
            $("input[name='isCatls']").eq(0).prop('checked', true);
        }else{
            $("input[name='isCatls']").eq(1).prop('checked', true);
        }
        $("#myPeerModal").modal("show");
    });
    //编辑保存按钮点击
    $("#modify-orderer").click(function(){
        $("#ordererList tbody tr").eq(orderId).find(".orderer_name").text($("#ordererName").val())
        $("#ordererList tbody tr").eq(orderId).find(".orderer_location").text($("#ordererLocation").val())
        $("#myOrdererModal").modal("hide");
        clear();
    })
    $("#modify-peer").click(function(){
        $("#peerList tbody tr").eq(peerId).find(".peer_name").text($("#peerName").val())
        $("#peerList tbody tr").eq(peerId).find(".peer_eventhubname").text($("#peerEventHubName").val())
        $("#peerList tbody tr").eq(peerId).find(".peer_location").text($("#peerLocation").val())
        $("#peerList tbody tr").eq(peerId).find(".peer_eventhublocation").text($("#peerEventHubLocation").val())

        if($("input[name='isCatls']:checked").val()==1){
            $("#peerList tbody tr").eq(peerId).find(".is_eventlistener").text('是').attr("value",$("input[name='isCatls']:checked").val())
        }else if($("input[name='isCatls']:checked").val()==0){
            $("#peerList tbody tr").eq(peerId).find(".is_eventlistener").text('否').attr("value",$("input[name='isCatls']:checked").val())
        }
        $("#myPeerModal").modal("hide");
        clear();
    })
    //删除按钮点击
    $("#ordererList .delete-btn").click(function () {
        orderId=$(this).parents("tr").index()
        delete_order_confirm(orderId)
    })
    $("#peerList .delete-btn").click(function () {
        peerId=$(this).parents("tr").index()
        delete_peer_confirm(peerId)
    })
    function delete_order_confirm(orderId){
        var r=confirm("确定删除该条数据？");
        if (r==true){
            $("#ordererList tbody tr").eq(orderId).remove()
        }
    }
    function delete_peer_confirm(peerId){
        var r=confirm("确定删除该条数据？");
        if (r==true){
            $("#peerList tbody tr").eq(peerId).remove()
        }
    }
    $("#modify-pw").click(function(){
        var account = $("#account").val();
        var password = $("#password").val();
        var passwordC = $("#passwordC").val();
        if (password == ""){
            alert("密码不能为空");
            return
        }
        if (passwordC == ""){
            alert("密码确认不能为空");
            return
        }
        if (password != passwordC){
            alert("两次输入密码不一致");
            return
        }
        $.post("/modifypassword",{"account":account,"password":password},function(data){
            var jsonData = eval("(" + data + ")");
            if (jsonData.result > 0){
                alert("密码修改成功");
            }
        });
    });

});