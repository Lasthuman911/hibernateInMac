package kr.co.aim.nanotrack.generic.exception;

@SuppressWarnings("serial")
public class DuplicateNameSignal extends LocalizedException
{
    public DuplicateNameSignal(String dataKey, String query)
    {
        super(ExceptionKey.Duplicate_Name_Exception, dataKey, query);
    }

    public DuplicateNameSignal(Throwable throwable, String dataKey, String query)
    {
        super(ExceptionKey.Duplicate_Name_Exception, throwable, dataKey, query);
    }

    @SuppressWarnings("unchecked")
    public DuplicateNameSignal(Class serviceClass, String operation, String dataKey, String query)
    {
        super(ExceptionKey.Duplicate_Name_Exception, serviceClass, operation, dataKey, query);
    }

    @SuppressWarnings("unchecked")
    public DuplicateNameSignal(Throwable throwable, Class serviceClass, String operation, String dataKey, String query)
    {
        super(ExceptionKey.Duplicate_Name_Exception, throwable, serviceClass, operation, dataKey, query);
    }
}
