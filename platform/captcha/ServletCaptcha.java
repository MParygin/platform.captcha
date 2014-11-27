package platform.captcha;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Captcha Servlet
 */
@WebServlet(name="captcha", urlPatterns="/captcha")
public final class ServletCaptcha extends HttpServlet {

    private static String ATTRIBUTE = "check";
    private static String HEX = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789_";
    private static int WIDTH = 128; // width (pixels)
    private static int HEIGHT = 48; // height (pixels)
    private static int NOISE = 400; // noise points

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        java.awt.image.BufferedImage bi = new java.awt.image.BufferedImage(WIDTH, HEIGHT, java.awt.image.BufferedImage.TYPE_INT_RGB);

        java.awt.Graphics2D g2 = (java.awt.Graphics2D) (bi.getGraphics());

        g2.setBackground(java.awt.Color.lightGray);
        g2.clearRect(0, 0, 128, HEIGHT);
        g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(java.awt.Color.BLACK);
        g2.setFont(g2.getFont().deriveFont(28f));

        StringBuilder tmp = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            int p = (int) (Math.random() * HEX.length());
            tmp.append(HEX.charAt(p));
        }
        session.setAttribute(ATTRIBUTE, tmp.toString());

        g2.drawString(tmp.toString(), 16, 28);

        java.util.Random r = new java.util.Random();
        for (int i = 0; i < NOISE; i++) {
            g2.setColor(new java.awt.Color((int) (Math.random() * 0xffffff)));
            int x = r.nextInt(WIDTH);
            int y = r.nextInt(HEIGHT);
            g2.drawLine(x, y, x + 1, y + 1);
        }

        response.setContentType("image/gif");
        javax.imageio.ImageIO.write(bi, "gif", response.getOutputStream());
    }
}
