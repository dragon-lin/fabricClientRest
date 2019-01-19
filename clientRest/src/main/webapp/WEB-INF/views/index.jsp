<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: linwf
  Date: 2018/11/18
  Time: 16:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>HyperLedger Fabric SDK配置</title>
    <link type="text/css" rel="stylesheet" href="./lib/bootstrap/css/bootstrap.min.css">
    <link type="text/css" rel="stylesheet" href="./css/common.css">
    <link type="text/css" rel="stylesheet" href="./css/style.css">
    <script type="text/javascript" src="./js/jquery-3.3.1.min.js"></script>
    <script type="text/javascript" src="./lib/bootstrap/js/bootstrap.min.js"></script>
</head>
<body>

<h3 th:text="登录成功"></h3>
<div class="container-fluid app-container-fluid">
    <div class="app-sidebar">
        <div class="userinfo">
            <img class="avatarUrl" src="../images/avatarUrl.png"><span class="nikename">${user.name}</span>
        </div>
        <div class="navbar">
            <ul>
                <li class="cspz active">
                    <a href="#">
                        <p>参数配置</p>
                    </a>
                </li>
                <li class="xtts">
                    <a href="#">
                        <p>系统调试</p>
                    </a>
                </li>
                <li class="xgmm">
                    <a href="#">
                        <p>密码修改</p>
                    </a>
                </li>
            </ul>
        </div>
    </div>
    <div class="app-container">
        <div class="app-header">
            <div class="logo">tinms</div>
        </div>
        <div class="app-content">
            <input type="hidden" id="rowId" value="${config.row_id}">
            <input type="hidden" id="leagueId" value="1">
            <ul class="row">
                <li class="col-xs-6 form-group">
                    <label class="col-sm-3 control-label">组织名称(OrgName)</label>
                    <div class="col-sm-9">
                        <input type="text" id="orgName" class="form-control" placeholder="" value="${config.org_name}">
                    </div>
                </li>
                <li class="col-xs-6 form-group">
                    <label class="col-sm-3 control-label">用户名称(Username)</label>
                    <div class="col-sm-9">
                        <input type="text" id="userName" class="form-control" placeholder="" value="${config.user_name}">
                    </div>
                </li>
                <li class="col-xs-6 form-group">
                    <label class="col-sm-3 control-label">证书路径(CryptoConfigPath)</label>
                    <div class="col-sm-9">
                        <input type="text" id="cryptoconfigPath" class="form-control" placeholder="" value="${config.cryptoconfig_path}">
                    </div>
                </li>
                <li class="col-xs-6 form-group">
                    <label class="col-sm-3 control-label">通道配置路径(ChannelArtifactsPath)</label>
                    <div class="col-sm-9">
                        <input type="text" id="channelartifactsPath" class="form-control" placeholder="" value="${config.channelartifacts_path}">
                    </div>
                </li>
                <li class="col-xs-6 form-group">
                    <label class="col-sm-3 control-label">组织msp_id(OrgMSPID)</label>
                    <div class="col-sm-9">
                        <input type="text" id="orgMspid" class="form-control" placeholder="" value="${config.org_mspid}">
                    </div>
                </li>
                <li class="col-xs-6 form-group">
                    <label class="col-sm-3 control-label">组织域名(OrgDomain)</label>
                    <div class="col-sm-9">
                        <input type="text" id="orgDomain" class="form-control" placeholder="" value="${config.org_domain}">
                    </div>
                </li>
                <li class="col-xs-6 form-group">
                    <label class="col-sm-3 control-label">排序域名(OrdererDomain)</label>
                    <div class="col-sm-9">
                        <input type="text" id="ordererDomain" class="form-control" placeholder="" value="${config.orderer_domain}">
                    </div>
                </li>
                <li class="col-xs-6 form-group">
                    <label class="col-sm-3 control-label">通道名称(ChannelName)</label>
                    <div class="col-sm-9">
                        <input type="text" id="channelName" class="form-control" placeholder="" value="${config.channel_name}">
                    </div>
                </li>
                <li class="col-xs-6 form-group">
                    <label class="col-sm-3 control-label">智能合约名称(ChaincodeName)</label>
                    <div class="col-sm-9">
                        <input type="text" id="chaincodeName" class="form-control" placeholder="" value="${config.chaincode_name}">
                    </div>
                </li>
                <li class="col-xs-6 form-group">
                    <label class="col-sm-3 control-label">智能合约源码(ChaincodeSource)</label>
                    <div class="col-sm-9">
                        <input type="text" id="chaincodeSource" class="form-control" placeholder="" value="${config.chaincode_source}">
                    </div>
                </li>
                <li class="col-xs-6 form-group">
                    <label class="col-sm-3 control-label">智能合约路径(ChaincodePath)</label>
                    <div class="col-sm-9">
                        <input type="text" id="chaincodePath" class="form-control" placeholder="" value="${config.chaincode_path}">
                    </div>
                </li>
                <li class="col-xs-6 form-group">
                    <label class="col-sm-3 control-label">智能合约策略(ChaincodePolicy)</label>
                    <div class="col-sm-9">
                        <input type="text" id="chaincodePolicy" class="form-control" placeholder="" value="${config.chaincode_policy}">
                    </div>
                </li>
                <li class="col-xs-6 form-group">
                    <label class="col-sm-3 control-label">智能合约版本(ChaincodeVersion)</label>
                    <div class="col-sm-9">
                        <input type="text" id="chaincodeVersion" class="form-control" placeholder="" value="${config.chaincode_version}">
                    </div>
                </li>
                <li class="col-xs-6 form-group">
                    <label class="col-sm-3 control-label">提案等待时间(ProposalWaittime)</label>
                    <div class="col-sm-9">
                        <input type="text" id="proposalWaittime" class="form-control" placeholder="" value="${config.proposal_waittime}">
                    </div>
                </li>
                <li class="col-xs-6 form-group">
                    <label class="col-sm-3 control-label">交易等待时间(InvokeWaittime)</label>
                    <div class="col-sm-9">
                        <input type="text" id="invokeWaittime" class="form-control" placeholder="" value="${config.invoke_waittime}">
                    </div>
                </li>
                <li class="col-xs-6 form-group">
                    <label class="col-sm-3 control-label">开启TLS(IsTls)</label>
                    <div class="col-sm-9">
                        <input type="radio" name="isTls" value="1" ${config.is_tls ? 'checked' :''}>是
                        <input type="radio" name="isTls" value="0" ${!config.is_tls ? 'checked' :''}>否
                    </div>
                </li>
                <li class="col-xs-6 form-group">
                    <label class="col-sm-3 control-label">开启CA TLS(IsCatls)</label>
                    <div class="col-sm-9">
                        <input type="radio" name="isCatls" value="1" ${config.is_catls ? 'checked' :''}>是
                        <input type="radio" name="isCatls" value="0" ${!config.is_catls ? 'checked' :''}>否
                    </div>
                </li>
            </ul>

            <div class="row btn-group div-block">
                <p class="col-xs-12">
                    <input id="save" name="save" type="button" class="btn btn-info btn-sm" value="保存" />
                </p>
            </div>
            <ul class="row">
                <li class="col-xs-12">
                    <div class="table">
                        <table id="ordererList" class="table table-bordered">
                            <caption>orderer:</caption>
                            <thead>
                            <tr>
                                <th>排序名称</br>(ordererName)</th>
                                <th>排序地址</br>(ordererLocation)</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${configOrdererList}" var="item">
                                <tr configId="${item.config_id}">
                                    <td>${item.orderer_name}</td>
                                    <td>${item.orderer_location}</td>
                                    <td>
                                        <input class="btn btn-primary btn-xs" type="button" data-toggle="modal" data-target="#myOrdererModal" value="编辑">
                                        <input class="btn btn-danger btn-xs" type="button" value="删除">
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </li>

                <li class="col-xs-12">
                    <div class="table">
                        <table id="peerList" class="table table-bordered">
                            <caption>peer:</caption>
                            <thead>
                            <tr>
                                <th>节点名称</br>(peerName)</th>
                                <th>节点事件名称</br>(peerEventHubName)</th>
                                <th>节点地址</br>(peerLocation)</th>
                                <th>节点事件地址</br>(peerEventHubLocation)</th>
                                <th>是否监听</br>(isEventListener)</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${configPeerList}" var="item">
                                <tr configId="${item.config_id}">
                                    <td>${item.peer_name}</td>
                                    <td>${item.peer_eventhubname}</td>
                                    <td>${item.peer_location}</td>
                                    <td>${item.peer_eventhublocation}</td>
                                    <td value="${item.is_eventlistener}">${item.is_eventlistener ? '是' :'否'}</td>
                                    <td>
                                        <input class="btn btn-primary btn-xs" type="button" data-toggle="modal" data-target="#myPeerModal" value="编辑">
                                        <input class="btn btn-danger btn-xs" type="button" value="删除">
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </li>
            </ul>
        </div>
    </div>
