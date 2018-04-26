package com.int492.cifarclssfr.imageclassfr.core;

import org.deeplearning4j.api.storage.StatsStorage;
import org.deeplearning4j.datasets.iterator.impl.CifarDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.ConvolutionLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.conf.layers.SubsamplingLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.ui.api.UIServer;
import org.deeplearning4j.ui.stats.StatsListener;
import org.deeplearning4j.ui.storage.InMemoryStatsStorage;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.File;
import java.io.IOException;

public class Cifar10ClassifierTraining {


    public static void main(String[] args) throws IOException {
/*
    ImageUtils.download(true);
    if(ImageUtils.convertBinaryToRGBImage(new File(Paths.get("").toAbsolutePath().toString().concat("/cifar-10-batches-bin/data_batch_1.bin")))){
        System.out.println(":)");
    } else {
        System.out.println("It's failed!");
    }
*/
        final int outputNum = 10;
        final int inputNum = 3;
        final int trainSamples = 60000;
        final int testSamples = 10000;
        final int batchSize = 10; /*100*/
        final int iterations = 2;
        final double learningRate = 0.005; /*0.003*/
        final int seed = 1234;
        int epoch = 20; /*30*/

        long initTime = System.currentTimeMillis();

        CifarDataSetIterator trainDataSetIterator =
                new CifarDataSetIterator(batchSize, trainSamples, true);
        System.out.println(trainDataSetIterator.getLabels());
        CifarDataSetIterator testDataSetIterator =
                new CifarDataSetIterator(batchSize, testSamples, false);
        System.out.println(testDataSetIterator.getLabels());

        ConvolutionLayer layer0 = new ConvolutionLayer.Builder(5, 5)
                .nIn(inputNum)
                .nOut(16)
                .stride(1, 1)
                .padding(2, 2)
                .weightInit(WeightInit.XAVIER)
                .activation(Activation.RELU)
                .build();

        SubsamplingLayer layer1 = new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX)
                .kernelSize(2, 2)
                .stride(2, 2)
                .build();

        ConvolutionLayer layer2 = new ConvolutionLayer.Builder(5, 5)
                .nOut(20)
                .stride(1, 1)
                .padding(2, 2)
                .weightInit(WeightInit.XAVIER)
                .activation(Activation.RELU)
                .build();

        SubsamplingLayer layer3 = new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX)
                .kernelSize(2, 2)
                .stride(2, 2)
                .build();

        ConvolutionLayer layer4 = new ConvolutionLayer.Builder(5, 5)
                .nOut(20)
                .stride(1, 1)
                .padding(2, 2)
                .weightInit(WeightInit.XAVIER)
                .activation(Activation.RELU)
                .build();

        SubsamplingLayer layer5 = new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX)
                .kernelSize(2, 2)
                .stride(2, 2)
                .build();

        OutputLayer layer6 = new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                .activation(Activation.SOFTMAX)
                .weightInit(WeightInit.XAVIER)
                .nOut(outputNum)
                .build();
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(seed)
                .iterations(iterations)
                .learningRate(learningRate)
                .weightInit(WeightInit.XAVIER)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .updater(Updater.RMSPROP)
                .list()
                .layer(0, layer0)
                .layer(1, layer1)
                .layer(2, layer2)
                .layer(3, layer3)
                .layer(4, layer4)
                .layer(5, layer5)
                .layer(6, layer6)
                .pretrain(false)
                .backprop(true)
                .setInputType(InputType.convolutional(32, 32, 3))
                .build();

        MultiLayerNetwork network = new MultiLayerNetwork(conf);

        StatsStorage statsStorage = new InMemoryStatsStorage();
        UIServer uiServer = UIServer.getInstance();
        uiServer.attach(statsStorage);
        network.setListeners(new StatsListener(statsStorage));
        System.out.println("Hyperparameters... \ninput = " + inputNum + " , " +
                "output num = " + outputNum + " , " +
                "batchSize = " + batchSize + " , " +
                "iterations = " + iterations + " , " +
                "learningRate = " + learningRate + " , " +
                "seed = " + seed + " , " +
                "epoch = " + epoch);
        for (; epoch > 0; epoch--) {
            System.out.println("Epoch remains ... " + epoch);
            network.fit(trainDataSetIterator);
        }

        System.out.println("Training time usage (milis) = " + (System.currentTimeMillis() - initTime));
        System.out.println("\nEvaluating...");
        Evaluation evaluation = network.evaluate(testDataSetIterator);
        System.out.println(evaluation.stats());

        uiServer.stop();
        /*if((new Scanner(System.in).nextLine().equals("x")) || true){
            uiServer.stop();
        }*/

        File file = new File("cifar10m".concat(String.valueOf(System.currentTimeMillis()).concat("_" + String.valueOf(evaluation.accuracy()))).concat(".zip"));
        ModelSerializer.writeModel(network, file, true);
    }


}
