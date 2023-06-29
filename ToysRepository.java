import java.util.ArrayList;
import java.util.List;
//import java.util.Random;

public class ToysRepository {

    ToysMapper toy_mapper;
    ToysFileOperation toys_file_operation;

    public ToysRepository(ToysFileOperation toys_file_operation, ToysMapper toy_mapper) {

        this.toy_mapper = toy_mapper;
        this.toys_file_operation = toys_file_operation;
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
            lines.add(toy_mapper.map(item));
        }

        toys_file_operation.saveAllLines(lines);
    }

    public List<Toys> getAllToys() {

        List<String> lines = toys_file_operation.readAllLines();
        List<Toys> toys = new ArrayList<>();
        
        for (String line : lines) {
            toys.add(toy_mapper.map(line));
        }
        return toys;
    }
}