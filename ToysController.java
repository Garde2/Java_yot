import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ToysController {
    
    ToysRepository toys_repository;
    Logger logger;

    public ToysController(ToysRepository toys_repository, Logger logger) {

        this.toys_repository = toys_repository;
        this.logger = logger;
    }


    public List<Toys> readAllToys() {

        return toys_repository.getAllToys();
    }

    public void saveToy(Toys toy) throws Exception {

        toys_repository.createToy(toy);
        this.logger.info("Сохраним товар...");
    }

    public Toys readToy(String toyId) throws Exception {

        List<Toys> toys = toys_repository.getAllToys();
        Toys toy = toySearch(toyId, toys);
        this.logger.info("Найдем товары...");
        return toy;
    }

    private static Toys toySearch(String toyId, List<Toys> toys) throws Exception {

        for (Toys toy : toys) {
            if (toy.getId().equals(toyId)) {
                return toy;
            }
        }
        throw new Exception("Товар не найден");
    }

    public void deleteToy(String toyId) {

        List<Toys> toys = toys_repository.getAllToys();
        List<Toys> newToys = new ArrayList<>();

        for (Toys toy : toys) {
            String tempId = toy.getId();
            Integer tempCount = toy.getCount();
            if (tempId.equals(toyId)) {
                if (tempCount > 1) {
                    toy.decCount();
                    newToys.add(toy);
                }
            } else {
                newToys.add(toy);
            }
        }
        toys_repository.saveToys(newToys);
        this.logger.info("Удалить товар");
    }

    public void editToy(String toyId, Toys newToy) throws Exception {

        List<Toys> toys = toys_repository.getAllToys();
        Toys toy = toySearch(toyId, toys);

        toy.setName(newToy.getName());
        toy.setCount(newToy.getCount());
        toy.setDropRate(newToy.getDropRate());
        toys_repository.saveToys(toys);
        this.logger.info("Редактировать товар");
    }

    public void addToy(Toys toy) {

        String toyId = toy.getId();
        List<Toys> toys = toys_repository.getAllToys();
        List<Toys> newToys = new ArrayList<>();
        Boolean added = false;

        for (Toys item : toys) {
            String tempId = item.getId();
            String tempName = item.getName();
            if (tempId.equals(toyId) && tempName.equals(toy.getName())) {
                    item.incCount();
                    newToys.add(item);
                    added = true;

            } else {
                newToys.add(item);
            }
        }
        if (!added) {
            toy.setCount(1);
            newToys.add(toy);
        }
        toys_repository.saveToys(newToys);
        this.logger.info("Удалить товар");
    }
}