package com.wumpus;

import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.train.MLTrain;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.back.Backpropagation;

import java.util.ArrayList;
import java.util.List;

public class WumpusAI {
    private BasicNetwork network;
    private List<double[]> inputs = new ArrayList<>();
    private List<double[]> outputs = new ArrayList<>();

    public WumpusAI() {
        network = new BasicNetwork();
        network.addLayer(new BasicLayer(null, true, 5));
        network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 6));
        network.addLayer(new BasicLayer(new ActivationSigmoid(), false, 5));
        network.getStructure().finalizeStructure();
        network.reset();
    }

    public void collectTrainingExample(double[] sensors, int action) {
        double[] output = new double[5];
        output[action] = 1.0;
        inputs.add(sensors.clone());
        outputs.add(output);
    }

    public void trainNetwork() {
        if (inputs.isEmpty()) return;

        double[][] inputArray = new double[inputs.size()][];
        double[][] outputArray = new double[outputs.size()][];
        for (int i = 0; i < inputs.size(); i++) {
            inputArray[i] = inputs.get(i);
            outputArray[i] = outputs.get(i);
        }

        MLDataSet trainingSet = new BasicMLDataSet(inputArray, outputArray);
        MLTrain trainer = new Backpropagation(network, trainingSet);

        int epoch = 0;
        do {
            trainer.iteration();
            epoch++;
        } while (trainer.getError() > 0.01 && epoch < 1000);
        trainer.finishTraining();

        System.out.println("Training finished. Epochs: " + epoch);
    }

    public int chooseAction(double[] sensors) {
        BasicMLData input = new BasicMLData(sensors);
        double[] output = network.compute(input).getData();
        int best = 0;
        for (int i = 1; i < output.length; i++) {
            if (output[i] > output[best]) best = i;
        }
        return best;
    }
}
