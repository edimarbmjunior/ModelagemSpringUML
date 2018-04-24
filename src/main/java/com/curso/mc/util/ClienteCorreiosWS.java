package com.curso.mc.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.springframework.http.HttpEntity;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.curso.mc.domain.EnderecoViaCep;

/**
 * Classe para recuperar informações do WS dos correios
 */
public class ClienteCorreiosWS implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private static final Set<String> CAMPOS = new HashSet<String>(Arrays.asList(
            "cep",
            "uf",
            "bairro",
            "cidade",
            "end",
            "complemento",
            "complemento2"
    ));

    /**
     * Recupera objeto Endereco pelo CEP
     * @param cep String no formato 00000000
     * @return instancia de br.com.correios.Endereco
     */
    @SuppressWarnings("null")
	public static EnderecoViaCep getEnderecoPorCep(String cep) {

        Document document = getCepResponse(cep);
        EnderecoViaCep endereco = null;

        if (document != null) {
            Map<String, String> mapa = buscaNodes(document.getChildNodes(), new HashMap<String, String>());

            endereco.setUf(mapa.get("uf"));
            endereco.setCep(mapa.get("cep"));
            endereco.setBairro(mapa.get("bairro"));
            endereco.setCidade(mapa.get("cidade"));
            endereco.setLogradouro(mapa.get("end"));
            endereco.setComplemento(mapa.get("complemento"));
            endereco.setComplemento2(mapa.get("complemento2"));
        }

        return endereco;
    }

    /**
     * Recupera Map<String,String> pelo CEP
     * @param cep String no formato 00000000
     * @return instancia de Map<String,String>
     */
    public static Map<String, String> getMapPorCep(String cep) {

        Document document = getCepResponse(cep);

        Map<String, String> mapa = buscaNodes(document.getChildNodes(), new HashMap<String, String>());

        // Remove campos desnecessários
        for (Iterator<Map.Entry<String,String>> it = mapa.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String,String> entry = it.next();
            if (!CAMPOS.contains(entry.getKey())) {
                it.remove();
            }
        }

        return mapa;
    }

    private static Document getCepResponse(String cep) {
        final String wellformedrequest = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"" +
                " xmlns:cli=\"http://cliente.bean.master.sigep.bsb.correios.com.br/\">" +
                "<soapenv:Header/>" +
                "<soapenv:Body>" +
                "<cli:consultaCEP>" +
                "<cep>"+cep+"</cep>" +
                "</cli:consultaCEP>" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";

        DefaultHttpClient httpclient;
        Document document = null;
        try {
            if (!Util.validaCep(cep)) {
                throw new RuntimeException("Formato de CEP inválido");
            }

            httpclient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost("https://apps.correios.com.br/SigepMasterJPA/AtendeClienteService/AtendeCliente");
            httpPost.setHeader(new BasicHeader("Content-Type", "text/xml;charset=UTF-8"));
            httpPost.setHeader(new BasicHeader("SOAPAction", "http://cliente.bean.master.sigep.bsb.correios.com.br/AtendeCliente/consultaCEP"));
            StringEntity s = new StringEntity(wellformedrequest, "UTF-8");
            httpPost.setEntity(s);
            HttpResponse response = httpclient.execute(httpPost);
            HttpEntity entity = (HttpEntity) response.getEntity();

            if (entity != null) {
                InputStream inputStream = ((org.apache.http.HttpEntity) entity).getContent();

                byte[] buffer = new byte[(int) ((org.apache.http.HttpEntity) entity).getContentLength()];
                inputStream.read(buffer);

                ByteArrayInputStream bais = new ByteArrayInputStream(buffer);

                SOAPMessage soapMessage = MessageFactory.newInstance().createMessage(null, bais);

                document = soapMessage.getSOAPBody().extractContentAsDocument();

                inputStream.close();
            }

            httpclient.getConnectionManager().shutdown();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return document;
    }

    private static Map<String, String> buscaNodes(NodeList nodeList, Map<String, String> mapa) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            mapa.put(nodeList.item(i).getNodeName(), nodeList.item(i).getTextContent());

            if (nodeList.item(i).getChildNodes() != null) {
                buscaNodes(nodeList.item(i).getChildNodes(), mapa);
            }
        }

        return mapa;
    }
}
