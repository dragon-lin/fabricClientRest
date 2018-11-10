package com.winyeahs.fabric.sdkinterface;

import com.winyeahs.fabric.sdkinterface.base.SdkInterfaceBase;
import org.apache.commons.codec.binary.Hex;
import org.hyperledger.fabric.protos.ledger.rwset.kvrwset.KvRwset;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Created by linwf on 2018/10/28.
 */
public class SdkInterfaceChannel extends SdkInterfaceBase {
    private SdkInterfaceOrg org;

    // 通道名称
    private String channelName;
    // 通道
    private Channel channel;

    public SdkInterfaceChannel(String channelName, SdkInterfaceOrg org){
        this.channelName = channelName;
        this.org = org;
    }
    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelName() {
        return this.channelName;
    }

    public void setChannel(HFClient client) throws InvalidArgumentException, TransactionException {
        //设置通道配置
        this.setChannelCfg(client);
    }

    public Channel getChannel() {
        return this.channel;
    }

    /**
     * 设置通道配置
     */
    private void setChannelCfg(HFClient client) throws InvalidArgumentException, TransactionException {
        client.setUserContext(this.org.getUser("Admin"));
        this.channel = client.newChannel(this.channelName);
        int OrderersCount = this.org.getOrderers().size();
        for (int i = 0; i < OrderersCount; i++) {
            Properties ordererProperties = new Properties();
            if (this.org.getOpenTLS()) {
                File ordererCert = Paths.get(this.org.getCryptoConfigPath(), "/ordererOrganizations", this.org.getOrdererDomain(),
                                             "orderers", this.org.getOrderers().get(i).getOrdererName(), "tls/server.crt").toFile();
                if (!ordererCert.exists()) {
                    throw new RuntimeException(
                            String.format("Missing cert file for: %s. Could not find at location: %s", this.org.getOrderers().get(i).getOrdererName(), ordererCert.getAbsolutePath()));
                }
                ordererProperties.setProperty("pemFile", ordererCert.getAbsolutePath());
            }
            ordererProperties.setProperty("hostnameOverride", this.org.getOrderers().get(i).getOrdererName());
            ordererProperties.setProperty("sslProvider", "openSSL");
            ordererProperties.setProperty("negotiationType", "TLS");
            ordererProperties.put("grpc.ManagedChannelBuilderOption.maxInboundMessageSize", 9000000);
            // 设置keepAlive以避免在不活跃的http2连接上超时的例子。在5分钟内，需要对服务器端进行更改，以接受更快的ping速率。
            ordererProperties.put("grpc.NettyChannelBuilderOption.keepAliveTime", new Object[]{5L, TimeUnit.MINUTES});
            ordererProperties.put("grpc.NettyChannelBuilderOption.keepAliveTimeout", new Object[]{8L, TimeUnit.SECONDS});
            ordererProperties.setProperty("ordererWaitTimeMilliSecs", "300000");
            this.channel.addOrderer(
                    client.newOrderer(this.org.getOrderers().get(i).getOrdererName(), this.org.getOrderers().get(i).getOrdererLocation(), ordererProperties));
        }

        int PeersCount = org.getPeers().size();
        for (int i = 0; i < PeersCount; i++) {
            Properties peerProperties = new Properties();
            if (this.org.getOpenTLS()) {
                File peerCert = Paths.get(this.org.getCryptoConfigPath(), "/peerOrganizations", this.org.getOrgDomain(),
                                          "peers", org.getPeers().get(i).getPeerName(), "tls/server.crt").toFile();
                if (!peerCert.exists()) {
                    throw new RuntimeException(String.format("Missing cert file for: %s. Could not find at location: %s", org.getPeers().get(i).getPeerName(), peerCert.getAbsolutePath()));
                }
                peerProperties.setProperty("pemFile", peerCert.getAbsolutePath());
            }
            peerProperties.setProperty("hostnameOverride", org.getPeers().get(i).getPeerName());
            peerProperties.setProperty("sslProvider", "openSSL");
            peerProperties.setProperty("negotiationType", "TLS");
            peerProperties.put("grpc.ManagedChannelBuilderOption.maxInboundMessageSize", 9000000);
            this.channel.addPeer(client.newPeer(this.org.getPeers().get(i).getPeerName(), this.org.getPeers().get(i).getPeerLocation(), peerProperties));
            if (org.getPeers().get(i).getIsEventHub()) {
                this.channel.addEventHub(client.newEventHub(this.org.getPeers().get(i).getPeerEventHubName(), this.org.getPeers().get(i).getPeerEventHubLocation(), peerProperties));
            }
        }

        if (!this.channel.isInitialized()) {
            this.channel.initialize();
        }
        if (null != this.org.getEventListener()) {
            this.channel.registerBlockListener(blockEvent -> {
                try {
                    this.org.getEventListener().received(this.execBlockInfo(blockEvent));
                } catch (Exception e) {
                    e.printStackTrace();
                    this.org.getEventListener().received(getFailFromString(e.getMessage()));
                }
            });
        }
    }

