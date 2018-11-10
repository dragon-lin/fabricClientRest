package com.winyeahs.fabric.sdkinterface;

import com.alibaba.fastjson.JSONObject;
import com.winyeahs.fabric.sdkinterface.base.SdkInterfaceBase;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.*;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;

import javax.swing.text.StyledEditorKit;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;


/**
 * Created by linwf on 2018/10/28.
 */
public class SdkInterfaceOrg extends SdkInterfaceBase {
    int SUCCESS = 200;
    int FAIL = 40029;
    int UN_LOGIN = 8;
    // 单个提案请求的超时时间以毫秒为单位
    private int proposalWaitTime = 200000;
    // 事务等待时间以秒为单位
    private int transactionWaitTime = 120;

    private static final Log log = LogFactory.getLog(SdkInterfaceOrg.class);

    // 用户名
    private String username;
    // 用户密码
    private String password;

    // orderer 排序服务器所在根域名，如：example.com
    private String ordererDomain;
    // orderer 排序服务器数组
    private List<SdkInterfaceOrderer> orderers = new LinkedList<>();

    // 组织名称，如：Org1
    private String orgName;
    // 组织ID，如：Org1MSP
    private String orgMSPID;
    // 组织所在根域名，如：org1.example.com
    private String orgDomain;
    // peer 排序服务器数据
    private List<SdkInterfacePeer> peers = new LinkedList<>();

    // 是否开启TLS
    private boolean openTLS;

    // 通道
    private SdkInterfaceChannel channel;

    // 智能合约
    private SdkInterfaceChaincode chaincode;
    /** 事件监听 */
    private EventListener eventListener;

    // channel-artifacts路径
    private String channelArtifactsPath;
    // crypto-config路径
    private String cryptoConfigPath;

    // 组织节点ca名称
    private String caName;
    // 组织节点ca访问地址（http://110.131.116.21:7054）
    private String caLocation;
    // 是否开启CA TLS
    private boolean openCATLS;

    private HFCAClient caClient;
    private HFClient client;

    private Map<String, User> userMap = new HashMap<>();

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public void setOrdererDomain(String ordererDomain) {
        this.ordererDomain = ordererDomain;
    }

    public String getOrdererDomain() {
        return this.ordererDomain;
    }

    public void addOrderer(String name, String location) {
        this.orderers.add(new SdkInterfaceOrderer(name, location));
    }

    public List<SdkInterfaceOrderer> getOrderers() {
        return this.orderers;
    }

    public void setOrgName(String orgName){
        this.orgName = orgName;
    }

    public String getOrgName(){
        return this.orgName;
    }

    public void setOrgMSPID(String orgMSPID){
        this.orgMSPID = orgMSPID;
    }

    public String getOrgMSPID(){
        return this.orgMSPID;
    }

    public void setOrgDomain(String orgDomain){
        this.orgDomain = orgDomain;
    }

    public String getOrgDomain(){
        return this.orgDomain;
    }

    public void addPeer(String peerName, String peerEventHubName, String peerLocation, String peerEventHubLocation, boolean isEventListener) {
        this.peers.add(new SdkInterfacePeer(peerName, peerEventHubName, peerLocation, peerEventHubLocation, isEventListener));
    }

    public List<SdkInterfacePeer> getPeers() {
        return this.peers;
    }

    public void setOpenTLS(boolean openTLS){
        this.openTLS = openTLS;
    }

    public boolean getOpenTLS(){
        return this.openTLS;
    }

    public void setChannel(String channelName){
        SdkInterfaceChannel channel = new SdkInterfaceChannel(channelName, this);
        this.channel = channel;
    }

    public SdkInterfaceChannel getChannel(){
        return this.channel;
    }

