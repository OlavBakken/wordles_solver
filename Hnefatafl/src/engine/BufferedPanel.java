package engine;

import java.awt.*;
import java.awt.image.*;

public class BufferedPanel extends Panel{
    static final long serialVersionUID = 743L;
    BufferedImage buffer;
    
    @Override
    public void update(Graphics g){
        if (buffer == null) buffer = new BufferedImage(super.getWidth(), super.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        paint(buffer.getGraphics());
        g.drawImage(buffer, 0, 0, null);
    }
}