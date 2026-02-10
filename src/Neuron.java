abstract class Neuron {

    double value = Math.random();
    int inLayer;
    int index;

    public Neuron(double value, int inLayer, int index) {
        this.value = value;
        this.inLayer = inLayer;
        this.index = index;
    }
}
