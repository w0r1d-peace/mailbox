package com.ruoyi.mailbox.service.handler.server;

import lombok.Builder;
import lombok.Data;

/**
 * @author tangJM.
 * @date 2021/10/14
 * @description
 */
@Data
@Builder
public class UniversalRecipient {
    private String name;
    private String email;
}
