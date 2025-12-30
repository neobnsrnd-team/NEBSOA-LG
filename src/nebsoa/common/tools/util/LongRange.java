package nebsoa.common.tools.util;


public class LongRange
{

    public LongRange(String range)
        throws InvalidValueException
    {
        parseRange(range);
    }

    public boolean inRange(long val)
    {
        if(ranges == null || ranges.length == 0)
            return true;
        for(int i = 0; i < ranges.length; i++)
        {
            if(ranges[i].length == 1 && val == ranges[i][0])
                return true;
            if(ranges[i].length == 2 && val >= ranges[i][0] && val <= ranges[i][1])
                return true;
        }

        return false;
    }

    public boolean inRange(int val)
    {
        return inRange(val);
    }

    private void parseRange(String range)
        throws InvalidValueException
    {
        String tokens[] = range.split(",");
        ranges = new long[tokens.length][];
        try
        {
            for(int i = 0; i < tokens.length; i++)
            {
                String vals[] = tokens[i].split("-");
                if(vals.length > 2)
                    throw new InvalidValueException("Invalid range format '" + range + "'");
                ranges[i] = new long[vals.length];
                for(int j = 0; j < vals.length; j++)
                    ranges[i][j] = Long.parseLong(vals[j]);

            }

        }
        catch(NumberFormatException nfe)
        {
            throw new InvalidValueException("Invalid range format '" + range + "'");
        }
    }

    private long ranges[][];
}
