package com.ruoyi.mailbox.service.handler.server;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UniversalMail {
    private String uid;
    private String fromer;
    private String receiver;
    private String bcc;
    private String cc;
    private String folder;
    private Boolean hasRead;
    private Boolean hasAttachment;
    private Date sendDate;
    private String title;
    private String emlPath;
    private String content;
    private String email;
    private List<UniversalAttachment> attachments;
}
