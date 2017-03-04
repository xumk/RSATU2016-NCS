package net;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Алексей on 04.02.2017.
 */
public class HemmingNet {
    private static double EPS = 0.1;
    private List<List<Integer>> referenceSamplesMatrix;
    private List<List<Double>> weightingCoefficientMatrix;
    private List<List<Double>> scalesFeedbackMatrix;
    private int numberNeurons;
    private int numberBinarySigns;
    private double threshold;
    private double weightInhibitorySynapse;
    private List<Integer> inputVector;
    private List<Double> errors;

    public HemmingNet() {
        referenceSamplesMatrix = new ArrayList<>();
        weightingCoefficientMatrix = new ArrayList<>();
        scalesFeedbackMatrix = new ArrayList<>();
        errors = new ArrayList<>();
    }

    private void calculateWeightsMatrixNeuronsFirstLayer() {
        List<Double> weightingCoefficient;
        for (List<Integer> referenceSample : referenceSamplesMatrix) {
            weightingCoefficient = new ArrayList<>();
            weightingCoefficient.addAll(
                    referenceSample.stream()
                            .map(this::calculateWeightsCoefficient)
                            .collect(Collectors.toList())
            );
            weightingCoefficientMatrix.add(weightingCoefficient);
        }
    }

    private Double calculateWeightsCoefficient(Integer coefficient) {
        return 0.5D * coefficient;
    }

    private void determineThreshold() {
        threshold = numberBinarySigns / 2D;
    }

    private void determineWeightInhibitorySynapse() {
        weightInhibitorySynapse = 1D / numberNeurons;
    }

    private void calculationScalesFeedbackMatrix() {
        List<Double> scalesFeedback;
        for (int i = 0; i < numberNeurons; i++) {
            scalesFeedback = new ArrayList<>();
            for (int j = 0; j < numberNeurons; j++) {
                scalesFeedback.add(definesValueSynopsis(i, j));
            }
            scalesFeedbackMatrix.add(scalesFeedback);
        }
    }

    private double definesValueSynopsis(int j, int p) {
        return j == p ? 1.0 : -weightInhibitorySynapse;
    }

    private List<Double> calculateFirstState(List<Integer> inputVector) {
        List<Double> result = new ArrayList<>();
        double neuronState;
        for (List<Double> weightingCoefficient : weightingCoefficientMatrix) {
            neuronState = 0;
            for (int i = 0; i < numberBinarySigns; i++) {
                neuronState += weightingCoefficient.get(i) * inputVector.get(i);
            }
            result.add(neuronState + threshold);
        }
        return result;
    }

    private List<Double> calculateOutputVector(List<Double> stateVector) {
        return stateVector.stream().map(this::activationFunction).collect(Collectors.toList());
    }

    private double activationFunction(double parameter) {
        return parameter > threshold ? threshold
                : parameter < 0D ? 0D : parameter;
    }

    private double calculationConditionStabilizationForOutputVector(List<Double> previousVector, List<Double> currentVector) {
        double result = 0D;
        for (int i = 0; i < numberNeurons; i++) {
            result += Math.pow(currentVector.get(i) - previousVector.get(i), 2);
        }
        return result;
    }

    public void teachTheNetwork() {
        // обучаем сеть
        determineThreshold(); // считаем парог
        determineWeightInhibitorySynapse(); // опеределяем абсолютное значение веса каждого ингибиторного синапса
        calculateWeightsMatrixNeuronsFirstLayer();
        calculationScalesFeedbackMatrix();
    }

    public void setNumberNeurons(int numberNeurons) {
        this.numberNeurons = numberNeurons;
    }

    public void setNumberBinarySigns(int numberBinarySigns) {
        this.numberBinarySigns = numberBinarySigns;
    }

    public void addReferenceSample(List<Integer> sample) {
        referenceSamplesMatrix.add(sample);
    }

    public List<List<Integer>> getReferenceSamplesMatrix() {
        return referenceSamplesMatrix;
    }

    public void submitInputVector(List<Integer> inputVector) {
        this.inputVector = inputVector;
    }

    public List<List<Double>> getWeightingCoefficientMatrix() {
        return weightingCoefficientMatrix;
    }

    public List<List<Double>> getScalesFeedbackMatrix() {
        return scalesFeedbackMatrix;
    }

    public Integer runNet() {
        errors.clear();
        // состояния первог слоя
        List<Double> followingVectorState = calculateFirstState(inputVector);
        // выходы для первого слоя
        List<Double> followingOutputVector = calculateOutputVector(followingVectorState);

        List<Double> previousOutputVector;
        double error;
        do {
            previousOutputVector = new ArrayList<>(followingOutputVector);
            followingVectorState = calculateFollowingState(previousOutputVector);
            followingOutputVector = calculateOutputVector(followingVectorState);
            error = calculationConditionStabilizationForOutputVector(previousOutputVector, followingOutputVector);
            errors.add(error);
        } while (error > EPS);
        return getFindVector(followingOutputVector);
    }

    public List<Double> getErrors() {
        return errors;
    }

    private Integer getFindVector(List<Double> followingOutputVector) {
        int findIndex = -1;
        int count = 0;
        for (int i = 0; i < numberNeurons; i++) {
            if (followingOutputVector.get(i) > 0) {
                ++count;
                findIndex = i;
            }
        }
        return count > 1 ? -1 : findIndex;
    }

    private List<Double> calculateFollowingState(List<Double> previousOutputVector) {
        List<Double> followingVectorState = new ArrayList<>();
        double result;
        double sum;
        for (int i = 0; i < numberNeurons; i++) {
            sum = 0;
            for (int j = 0; j < numberNeurons; j++) {
                if (i != j) {
                    sum += previousOutputVector.get(j);
                }
            }
            sum *= weightInhibitorySynapse;
            result = previousOutputVector.get(i) - sum;
            followingVectorState.add(result);
        }
        return followingVectorState;
    }
}
