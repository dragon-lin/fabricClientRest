package com.winyeahs.fabric.clientrest.mapper;


import com.winyeahs.fabric.clientrest.model.FabricConfigModel;
import com.winyeahs.fabric.clientrest.model.FabricConfigOrdererModel;
import com.winyeahs.fabric.clientrest.model.FabricConfigPeerModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by linwf on 2018/11/18.
 */
public interface FabricConfigMapper {
    /**
     * 根据联盟Id获取配置
     * @param leagueId 联盟Id
     * @return
     */
    public List<FabricConfigModel> queryFabricConfig(@Param("leagueId") String leagueId);
    /**
     * 添加配置
     * @param fabricConfig 配置
     * @return
     */
    public int addFabricConfig(FabricConfigModel fabricConfig);
    /**
     * 修改配置
     * @param fabricConfig 配置
     * @return
     */
    public int modifyFabricConfig(FabricConfigModel fabricConfig);
    /**
     * 根据配置Id获取排序（orderer）配置
     * @param configId 配置Id
     * @return
     */
    public List<FabricConfigOrdererModel> queryFabricOrderer(@Param("configId") int configId);
    /**
     * 添加orderer配置
     * @param fabricConfigOrderer orderer配置
     * @return
     */
    public int addFabricOrderer(FabricConfigOrdererModel fabricConfigOrderer);
    /**
     * 修改orderer配置
     * @param fabricConfigOrderer orderer配置
     * @return
     */
    public int modifyFabricOrderer(FabricConfigOrdererModel fabricConfigOrderer);
    /**
     * 删除orderer配置
     * @param configId 配置id
     * @return
     */
    public int deleteFabricOrderer(@Param("configId") int configId);
    /**
     * 根据配置Id获取节点（peer）配置
     * @param configId 配置Id
     * @return
     */
    public List<FabricConfigPeerModel> queryFabricPeer(@Param("configId") int configId);
    /**
     * 添加peer配置
     * @param fabricConfigPeer peer配置
     * @return
     */
    public int addFabricPeer(FabricConfigPeerModel fabricConfigPeer);
    /**
     * 修改orderer配置
     * @param fabricConfigPeer peer配置
     * @return
     */
    public int modifyFabricPeer(FabricConfigPeerModel fabricConfigPeer);
    /**
     * 删除peer配置
     * @param configId 配置id
     * @return
     */
    public int deleteFabricPeer(@Param("configId") int configId);
}
