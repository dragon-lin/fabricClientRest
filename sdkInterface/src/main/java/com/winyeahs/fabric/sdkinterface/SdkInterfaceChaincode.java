package com.winyeahs.fabric.sdkinterface;

import com.winyeahs.fabric.sdkinterface.base.SdkInterfaceBase;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.ChaincodeEndorsementPolicyParseException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by linwf on 2018/10/28.
 */
public class SdkInterfaceChaincode extends SdkInterfaceBase {
    // 智能合约名称
    private String chaincodeName; // mycc
    // 智能合约的go环境路径
    private String chaincodeSource; // /opt/gopath
    // 智能合约安装路径
    private String chaincodePath; // github.com/hyperledger/fabric/kafkapeer/chaincode/go/example02
    // 智能合约背书策略文件存放路径
    private String chaincodePolicy; // /home/policy.yaml
    // 智能合约版本号
    private String chaincodeVersion; // 1.0
    // 智能合约ID
    private ChaincodeID chaincodeID;
    // 单个提案请求的超时时间(以毫秒为单位)
    private int proposalWaitTime = 200000;
    // 事务等待时间(以秒为单位)
    private int transactionWaitTime = 120;

    public void setChaincodeName(String chaincodeName) {
        this.chaincodeName = chaincodeName;
        setChaincodeID();
    }

    public String getChaincodeName() {
        return this.chaincodeName;
    }

    public void setChaincodeSource(String chaincodeSource) {
        this.chaincodeSource = chaincodeSource;
        setChaincodeID();
    }

    public String getChaincodeSource() {
        return this.chaincodeSource;
    }

    public void setChaincodePath(String chaincodePath) {
        this.chaincodePath = chaincodePath;
        setChaincodeID();
    }

    public String getChaincodePath() {
        return this.chaincodePath;
    }

    public void setChaincodePolicy(String chaincodePolicy) {
        this.chaincodePolicy = chaincodePolicy;
    }

    public String getChaincodePolicy() {
        return this.chaincodePolicy;
    }

    public void setChaincodeVersion(String chaincodeVersion) {
        this.chaincodeVersion = chaincodeVersion;
        setChaincodeID();
    }

    public String getChaincodeVersion() {
        return this.chaincodeVersion;
    }

    public void setProposalWaitTime(int proposalWaitTime) {
        this.proposalWaitTime = proposalWaitTime;
    }

    public int getProposalWaitTime() {
        return this.proposalWaitTime;
    }

    public void setTransactionWaitTime(int transactionWaitTime) {
        this.transactionWaitTime = proposalWaitTime;
    }

    public int getTransactionWaitTime() {
        return this.transactionWaitTime;
    }

    private void setChaincodeID() {
        if (null != chaincodeName && null != chaincodePath && null != chaincodeVersion) {
            chaincodeID = ChaincodeID.newBuilder().setName(chaincodeName).setVersion(chaincodeVersion).setPath(chaincodePath).build();
        }
    }

    public ChaincodeID getChaincodeID() {
        return this.chaincodeID;
    }