    /**
     * Peer加入频道
     *
     * @param peer 中继节点信息
     */
    Map<String, String> joinPeer(SdkInterfacePeer peer) throws InvalidArgumentException, ProposalException {
        File peerCert = Paths.get(org.getCryptoConfigPath(), "/peerOrganizations", org.getOrgDomain(), "peers", peer.getPeerName(), "tls/server.crt")
                .toFile();
        if (!peerCert.exists()) {
            throw new RuntimeException(String.format("Missing cert file for: %s. Could not find at location: %s", peer.getPeerName(), peerCert.getAbsolutePath()));
        }
        Properties peerProperties = new Properties();
        peerProperties.setProperty("pemFile", peerCert.getAbsolutePath());
        peerProperties.setProperty("hostnameOverride", peer.getPeerName());
        peerProperties.setProperty("sslProvider", "openSSL");
        peerProperties.setProperty("negotiationType", "TLS");
        // 在grpc的NettyChannelBuilder上设置特定选项
        peerProperties.put("grpc.ManagedChannelBuilderOption.maxInboundMessageSize", 9000000);
        Peer fabricPeer = org.getClient().newPeer(peer.getPeerName(), peer.getPeerLocation(), peerProperties);
        for (Peer peerNow : channel.getPeers()) {
            if (peerNow.getUrl().equals(fabricPeer.getUrl())) {
                return getFailFromString("peer has already in channel");
            }
        }
        channel.joinPeer(fabricPeer);
        if (peer.getIsEventHub()) {
            channel.addEventHub(org.getClient().newEventHub(peer.getPeerEventHubName(), peer.getPeerEventHubLocation(), peerProperties));
        }
        return getSuccessFromString("peer join channel success");
    }

    /**
     * 根据交易Id查询区块数据
     *
     * @param txID transactionID
     */
    public Map<String, String> queryBlockByTransactionID(String txID) throws InvalidArgumentException, ProposalException, IOException {
        return this.execBlockInfo(this.channel.queryBlockByTransactionID(txID));
    }

    /**
     * 根据哈希值查询区块数据
     *
     * @param blockHash hash
     */
    public Map<String, String> queryBlockByHash(byte[] blockHash) throws InvalidArgumentException, ProposalException, IOException {
        return this.execBlockInfo(this.channel.queryBlockByHash(blockHash));
    }

    /**
     * 根据区块高度查询区块数据
     *
     * @param blockNumber 区块高度
     */
    public Map<String, String> queryBlockByNumber(long blockNumber) throws InvalidArgumentException, ProposalException, IOException {
        return this.execBlockInfo(this.channel.queryBlockByNumber(blockNumber));
    }

