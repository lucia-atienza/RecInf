public class FilterDoubleSpace implements IFilter
{
    @Override
    public String execute(String s)
    {
        return s.replaceAll( "\\s+", " ");
    }
}
