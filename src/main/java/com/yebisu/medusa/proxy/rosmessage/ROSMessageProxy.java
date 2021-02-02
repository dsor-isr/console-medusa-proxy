package com.yebisu.medusa.proxy.rosmessage;

import com.yebisu.medusa.proxy.rosmessage.dto.Content;

public interface ROSMessageProxy {
    Content pingForROSMessageState(String ip);
}
