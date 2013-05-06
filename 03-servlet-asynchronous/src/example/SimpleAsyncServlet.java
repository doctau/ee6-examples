package example;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns="/SimpleAsyncServlet", asyncSupported=true)
public class SimpleAsyncServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		AsyncContext actx = request.startAsync();
		actx.start(new SleepingRunnable(actx));
	}
	
	static class SleepingRunnable implements Runnable {
		private final AsyncContext actx;
		public SleepingRunnable(AsyncContext actx) {
			this.actx = actx;
		}

		public void run() {
			try {
				Thread.sleep(5000);
				PrintWriter writer = actx.getResponse().getWriter();
				writer.print("Sleepy hello world");
			} catch (IOException | InterruptedException e) {
				throw new RuntimeException(e);
			} finally {
				actx.complete();
			}
		}
		
	}
}
