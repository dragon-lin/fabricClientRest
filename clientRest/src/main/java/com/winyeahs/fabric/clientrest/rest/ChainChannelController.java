package com.winyeahs.fabric.clientrest.rest;

import com.alibaba.fastjson.JSONObject;
import com.winyeahs.fabric.clientrest.service.ChainBlockService;
import com.winyeahs.fabric.clientrest.service.ChainChannelService;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by linwf on 2018/11/4.
 */
@Controller
@RequestMapping("/chainchannel")
public class ChainChannelController {
    @Resource
    private ChainChannelService chainChannelService;

    /**
     * 创建通道
     * @return
     */
    @RequestMapping(value = "/createChannel", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public String createChannel(){
        return chainChannelService.createChannel();
    }

    /**
     * 节点加入通道
     * @return
     */
    @RequestMapping(value = "/joinPeer", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public String joinPeer(){ //@RequestBody Map<String, Object> map) {
        //JSONObject json = new JSONObject(map);
        //String txId = json.getString("txId");
        return chainChannelService.joinPeer();
    }

}
