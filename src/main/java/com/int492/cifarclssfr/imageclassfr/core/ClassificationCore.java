package com.int492.cifarclssfr.imageclassfr.core;

import com.int492.cifarclssfr.imageclassfr.model.PredictionModel;
import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ClassificationCore {

    private static final String TRAINED_MODEL_SRC = "cifar10m1524197627132_6014.zip";

    /** Supporting for 32x32 pixels image and 3 color channels (rgb). */
    public static PredictionModel identifyByFile(File imgFile) throws IOException {
        MultiLayerNetwork cifar10Model = ModelSerializer.restoreMultiLayerNetwork(new File(TRAINED_MODEL_SRC), false);
        NativeImageLoader imageLoader = new NativeImageLoader(32, 32, 3);
        INDArray image = imageLoader.asMatrix(imgFile);
        //System.out.println("INDArray save form -> "+image);
        INDArray predictedINDArray = cifar10Model.output(image);
        //System.out.println(predictedINDArray);
        return predict4FullPredictionModel(predictedINDArray);
    }

   /* *//** @return prediction detail from predict set *//*
    public static LinkedHashMap<String ,String> identifyByFilePath(LinkedHashMap<String, String> toPredictedMap){
        toPredictedMap.forEach((sessId, path) -> {
            System.out.println(sessId + " : " + path);
            *//*try {
                File imageFile = new File(path);
                path = identifyByFile(imageFile).toString();
                imageFile.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }*//*
            *//* TODO : You may activate this when ready. *//*
        });
        return toPredictedMap;
    }*/

    /** @return prediction detail from predict set */
    public static PredictionModel identifyByFilePath(String path) throws IOException {
//        return new PredictionModel(999, "UFO", 99.99);
        return identifyByFile(new File(path));
    }

    /** @return index of cifar10's predicted INDArray. */
    private static Number[] predict4IndexNConfidentialPct(INDArray predictedINDArray){
        int maxPredictedLabelIndex = 0;
        double maxPredictedValue = 0.0; /* Accuracy percentage. */
        for(int i = 0 ; i < predictedINDArray.columns() /*follwing ccifar10's class */; i++){
            double currentIndexValue = predictedINDArray.getColumn(i).amaxNumber().doubleValue();
            if(maxPredictedValue < currentIndexValue){
                maxPredictedValue = currentIndexValue;
                maxPredictedLabelIndex = i;
            }
        }
        return new Number[]{maxPredictedLabelIndex, maxPredictedValue};
    }

    /** @return label of cifar10 label decode from INDArray index. */
    private static String predict4ClassLabel(INDArray predictionINDArray){
        /** Lebels  : [airplane, automobile, bird, cat, deer, dog, frog, horse, ship, truck, ] */
        switch (predict4IndexNConfidentialPct(predictionINDArray)[0].intValue()){
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

    /** @return label of cifar10 label decode from INDArray index. */
    private static PredictionModel predict4FullPredictionModel(INDArray predictionINDArray){
        int index = predict4IndexNConfidentialPct(predictionINDArray)[0].intValue();
        double confidentialPct = predict4IndexNConfidentialPct(predictionINDArray)[1].doubleValue();
        String label = predict4ClassLabel(predictionINDArray);
        return new PredictionModel(index, label, confidentialPct*100);
    }
}
