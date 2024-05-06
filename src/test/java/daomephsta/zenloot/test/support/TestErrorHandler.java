package daomephsta.zenloot.test.support;

import daomephsta.zenloot.zenscript.ErrorHandler;


public class TestErrorHandler implements ErrorHandler
{
    public static class ZenLootException extends RuntimeException
    {
        private ZenLootException(String message)
        {
            super(message);
        }
    }

    @Override
    public void error(String message)
    {
        throw new ZenLootException(message);
    }

    @Override
    public void warn(String message)
    {
        throw new ZenLootException(message);
    }
}
