package ru.alexeylisyutenko.ai.connectfour.dataset;

public class ConnectFourDatasetGeneratorApplication {
    public static void main(String[] args) {
        ConnectFourDatasetGenerator generator = new DefaultConnectFourDatasetGenerator();
        generator.generate(10000, 20000, "training_set.bin", "test_set.bin");
    }
}
