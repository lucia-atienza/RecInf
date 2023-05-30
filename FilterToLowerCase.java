public class FilterToLowerCase implements IFilter
{
    @Override
    public String execute(String s)
    {
        return s.toLowerCase();
    }
}
