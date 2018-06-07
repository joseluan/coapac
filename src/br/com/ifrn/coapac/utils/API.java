package br.com.ifrn.coapac.utils;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class API {

    private static String token;
    private static String matricula;
    private static String senha;
    private static HashMap<String, String> meus_dados;

    public API(String matricula, String senha) {
        setMatricula(matricula);
        setSenha(senha);
    }

    @SuppressWarnings("unchecked")
	public String buscarToken() throws IOException {
        //Pegando o Token com POST
        Form form = Form.form();
        form.add("username", API.matricula);
        form.add("password", API.senha);

        HttpResponse response = Request.Post("https://suap.ifrn.edu.br/api/v2/autenticacao/token/")
                .bodyForm(form.build()).execute().returnResponse();
        HashMap<String, String> tokenHM;
        if (response != null) {
            InputStream source = response.getEntity().getContent();
            Reader reader = new InputStreamReader(source);
            Gson gson = new Gson();
            tokenHM = gson.fromJson(reader, HashMap.class); //você pode ultilizar o Gson ou org.json 
            return tokenHM.get("token").toString();
        }
        return "";
    }

    @SuppressWarnings("unchecked")
	public HashMap<String, String> buscarDados() throws IOException,org.apache.http.client.ClientProtocolException {
        //Buscando Meus Dados com GET
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String URLALUNO = "https://suap.ifrn.edu.br/api/v2/edu/alunos/" + matricula + "/";
        String URLSERVIDOR = "https://suap.ifrn.edu.br/api/v2/rh/servidores/" + matricula + "/";
        String auth = "Basic MjAxNDEwNjQwMTAyMjA6U29hZDU1NTM=";//fixo para alunos
        String responseGET;
        try {
            //buscando um ALUNO
            HttpGet httpget = new HttpGet(URLALUNO);
            //adicionando informações ao cabeçalho da requisição
            httpget.addHeader("Accept", "application/json");
            httpget.addHeader("X-CSRFToken", getToken());
            httpget.addHeader("Authorization", auth);

            System.out.println("Executing request " + httpget.getRequestLine());
            //verifica se a api está funcionando
            ResponseHandler<String> responseHandler = (final HttpResponse response) -> {
                int status = response.getStatusLine().getStatusCode();
                System.out.println("aqui...................");
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            };
            responseGET = httpclient.execute(httpget, responseHandler);
        } catch (org.apache.http.client.ClientProtocolException e) {
            e.printStackTrace();
            
            //buscando um SERVIDOR
            HttpGet httpget = new HttpGet(URLSERVIDOR);
            //adicionando informações ao cabeçalho da requisição
            httpget.addHeader("Accept", "application/json");
            httpget.addHeader("X-CSRFToken", getToken());
            httpget.addHeader("Authorization", auth);

            System.out.println("Executing request " + httpget.getRequestLine());
            //verifica se a api está funcionando
            ResponseHandler<String> responseHandler = (final HttpResponse response) -> {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            };
            responseGET = httpclient.execute(httpget, responseHandler);
        }

        //criando um objeto Gson e transformando uma ressposta da requisição num objeto HashMap
        Gson gson = new Gson();
        meus_dados = gson.fromJson(responseGET, HashMap.class);
        return gson.fromJson(responseGET, HashMap.class);
    }

    //get e set
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        API.token = token;
    }

    public String getMatricula() {
        return matricula;
    }

    public static void setMatricula(String matricula) {
        API.matricula = matricula;
    }

    public String getSenha() {
        return senha;
    }

    public static void setSenha(String senha) {
        API.senha = senha;
    }

    public static HashMap<String, String> getMeus_dados() {
        return meus_dados;
    }

    public static void setMeus_dados(HashMap<String, String> meus_dados) {
        API.meus_dados = meus_dados;
    }

}