    public void setChaincode(String chaincodeName, String chaincodeSource, String chaincodePath, String chaincodePolicy, String chaincodeVersion, int proposalWaitTime, int invokeWaitTime){
        SdkInterfaceChaincode chaincode = new SdkInterfaceChaincode();
        chaincode.setChaincodeName(chaincodeName);
        chaincode.setChaincodeSource(chaincodeSource);
        chaincode.setChaincodePath(chaincodePath);
        chaincode.setChaincodePolicy(chaincodePolicy);
        chaincode.setChaincodeVersion(chaincodeVersion);
        chaincode.setProposalWaitTime(proposalWaitTime);
        chaincode.setTransactionWaitTime(invokeWaitTime);
        this.chaincode = chaincode;
    }

    public SdkInterfaceChaincode getChaincode(){
        return this.chaincode;
    }

    public void setEventListener(EventListener eventListener){
        this.eventListener = eventListener;
    }

    public EventListener getEventListener(){
        return this.eventListener;
    }

    public void setChannelArtifactsPath(String channelArtifactsPath){
        this.channelArtifactsPath = channelArtifactsPath;
    }

    public String getChannelArtifactsPath(){
        return this.channelArtifactsPath;
    }

    public void setCryptoConfigPath(String cryptoConfigPath){
        this.cryptoConfigPath = cryptoConfigPath;
    }

    public String getCryptoConfigPath(){
        return this.cryptoConfigPath;
    }

    public void setCaName(String caName){
        this.caName = caName;
    }

    public String getCaName(){
        return this.caName;
    }

    public void setCaLocation(String caLocation){
        this.caLocation = caLocation;
    }

    public String getCaLocation(){
        return this.caLocation;
    }

    public void setOpenCATLS(boolean openCATLS){
        this.openCATLS = openCATLS;
    }

    public boolean getOpenCATLS(){
        return this.openCATLS;
    }

    public HFClient getClient(){
        return this.client;
    }

    public void addUser(SdkInterfaceUser user) {
        userMap.put(user.getName(), user);
    }

    public User getUser(String name) {
        return userMap.get(name);
    }

