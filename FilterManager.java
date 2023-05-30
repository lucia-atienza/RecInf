import java.util.ArrayList;
import java.util.List;

public class FilterManager
{
    private final List<IFilter> filterSequence = new ArrayList<>();
    public void add(IFilter f)
    {
        filterSequence.add(f);
    }

    public String process(String s)
    {
        for(IFilter f : filterSequence) {
            s = f.execute(s);
        }
        return s;
    }

}
