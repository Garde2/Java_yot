import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Logger;

public class ToysView {

    private final ToysController toys_controller;
    private final ToysController toys_in_game;
    private final ToysController toys_not_in_game;
    private Logger logger;

    public ToysView(ToysController toys_controller, ToysController toys_in_game, ToysController toys_not_in_game, 
                    Logger logger) {
        this.toys_controller = toys_controller;
        this.toys_in_game = toys_in_game;
        this.toys_not_in_game = toys_not_in_game;
        this.logger = logger;
    }
    
    public void run() throws Exception{

        while (true){
            listToys(" Все игрушки в магазине: ", toys_controller.readAllToys());
            listToys(" Игрушки в розыгрыше: ", toys_in_game.readAllToys());
            listToys(" Выигранные игрушки: ", toys_not_in_game.readAllToys());

            
            System.out.println("Выберите действие!");
            String cmd = prompt("new - Новый товар,\nedit - Редактировать товар, \nread - Найти товар, \ndelete - Удалить товар, \nadd - Выбрать призовую, \nout - Убрать призовой, \nquit - Выход из приложения, \n: ");
            this.logger.info("Enter command: " + cmd);

            switch (cmd) {
                case "new":
                    createToy();
                    break;               
                
                case "edit":
                    try {
                        editToy();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        this.logger.warning(e.getMessage());
                    }
                    break;

                case "read":
                    try {
                        readToy();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        this.logger.warning(e.getMessage());
                    }
                    break;

                case "delete":
                    try {
                        deleteToy();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        this.logger.warning(e.getMessage());
                    }
                    break;
                case "add":
                    chooseToyinGame();
                    break;

                case "out":
                    toyinGame();
                    break;

                case "quit":
                    return;
            }
        }
    }

    private void listToys(String title, List<Toys> listToys) {

        int idLen = 3;
        int nameLen = 40;
        int countLen = 5;
        int dropRateLen = 3;
        int fullLen = idLen + nameLen + countLen + dropRateLen + 13;
        int titleLen = title.length();
        System.out.println("Игрушки:");
        System.out.println(" ".repeat((fullLen-titleLen)/2) + title + "=".repeat((fullLen-titleLen)/2));
        
        System.out.printf("| %3s | %-40s | %5s | %3s |\n", "Id", "Name", "Count", "d/r");
        System.out.println("_________________________________");
        //System.out.println(" ".repeat(fullLen));
        
        for (Toys toy : listToys) {
            String name = toy.getName();
            if (name.length() >= nameLen) {
                name = name.substring(0, nameLen-3) + "___";
            }
            System.out.printf("| %3s | %-40s | %5d | %3d |\n", toy.getId(), name, toy.getCount(), toy.getDropRate());
        }
        System.out.println("_________________________________");
        //System.out.println(" ".repeat(fullLen));

    }

    private String prompt(String message) {

        Scanner scan = new Scanner(System.in);
        //Scanner scan = ViewToy.promt(message);
        System.out.print(message);
        return scan.nextLine();
    }

    private Toys inputToy() {

        String name = prompt("Название игрушки: ");
        String count = prompt("Количество игрушек: ");
        Integer dropRate = Integer.valueOf(prompt("Частота выпадения: "));
        Toys toys = new Toys(name, Integer.valueOf(count));
        toys.setDropRate(dropRate);
        return toys;
    }

    private void createToy() throws Exception {

        toys_controller.saveToy(inputToy());
    }

    private void readToy() throws Exception {

        String toysId = getToyId("Введите Id игрушки: ");

        Toys toys = toys_controller.readToy(toysId);
        System.out.println(toys);
    }
   
    private void editToy() throws Exception {

        String toysId = getToyId("Введиет Id игрушки для редактирования: ");
        toys_controller.editToy(toysId, inputToy());
        Toys toys = toys_controller.readToy(toysId);
        System.out.println(toys);
    }

    private String getToyId(String message) {

        String readToysId = prompt(message);
        return readToysId;
    }

    private void deleteToy() {

        String toysId = getToyId("Введите Id игрушки для удаления: ");
        toys_controller.deleteToy(toysId);
    }


    public void toyinGame() throws Exception {

        String toysId = getToyId("Введиет Id призовой игрушки для выдачи: ");
        Toys toys = toys_in_game.readToy(toysId);
        toys_in_game.deleteToy(toysId);
        toys_not_in_game.addToy(toys);
    }

    public void chooseToyinGame() {

        List<Toys> allToys = toys_controller.readAllToys();
        List<Integer> allDropRates = new ArrayList<>();
        Toys toyingame = new Toys("", 0);        
        Integer summDropRates = 0;

        for (int i = 0; i < allToys.size(); i++) {
            Integer dropRate = allToys.get(i).getDropRate();
            summDropRates += dropRate;
            allDropRates.add(dropRate);
        }

        List<Double> normalized = new ArrayList<>();

        for (int i = 0; i < allDropRates.size(); i++) {
            normalized.add((double) allDropRates.get(i) / (double) summDropRates);
        }

        List<Double> accumulated = new ArrayList<>();
        Double acc = 0d;

        for (int i = 0; i < normalized.size(); i++) {
            acc += normalized.get(i);
            accumulated.add(acc);
        }

        accumulated.set(accumulated.size()-1, 1d);
        Double rand = new Random().nextDouble();

        for (int i = 0; i < accumulated.size(); i++) {
            if (rand <= accumulated.get(i)) {
                toyingame = allToys.get(i);
                break;
            }
        }
        toys_controller.deleteToy(toyingame.getId());
        toys_in_game.addToy(toyingame);

    }
}