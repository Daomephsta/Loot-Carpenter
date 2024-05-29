package daomephsta.loot_carpenter.test.support;

import daomephsta.loot_carpenter.zenscript.ErrorHandler;


public class TestErrorHandler implements ErrorHandler
{
    public static class LootCarpenterException extends RuntimeException
    {
        private LootCarpenterException(String message)
        {
            super(message);
        }
    }

    @Override
    public void error(String message)
    {
        throw new LootCarpenterException(message);
    }

    @Override
    public void warn(String message)
    {
        throw new LootCarpenterException(message);
    }
}
