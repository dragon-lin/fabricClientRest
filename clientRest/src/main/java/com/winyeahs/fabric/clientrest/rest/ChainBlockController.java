package com.winyeahs.fabric.clientrest.rest;

import com.alibaba.fastjson.JSONObject;
import com.winyeahs.fabric.clientrest.service.ChainBlockService;
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
@RequestMapping("/chainblock")
public class ChainBlockController {
    @Resource
    private ChainBlockService chainBlockService;

    /**
     * 根据交易Id查询区块数据
     * @param map
     * @return
     */
    @RequestMapping(value = "/queryBlockByTransactionID", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public String queryBlockByTransactionID(@RequestBody Map<String, Object> map) {
        JSONObject json = new JSONObject(map);
        String txId = json.getString("txId");
        return chainBlockService.queryBlockByTransactionID(txId);
    }

    /**
     * 根据哈希值查询区块数据
     * @param map
     * @return
     */
    @RequestMapping(value = "/queryBlockByHash", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public String queryBlockByHash(@RequestBody Map<String, Object> map) {
        JSONObject json = new JSONObject(map);
        String hash = json.getString("hash");
        try {
            return chainBlockService.queryBlockByHash(Hex.decodeHex(hash.toCharArray()));
        } catch (DecoderException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据区块高度查询区块数据
     * @param map
     * @return
     */
    @RequestMapping(value = "/queryBlockByNumber", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public String queryBlockByNumber(@RequestBody Map<String, Object> map) {
        JSONObject json = new JSONObject(map);
        String blockNumber = json.getString("blockNumber");
        return chainBlockService.queryBlockByNumber(Long.valueOf(blockNumber));
    }

    /**
     * 查询当前区块信息
     * @return
     */
    @RequestMapping(value = "/queryBlockchainInfo", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public String queryCurrentBlockInfo() {
        return chainBlockService.queryCurrentBlockInfo();
    }
}
