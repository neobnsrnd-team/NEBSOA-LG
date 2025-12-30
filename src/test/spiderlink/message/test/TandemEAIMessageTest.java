package test.spiderlink.message.test;

import test.spiderlink.message.TandemEAIMessage;

public class TandemEAIMessageTest {
    public static void main(String[] args) throws Exception{
        TandemEAIMessage msg = new TandemEAIMessage();
        String data = "test_test_test_test";
        msg.setData(data.getBytes());
        byte[] msgData = msg.marshall(); 
        System.out.println(new String(msgData));
        //msg.unmarshall(msgData);
        //System.out.println(msg.toString());
        
        ///////////////
        String sample="00000006010000000334NKES  20060620IB01            200606201020082040000002    IB00010000                             000000000000000200606202006062010200820                   DEIRT004101                                      000000000000000000000000000000000000000000 IB6400369  000000000000000100000000000000000000000000000000000000        E10099              ELB02003000001000   20060620102008400000022006062005EI4000000205050654480000000103313025574   0000000100000000　　　　　　　　　　　　　　　　　　　　0200                                611016563438    12345678901234567890123456789012345678";
        msg.unmarshall(sample.getBytes());
        System.out.println(msg.toString());
    }
}
