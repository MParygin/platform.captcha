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

    private static String ATTRIBUTE = "check"; // name of session attribute for store the value of captcha
    private static String DICT = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789_";  // dictionary
    private static int CHARS = 5;
    private static int WIDTH = 128; // width (pixels)
    private static int HEIGHT = 48; // height (pixels)
    private static int NOISE = 400; // noise points
    private static float FONT_SIZE = 28f; // font size

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        // image
        java.awt.image.BufferedImage bi = new java.awt.image.BufferedImage(WIDTH, HEIGHT, java.awt.image.BufferedImage.TYPE_INT_RGB);
        java.awt.Graphics2D g2 = (java.awt.Graphics2D) (bi.getGraphics());
        g2.setBackground(java.awt.Color.lightGray);
        g2.clearRect(0, 0, WIDTH, HEIGHT);
        g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(java.awt.Color.BLACK);
        g2.setFont(g2.getFont().deriveFont(FONT_SIZE));

        // create text
        StringBuilder tmp = new StringBuilder();
        for (int i = 0; i < CHARS; i++) {
            tmp.append(DICT.charAt((int) (Math.random() * DICT.length())));
        }
        session.setAttribute(ATTRIBUTE, tmp.toString());

        g2.drawString(tmp.toString(), 16, 28);

        // noise
        java.util.Random r = new java.util.Random();
        for (int i = 0; i < NOISE; i++) {
            g2.setColor(new java.awt.Color((int) (Math.random() * 0xffffff)));
            int x = r.nextInt(WIDTH);
            int y = r.nextInt(HEIGHT);
            g2.drawLine(x, y, x + 1, y + 1);
        }

        // out
        response.setContentType("image/gif");
        javax.imageio.ImageIO.write(bi, "gif", response.getOutputStream());
    }
}
