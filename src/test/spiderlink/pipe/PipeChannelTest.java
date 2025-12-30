package test.spiderlink.pipe;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.*;
import java.nio.channels.*;

public class PipeChannelTest
{
    public static void main(String[] args) throws Exception
    {
        Pipe pipe=Pipe.open();
        Pipe.SinkChannel sink=pipe.sink();
        Pipe.SourceChannel source=pipe.source();

        DataSender sender=new DataSender(sink);
        DataReceiver receiver=new DataReceiver(source);

        System.out.println("Pipe.SinkChannel 의 validOps() : "+sink.validOps());
        System.out.println("Pipe.SourceChannel 의 validOps() : "+source.validOps());

        sender.start();
        receiver.start();
    }

    static class DataSender extends Thread
    {
        Pipe.SinkChannel sink;
        FileInputStream fin;
        
        FileChannel fch ;
        
        DataSender(Pipe.SinkChannel sink) throws FileNotFoundException
        {
            this.sink=sink;
            fin = new FileInputStream("c:\\예제\\Oper3.java");
            fch = fin.getChannel();
            
        }

        public void run()
        {
            ByteBuffer byteBuf=ByteBuffer.allocate(10);
//            LongBuffer longBuf=byteBuf.asLongBuffer();
            int len=0;
            try {
                while((len=fch.read(byteBuf))!= -1)
                {
//                long time=System.currentTimeMillis();
//              longBuf.put(0, time);
                    
                        System.out.println("file read"+ len+" bytes");
                        byteBuf.flip();
                        System.out.println("buffer flip");
                        sink.write(byteBuf);
                        System.out.println("buffer write to source");
                        byteBuf.clear();
                        System.out.println("buffer clear ok");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally{
                try {
                    sink.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class DataReceiver extends Thread
    {
        Pipe.SourceChannel source;

        DataReceiver(Pipe.SourceChannel source)
        {
            this.source=source;
        }

        public void run()
        {
            ByteBuffer byteBuf=ByteBuffer.allocate(10);
//            LongBuffer longBuf=byteBuf.asLongBuffer();
            int len=0;
            try {
                while((len=source.read(byteBuf)) != -1)
                {
                    System.out.print("데이터 받음 : "+new String(byteBuf.array()));
                    byteBuf.clear();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally{
                try {
                    source.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
