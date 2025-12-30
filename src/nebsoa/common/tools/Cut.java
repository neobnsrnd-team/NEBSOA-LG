package nebsoa.common.tools;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import nebsoa.common.tools.util.LongRange;

public class Cut
{

    public Cut()
    {
    }

    public static void main(String args[])
        throws Exception
    {
        String delimiter = " ";
        LongRange range = null;
        int i;
        for(i = 0; i < args.length && args[i].startsWith("-"); i++)
        {
            if(args[i].equals("-d"))
            {
                delimiter = args[++i];
                continue;
            }
            if(args[i].equals("-f"))
            {
                range = new LongRange(args[++i]);
                continue;
            }
            if(args[i].equals("-?"))
                usage();
        }

        if(range == null)
            usage();
        InputStream in;
        if(i < args.length)
            in = new FileInputStream(args[i]);
        else
            in = System.in;
        Cut cut = new Cut();
        cut.setDelimiter(delimiter);
        cut.setFields(range);
        cut.setStream(in);
        cut.start();
    }

    private static void usage()
    {
        System.err.println("\nUsage: java " + (nebsoa.common.tools.Cut.class).getName() 
        + " [OPTION]... [FILE]" + "\n" 
        + "\nPrint selected parts of lines from each FILE to standard output." 
        + "\nWith no FILE read standard input." + "\n" 
        + "\n  -d DELIM  use DELIM instead of TAB for field delimiter;" 
        + "\n  -f LIST   output only these fields;" 
        + "\n  -?        display this help and exit;" + "\n" 
        + "\nEach LIST is made up of one range, or many ranges separated" 
        + "\nby commas.  Each range is one of:" + "\n" 
        + "\n  N     N'th field, counted from 1" 
        + "\n  N-M   from N'th to M'th (included) field");
        //System.exit(1);
    }

    private void start()
        throws IOException
    {
        for(String line = null; (line = in.readLine()) != null;)
        {
            String tokens[] = line.split(delimiter);
            boolean first = true;
            for(int i = 0; i < tokens.length; i++)
            {
                if(!fields.inRange(i + 1))
                    continue;
                if(!first)
                    System.out.print(" ");
                System.out.print(tokens[i]);
                first = false;
            }

            System.out.println("");
        }

    }

    private void setStream(InputStream in)
    {
        this.in = new BufferedReader(new InputStreamReader(in));
    }

    private void setFields(LongRange range)
    {
        fields = range;
    }

    private void setDelimiter(String delimiter)
    {
        this.delimiter = delimiter;
    }

    private BufferedReader in;
    private LongRange fields;
    private String delimiter;
}