    /**
     * 初始化智能合约
     * @return
     */
    public Map<String, String> chainCodeInstall(SdkInterfaceOrg org) throws InvalidArgumentException, ProposalException {
        // Send transaction proposal to all peers
        InstallProposalRequest installProposalRequest = org.getClient().newInstallProposalRequest();
        installProposalRequest.setChaincodeName(chaincodeName);
        installProposalRequest.setChaincodeVersion(chaincodeVersion);
        installProposalRequest.setChaincodeSourceLocation(new File(chaincodeSource));
        installProposalRequest.setChaincodePath(chaincodePath);
        installProposalRequest.setChaincodeLanguage(TransactionRequest.Type.GO_LANG);
        installProposalRequest.setProposalWaitTime(proposalWaitTime);

        Collection<ProposalResponse> installProposalResponses = org.getClient().sendInstallProposal(installProposalRequest, org.getChannel().getChannel().getPeers());
        return toPeerResponse(installProposalResponses, false);
    }
    /**
     * 实例化智能合约
     * @param args 查询参数数组
     * @return
     */
    public Map<String, String> chainCodeInstantiate(SdkInterfaceOrg org, String[] args) throws InterruptedException, InvalidArgumentException, TimeoutException, ExecutionException, IOException, ChaincodeEndorsementPolicyParseException, ProposalException {
        // Send transaction proposal to all peers
        InstantiateProposalRequest instantiateProposalRequest = org.getClient().newInstantiationProposalRequest();
        instantiateProposalRequest.setChaincodeID(chaincodeID);
        instantiateProposalRequest.setProposalWaitTime(proposalWaitTime);
        instantiateProposalRequest.setArgs(args);

        ChaincodeEndorsementPolicy chaincodeEndorsementPolicy = new ChaincodeEndorsementPolicy();
        chaincodeEndorsementPolicy.fromYamlFile(new File(this.chaincodePolicy));//"code/src/policy/chaincodeendorsementpolicy.yaml"));
        instantiateProposalRequest.setChaincodeEndorsementPolicy(chaincodeEndorsementPolicy);

        Map<String, byte[]> tm2 = new HashMap<>();
        tm2.put("HyperLedgerFabric", "InstantiateProposalRequest:JavaSDK".getBytes(UTF_8));
        tm2.put("method", "InstantiateProposalRequest".getBytes(UTF_8));
        tm2.put("result", ":)".getBytes(UTF_8));
        instantiateProposalRequest.setTransientMap(tm2);

        long currentStart = System.currentTimeMillis();
        Collection<ProposalResponse> instantiateProposalResponses = org.getChannel().getChannel().sendInstantiationProposal(instantiateProposalRequest, org.getChannel().getChannel().getPeers());
        return this.toOrdererResponse(instantiateProposalResponses, org);
    }
    /**
     * 升级智能合约
     * @param args 查询参数数组
     * @return
     */
    public Map<String, String> chainCodeUpgrade(SdkInterfaceOrg org, String[] args) throws InterruptedException, InvalidArgumentException, TimeoutException, ExecutionException, IOException, ChaincodeEndorsementPolicyParseException, ProposalException {
        /// Send transaction proposal to all peers
        UpgradeProposalRequest upgradeProposalRequest = org.getClient().newUpgradeProposalRequest();
        upgradeProposalRequest.setChaincodeID(this.chaincodeID);
        upgradeProposalRequest.setProposalWaitTime(proposalWaitTime);
        upgradeProposalRequest.setArgs(args);

        ChaincodeEndorsementPolicy chaincodeEndorsementPolicy = new ChaincodeEndorsementPolicy();
        chaincodeEndorsementPolicy.fromYamlFile(new File(this.chaincodePolicy));//"code/src/policy/chaincodeendorsementpolicy.yaml"));
        upgradeProposalRequest.setChaincodeEndorsementPolicy(chaincodeEndorsementPolicy);

        Map<String, byte[]> tm2 = new HashMap<>();
        tm2.put("HyperLedgerFabric", "InstantiateProposalRequest:JavaSDK".getBytes(UTF_8));
        tm2.put("method", "InstantiateProposalRequest".getBytes(UTF_8));
        tm2.put("result", ":)".getBytes(UTF_8));
        upgradeProposalRequest.setTransientMap(tm2);

        Collection<ProposalResponse> upgradeProposalResponses = org.getChannel().getChannel().sendUpgradeProposal(upgradeProposalRequest, org.getChannel().getChannel().getPeers());
        return this.toOrdererResponse(upgradeProposalResponses, org);
    }
    /**
     * 执行智能合约
     * @param args 参数数组
     * @return
     */
    public Map<String, String> chainCodeInvoke(SdkInterfaceOrg org, String fcn, String[] args) throws InvalidArgumentException, ProposalException, InterruptedException, ExecutionException, TimeoutException, UnsupportedEncodingException {
        TransactionProposalRequest transactionProposalRequest = org.getClient().newTransactionProposalRequest();
        transactionProposalRequest.setChaincodeID(chaincodeID);
        transactionProposalRequest.setFcn(fcn);
        transactionProposalRequest.setArgs(args);
        transactionProposalRequest.setProposalWaitTime(proposalWaitTime);

        Map<String, byte[]> tm2 = new HashMap<>();
        tm2.put("HyperLedgerFabric", "TransactionProposalRequest:JavaSDK".getBytes(UTF_8));
        tm2.put("method", "TransactionProposalRequest".getBytes(UTF_8));
        tm2.put("result", ":)".getBytes(UTF_8));
        transactionProposalRequest.setTransientMap(tm2);

        Collection<ProposalResponse> transactionProposalResponses = org.getChannel().getChannel().sendTransactionProposal(transactionProposalRequest, org.getChannel().getChannel().getPeers());
        return this.toOrdererResponse(transactionProposalResponses, org);
    }
    /**
     * 查询智能合约
     * @param args 查询参数数组
     * @return
     */
    public Map<String, String> chainCodeQuery(SdkInterfaceOrg org, String fcn, String[] args) throws InvalidArgumentException, ProposalException {
        QueryByChaincodeRequest queryByChaincodeRequest = org.getClient().newQueryProposalRequest();
        queryByChaincodeRequest.setArgs(args);
        queryByChaincodeRequest.setFcn(fcn);
        queryByChaincodeRequest.setChaincodeID(this.chaincodeID);
        queryByChaincodeRequest.setProposalWaitTime(this.proposalWaitTime);

        Map<String, byte[]> tm2 = new HashMap<>();
        tm2.put("HyperLedgerFabric", "QueryByChaincodeRequest:JavaSDK".getBytes(UTF_8));
        tm2.put("method", "QueryByChaincodeRequest".getBytes(UTF_8));
        queryByChaincodeRequest.setTransientMap(tm2);

        Collection<ProposalResponse> queryProposalResponses = org.getChannel().getChannel().queryByChaincode(queryByChaincodeRequest, org.getChannel().getChannel().getPeers());
        return this.toPeerResponse(queryProposalResponses, true);
    }
    /**
     * 获取实例化合约、升级合约以及invoke合约的返回结果集合
     *
     * @param proposalResponses 请求返回集合
     * @param org               中继组织对象
     */
    private Map<String, String> toOrdererResponse(Collection<ProposalResponse> proposalResponses, SdkInterfaceOrg org) throws InvalidArgumentException, UnsupportedEncodingException, InterruptedException, ExecutionException, TimeoutException {
        Map<String, String> resultMap = new HashMap<>();
        Collection<ProposalResponse> successful = new LinkedList<>();
        Collection<ProposalResponse> failed = new LinkedList<>();
        for (ProposalResponse response : proposalResponses) {
            if (response.getStatus() == ProposalResponse.Status.SUCCESS) {
                successful.add(response);
            } else {
                failed.add(response);
            }
        }

        Collection<Set<ProposalResponse>> proposalConsistencySets = SDKUtils.getProposalConsistencySets(proposalResponses);
        if (proposalConsistencySets.size() != 1) {

        }
        if (failed.size() > 0) {
            ProposalResponse firstTransactionProposalResponse = failed.iterator().next();
            resultMap.put("code", "error");
            resultMap.put("data", firstTransactionProposalResponse.getMessage());
            return resultMap;
        } else {
            ProposalResponse resp = proposalResponses.iterator().next();
            byte[] x = resp.getChaincodeActionResponsePayload();
            String resultAsString = null;
            if (x != null) {
                resultAsString = new String(x, "UTF-8");
            }
            org.getChannel().getChannel().sendTransaction(successful).get(transactionWaitTime, TimeUnit.SECONDS);
            resultMap.put("code", "success");
            resultMap.put("data", resultAsString);
            resultMap.put("txid", resp.getTransactionID());
            return resultMap;
        }
    }
    /**
     * 获取安装合约以及query合约的返回结果集合
     *
     * @param proposalResponses 请求返回集合
     * @param checkVerified     是否验证提案
     */
    private Map<String, String> toPeerResponse(Collection<ProposalResponse> proposalResponses, boolean checkVerified) {
        Map<String, String> resultMap = new HashMap<>();
        for (ProposalResponse proposalResponse : proposalResponses) {
            if ((checkVerified && !proposalResponse.isVerified()) || proposalResponse.getStatus() != ProposalResponse.Status.SUCCESS) {
                String data = String.format("Failed install/query proposal from peer %s status: %s. Messages: %s. Was verified : %s",
                        proposalResponse.getPeer().getName(), proposalResponse.getStatus(), proposalResponse.getMessage(), proposalResponse.isVerified());
                resultMap.put("code", "error");
                resultMap.put("data", data);
            } else {
                String payload = proposalResponse.getProposalResponse().getResponse().getPayload().toStringUtf8();
                resultMap.put("code", "success");
                resultMap.put("data", payload);
                resultMap.put("txid", proposalResponse.getTransactionID());
            }
        }
        return resultMap;
    }
}
