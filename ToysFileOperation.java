import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ToysFileOperation {

    private String fileName;
    public Logger logger;

    public ToysFileOperation(String fileName, Logger logger) throws SecurityException, IOException {

        this.logger = logger;

        this.fileName = fileName;

        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.flush();
            this.logger.info("Записано");

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            this.logger.warning(ex.getMessage());
        }
    }

    public List<String> readAllLines() {

        List<String> lines = new ArrayList<>();
        try {
            File file = new File(fileName);
            
            FileReader fr = new FileReader(file);
            
            BufferedReader reader = new BufferedReader(fr);
           
            String line = reader.readLine();
            if (line != null) {
                lines.add(line);
            }
            while (line != null) {
               
                line = reader.readLine();
                if (line != null) {
                    lines.add(line);
                }
            }
            fr.close();

            this.logger.info("Все данные прочитаны");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            this.logger.warning("Файл не найден");

        } catch (IOException e) {
            e.printStackTrace();
            this.logger.warning("Ошибка!");
        }
        return lines;
    }

    public void saveAllLines(List<String> lines) {

        try (FileWriter writer = new FileWriter(fileName, false)) {
            for (String line : lines) {
              
                writer.write(line);
              
                writer.append('\n');
            }
            writer.flush();
            this.logger.info("Все данные сохранены!");
            
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            this.logger.warning(ex.getMessage());
        }
    }
}