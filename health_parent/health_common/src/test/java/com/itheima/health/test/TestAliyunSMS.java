package com.itheima.health.test;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.health.util.SMSUtils;
import com.itheima.health.util.ValidateCodeUtils;
import org.junit.Test;

public class TestAliyunSMS {

    @Test
    public void testSendMessage() throws ClientException {
        Integer code = ValidateCodeUtils.generateValidateCode(6);
        SMSUtils.sendShortMessage("18573343663",String.valueOf(code));
    }
}
