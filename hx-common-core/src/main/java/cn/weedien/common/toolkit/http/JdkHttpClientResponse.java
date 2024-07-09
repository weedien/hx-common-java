package cn.weedien.common.toolkit.http;

import cn.weedien.common.constant.Constants;
import cn.weedien.common.constant.HttpHeaderConstants;
import cn.weedien.common.toolkit.IOUtil;
import lombok.SneakyThrows;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

/**
 * Represents a client-side HTTP response with JDK implementation
 */
public class JdkHttpClientResponse implements HttpClientResponse {

    private final HttpURLConnection conn;

    private InputStream responseStream;

    private Header responseHeader;

    private static final String CONTENT_ENCODING = "gzip";

    public JdkHttpClientResponse(HttpURLConnection conn) {
        this.conn = conn;
    }

    @Override
    public Header headers() {
        if (this.responseHeader == null) {
            this.responseHeader = Header.newInstance();
        }
        for (Map.Entry<String, List<String>> entry : conn.getHeaderFields().entrySet()) {
            this.responseHeader.addOriginalResponseHeader(entry.getKey(), entry.getValue());
        }
        return this.responseHeader;
    }

    @Override
    @SneakyThrows
    public InputStream body() {
        Header headers = headers();
        InputStream errorStream = this.conn.getErrorStream();
        this.responseStream = (errorStream != null ? errorStream : this.conn.getInputStream());
        String contentEncoding = headers.getValue(HttpHeaderConstants.CONTENT_ENCODING);
        // Used to process http content_encoding, when content_encoding is GZIP, use GZIPInputStream
        if (CONTENT_ENCODING.equals(contentEncoding)) {
            byte[] bytes = IOUtil.tryDecompress(this.responseStream);
            return new ByteArrayInputStream(bytes);
        }
        return this.responseStream;
    }

    @Override
    @SneakyThrows
    public int statusCode() {
        return this.conn.getResponseCode();
    }

    @Override
    @SneakyThrows
    public String statusText() {
        return this.conn.getResponseMessage();
    }

    @Override
    public String bodyString() {
        return IOUtil.toString(this.body(), Constants.ENCODE);
    }

    @Override
    public void close() {
        IOUtil.closeQuietly(this.responseStream);
    }
}
