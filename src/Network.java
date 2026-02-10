import java.util.ArrayList;
import java.util.List;

public class Network {

    int hiddenlayers = Config.hiddenlayers;
    int start = 0;
    int curLabel;

    List<Layer> layers = new ArrayList<>();
    List<Layer> hlayers = new ArrayList<>();

    public void assignLayers(List<Data> d) {
        assignL1(d, 0);
        assignHL();
        assignOL();
    }

    public void assignL1(List<Data> d, int index) {
        Layer L1 = new Layer();
        L1.depth = Config.inputs;

        Data curData = d.get(index);

        for (int i = 0; i < Config.inputs; i++) {
            NeuronIn neuron = new NeuronIn(curData.inputs[i] / 255.0, 0, i);
            L1.neurons.add(neuron);
        }

        if (!layers.isEmpty()) layers.set(0, L1);
        else layers.add(L1);

        curLabel = curData.label;
    }

    public List<Layer> assignHL() {
        for (int i = 0; i < hiddenlayers; i++) {
            Layer LH = new Layer();
            LH.depth = Config.depth;

            Layer prev = layers.getLast();

            for (int j = 0; j < LH.depth; j++) {
                double[] weights = new double[prev.depth];
                for (int k = 0; k < prev.depth; k++) {
                    weights[k] = (Math.random() - 0.5) * 2;
                }

                NeuronH neuron = new NeuronH(weights, Math.random(), 0, layers.size(), j);
                LH.neurons.add(neuron);
            }

            layers.add(LH);
            hlayers.add(LH);
        }
        return hlayers;
    }

    public Layer assignOL() {
        Layer LO = new Layer();
        LO.depth = Config.outputs;

        Layer prev = layers.getLast();

        for (int i = 0; i < Config.outputs; i++) {
            double[] weights = new double[prev.depth];
            for (int j = 0; j < prev.depth; j++) {
                weights[j] = (Math.random() - 0.5) * 2;
            }

            NeuronOut neuron = new NeuronOut(0, layers.size(), i, weights, Math.random(), 0);
            LO.neurons.add(neuron);
        }

        layers.add(LO);
        return LO;
    }


    private double weightedSum(int prevLayerIndex, double[] weights, double bias) {
        Layer prev = layers.get(prevLayerIndex);
        double sum = bias;
        for (int i = 0; i < prev.depth; i++) {
            sum += prev.neurons.get(i).value * weights[i];
        }
        return sum;
    }

    public void forward(Neuron n) {
        if (n instanceof NeuronH h) {
            h.z = weightedSum(h.inLayer - 1, h.weights, h.bias);
            h.value = Utils.sig(h.z);
        }
        else if (n instanceof NeuronOut o) {
            o.z = weightedSum(o.inLayer - 1, o.weights, o.bias);
            o.value = Utils.sig(o.z);
        }
    }

    public void forwardAll() {
        for (int l = 1; l < layers.size(); l++) {
            for (Neuron n : layers.get(l).neurons) {
                forward(n);
            }
        }
        Layer out = layers.getLast();
        for (int i = 0; i < out.depth; i++) {
            ((NeuronOut) out.neurons.get(i)).y = (i == curLabel) ? 1 : 0;
        }
    }

    private double deltaOut(NeuronOut n) {
        return (n.value - n.y) * Utils.der_sig(n.z);
    }

    private double deltaHidden(NeuronH n) {
        Layer next = layers.get(n.inLayer);
        double sum = 0;

        for (Neuron neuron : next.neurons) {
            if (neuron instanceof NeuronOut o) {
                sum += o.weights[n.index] * deltaOut(o);
            }
            else if (neuron instanceof NeuronH h) {
                sum += h.weights[n.index] * h.delta;
            }
        }

        n.delta = Utils.der_sig(n.z) * sum;
        return n.delta;
    }

    public void backprop() {

        Layer out = layers.getLast();
        Layer prev = layers.get(layers.size() - 2);

        for (Neuron neuron : out.neurons) {
            NeuronOut n = (NeuronOut) neuron;
            double delta = deltaOut(n);

            n.bias -= Config.L * delta;

            for (int i = 0; i < n.weights.length; i++) {
                n.weights[i] -= Config.L * delta * prev.neurons.get(i).value;
            }
        }

        for (int l = layers.size() - 2; l > 0; l--) {
            Layer layer = layers.get(l);
            Layer prevLayer = layers.get(l - 1);

            for (Neuron neuron : layer.neurons) {
                NeuronH n = (NeuronH) neuron;
                double delta = deltaHidden(n);

                n.bias -= Config.L * delta;

                for (int i = 0; i < n.weights.length; i++) {
                    n.weights[i] -= Config.L * delta * prevLayer.neurons.get(i).value;
                }
            }
        }
    }
}
