package test.spiderlink.pipe;

import java.io.*;

public class PipeStreamTest
{
    public static void main(String[] args) throws Exception
    {
        FileInputStream fin = new FileInputStream("c:\\예제\\Oper3.java");
        PipedOutputStream pos=new PipedOutputStream();
        PipedInputStream pis=new PipedInputStream(pos);
        
        for (int i; (i=fin.read())!=-1; )
        {
            pos.write(i);
        }
        fin.close();
        System.out.println("----------fin.close--------------------");
        pos.close();
        System.out.println("----------pos.close--------------------");

        System.out.println("----------go to write--------------------");
        int i=0;
        while((i=pis.read())!=-1 )
        {
            System.out.write(i);
        }
        System.out.println("----------now close--------------------");
        
        pis.close();
        System.out.println("----------pis.close--------------------");
    }
}