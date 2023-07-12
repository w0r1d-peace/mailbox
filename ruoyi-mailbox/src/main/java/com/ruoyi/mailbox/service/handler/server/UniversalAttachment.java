package com.ruoyi.mailbox.service.handler.server;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tangJM.
 * @date 2021/10/13
 * @description
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UniversalAttachment {
    private String path;
    private String cid;
    private String name;
    private String contentType;
}

