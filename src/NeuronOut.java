public class NeuronOut extends Neuron {
    double[] weights = new double[Config.depth];
    double bias;
    double y;
    public double z;


    public NeuronOut(double value, int inLayer, int index, double[] weights, double bias, double y) {
        super(value, inLayer, index);
        this.weights = weights;
        this.bias = bias;
        this.y = y;
    }
}