    /**
     * 查询当前区块信息
     */
    public Map<String, String> queryCurrentBlockInfo() throws InvalidArgumentException, ProposalException {
        JSONObject blockchainInfo = new JSONObject();
        blockchainInfo.put("height", channel.queryBlockchainInfo().getHeight());
        blockchainInfo.put("currentBlockHash", Hex.encodeHexString(channel.queryBlockchainInfo().getCurrentBlockHash()));
        blockchainInfo.put("previousBlockHash", Hex.encodeHexString(channel.queryBlockchainInfo().getPreviousBlockHash()));
        return this.getSuccessFromString(blockchainInfo.toString());
    }
    /**
     * 解析区块信息对象
     *
     * @param blockInfo 区块信息对象
     */
    private Map<String, String> execBlockInfo(BlockInfo blockInfo) throws IOException, InvalidArgumentException {
        final long blockNumber = blockInfo.getBlockNumber();
        JSONObject blockJson = new JSONObject();
        blockJson.put("blockNumber", blockNumber);
        blockJson.put("dataHash", Hex.encodeHexString(blockInfo.getDataHash()));
        blockJson.put("previousHashID", Hex.encodeHexString(blockInfo.getPreviousHash()));
        blockJson.put("calculatedBlockHash", Hex.encodeHexString(SDKUtils.calculateBlockHash(this.org.getClient(), blockNumber, blockInfo.getPreviousHash(), blockInfo.getDataHash())));
        blockJson.put("envelopeCount", blockInfo.getEnvelopeCount());
        JSONArray envelopeJsonArray = new JSONArray();
        for (BlockInfo.EnvelopeInfo info : blockInfo.getEnvelopeInfos()) {
            JSONObject envelopeJson = new JSONObject();
            envelopeJson.put("channelId", info.getChannelId());
            envelopeJson.put("transactionID", info.getTransactionID());
            envelopeJson.put("validationCode", info.getValidationCode());
            envelopeJson.put("timestamp", super.parseDateFormat(new Date(info.getTimestamp().getTime())));
            envelopeJson.put("type", info.getType());
            envelopeJson.put("createId", info.getCreator().getId());
            envelopeJson.put("createMSPID", info.getCreator().getMspid());
            envelopeJson.put("isValid", info.isValid());
            envelopeJson.put("nonce", Hex.encodeHexString(info.getNonce()));
            if (info.getType() == BlockInfo.EnvelopeType.TRANSACTION_ENVELOPE) {
                BlockInfo.TransactionEnvelopeInfo txeInfo = (BlockInfo.TransactionEnvelopeInfo) info;
                JSONObject transactionEnvelopeInfoJson = new JSONObject();
                int txCount = txeInfo.getTransactionActionInfoCount();
                transactionEnvelopeInfoJson.put("txCount", txCount);
                transactionEnvelopeInfoJson.put("isValid", txeInfo.isValid());
                transactionEnvelopeInfoJson.put("validationCode", txeInfo.getValidationCode());
                JSONArray transactionActionInfoJsonArray = new JSONArray();
                for (int i = 0; i < txCount; i++) {
                    BlockInfo.TransactionEnvelopeInfo.TransactionActionInfo txInfo = txeInfo.getTransactionActionInfo(i);
                    int endorsementsCount = txInfo.getEndorsementsCount();
                    int chaincodeInputArgsCount = txInfo.getChaincodeInputArgsCount();
                    JSONObject transactionActionInfoJson = new JSONObject();
                    transactionActionInfoJson.put("responseStatus", txInfo.getResponseStatus());
                    transactionActionInfoJson.put("responseMessageString", printableString(new String(txInfo.getResponseMessageBytes(), "UTF-8")));
                    transactionActionInfoJson.put("endorsementsCount", endorsementsCount);
                    transactionActionInfoJson.put("chaincodeInputArgsCount", chaincodeInputArgsCount);
                    transactionActionInfoJson.put("status", txInfo.getProposalResponseStatus());
                    transactionActionInfoJson.put("payload", printableString(new String(txInfo.getProposalResponsePayload(), "UTF-8")));
                    JSONArray endorserInfoJsonArray = new JSONArray();
                    for (int n = 0; n < endorsementsCount; ++n) {
                        BlockInfo.EndorserInfo endorserInfo = txInfo.getEndorsementInfo(n);
                        String signature = Hex.encodeHexString(endorserInfo.getSignature());
                        String id = endorserInfo.getId();
                        String mspId = endorserInfo.getMspid();
                        JSONObject endorserInfoJson = new JSONObject();
                        endorserInfoJson.put("signature", signature);
                        endorserInfoJson.put("id", id);
                        endorserInfoJson.put("mspId", mspId);
                        endorserInfoJsonArray.put(endorserInfoJson);
                    }
                    transactionActionInfoJson.put("endorserInfoArray", endorserInfoJsonArray);
                    JSONArray argJsonArray = new JSONArray();
                    for (int z = 0; z < chaincodeInputArgsCount; ++z) {
                        argJsonArray.put(printableString(new String(txInfo.getChaincodeInputArgs(z), "UTF-8")));
                    }
                    transactionActionInfoJson.put("argArray", argJsonArray);
                    TxReadWriteSetInfo rwsetInfo = txInfo.getTxReadWriteSet();
                    JSONObject rwsetInfoJson = new JSONObject();
                    if (null != rwsetInfo) {
                        int nsRWsetCount = rwsetInfo.getNsRwsetCount();
                        rwsetInfoJson.put("nsRWsetCount", nsRWsetCount);
                        JSONArray nsRwsetInfoJsonArray = new JSONArray();
                        for (TxReadWriteSetInfo.NsRwsetInfo nsRwsetInfo : rwsetInfo.getNsRwsetInfos()) {
                            final String namespace = nsRwsetInfo.getNamespace();
                            KvRwset.KVRWSet rws = nsRwsetInfo.getRwset();
                            JSONObject nsRwsetInfoJson = new JSONObject();

                            JSONArray readJsonArray = new JSONArray();
                            int rs = -1;
                            for (KvRwset.KVRead readList : rws.getReadsList()) {
                                rs++;
                                String key = readList.getKey();
                                long readVersionBlockNum = readList.getVersion().getBlockNum();
                                long readVersionTxNum = readList.getVersion().getTxNum();
                                JSONObject readInfoJson = new JSONObject();
                                readInfoJson.put("namespace", namespace);
                                readInfoJson.put("readSetIndex", rs);
                                readInfoJson.put("key", key);
                                readInfoJson.put("readVersionBlockNum", readVersionBlockNum);
                                readInfoJson.put("readVersionTxNum", readVersionTxNum);
                                readInfoJson.put("version", String.format("[%s : %s]", readVersionBlockNum, readVersionTxNum));
                                readJsonArray.put(readInfoJson);
                            }
                            nsRwsetInfoJson.put("readSet", readJsonArray);

                            JSONArray writeJsonArray = new JSONArray();
                            rs = -1;
                            for (KvRwset.KVWrite writeList : rws.getWritesList()) {
                                rs++;
                                String key = writeList.getKey();
                                String valAsString = printableString(new String(writeList.getValue().toByteArray(), "UTF-8"));
                                JSONObject writeInfoJson = new JSONObject();
                                writeInfoJson.put("namespace", namespace);
                                writeInfoJson.put("writeSetIndex", rs);
                                writeInfoJson.put("key", key);
                                writeInfoJson.put("value", valAsString);
                                writeJsonArray.put(writeInfoJson);
                            }
                            nsRwsetInfoJson.put("writeSet", writeJsonArray);
                            nsRwsetInfoJsonArray.put(nsRwsetInfoJson);
                        }
                        rwsetInfoJson.put("nsRwsetInfoArray", nsRwsetInfoJsonArray);
                    }
                    transactionActionInfoJson.put("rwsetInfo", rwsetInfoJson);
                    transactionActionInfoJsonArray.put(transactionActionInfoJson);
                }
                transactionEnvelopeInfoJson.put("transactionActionInfoArray", transactionActionInfoJsonArray);
                envelopeJson.put("transactionEnvelopeInfo", transactionEnvelopeInfoJson);
            }
            envelopeJsonArray.put(envelopeJson);
        }
        blockJson.put("envelopes", envelopeJsonArray);
        return getSuccessFromString(blockJson.toString());
    }
    private Map<String, String> getSuccessFromString(String data) {
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("code", "success");
        resultMap.put("data", data);
        return resultMap;
    }

    private Map<String, String> getFailFromString(String data) {
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("code", "error");
        resultMap.put("data", data);
        return resultMap;
    }

    private String printableString(final String string) {
        int maxLogStringLength = 64;
        if (string == null || string.length() == 0) {
            return string;
        }
        String ret = string.replaceAll("[^\\p{Print}]", "?");
        ret = ret.substring(0, Math.min(ret.length(), maxLogStringLength)) + (ret.length() > maxLogStringLength ? "..." : "");
        return ret;
    }
}
