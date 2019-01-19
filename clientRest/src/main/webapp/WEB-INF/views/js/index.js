/**
 * Created by linwf on 2018/12/18.
 */

$(function(){
    $("#save").click(function(){
        var jsonParam = getParam();
        $.post("/save",{"jsonParam":jsonParam},function(data){
            var jsonData = eval("(" + data + ")");
            if (jsonData.result > 0){
                alert("保存成功");
            }
        });
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







});