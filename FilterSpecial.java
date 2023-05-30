public class FilterSpecial implements IFilter
{
    @Override
    public String execute(String s)
    {
        return s.replaceAll("(\\s-|-\\s)", " ");
    }
}
