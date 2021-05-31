package DynamicLayout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;
import java.util.ArrayList;
import java.util.Iterator;

public class DynamicLayout implements LayoutManager2 {
    private ArrayList<Component> elementos = new ArrayList();
    private ArrayList<Integer> percentsWid = new ArrayList();
    private ArrayList<Integer> percentsHeig = new ArrayList();
    private ArrayList<Integer> percentsPositionsX = new ArrayList();
    private ArrayList<Integer> percentsPositionsY = new ArrayList();
    int width;
    int height;

    public DynamicLayout(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void addLayoutComponent(Component comp, Object constraints) {
        this.addLayoutComponent((String)constraints, comp);
    }

    /** @deprecated */
    @Deprecated
    public void addLayoutComponent(String name, Component comp) {
        synchronized(comp.getTreeLock()) {
        
             /**
             * Esse array de ELEMENTOS é responsável por guardar todos os filhos
             * de um pai que implementou o DynamicLayout (Seja um JFrame ou um JPanel).
             *
             * Com esses componentes armazenados em um arrayList, é possível trabalhar
             * com seus tamanhos e posições separadamente
             */
            this.elementos.add(comp);

            /**
             * Os outros ArrayLists são usados no armazenamento da porcentagem
             * de um x elemento de acordo com o seu elemento pai.
             *
             * Basicamente, o gerenciador funciona em cima disso!
             * Se um elemento filho que tem a largura de 10px e está dentro de um elemento pai
             * que possui 100px de largura, logo, pode-se entender que o elemento filho ocupa
             * 10% do elemento pai. Quando o pai é redimensionado, a porcentagem não mudará,
             * o elemento filho ainda possuirá 10% do pai; porém, quando o pai é redimensionado
             * os 10% do filho não valerá mais 10.
             *
             * Ex: 10% de 100 = 10;
             *     10% de 200 = 20.
             *
             * E o mesmo vale para as posições x e y
             *
             */
            this.percentsWid.add(this.getPercent(this.width, comp.getWidth()));
            this.percentsHeig.add(this.getPercent(this.height, comp.getHeight()));
            this.percentsPositionsX.add(this.getPercent(this.width, (int)comp.getLocation().getX()));
            this.percentsPositionsY.add(this.getPercent(this.height, (int)comp.getLocation().getY()));
        }
    }

    public Dimension maximumLayoutSize(Container target) {
        return new Dimension(2147483647, 2147483647);
    }

    public void layoutContainer(Container target) {
        int height = target.getHeight();
        int width = target.getWidth();
        int cont = 0;

        for(Iterator var5 = this.elementos.iterator(); var5.hasNext(); ++cont) {
            Component c = (Component)var5.next();
            int widthFinal = this.geraTamanhoWid(width, cont);
            int heightFinal = this.geraTamanhoHeig(height, cont);
            c.setBounds(this.geraPositionX(width, cont), this.geraPositionY(height, cont), widthFinal, heightFinal);
        }

    }

    private int getPercent(int tamPai, int tamFilho) {
        return tamFilho * 100 / tamPai;
    }

    private int geraPositionX(int tamPai, int cont) {
        return (Integer)this.percentsPositionsX.get(cont) * tamPai / 100;
    }

    private int geraPositionY(int tamPai, int cont) {
        return (Integer)this.percentsPositionsY.get(cont) * tamPai / 100;
    }

    private int geraTamanhoWid(int tamPai, int cont) {
        return (Integer)this.percentsWid.get(cont) * tamPai / 100;
    }

    private int geraTamanhoHeig(int tamPai, int cont) {
        return (Integer)this.percentsHeig.get(cont) * tamPai / 100;
    }

    public Dimension preferredLayoutSize(Container parent) {
        return null;
    }

    public Dimension minimumLayoutSize(Container parent) {
        return null;
    }

    public void invalidateLayout(Container target) {
    }

    public float getLayoutAlignmentX(Container parent) {
        return 0.0F;
    }

    public float getLayoutAlignmentY(Container parent) {
        return 0.0F;
    }

    public void removeLayoutComponent(Component comp) {
    }
}

