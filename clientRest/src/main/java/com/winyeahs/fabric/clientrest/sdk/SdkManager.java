package com.winyeahs.fabric.clientrest.sdk;

import com.winyeahs.fabric.sdkinterface.SdkInterfaceOrg;
import org.hyperledger.fabric.sdk.exception.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Created by linwf on 2018/10/28.
 */
public class SdkManager {

    private SdkInterfaceOrg sdkInterfaceOrg;

    private static SdkManager instance;
    public static synchronized SdkManager getInstance() {
        if (null == instance) {
            instance = new SdkManager();
        }
        return instance;
    }
    private SdkManager() {
        this.sdkInterfaceOrg = new SdkInterfaceOrg();
        //从数据库中读取配置并设置
        this.setOrgCfgFromDb();
    }

    /**
     * 从数据库中读取配置并设置
     */
    private void setOrgCfgFromDb() {
        //从数据库中读取配置 TODO
        this.sdkInterfaceOrg.setOrgName("Org1");
        this.sdkInterfaceOrg.setUsername("Admin");
        //this.sdkInterfaceOrg.setCryptoConfigPath("C:\\linwf\\开发\\java开发\\fabricClientRest\\clientRest\\src\\main\\resources\\fabric\\kafkapeer\\crypto-config");
        //this.sdkInterfaceOrg.setChannelArtifactsPath("C:\\linwf\\开发\\java开发\\fabricClientRest\\clientRest\\src\\main\\resources\\fabric\\kafkapeer\\channel-artifacts");
        this.sdkInterfaceOrg.setCryptoConfigPath("crypto-config");
        this.sdkInterfaceOrg.setChannelArtifactsPath("channel-artifacts");
        this.sdkInterfaceOrg.setOrgMSPID("Org1MSP");
        this.sdkInterfaceOrg.setOrgDomain("org1.example.com");
        this.sdkInterfaceOrg.addPeer("peer0.org1.example.com", "peer0.org1.example.com", "grpc://192.168.235.7:7051", "grpc://192.168.235.7:7053", true);
        this.sdkInterfaceOrg.setOrdererDomain("example.com");
        this.sdkInterfaceOrg.addOrderer("orderer0.example.com", "grpc://192.168.235.3:7050");
        this.sdkInterfaceOrg.addOrderer("orderer1.example.com", "grpc://192.168.235.4:7050");
        this.sdkInterfaceOrg.addOrderer("orderer2.example.com", "grpc://192.168.235.5:7050");
        this.sdkInterfaceOrg.setChannel("mychannel");
        this.sdkInterfaceOrg.setChaincode("mycc", "/opt/gopath", "github.com/hyperledger/fabric/kafkapeer/chaincode/go/example02", "chaincodeendorsementpolicy.yaml", "1.0", 90000, 120);
        this.sdkInterfaceOrg.setOpenTLS(true);
        this.sdkInterfaceOrg.setOpenCATLS(false);
        try {
            this.sdkInterfaceOrg.init();
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        } catch (TransactionException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (CryptoException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String DemoMethod() {
        return this.sdkInterfaceOrg.DemoMethod();
    }

    /**
     * 安装智能合约
     * @return
     */
    public Map<String, String> chainCodeInstall() throws InvalidArgumentException, ProposalException {
        return this.sdkInterfaceOrg.chainCodeInstall();
    }
    /**
     * 实例化智能合约
     * @param args 查询参数数组
     * @return
     */
    public Map<String, String> chainCodeInstantiate(String[] args) throws InterruptedException, InvalidArgumentException, ExecutionException, ChaincodeEndorsementPolicyParseException, TimeoutException, ProposalException, IOException {
        return this.sdkInterfaceOrg.chainCodeInstantiate(args);
    }
    /**
     * 执行智能合约
     * @param args 查询参数数组
     * @return
     */
    public Map<String, String> chainCodeUpgrade(String[] args) throws InterruptedException, InvalidArgumentException, ExecutionException, ChaincodeEndorsementPolicyParseException, TimeoutException, ProposalException, IOException {
        return this.sdkInterfaceOrg.chainCodeUpgrade(args);
    }
    /**
     * 执行智能合约
     * @param args 查询参数数组
     * @return
     */
    public Map<String, String> chainCodeInvoke(String fcn, String[] args) throws ProposalException, InvalidArgumentException, InterruptedException, ExecutionException, TimeoutException, UnsupportedEncodingException {
        return this.sdkInterfaceOrg.chainCodeInvoke(fcn, args);
    }
    /**
     * 查询智能合约
     * @param args 查询参数数组
     * @return
     */
    public Map<String, String> chainCodeQuery(String fcn, String[] args) throws ProposalException, InvalidArgumentException {
        return this.sdkInterfaceOrg.chainCodeQuery(fcn, args);
    }

    /**
     * 根据交易Id查询区块数据
     * @param txID 交易Id
     * @return
     */
    public Map<String, String> queryBlockByTransactionID(String txID) throws ProposalException, InvalidArgumentException {
        return this.sdkInterfaceOrg.queryBlockByTransactionID(txID);
    }
    /**
     * 根据哈希值查询区块数据
     * @param blockHash 哈希值
     * @return
     */
    public Map<String, String> queryBlockByHash(byte[] blockHash) throws ProposalException, InvalidArgumentException {
        return this.sdkInterfaceOrg.queryBlockByHash(blockHash);
    }
    /**
     * 根据区块Id查询区块数据
     * @param blockNumber 区块ID
     * @return
     */
    public Map<String, String> queryBlockByNumber(long blockNumber) throws ProposalException, InvalidArgumentException {
        return this.sdkInterfaceOrg.queryBlockByNumber(blockNumber);
    }
    /**
     * 查询区块数据
     * @return
     */
    public Map<String, String> queryCurrentBlockInfo() throws ProposalException, InvalidArgumentException {
        return this.sdkInterfaceOrg.queryCurrentBlockInfo();
    }
}
