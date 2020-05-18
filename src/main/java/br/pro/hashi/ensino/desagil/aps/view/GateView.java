package br.pro.hashi.ensino.desagil.aps.view;

import br.pro.hashi.ensino.desagil.aps.model.Gate;
import br.pro.hashi.ensino.desagil.aps.model.Light;
import br.pro.hashi.ensino.desagil.aps.model.Switch;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;

public class GateView extends FixedPanel implements ActionListener, MouseListener {

    // Checkboxes
    private final JCheckBox In0Box;
    private final JCheckBox In1Box;

    // Switches (atributos da classe Switch para selecionar checkboxes)
    private final Switch Switch0;
    private final Switch Switch1;
    private final Light light;
    private final Image image;


    public GateView(Gate gate) {

        // Como subclasse de FixedPanel, esta classe agora
        // exige que uma largura e uma altura sejam fixadas.
        super(250, 120);

        In0Box = new JCheckBox();
        In1Box = new JCheckBox();
        Switch0 = new Switch();
        Switch1 = new Switch();
        light = new Light(255, 0, 0, 0, 0, 0);

        // Adiciona as Labels & Checkboxs criadas anteriormente na ordem de exibição
        // Lembrando que a classe extends de JPanel para funcionar como contêiner.
        if (gate.getInputSize() != 1) {
            // Painel de exibição para gates com inputSize != 1
            // Portas: AND, NAND, OR, XOR
            add(In0Box, 10, 29, 20, 20);
            add(In1Box, 10, 68, 20, 20);

            // Pega a imagem de cada porta para desenhar posteriormente
            String name = gate.toString() + ".png";
            URL url = getClass().getClassLoader().getResource(name);
            image = getToolkit().getImage(url);

            // Adiciona ActionListener para as entradas e desabilita a edição da box de saida.
            In0Box.addActionListener(this);
            In1Box.addActionListener(this);
        } else {
            // Painel de exibição para gates com inputSize = 1
            // Portas: NOT
            add(In0Box, 10, 48, 20, 20);

            // Pega a imagem da porta NOT
            String name = gate.toString() + ".png";
            URL url = getClass().getClassLoader().getResource(name);
            image = getToolkit().getImage(url);

            // Adiciona ActionListener para as entradas e desabilita a edição da box de saida.
            In0Box.addActionListener(this);
        }

        // Conecta os respectivos sinais às entradas
        if (gate.getInputSize() != 1) {
            gate.connect(0, Switch0);
            gate.connect(1, Switch1);
        } else {
            gate.connect(0, Switch0);
        }
        // Conecta o gate ao receiver light
        light.connect(0, gate);

        addMouseListener(this);

        // Atualiza a classe para uma nova iteração
        update();
    }

    // Método update serve para atualizar a cor do painel de resposta
    private void update() {
        repaint();
    }

    // Método actionPerformed realiza as conexões de
    // cada Gate emitindo sinais de acordo com a classe
    // Switch, em que diz: Quando o botão está marcado (checkbox)
    // um sinal true é emitido e o contrário um sinal false é emitido.
    @Override
    public void actionPerformed(ActionEvent event) {
        // Define o sinal se é true ou false dependendo se a
        // checkbox está selecionada ou não.
        boolean In0State = In0Box.isSelected();
        boolean In1State = In1Box.isSelected();

        // Utiliza o Switch para identificar o sinais como true or false
        // de acordo com a checkbox selecionada ou não
        if (In0State) {
            Switch0.turnOn();
        } else
            Switch0.turnOff();
        if (In1State) {
            Switch1.turnOn();
        } else
            Switch1.turnOff();

        // Atualiza a classe para uma nova iteração
        update();
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        // Descobre em qual posição o clique ocorreu.
        int x = event.getX();
        int y = event.getY();
        int raio = 10;
        int x_centro = 210;
        int y_centro = 58;

        // Fórmula da distância entre pontos:
        // se a distancia entre o click e o centro for menor que o raio do circulo, o click foi dentro
        // Se o clique foi dentro do circulo colorido...
        if (Math.sqrt(Math.pow(x - x_centro, 2) + Math.pow(y - y_centro, 2)) < raio) {

            // só deixa alterar a cor do resultado se a saída for true
            if (light.getEmitter().read()) {
                Color actual = light.getColor();
                // ...então abrimos a janela seletora de cor...
                Color color = JColorChooser.showDialog(this, null, actual);
                // ...atualiza a cor da saída...
                light.setColor(color);
                // ...e chamamos repaint para atualizar a tela.
                repaint();
            } else {
                Color actual = light.getColor();
                // ...então abrimos a janela seletora de cor...
                Color color = JColorChooser.showDialog(this, null, actual);
                // ...atualiza a cor da saída...
                light.setOffColor(color);
            }

            repaint();
        }

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    @Override
    public void paintComponent(Graphics g) {

        // Não podemos esquecer desta linha, pois não somos os
        // únicos responsáveis por desenhar o painel, como era
        // o caso nos Desafios. Agora é preciso desenhar também
        // componentes internas, e isso é feito pela superclasse.
        super.paintComponent(g);

        // Desenha a imagem, passando sua posição e seu tamanho.
        g.drawImage(image, 20, 10, 192, 96, this);

        // Desenha um quadrado cheio.
        if (light.getEmitter().read()) {
            g.setColor(light.getColor());
        } else {
            g.setColor(light.getOffColor());
        }
        g.fillOval(200, 48, 20, 20);

        // Linha necessária para evitar atrasos
        // de renderização em sistemas Linux.
        getToolkit().sync();
    }
}
