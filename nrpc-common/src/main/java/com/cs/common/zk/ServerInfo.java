package com.cs.common.zk;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ServerInfo {

    private String host;

    private Integer port;

}
