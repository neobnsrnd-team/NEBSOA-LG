package nebsoa.common.tools;

import java.io.*;
import java.util.*;

public class Sort
{
    private BufferedReader in;

    public Sort(String fileName) throws FileNotFoundException{
       this (new FileReader(fileName));
    }
    
    public Sort(Reader reader)
    {
        if(reader == null){
            throw new NullPointerException("Reader is null");
        }
        in = new BufferedReader(reader);
    }


    public static void main(String args[])
        throws Exception
    {
        Sort sort = new Sort("/windows/SetupWLD.log");
        sort.start();
    }

    private static void usage()
    {
        System.err.println("\nUsage: java " + (nebsoa.common.tools.Sort.class).getName() + " [FILE]" + "\n" + "\nWrite sorted contents of FILE or standard input to standard output.");
//        System.exit(2);
    }

    private void start()
        throws IOException
    {

        try{
            TreeMap lines = new TreeMap();
            for(String line = null; (line = in.readLine()) != null;)
            {
                int i[] = (int[])(int[])lines.get(line);
                if(i == null)
                {
                    i = new int[1];
                    i[0] = 0;
                    lines.put(line, i);
                }
                i[0]++;
            }
    
            for(Iterator it = lines.keySet().iterator(); it.hasNext();)
            {
                String l = (String)it.next();
                int count[] = (int[])(int[])lines.get(l);
                int i = 0;
                while(i < count[0]) 
                {
                    System.out.println(l);
                    i++;
                }
            }
        }finally{
            if (in != null){
                try{
                    in.close();
                }catch(Exception e){}
            }
        }

    }

}
