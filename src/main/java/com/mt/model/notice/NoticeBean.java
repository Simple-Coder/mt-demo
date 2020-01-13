package com.mt.model.notice;

import lombok.Data;

/**
 * @ClassName: NoticeBean
 * @Description:
 * @Author: xiedong
 * @Date: 2020/1/13 14:43
 */
@Data
public class NoticeBean {
    private NoticeHeader header;
    private NoticeBody body;
}
