package nebsoa.common.tools;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class Tail
{
    private int count;
    private boolean follow;
    private BufferedReader in;
    private RandomAccessFile ra;
    
    public Tail()
    {
        count = 100;
        follow = false;
    }

    public static void main(String args[])
        throws Exception
    {
        tail(new String[]{"-20","/windows/SetupWLD.log"});
    }
        
    public static void tail(String args[])
        throws Exception
    {
        Integer c = null;
        boolean f = false;
        boolean help = false;
        int i;
        for(i = 0; i < args.length && args[i].startsWith("-"); i++)
        {
            String nums[] = args[i].split("[^0-9]+");
            for(int j = 1; j < nums.length; j++)
            {
                if(c != null)
                    throw new Exception("Exceeded Options");
                c = Integer.valueOf(nums[j]);
            }

            f |= args[i].indexOf("f") > 0;
            help |= args[i].indexOf("?") > 0;
        }

        if(help)
            usage();
        InputStream in;
        if(i < args.length)
            in = new FileInputStream(args[i]);
        else
            in = System.in;
        Tail tail = new Tail();
        tail.setCount(c == null ? 10 : c.intValue());
        tail.setFollow(f);
        tail.setStream(in);
        tail.start();
    }

    private static void usage()
    {
        System.err.println("\nUsage: java " + (nebsoa.common.tools.Tail.class).getName()
                + " [OPTION]... [FILE]" + "\n" 
                + "\nPrint the last 10 lines of each FILE to standard output." 
                + "\nWith no FILE read standard input." + "\n" 
                + "\n  -f      output appended data as the file grows;" 
                + "\n  -<n>    output the last N lines, instead of the last 10;" 
                + "\n  -?      display this help and exit;" 
                + "\n" + "\nOptions can put strung together such as tail -20f");
        //System.exit(1);
    }

    private void start()
        throws IOException
    {
        try{
            printLines();
            while(follow) 
            {
                while(!in.ready()) 
                    try
                    {
                        Thread.sleep(1000L);
                    }
                    catch(InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                System.out.println(in.readLine());
            }
        }finally{
            if (in != null){
                try{
                    in.close();
                }catch(Exception e){}
            }
        }
    }

    private void printLines()
        throws IOException
    {
        ArrayList lines = new ArrayList(count);
        int cursor = 0;
        while(in.ready()) 
            if(cursor < count)
                lines.add(cursor++, in.readLine());
            else
                lines.set(cursor++ % count, in.readLine());
        int start = cursor <= count ? 0 : cursor;
        int end = start + Math.min(cursor, count);
        for(int i = start; i < end; i++)
            System.out.println(lines.get(i % count));

        lines = null;
    }

    private void setStream(InputStream in)
    {
        this.in = new BufferedReader(new InputStreamReader(in));
    }

    private void setRandomAccess(RandomAccessFile ra)
    {
        this.ra = ra;
    }

    private void setFollow(boolean f)
    {
        follow = f;
    }

    private void setCount(int i)
    {
        count = i;
    }
}
