package com.ruoyi.mailbox.service.handler.server;

import com.ruoyi.common.enums.ProxyTypeEnum;
import lombok.*;

/**
 * @author tangJM.
 * @date 2021/10/14
 * @description
 */
@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MailConnCfg {
    private String email;
    private String password;
    private String host;
    private Integer port;
    private boolean ssl;
    private ProxyTypeEnum proxyType;
    private String proxyHost;
    private Integer proxyPort;
    private String proxyUsername;
    private String proxyPassword;
    private String socksProxyHost;
    private Integer socksProxyPort;
}
