import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ToysMapper {

    public String map(Toys toy) {

        return String.format("%s,%s,%d,%d", toy.getId(), toy.getName(), toy.getCount(), toy.getDropRate());
    }

    public Toys map(String line) {

        List<String> lines = new ArrayList<String>(Arrays.asList(line.split(",")));
        
        Toys toy = new Toys(lines.get(0), lines.get(1), Integer.parseInt(lines.get(2)), Integer.parseInt(lines.get(3)));
        return toy;
    }
}