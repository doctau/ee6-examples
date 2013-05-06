package example;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class Rot13Filter implements Filter {
	public void init(FilterConfig paramFilterConfig) throws ServletException {}
	public void destroy() {}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		chain.doFilter(req, new Rot13Response((HttpServletResponse) resp));
	}
	
	static class Rot13Response extends HttpServletResponseWrapper {
		public Rot13Response(HttpServletResponse resp) {
			super(resp);
		}

		public PrintWriter getWriter() throws IOException {
			final PrintWriter writer = super.getWriter();
			return new PrintWriter(writer) {
			    public void write(int c) {
			    	writer.write(rot13(c));
			    }

				public void write(char buf[], int off, int len) {
			    	char[] tmp = Arrays.copyOfRange(buf, off, off + len);
			    	writer.write(rot13(tmp));
			    }

				public void write(String s, int off, int len) {
			    	char[] tmp = s.substring(off, off + len).toCharArray();
			    	writer.write(rot13(tmp));
			    	
			    }

			    private int rot13(int c) {
					if (c >= 'a' && c <= 'z') return ((c - 'a') + 13) % 26 + 'a';
					else if (c >= 'A' && c <= 'Z') return ((c - 'A') + 13) % 26 + 'A';
					else return c;
				}

			    private char[] rot13(char[] in) {
			    	char[] out = new char[in.length];
					for (int i = 0; i < in.length; i++)
						out[i] = (char) rot13(in[i]);
					return out;
				}
			};
		}
	}
}
