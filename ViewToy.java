import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Logger;

public class ViewToy {

    private final ControllerToy controllerToy;
    private final ControllerToy controllerPrizeToy;
    private final ControllerToy controllerGivenOutPrizeToy;
    private Logger logger;

    public ViewToy(ControllerToy controllerToy, ControllerToy controllerPrizeToy, ControllerToy controllerGivenOutPrizeToy, 
                    Logger logger) {
        this.controllerToy = controllerToy;
        this.controllerPrizeToy = controllerPrizeToy;
        this.controllerGivenOutPrizeToy = controllerGivenOutPrizeToy;
        this.logger = logger;
    }
    
    public void run() throws Exception{

        while (true){
            listToys(" Все игрушки в магазине: ", controllerToy.readAllToys());
            listToys(" Игрушки в розыгрыше: ", controllerPrizeToy.readAllToys());
            listToys(" Выигранные игрушки: ", controllerGivenOutPrizeToy.readAllToys());

            
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
                    choosePrizeToy();
                    break;

                case "out":
                    giveOutPrizeToy();
                    break;

                case "quit":
                    return;
            }
        }
    }

    private void listToys(String title, List<Toy> listToys) {

        int idLen = 3;
        int nameLen = 40;
        int countLen = 5;
        int dropRateLen = 3;
        int fullLen = idLen + nameLen + countLen + dropRateLen + 13;
        int titleLen = title.length();
        System.out.println("Игрушки:");
        System.out.println(" ".repeat((fullLen-titleLen)/2) + title + "=".repeat((fullLen-titleLen)/2));
        
        System.out.printf("| %3s | %-40s | %5s | %3s |\n", "Id", "Name", "Count", "d/r");
        //System.out.println("_________________________________");
        System.out.println(" ".repeat(fullLen));
        
        for (Toy toy : listToys) {
            String name = toy.getName();
            if (name.length() >= nameLen) {
                name = name.substring(0, nameLen-3) + "___";
            }
            System.out.printf("| %3s | %-40s | %5d | %3d |\n", toy.getId(), name, toy.getCount(), toy.getDropRate());
        }
        //System.out.println("_________________________________");
        System.out.println(" ".repeat(fullLen));

    }

    private String prompt(String message) {

        Scanner scan = new Scanner(System.in);
        //Scanner scan = ViewToy.promt(message);
        System.out.print(message);
        return scan.nextLine();
    }

    private Toy inputToy() {

        String name = prompt("Название игрушки: ");
        String count = prompt("Количество игрушек: ");
        Integer dropRate = Integer.valueOf(prompt("Частота выпадения: "));
        Toy toy = new Toy(name, Integer.valueOf(count));
        toy.setDropRate(dropRate);
        return toy;
    }

    private void createToy() throws Exception {

        controllerToy.saveToy(inputToy());
    }

    private void readToy() throws Exception {

        String toyId = getToyId("Введите Id игрушки: ");

        Toy toy = controllerToy.readToy(toyId);
        System.out.println(toy);
    }

    private void deleteToy() {

        String toyId = getToyId("Введите Id игрушки для удаления: ");
        controllerToy.deleteToy(toyId);
    }

    private void editToy() throws Exception {

        String toyId = getToyId("Введиет Id игрушки для редактирования: ");
        controllerToy.editToy(toyId, inputToy());
        Toy toy = controllerToy.readToy(toyId);
        System.out.println(toy);
    }

    private String getToyId(String message) {

        String readToyId = prompt(message);
        return readToyId;
    }

    public void giveOutPrizeToy() throws Exception {

        String toyId = getToyId("Введиет Id призовой игрушки для выдачи: ");
        Toy toy = controllerPrizeToy.readToy(toyId);
        controllerPrizeToy.deleteToy(toyId);
        controllerGivenOutPrizeToy.addToy(toy);
    }

    public void choosePrizeToy() {

        List<Toy> allToys = controllerToy.readAllToys();
        List<Integer> allDropRates = new ArrayList<>();
        Toy prizeToy = new Toy("", 0);        
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
                prizeToy = allToys.get(i);
                break;
            }
        }
        controllerToy.deleteToy(prizeToy.getId());
        controllerPrizeToy.addToy(prizeToy);

    }
}