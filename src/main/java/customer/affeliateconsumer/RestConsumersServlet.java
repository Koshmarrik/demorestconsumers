package customer.affeliateconsumer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by roman rasskazov on 29.05.2015.
 */
public class RestConsumersServlet extends HttpServlet {



    @Override
    public void init() throws ServletException {
        super.init();
        RestScheduler.runScheduler();
    }

    public void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws IOException {
        httpServletResponse.getWriter().print("DynaObject RestClient");
    }

    @Override
    public void destroy() {
        RestScheduler.releaseThreads();
        super.destroy();
    }
}
