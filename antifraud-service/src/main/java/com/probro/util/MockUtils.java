package com.probro.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.security.Principal;
import java.util.*;

@Service
public class MockUtils {

    public String loadExernalFile(String filePath) throws Exception {
        String data = FileUtils.readFileToString(new File(filePath), "UTF-8");

        return StringUtils.trim(data);
    }

    public List<String> loadToLines(String filePath) throws Exception {
        List<String> data = FileUtils.readLines(new File(filePath), "UTF-8");

        return data;
    }

    public String loadTestJson(String filePath) throws Exception {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(filePath);
        StringBuilder out = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String read;
        while ((read = reader.readLine()) != null) {
            out.append(read);
            out.append("\r\n");
        }

        return out.toString();
    }

    public HttpHeaders mockHttpHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("X-Portal-Name", "userxxx");
        httpHeaders.add("X-Portal-Organization-Code", "test");

        return httpHeaders;
    }

    public HttpServletRequest mockHttpRequest() {
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.addHeader("X-Portal-Name", "userxxx");
        httpServletRequest.addHeader("X-Portal-Organization-Code", "test");

        return httpServletRequest;
    }


    public HttpServletRequest mockHttpRequest2() {
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.addHeader("X-Portal-Name", "userxxx");
        httpServletRequest.addHeader("X-Portal-Organization-Code", "001");

        return httpServletRequest;
    }


    public HttpServletRequest mockHttpRequest3() {
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.addHeader("X-Portal-Name", "userxxx");
        httpServletRequest.addHeader("X-Portal-Organization-Code", "visitorapp1,viewApp2,poc_domain,multilingual,");

        return httpServletRequest;
    }


    public HttpServletRequest mockHttpRequest4() {
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.addHeader("X-Portal-Name", "userxxx");
        httpServletRequest.addHeader("X-Portal-Organization-Code", "visitorapp1, viewApp5");

        return httpServletRequest;
    }


    public HttpServletRequest mockHttpRequest5() {
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.addHeader("X-Portal-Name", "userxxx");
        httpServletRequest.addHeader("X-Portal-Organization-Code", "visitorapp1, viewApp5");
        httpServletRequest.addHeader("X-TRACE-ID", "entry" + RandomUtils.nextInt(0, 10000));

        return httpServletRequest;
    }


    public HttpServletRequest mockHttpRequest6() {
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.addHeader("X-Portal-Name", "userxxx");
        httpServletRequest.addHeader("X-Portal-Organization-Code", "visitorapp1, viewApp5");
        httpServletRequest.addHeader("X-TRACE-ID", "entryOld" + RandomUtils.nextInt(0, 10000));
        httpServletRequest.addHeader("x-trace-id", "entryNew" + RandomUtils.nextInt(0, 10000));

        httpServletRequest.addHeader("X-abc-ID", "abc" + RandomUtils.nextInt(0, 10000));
        httpServletRequest.addHeader("X-business-ID", "business" + RandomUtils.nextInt(0, 10000));

        return httpServletRequest;
    }

    public HttpServletRequest mockHttpRequest7() {
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.addHeader("X-Portal-Name", "userxxx");
        httpServletRequest.addHeader("X-Portal-Organization-Code", "visitorapp1");
        httpServletRequest.addHeader("newVersion", "1");
        httpServletRequest.setParameter("curPage", "2");
        httpServletRequest.setParameter("pageSize", "10");
        httpServletRequest.setParameter("filters", "[{\"key\":\"type\",\"value\":\"db\"},{\"key\":\"searchName\", \"value\":\"test\"}]");

        return httpServletRequest;
    }

    public HttpServletRequest mockHttpRequest8() {
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.addHeader("X-Portal-Name", "userxxx");
        httpServletRequest.addHeader("X-Portal-Organization-Code", "visitorapp1");
        httpServletRequest.setParameter("name", "kiss");
        httpServletRequest.setParameter("type", "derived");
//        httpServletRequest.setParameter("name", "testSql324");
//        httpServletRequest.setParameter("type", "db");

        return httpServletRequest;
    }

    public HttpServletRequest mockHttpRequest9() {
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.addHeader("X-Portal-Name", "userxxx");
        httpServletRequest.addHeader("X-Portal-Organization-Code", "a8202aea546f48979754bdd45c471b08");
        httpServletRequest.addHeader("newVersion", "1");
        httpServletRequest.setParameter("curPage", "1");
        httpServletRequest.setParameter("pageSize", "10");
        httpServletRequest.setParameter("filters", "[{\"key\":\"type\",\"value\":\"derived\"}]");

        return httpServletRequest;
    }

    public HttpServletRequest mockHttpRequest10() {
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.addHeader("X-Portal-Name", "userxxx");
        httpServletRequest.addHeader("X-Portal-Organization-Code", "a8202aea546f48979754bdd45c471b08");
        httpServletRequest.setParameter("name", "derived_2002");
        httpServletRequest.setParameter("type", "derived");
//        httpServletRequest.setParameter("name", "testSql324");
//        httpServletRequest.setParameter("type", "db");

        return httpServletRequest;
    }

    public HttpServletRequest mockHttpRequest11() {
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.addHeader("X-Portal-Name", "userxxx");
        httpServletRequest.addHeader("X-Portal-Organization-Code", "visitorapp1,viewApp2,poc_domain,multilingual,");
        httpServletRequest.setParameter("name", "mauApi011");
        httpServletRequest.setParameter("type", "basic");
//        httpServletRequest.setParameter("name", "testSql324");
//        httpServletRequest.setParameter("type", "db");

        return httpServletRequest;
    }


    public HttpServletRequest mockHttpRequestTag01() {
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.addHeader("X-Portal-Name", "userxxx");
        httpServletRequest.addHeader("X-Portal-Organization-Code", "abc");
        httpServletRequest.addHeader("newVersion", "1");
//        httpServletRequest.setParameter("name", "testTags01");
//        httpServletRequest.setParameter("type", "basic");
//        httpServletRequest.setParameter("name", "testSql324");
//        httpServletRequest.setParameter("type", "db");

        return httpServletRequest;
    }


    public HttpServletRequest mockHttpRequest12() {
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.addHeader("X-Portal-Name", "userxxx");
        httpServletRequest.addHeader("X-Portal-Organization-Code", "visitorapp1");
        httpServletRequest.setParameter("name", "PB_PhoneUsed_Last6Mon_I");
        httpServletRequest.setParameter("version", "2");
        httpServletRequest.setParameter("type", "db");


        return httpServletRequest;
    }

    public HttpServletRequest mockHttpRequest13() {
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.addHeader("X-Portal-Name", "userxxx");
        httpServletRequest.addHeader("X-Portal-Organization-Code", "visitorapp1");
        httpServletRequest.setParameter("name", "PB_PhoneUsed_Last6Mon_I");
        httpServletRequest.setParameter("version", "2");
        httpServletRequest.setParameter("type", "db");


        return httpServletRequest;
    }

    public HttpServletRequest mockHttpRequest14() {
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.addHeader("X-Portal-Name", "userxxx");
        httpServletRequest.addHeader("X-Portal-Organization-Code", "visitorapp1");
        httpServletRequest.setParameter("name", "PB_PhoneUsed_Last6Mon_I");
        httpServletRequest.setParameter("version", "1");
        httpServletRequest.setParameter("type", "db");
        httpServletRequest.setParameter("targetName", "cunzai");
        httpServletRequest.setParameter("targetDisplayName", "cunzai");


        return httpServletRequest;
    }


    public HttpServletRequest mockHttpRequest15() {
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.addHeader("X-Portal-Name", "userxxx");
        httpServletRequest.addHeader("X-Portal-Organization-Code", "visitorapp1");
        httpServletRequest.setParameter("name", "testMauApi01");
        httpServletRequest.setParameter("version", "1");
        httpServletRequest.setParameter("type", "api");
        httpServletRequest.setParameter("input", "{\"S_S_ENTRYID\":\"string\"}");


        return httpServletRequest;
    }

    public HttpServletRequest mockHttpRequest16() {
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.addHeader("X-Portal-Name", "userxxx");
        httpServletRequest.addHeader("X-Portal-Organization-Code", "visitorapp1");
        httpServletRequest.setParameter("name", "testMauApi01");
        httpServletRequest.setParameter("version", "1");
        httpServletRequest.setParameter("type", "api");
        httpServletRequest.setParameter("output", "{\"default\":\"string\"}");


        return httpServletRequest;
    }

    public HttpServletRequest mockHttpRequest17() {
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.addHeader("X-Portal-Name", "userxxx");
        httpServletRequest.addHeader("X-Portal-Organization-Code", "mauDerived");
        httpServletRequest.setParameter("name", "cunzai");
        httpServletRequest.setParameter("version", "1");
        httpServletRequest.setParameter("type", "db");
        httpServletRequest.setParameter("output", "{\"default\":\"string\"}");


        return httpServletRequest;
    }


    public HttpServletRequest mockHttpRequest18() {
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.addHeader("X-Portal-Name", "userxxx");
        httpServletRequest.addHeader("X-Portal-Organization-Code", "visitorapp1,viewApp2,poc_domain,multilingual,a8202aea546f48979754bdd45c471b08");
        httpServletRequest.setParameter("name", "test_0821");
        httpServletRequest.setParameter("type", "derived");

        return httpServletRequest;
    }

    public HttpServletRequest mockOutputMatched1() {
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.addHeader("X-Portal-Name", "userxxx");
        httpServletRequest.addHeader("X-Portal-Organization-Code", "mauDerived");
        httpServletRequest.setParameter("name", "mauXingtaiDb02");
//        httpServletRequest.setParameter("version", "1");
        httpServletRequest.setParameter("type", "db");
        httpServletRequest.setParameter("fullOutput", "{\"NAME\":\"numeric\"}");
        httpServletRequest.setParameter("selectedOutput", "{\"NAME\":\"numeric\"}");
        httpServletRequest.setParameter("Action", "isOutputMatched");

        return httpServletRequest;
    }

}