</div>
<div class="modal fade" id="myOrdererModal" tabindex="-1" role="dialog" aria-labelledby="myOrdererModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">orderer</h4>
            </div>
            <div class="modal-body">
                <ul class="row">
                    <li class="col-xs-12 form-group">
                        <label class="col-sm-3 control-danger">排序名称(ordererName)</label>
                        <div class="col-sm-9">
                            <input type="text" id="ordererName" class="form-control" placeholder="" value="">
                        </div>
                    </li>
                    <li class="col-xs-12 form-group">
                        <label class="col-sm-3 control-danger">排序地址(ordererLocation)</label>
                        <div class="col-sm-9">
                            <input type="text " id="ordererLocation" class="form-control" placeholder="" value="">
                        </div>
                    </li>
                </ul>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-danger" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-danger">保存</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<div class="modal fade" id="myPeerModal" tabindex="-1" role="dialog" aria-labelledby="myPeerModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">peer</h4>
            </div>
            <div class="modal-body">
                <ul class="row">
                    <li class="col-xs-12 form-group">
                        <label class="col-sm-3 control-label">节点名称(peerName)</label>
                        <div class="col-sm-9">
                            <input type="text" id="peerName" class="form-control" placeholder="" value="">
                        </div>
                    </li>
                    <li class="col-xs-12 form-group">
                        <label class="col-sm-3 control-label">节点事件名称(peerEventHubName)</label>
                        <div class="col-sm-9">
                            <input type="text" id="peerEventHubName" class="form-control" placeholder="" value="">
                        </div>
                    </li>
                    <li class="col-xs-12 form-group">
                        <label class="col-sm-3 control-label">节点地址(ordererLocation)</label>
                        <div class="col-sm-9">
                            <input type="text" id="peerLocation" class="form-control" placeholder="" value="">
                        </div>
                    </li>
                    <li class="col-xs-12 form-group">
                        <label class="col-sm-3 control-label">节点事件地址(peerEventHubLocation)</label>
                        <div class="col-sm-9">
                            <input type="text" id="peerEventHubLocation" class="form-control" placeholder="" value="">
                        </div>
                    </li>
                    <li class="col-xs-12 form-group">
                        <label class="col-sm-3 control-label">节点事件地址(peerEventHubLocation)</label>
                        <div class="col-sm-9">
                            <input type="radio" name="isCatls" value="1" ${config.is_catls ? 'checked' :''}>是
                            <input type="radio" name="isCatls" value="0" ${!config.is_catls ? 'checked' :''}>否
                        </div>
                    </li>
                </ul>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary">保存</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<script type="text/javascript" src="./js/index.js"></script>
</body>
</html>
