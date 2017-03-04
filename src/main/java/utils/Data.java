package utils;

/**
 * Created by Алексей on 05.02.2017.
 */
public class Data {
    Double value;
    Integer indexValue;
    public Data( Double value, Integer indexValue) {
        this.value = value;
        this.indexValue = indexValue;
    }

    public Integer getIndexValue() {
        return indexValue;
    }

    public Data setIndexValue(Integer indexValue) {
        this.indexValue = indexValue;
        return this;
    }

    public Double getValue() {
        return value;
    }

    public Data setValue(Double value) {
        this.value = value;
        return this;
    }
}
