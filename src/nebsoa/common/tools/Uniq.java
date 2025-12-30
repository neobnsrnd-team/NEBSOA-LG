package nebsoa.common.tools;

import java.io.*;

public class Uniq
{

    private BufferedReader in;

    public Uniq(String fileName) throws FileNotFoundException{
       this (new FileReader(fileName));
    }
    
    public Uniq(Reader reader)
    {
        if(reader == null){
            throw new NullPointerException("Reader is null");
        }
        in = new BufferedReader(reader);
    }


    public static void main(String args[])
        throws Exception
    {
        Uniq uniq = new Uniq("/windows/SetupWLD.log");
        uniq.start();
    }

    private static void usage()
    {
        System.err.println("\nUsage: java " + (nebsoa.common.tools.Uniq.class).getName() + " [FILE]" + "\n" + "\nDiscard all but one of successive identical lines from FILE" + "\nor standard input, writing to standard output.");
        //System.exit(2);
    }

    private void start()
        throws IOException
    {
        String line = null;
        String lastLine = null;
        do
        {
            if((line = in.readLine()) == null)
                break;
            if(!line.equals(lastLine))
            {
                System.out.println(line);
                lastLine = line;
            }
        } while(true);
    }

}
