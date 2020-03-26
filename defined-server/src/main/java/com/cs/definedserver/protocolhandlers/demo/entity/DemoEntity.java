package com.cs.definedserver.protocolhandlers.demo.entity;

import lombok.Data;
import lombok.ToString;
/**
 * @Author wei
 * @Time 2020/3/16
 * @Description
 */
@Data
@ToString
public class DemoEntity {

    private String deviceCode;

    private String alarmType;

    private String deviceType;

    private String loop;

    private String addressCode;

}
