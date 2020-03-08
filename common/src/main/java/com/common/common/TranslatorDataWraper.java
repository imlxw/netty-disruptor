package com.common.common;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

@Data
public class TranslatorDataWraper {
    private String msg;
    private ChannelHandlerContext context;
}
