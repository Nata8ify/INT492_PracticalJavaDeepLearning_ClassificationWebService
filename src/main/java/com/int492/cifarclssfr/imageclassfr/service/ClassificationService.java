package com.int492.cifarclssfr.imageclassfr.service;

import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class ClassificationService {

    private static final String TRAINED_MODEL_SRC = "cifar10m1524197627132_6014.zip";

    /** Supporting for 32x32 pixels image and 3 color channels (rgb). */
    public String identifyByFile(File imgFile) throws IOException {
        MultiLayerNetwork cifar10Model = ModelSerializer.restoreMultiLayerNetwork(new File(TRAINED_MODEL_SRC), false);
        NativeImageLoader imageLoader = new NativeImageLoader(32, 32, 3);
        INDArray image = imageLoader.asMatrix(imgFile);
        System.out.println("INDArray save form -> "+image);
        INDArray predictedINDArray = cifar10Model.output(image);
        System.out.println(predictedINDArray);
        return predict4ClassLabel(predictedINDArray);
    }

    /** @return index of cifar10's predicted INDArray. */
    private int predict4Index(INDArray predictedINDArray){
        int maxPredictedLabelIndex = 0;
        double maxPredictedValue = 0.0; /* Accuracy percentage. */
        for(int i = 0 ; i < predictedINDArray.columns() /*follwing ccifar10's class */; i++){
            double currentIndexValue = predictedINDArray.getColumn(i).amaxNumber().doubleValue();
            if(maxPredictedValue < currentIndexValue){
                maxPredictedValue = currentIndexValue;
                maxPredictedLabelIndex = i;
            }
        }
        return maxPredictedLabelIndex;
    }

    /** @return label of cifar10 label decode from INDArray index. */
    private String predict4ClassLabel(INDArray predictionINDArray){
        /** Lebels  : [airplane, automobile, bird, cat, deer, dog, frog, horse, ship, truck, ] */
        switch (predict4Index(predictionINDArray)){
            case 0 : return "airplane";
            case 1 : return "automobile";
            case 2 : return "bird";
            case 3 : return "cat";
            case 4 : return "deer";
            case 5 : return "dog";
            case 6 : return "frog";
            case 7 : return "horse";
            case 8 : return "ship";
            case 9 : return "truck";
            default: throw  new IllegalStateException("Nope! Nope! No such case!");
        }
    }
}
