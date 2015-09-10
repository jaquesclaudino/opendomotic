package com.opendomotic.device.impl;

import com.opendomotic.device.Device;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/**
 *
 * @author Jaques Claudino
 * @param <T>
 */
public class HttpDevice<T> implements Device<T> {

    private static final Logger LOG = Logger.getLogger(HttpDevice.class.getName());    
    private static final boolean SHOW_LOG = false;
    private static final int DEFAULT_TIMEOUT = 100;
    
    private String host;
    private String path;
    private String user;
    private String password;
    private String queryParam;
    private HttpClient httpClient;
    private int timeout = DEFAULT_TIMEOUT;
    
    @Override
    public T getValue() throws Exception {
        return (T) makeRequest(getURI());
    }

    @Override
    public void setValue(T value) throws Exception {
        StringBuilder sb = new StringBuilder(getURI());
        if (queryParam != null) {
            sb.append('?');
            sb.append(queryParam);
        }
        sb.append('=');
        sb.append(value);
        
        makeRequest(sb.toString());
    }
    
    private HttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = new DefaultHttpClient();

            if (user != null && !user.isEmpty()) {
                UsernamePasswordCredentials creds = new UsernamePasswordCredentials(user, password);
                ((AbstractHttpClient) httpClient).getCredentialsProvider().setCredentials(AuthScope.ANY, creds);
            }

            HttpParams httpParameters = httpClient.getParams();
            HttpConnectionParams.setTcpNoDelay(httpParameters, true);
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeout);
            HttpConnectionParams.setSoTimeout(httpParameters, timeout); 
            HttpConnectionParams.setSoKeepalive(httpParameters, false);
            HttpConnectionParams.setStaleCheckingEnabled(httpParameters, false);
        }        
        return httpClient;
    }
    
    private String readResponseLine(HttpResponse response) throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        return rd.readLine();
    }
    
    protected String makeRequest(String uri) throws IOException {
        long time = System.currentTimeMillis();
        
        String responseStr = null;
        HttpGet request = new HttpGet(uri);
        try {
            HttpResponse response = getHttpClient().execute(request);    
            responseStr = readResponseLine(response);
        } finally {
            request.releaseConnection();
        }
        
        if (SHOW_LOG) {
            LOG.log(Level.INFO, "Response={0} | {1} | {2} ms", new Object[] {responseStr, uri, System.currentTimeMillis() - time});
        }
        
        return responseStr;
    }
    
    protected String getURI() {
        return "http://" + host + "/" + path;
    }

    public void setHost(String host) {
        this.host = host;
    }
    
    public void setPath(String path) {
        this.path = path;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setQueryParam(String queryParam) {
        this.queryParam = queryParam;
    }

    @Override
    public String toString() {
        return "HttpDevice{" + "host=" + host + ", path=" + path + '}';
    }
    
}
