import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class StringToArray
{
    public ArrayList<String> toArray(String text)
    {
        ArrayList<String> result =  new ArrayList<>(List.of(text.split("\\s")));
        result.removeAll(Arrays.asList("", null));
        return result;
    }
}
