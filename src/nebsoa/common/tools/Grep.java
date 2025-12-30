package nebsoa.common.tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.regex.Pattern;

public class Grep
{
    private PrintStream out;
    private BufferedReader in;
    private String regex;
    
    public Grep(String pattern, String file, PrintStream writer) 
        throws FileNotFoundException {
        this.regex=pattern;
        in = new BufferedReader(new FileReader(file));
        if(writer==null){
            writer = System.out;
        }
        out = writer;
    }
    
    public Grep(String pattern, String file) 
        throws FileNotFoundException  {
        this (pattern, file, null);
    }

    private static void usage()
    {
        System.err.println("\nUsage: java " + (nebsoa.common.tools.Grep.class).getName()
                + " PATTERN [FILE]" + "\n" 
                + "\nSearch for PATTERN in each FILE or standard input." 
                + "\nPATTERN can be regular expression recognized by " 
                + "\nthe class java.util.regexp.Pattern.");
        //System.exit(2);
    }

    private void start() throws IOException {
        try{
            Pattern p = Pattern.compile(regex);
            String line = null;
            do
            {
                if((line = in.readLine()) == null)
                    break;
                if(p.matcher(line).find())
                    System.out.println(line);
            } while(true);
        }finally{
            if (in != null){
                try{
                    in.close();
                }catch(Exception e){}
            }
        }
    }
    
    public static void main(String args[])
    throws Exception
    {
//        if(args.length <2){
//            usage();
//            return;
//        }
//        String regex = args[0];
//        Grep grep = new Grep(regex,args[1]);
        Grep grep = new Grep("Adap","/windows/SetupWLD.log");
        grep.start();
    }
}
