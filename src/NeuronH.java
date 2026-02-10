public class NeuronH extends Neuron {
    double[] weights = new double[Config.depth];
    double bias;
    public double delta;
    public double z;


    public NeuronH(double[] weights, double bias, double value, int inLayer, int index) {
        super(value, inLayer, index);
        this.weights = weights;
        this.bias = bias;
    }
}
