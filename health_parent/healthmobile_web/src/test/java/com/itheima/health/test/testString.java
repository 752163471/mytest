package com.itheima.health.test;

import org.junit.Test;

public class testString {

    @Test
    public void testStr(){
        String s1 = "hello";

        if (s1 == "hello"){
            System.out.println("s1=\"hello\"");
        }else {
            System.out.println("s1 !=\"hello\"");
        }
    }
}
