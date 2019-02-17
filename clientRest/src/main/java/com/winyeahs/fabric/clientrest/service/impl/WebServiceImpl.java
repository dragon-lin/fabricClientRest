package com.winyeahs.fabric.clientrest.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.winyeahs.fabric.clientrest.mapper.FabricConfigMapper;
import com.winyeahs.fabric.clientrest.mapper.UserMapper;
import com.winyeahs.fabric.clientrest.model.FabricConfigModel;
import com.winyeahs.fabric.clientrest.model.FabricConfigOrdererModel;
import com.winyeahs.fabric.clientrest.model.FabricConfigPeerModel;
import com.winyeahs.fabric.clientrest.model.UserModel;
import com.winyeahs.fabric.clientrest.sdk.SdkManager;
import com.winyeahs.fabric.clientrest.service.WebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * Created by linwf on 2018/11/26.
 */
@Service("WebService")
public class WebServiceImpl implements WebService{
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FabricConfigMapper fabricConfigMapper;

    public UserModel getUser(String account, String password){
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        return userMapper.getUser(account, md5Password);
    }
    public int save(JSONObject json){
        int result = -1;
        int configId = 0;
        FabricConfigModel fabricConfig = new FabricConfigModel();
        fabricConfig.setRow_id(json.getInteger("rowId"));
        fabricConfig.setLeague_id(1);
        fabricConfig.setOrg_name(json.getString("orgName"));
        fabricConfig.setUser_name(json.getString("userName"));
        fabricConfig.setCryptoconfig_path(json.getString("cryptoconfigPath"));
        fabricConfig.setChannel_name(json.getString("channelName"));
        fabricConfig.setOrg_mspid(json.getString("orgMspid"));
        fabricConfig.setOrg_domain(json.getString("orgDomain"));
        fabricConfig.setOrderer_domain(json.getString("ordererDomain"));
        fabricConfig.setChannel_name(json.getString("channelName"));
        fabricConfig.setChaincode_name(json.getString("chaincodeName"));
        fabricConfig.setChaincode_source(json.getString("chaincodeSource"));
        fabricConfig.setChaincode_path(json.getString("chaincodePath"));
        fabricConfig.setChaincode_policy(json.getString("chaincodePolicy"));
        fabricConfig.setChaincode_version(json.getString("chaincodeVersion"));
        fabricConfig.setProposal_waittime(json.getInteger("proposalWaittime"));
        fabricConfig.setInvoke_waittime(json.getInteger("invokeWaittime"));
        fabricConfig.setIs_tls(json.getBoolean("isTls"));
        fabricConfig.setIs_catls(json.getBoolean("isCatls"));
        //保存配置
        if (fabricConfig.getRow_id() > 0){
            result = this.fabricConfigMapper.modifyFabricConfig(fabricConfig);
            configId = result;
        }else{
            result = this.fabricConfigMapper.addFabricConfig(fabricConfig);
            configId = fabricConfig.getRow_id();
        }
        //orderer操作
        //删除全部orderer
        this.fabricConfigMapper.deleteFabricOrderer(-1);
        JSONArray arrayJson = json.getJSONArray("ordererList");
        for(int i = 0; i < arrayJson.size(); i++){
            JSONObject ordererItem = arrayJson.getJSONObject(i);
            FabricConfigOrdererModel fabricConfigOrderer = new FabricConfigOrdererModel();
            fabricConfigOrderer.setOrderer_name(ordererItem.getString("ordererName"));
            fabricConfigOrderer.setOrderer_location(ordererItem.getString("ordererLocation"));
            fabricConfigOrderer.setConfig_id(configId);
            //保存orderer
            this.fabricConfigMapper.addFabricOrderer(fabricConfigOrderer);
        }
        //peer操作
        //删除全部peer
        this.fabricConfigMapper.deleteFabricPeer(-1);
        arrayJson = json.getJSONArray("peerList");
        for(int i = 0; i < arrayJson.size(); i++){
            JSONObject peerItem = arrayJson.getJSONObject(i);
            FabricConfigPeerModel fabricConfigPeer = new FabricConfigPeerModel();
            fabricConfigPeer.setPeer_name(peerItem.getString("peerName"));
            fabricConfigPeer.setPeer_eventhubname(peerItem.getString("peerEventHubName"));
            fabricConfigPeer.setPeer_location(peerItem.getString("peerLocation"));
            fabricConfigPeer.setPeer_eventhublocation(peerItem.getString("peerEventHubLocation"));
            fabricConfigPeer.setIs_eventlistener(peerItem.getBoolean("isEventListener"));
            fabricConfigPeer.setConfig_id(configId);
            //保存peer
            this.fabricConfigMapper.addFabricPeer(fabricConfigPeer);
        }
        if (result > 0){
            SdkManager manager = SdkManager.getInstance(fabricConfigMapper);
            manager.setOrgCfgFromDb();
        }
        return result;
    }

    /**
     * 修改密码
     * @param account 帐户
     * @param password 密码
     * @return
     */
    public int modifyPassword(String account, String password){
        String newPassword = password;
        Md5PasswordEncoder passwordEncoder = new Md5PasswordEncoder();
        newPassword = passwordEncoder.encodePassword(newPassword, null);
        return this.userMapper.modifyPassword(account, newPassword);
    }
}
