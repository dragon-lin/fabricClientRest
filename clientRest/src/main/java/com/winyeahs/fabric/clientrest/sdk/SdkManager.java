package com.winyeahs.fabric.clientrest.sdk;

import com.winyeahs.fabric.clientrest.mapper.FabricConfigMapper;
import com.winyeahs.fabric.clientrest.model.FabricConfigModel;
import com.winyeahs.fabric.clientrest.model.FabricConfigOrdererModel;
import com.winyeahs.fabric.clientrest.model.FabricConfigPeerModel;
import com.winyeahs.fabric.sdkinterface.SdkInterfaceOrg;
import org.apache.log4j.Logger;
import org.hyperledger.fabric.sdk.exception.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Created by linwf on 2018/10/28.
 */
public class SdkManager {
    private static Logger log = Logger.getLogger(SdkManager.class);

    private final SdkInterfaceOrg sdkInterfaceOrg;
    private final FabricConfigMapper fabricConfigMapper;

    private static SdkManager instance;
    public static synchronized SdkManager getInstance(FabricConfigMapper fabricConfigMapper) {
        if (null == instance) {
            instance = new SdkManager(fabricConfigMapper);
        }
        /*
        try {
            instance.sdkInterfaceOrg.initClient();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return instance;
    }
    private SdkManager(FabricConfigMapper fabricConfigMapper) {
        this.sdkInterfaceOrg = new SdkInterfaceOrg();
        this.fabricConfigMapper = fabricConfigMapper;
        //从数据库中读取配置并设置
        this.setOrgCfgFromDb();
    }

    /**
     * 从数据库中读取配置并设置
     */
    public void setOrgCfgFromDb() {
        List<FabricConfigModel> configList =  this.fabricConfigMapper.queryFabricConfig("");
        if (configList != null && configList.size() > 0){
            FabricConfigModel configData = configList.get(0);
            List<FabricConfigOrdererModel> configOrdererList = fabricConfigMapper.queryFabricOrderer(configData.getRow_id());
            List<FabricConfigPeerModel> configPeerList = fabricConfigMapper.queryFabricPeer(configData.getRow_id());

            this.sdkInterfaceOrg.setOrgName(configData.getOrg_name());
            this.sdkInterfaceOrg.setUsername(configData.getUser_name());
            this.sdkInterfaceOrg.setCryptoConfigPath(configData.getCryptoconfig_path());
            this.sdkInterfaceOrg.setChannelArtifactsPath(configData.getChannelartifacts_path());
            this.sdkInterfaceOrg.setOrgMSPID(configData.getOrg_mspid());
            this.sdkInterfaceOrg.setOrgDomain(configData.getOrg_domain());
            for (int i = 0; i < configPeerList.size(); i++) {
                this.sdkInterfaceOrg.addPeer(configPeerList.get(i).getPeer_name(), configPeerList.get(i).getPeer_eventhubname(), configPeerList.get(i).getPeer_location(), configPeerList.get(i).getPeer_eventhublocation(), configPeerList.get(i).getIs_eventlistener());
            }
            this.sdkInterfaceOrg.setOrdererDomain(configData.getOrderer_domain());
            for (int i = 0; i < configOrdererList.size(); i++) {
                this.sdkInterfaceOrg.addOrderer(configOrdererList.get(i).getOrderer_name(), configOrdererList.get(i).getOrderer_location());
            }
            this.sdkInterfaceOrg.setChannel(configData.getChannel_name());
            this.sdkInterfaceOrg.setChaincode(configData.getChaincode_name(), configData.getChaincode_source(), configData.getChaincode_path(), configData.getChaincode_policy(), configData.getChaincode_version(), configData.getProposal_waittime(), configData.getInvoke_waittime());
            this.sdkInterfaceOrg.setOpenTLS(configData.getIs_tls());
            this.sdkInterfaceOrg.setOpenCATLS(configData.getIs_catls());
            try {
                if (!this.sdkInterfaceOrg.inited()) {
                    this.sdkInterfaceOrg.setEventListener(map -> {
                        log.debug(map.get("code"));
                        log.debug(map.get("data"));
                    });
                    this.sdkInterfaceOrg.init();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String DemoMethod() {
        return this.sdkInterfaceOrg.DemoMethod();
    }

    /**
     * 安装智能合约
     * @return
     */
    public Map<String, String> chainCodeInstall() throws InvalidArgumentException, ProposalException, TransactionException, IOException {
        return this.sdkInterfaceOrg.chainCodeInstall();
    }
    /**
     * 实例化智能合约
     * @param args 查询参数数组
     * @return
     */
    public Map<String, String> chainCodeInstantiate(String[] args) throws InterruptedException, InvalidArgumentException, ExecutionException, ChaincodeEndorsementPolicyParseException, TimeoutException, ProposalException, IOException, TransactionException {
        return this.sdkInterfaceOrg.chainCodeInstantiate(args);
    }
    /**
     * 执行智能合约
     * @param args 查询参数数组
     * @return
     */
    public Map<String, String> chainCodeUpgrade(String[] args) throws InterruptedException, InvalidArgumentException, ExecutionException, ChaincodeEndorsementPolicyParseException, TimeoutException, ProposalException, IOException, TransactionException {
        return this.sdkInterfaceOrg.chainCodeUpgrade(args);
    }
    /**
     * 执行智能合约
     * @param args 查询参数数组
     * @return
     */
    public Map<String, String> chainCodeInvoke(String fcn, String[] args) throws ProposalException, InvalidArgumentException, InterruptedException, ExecutionException, TimeoutException, IOException, TransactionException, IllegalAccessException, InvocationTargetException, InstantiationException, CryptoException, NoSuchMethodException, ClassNotFoundException {
        return this.sdkInterfaceOrg.chainCodeInvoke(fcn, args);
    }
    /**
     * 查询智能合约
     * @param args 查询参数数组
     * @return
     */
    public Map<String, String> chainCodeQuery(String fcn, String[] args) throws ProposalException, InvalidArgumentException, TransactionException, IOException {
        return this.sdkInterfaceOrg.chainCodeQuery(fcn, args);
    }

    /**
     * 根据交易Id查询区块数据
     * @param txID 交易Id
     * @return
     */
    public Map<String, String> queryBlockByTransactionID(String txID) throws ProposalException, InvalidArgumentException, IOException, TransactionException {
        return this.sdkInterfaceOrg.queryBlockByTransactionID(txID);
    }
    /**
     * 根据哈希值查询区块数据
     * @param blockHash 哈希值
     * @return
     */
    public Map<String, String> queryBlockByHash(byte[] blockHash) throws ProposalException, InvalidArgumentException, IOException, TransactionException {
        return this.sdkInterfaceOrg.queryBlockByHash(blockHash);
    }
    /**
     * 根据区块Id查询区块数据
     * @param blockNumber 区块ID
     * @return
     */
    public Map<String, String> queryBlockByNumber(long blockNumber) throws ProposalException, InvalidArgumentException, IOException, TransactionException {
        return this.sdkInterfaceOrg.queryBlockByNumber(blockNumber);
    }
    /**
     * 查询区块数据
     * @return
     */
    public Map<String, String> queryCurrentBlockInfo() throws ProposalException, InvalidArgumentException, TransactionException, IOException {
        return this.sdkInterfaceOrg.queryCurrentBlockInfo();
    }

    /**
     * 创建通道
     * @return
     */
    public Map<String, String> createChannel() throws ProposalException, InvalidArgumentException, IOException, TransactionException {
        return this.sdkInterfaceOrg.createChannel();
    }

    /**
     * 节点加入通道
     * @return
     */
    public Map<String, String> joinPeer() throws ProposalException, InvalidArgumentException, TransactionException, IOException {
        return this.sdkInterfaceOrg.joinPeer();
    }
}
