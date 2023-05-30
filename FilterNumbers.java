public class FilterNumbers implements IFilter
{
    @Override
    public String execute(String s)
    {
            s = s.replaceAll("-+ | -+", " ");
            return s.replaceAll("\\s[0-9]+\\s", " ");
    }
}
