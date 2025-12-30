package nebsoa.common.tools.util;

import java.io.IOException;

public class InvalidValueException extends IOException
{

    public InvalidValueException()
    {
    }

    public InvalidValueException(String s)
    {
        super(s);
    }

    static final long serialVersionUID = 0x901a13fcabd5497cL;
}
