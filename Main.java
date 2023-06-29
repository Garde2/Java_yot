import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Main {

    public static Logger logger = Logger.getLogger(Main.class.getName());

        public static void main(String[] args) throws Exception {

        LogManager.getLogManager().readConfiguration(Main.class.getResourceAsStream("logging.properties"));
        logger.info("Leggo!");

        ToysFileOperation toys_file_operation = new ToysFileOperation("toys.txt", logger);
        ToysFileOperation toys_in_game_file_operation = new ToysFileOperation("ToysInGame.txt", logger);
        ToysFileOperation toysNotinGamefileoperation = new ToysFileOperation("ToysNotinGame.txt", logger);

        ToysRepository toys_repository = new ToysRepository(toys_file_operation, new ToysMapper());
        ToysRepository toysinGame_repository = new ToysRepository(toys_in_game_file_operation, new ToysMapper());
        ToysRepository toysNotinGame_repository = new ToysRepository(toysNotinGamefileoperation, new ToysMapper());

        ToysController toys_controller = new ToysController(toys_repository, logger);
        ToysController toys_in_game = new ToysController(toysinGame_repository, logger);
        ToysController toys_not_in_game = new ToysController(toysNotinGame_repository, logger);
        
        ToysView view = new ToysView(toys_controller, toys_in_game, toys_not_in_game, logger);
        view.run();

    }
}