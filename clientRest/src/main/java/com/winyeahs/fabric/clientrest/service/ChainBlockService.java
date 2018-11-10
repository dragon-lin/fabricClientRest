package com.winyeahs.fabric.clientrest.service;

/**
 * Created by linwf on 2018/11/4.
 */
public interface ChainBlockService {
    String queryBlockByTransactionID(String txID);
    String queryBlockByHash(byte[] blockHash);
    String queryBlockByNumber(long blockNumber);
    String queryCurrentBlockInfo();
}