    public String DemoMethod() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", SUCCESS);
        jsonObject.put("result", "SdkInterfaceOrg.demoMethod");
        return jsonObject.toString();
    }

    /**
     * 组织初始化
     */
    public void init() throws InvalidArgumentException, TransactionException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException, CryptoException, ClassNotFoundException, IOException {
        if (this.getPeers().size() == 0) {
            throw new RuntimeException("peers is null or peers size is 0");
        }
        if (this.getOrderers().size() == 0) {
            throw new RuntimeException("orderers is null or orderers size is 0");
        }
        if (this.getChaincode() == null) {
            throw new RuntimeException("chaincode must be instantiated");
        }

        // 根据TLS开启状态循环确认Peer节点各服务的请求grpc协议
        for (int i = 0; i < this.getPeers().size(); i++) {
            this.getPeers().get(i).setPeerLocation(super.grpcTLSify(this.getOpenTLS(), this.getPeers().get(i).getPeerLocation()));
            this.getPeers().get(i).setPeerEventHubLocation(super.grpcTLSify(this.getOpenTLS(), this.getPeers().get(i).getPeerEventHubLocation()));
        }
        // 根据TLS开启状态循环确认Orderer节点各服务的请求grpc协议
        for (int i = 0; i < this.getOrderers().size(); i++) {
            this.getOrderers().get(i).setOrdererLocation(super.grpcTLSify(this.getOpenTLS(), this.getOrderers().get(i).getOrdererLocation()));
        }
        // 根据CATLS开启状态循环确认CA服务的请求http协议
        if (this.getCaLocation() != null) {
            this.setCaLocation(super.httpTLSify(this.getOpenCATLS(), this.getCaLocation()));
        }

        File skFile = Paths.get(cryptoConfigPath, "/peerOrganizations/", this.orgDomain, String.format("/users/%s@%s/msp/keystore", "Admin", this.orgDomain)).toFile();
        File certificateFile = Paths.get(cryptoConfigPath, "/peerOrganizations/", this.getOrgDomain(), //add by linwf in 2018-07-09
                String.format("/users/%s@%s/msp/signcerts/cert.pem", "Admin", this.orgDomain)).toFile(); //add by linwf in 2018-07-09
        log.debug("skFile = " + skFile.getAbsolutePath());
        log.debug("certificateFile = " + certificateFile.getAbsolutePath());
        // 一个特殊的用户，可以创建通道，连接对等点，并安装链码
        SdkInterfaceUser user = new SdkInterfaceUser(this.username);
        user.setMspId(this.orgMSPID);
        String certificate = new String(IOUtils.toByteArray(new FileInputStream(certificateFile)), "UTF-8");
        PrivateKey privateKey = super.getPrivateKeyFromBytes(IOUtils.toByteArray(new FileInputStream(super.findFileSk(skFile))));
        user.setEnrollment(new StoreEnrollment(privateKey, certificate));
        //user.saveState();
        addUser(user);

        this.client = HFClient.createNewInstance();
        this.client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
        this.channel.setChannel(this.client);
    }
    /**
     * 自定义注册登记操作类
     *
     */
    protected static final class StoreEnrollment implements Enrollment, Serializable {

        private static final long serialVersionUID = 6965341351799577442L;

        // 私钥
        private final PrivateKey privateKey;
        // 授权证书
        private final String certificate;

        public StoreEnrollment(PrivateKey privateKey, String certificate) {
            this.certificate = certificate;
            this.privateKey = privateKey;
        }

        @Override
        public PrivateKey getKey() {
            return privateKey;
        }

        @Override
        public String getCert() {
            return certificate;
        }
    }
    /**
     * 初始化智能合约
     * @return
     */
    public Map<String, String> chainCodeInstall() throws ProposalException, InvalidArgumentException {
        return this.chaincode.chainCodeInstall(this);
    }
    /**
     * 实例化智能合约
     * @param args 查询参数数组
     * @return
     */
    public Map<String, String> chainCodeInstantiate(String[] args) throws InterruptedException, InvalidArgumentException, ExecutionException, ChaincodeEndorsementPolicyParseException, TimeoutException, ProposalException, IOException {
        return this.chaincode.chainCodeInstantiate(this, args);
    }
    /**
     * 升级智能合约
     * @param args 查询参数数组
     * @return
     */
    public Map<String, String> chainCodeUpgrade(String[] args) throws InterruptedException, InvalidArgumentException, ExecutionException, ChaincodeEndorsementPolicyParseException, TimeoutException, ProposalException, IOException {
        return this.chaincode.chainCodeUpgrade(this, args);
    }
    /**
     * 执行智能合约
     * @param args 查询参数数组
     * @return
     */
    public Map<String, String> chainCodeInvoke(String fcn, String[] args) throws InterruptedException, InvalidArgumentException, UnsupportedEncodingException, ExecutionException, TimeoutException, ProposalException {
        return this.chaincode.chainCodeInvoke(this, fcn, args);
    }

    /**
     * 查询智能合约
     * @param args 查询参数数组
     * @return
     */
    public Map<String, String> chainCodeQuery(String fcn, String[] args) throws ProposalException, InvalidArgumentException {
        return this.chaincode.chainCodeQuery(this, fcn, args);
    }

    /**
     * 根据交易Id查询区块数据
     * @param txID 交易Id
     * @return
     */
    public Map<String, String> queryBlockByTransactionID(String txID){
        try {
            return this.channel.queryBlockByTransactionID(txID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 根据哈希值查询区块数据
     * @param blockHash 交易Id
     * @return
     */
    public Map<String, String> queryBlockByHash(byte[] blockHash){
        try {
            return this.channel.queryBlockByHash(blockHash);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 根据区块Id查询区块数据
     * @param blockNumber 区块ID
     * @return
     */
    public Map<String, String> queryBlockByNumber(long blockNumber){
        try {
            return this.channel.queryBlockByNumber(blockNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 查询当前区块信息
     * @return
     */
    public Map<String, String> queryCurrentBlockInfo(){
        try {
            return this.channel.queryCurrentBlockInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
