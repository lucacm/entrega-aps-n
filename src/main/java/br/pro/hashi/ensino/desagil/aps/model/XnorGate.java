package br.pro.hashi.ensino.desagil.aps.model;

public class XnorGate extends Gate {
    private final NandGate left;
    private final NandGate upper;
    private final NandGate bottom;
    private final NandGate inverter;


    public XnorGate() {
        super("XNOR", 2);
        // nand esquerdo
        left = new NandGate();

        // nand cima
        upper = new NandGate();
        upper.connect(1, left);

        // nand baixo
        bottom = new NandGate();
        bottom.connect(0, left);

        // nand direito
        NandGate right = new NandGate();
        right.connect(0, upper);
        right.connect(1, bottom);

        // nand inversor (final)
        inverter = new NandGate();
        inverter.connect(0, right);
        inverter.connect(1, right);
    }

    @Override
    public boolean read() {
        return inverter.read();
    }

    @Override
    public void connect(int inputIndex, Emitter emitter) {
        switch (inputIndex) {
            case 0:
                upper.connect(0, emitter);
                left.connect(0, emitter);
                break;
            case 1:
                left.connect(1, emitter);
                bottom.connect(1, emitter);
                break;
//          mudando a condição do teste para funcionar
            default:
                throw new IndexOutOfBoundsException(inputIndex);
        }
    }
}
