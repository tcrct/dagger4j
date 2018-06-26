package com.dagger4j.db.mongodb.client;


import com.dagger4j.db.DBConnect;
import com.dagger4j.kit.ToolsKit;

import java.util.Arrays;
import java.util.List;

/**
 * @author Created by laotang
 * @date createed in 2018/6/26.
 */
public class MongoConnect extends DBConnect {

    public MongoConnect(String host, int port, String database, String username, String password, String url) {
        super(host, port, database, username, password, url);
    }

    /**
     * 取集群地址<br/>
     * 此时host字符串的格式为： ip:port,ip1:port1,ip2:port2，注意小写逗号分隔
     * @return
     */
    public List<String> getRepliCaset() {
        List<String> repliCaset = null;
        String[] hostArray = host.split(",");
        // 集群必须由两个节点组成
        if(ToolsKit.isNotEmpty(hostArray) && hostArray.length > 1) {
            repliCaset = Arrays.asList(hostArray);
        }
        return repliCaset;
    }
}
