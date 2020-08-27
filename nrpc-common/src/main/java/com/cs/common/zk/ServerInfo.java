package com.cs.common.zk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class ServerInfo {

    private String host;

    private Integer port;

}
