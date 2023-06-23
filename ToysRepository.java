import java.util.ArrayList;
import java.util.List;
//import java.util.Random;

public class ToysRepository {

    ToysMapper Toy_mapper;
    ToysFileOperation fileOperationToy;

    public ToysRepository(ToysFileOperation fileOperationToy, ToysMapper Toy_mapper) {

        this.Toy_mapper = Toy_mapper;
        this.fileOperationToy = fileOperationToy;
    }

    public void createToy(Toys toy) {

        List<Toys> toys = getAllToys();
        int max = 0;

        for (Toys item : toys) {
            int id = Integer.parseInt(item.getId());
            if (max < id){
                max = id;
            }
        }

        int newId = max + 1;
        toy.setId(Integer.toString(newId));
        toys.add(toy);
        saveToys(toys);
    }

    public void saveToys(List<Toys> toys) {

        List<String> lines = new ArrayList<>();

        for (Toys item: toys) {
            lines.add(Toy_mapper.map(item));
        }

        fileOperationToy.saveAllLines(lines);
    }

    public List<Toys> getAllToys() {

        List<String> lines = fileOperationToy.readAllLines();
        List<Toys> toys = new ArrayList<>();
        
        for (String line : lines) {
            toys.add(Toy_mapper.map(line));
        }
        return toys;
    }
}