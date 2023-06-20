public class Toy {
    String id;
    String name;
    Integer count;
    Integer dropRate;
    

    public Toy(String id, String name, Integer count, Integer dropRate) {

        this.id = id;
        this.name = name;
        this.count = count; //количество игрушек
        this.dropRate = dropRate; //частота выпадения 0-100
    }

    public Toy(String name, Integer count) {

        this.name = name;
        this.count = count;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setDropRate(Integer dropRate) {
        this.dropRate = dropRate;
    }
  
    public void decCount() {                //убрать из розыгрыша 1
        this.count -= 1;
    }

    public void decCount(Integer decCount) {
        this.count -= decCount;
    }
   
    public void incCount() {
        this.count += 1;
    }

    public void incCount(Integer incCount) {   //больше игрушек
        this.count += incCount;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Integer getCount() {
        return this.count;
    }

    public Integer getDropRate() {
        return this.dropRate;
    }

    @Override
    public String toString() {

        return String.format("Id: %s, название: %s, количество: %s, частота выпадения: %s.\n", 
                            this.id, this.name, this.count, this.dropRate);
    }
}