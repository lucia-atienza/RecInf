public class FilterCarNoNecessary implements IFilter
{
    @Override
    public String execute(String s)
    {
        return s.replaceAll("[\\.,¿;:\\?=!¡<>*+()/\"\']", " ");
    }
}
