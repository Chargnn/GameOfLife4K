import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.*;
import java.util.Random;

public class Game extends Canvas {
    JFrame frame = new JFrame("Game of life");
    int size = 500;
    BufferedImage img = new BufferedImage(size,size, BufferedImage.TYPE_INT_RGB);
    int[] pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
    boolean[] lifes = new boolean[size * size];

    public static void main(String[] args){
        final Game game = new Game();

        game.setPreferredSize(new Dimension(1024, 1024));
        game.frame.setVisible(true);
        game.frame.add(game);
        game.frame.pack();
        game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.start();
    }

    int generation = 0;

    void start() {
        Random random = new Random();
        for(int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (random.nextFloat() > 0.5)
                    pixels[x + y * size] = 26456;
            }
        }

        for (int i = 0; i < size * size; i++)
            lifes[i] = pixels[i] != 0 ? true : false;

        while(true){
            render();
            processPixel();
            generation++;
        }
    }

    void render(){
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();
        g.drawImage(img, 0, 0, 1024, 1024, null);
        g.setColor(Color.white);
        g.setFont(Font.getFont("Arial"));
        int cpt = 0;
        for(int i = 0; i < lifes.length; i++)
            if(lifes[i])
                cpt++;

        g.drawString("Cells: " + cpt, 5, 10);
        g.drawString("Generation: " + generation, 5, 20);
        g.dispose();
        bs.show();

        try {
            Thread.sleep(20);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void processPixel(){
        int aliveAround = 0;
        boolean[] temp = new boolean[size*size];
        for(int i = 0; i < temp.length; i++)
            temp[i] = lifes[i];

        for(int y = 0; y < size; y++){
            for(int x = 0; x < size; x++){
                int l = x+y*size;
                    if(x+1 < size && lifes[(x+1) + (y) * size])
                        aliveAround++;
                    if(x+1 < size && y+1 < size && lifes[(x+1) + (y+1) * size])
                        aliveAround++;
                    if(y+1 < size && lifes[(x) + (y+1) * size])
                        aliveAround++;
                    if(x-1 >= 0 && lifes[(x-1) + (y) * size])
                        aliveAround++;
                    if(x-1 >= 0 && y-1 >= 0 && lifes[(x-1) + (y-1) * size])
                        aliveAround++;
                    if(x+1 < size && y-1 >= 0 && lifes[(x+1) + (y-1) * size])
                        aliveAround++;
                    if(x-1 >= 0 && y+1 < size  && lifes[(x-1) + (y+1) * size])
                        aliveAround++;
                    if(y-1 >= 0 && lifes[(x) + (y-1) * size])
                        aliveAround++;

                    if(aliveAround == 3)
                        temp[l] = true;

                    if(aliveAround > 3 || aliveAround < 2)
                        temp[l] = false;

                aliveAround = 0;
            }
        }
        for (int i = 0; i < size * size; i++) {
            lifes[i] = temp[i];
            pixels[i] = lifes[i] ? 123412 : 0;
        }
    }
}
