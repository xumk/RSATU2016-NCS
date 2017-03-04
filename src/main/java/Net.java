import java.util.*;

/**
 * Created by Алексей on 18.01.2017.
 */
class Net {
    List<List<Double>> w;    // весовые коэффициенты (== это образцам)
    int n;    // количество входов сети

    float activation(float v) {
        return v > n ? n : v < 0 ? 0 : v;
    }

    public Net(int numInputs) {
        w = new ArrayList<>();
        n = numInputs;
    }

    int learnImage(List<Double> image) {
        w.add(image);
        return w.size();
    }

    // аналогично recognizeImage, только, судя по алгоритму,
    // можно обойтись только измерением максимального выхода
    // если максимальных > 1, то однозначного ответа сеть не даст
    int recognizeImageFast(List<Double> image) {
        float sumMax = (-1e+37f);  // максимальная сумма на выходе
        int iMax = (-1);        // индекс максимального совпадения образов
        // цикл инициализации сети
        for (int i = 0; i < w.size(); ++i) {
            float sum1 = (0.0f);
            for (int j = 0; j < n; ++j)
                sum1 += image.get(j) * w.get(i).get(j);
            sum1 = activation(0.5f * (sum1 + n));
            if (sum1 > sumMax) {
                sumMax = sum1;
                iMax = i;
            } else {
                if (sum1 == sumMax) {
                    iMax = -1;
                }
            }
        }
        return iMax;
    }

    public static void main(String[] args) {  // входы можно сделать и целочисленными, роли играть не будет
        Net net = new Net(15);
        net.learnImage(Arrays.asList(1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 1.0, 1.0));// символ С
        net.learnImage(Arrays.asList(1., 1., 1., 1., 0., 0., 1., 1., 0., 1., 0., 0., 1., 1., 1.));// Символ E
        net.learnImage(Arrays.asList(1., 1., 1., 1., 0., 1., 1., 0., 1., 1., 0., 1., 1., 1., 1.));// Символ О
        System.out.println(net.recognizeImageFast(Arrays.asList(1., 1., 1., 1., 0., 1., 1., 0., 1., 1., 0., 1., 1., 1., 1.)));// искаженный
    }
}

