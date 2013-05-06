package example;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Paths;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns="/NIOAsyncServlet", asyncSupported=true)
public class NIOAsyncServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		final AsyncContext ctx = request.startAsync();
		AsynchronousFileChannel channel = AsynchronousFileChannel.open(Paths.get("/tmp/foo"));
		final ByteBuffer buffer = ByteBuffer.allocate(4096);
		channel.read(buffer, 0, null, new CompletionHandler<Integer,Object>() {
			public void completed(Integer length, Object o) {
				try {
					// do something with /length/ bytes from /buffer/
					byte[] bs = new byte[length];
					buffer.flip();
					buffer.get(bs, 0, length);
					ctx.getResponse().getOutputStream().write(bs);
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					ctx.complete();
				}
			}

			public void failed(Throwable ex, Object attachment) {
				try {
					ex.printStackTrace();
				} finally {
					ctx.complete();
				}
			}
		});

	}

}
