public class FilterNewLine implements IFilter
{
    @Override
    public String execute(String s)
    {
        return s.replaceAll( "\\r\\n", " ");
    }
}
